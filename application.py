import os

from cs50 import SQL
from flask import Flask, flash, jsonify, redirect, render_template, request, session
from flask_session import Session
from tempfile import mkdtemp
from werkzeug.exceptions import default_exceptions, HTTPException, InternalServerError
from werkzeug.security import check_password_hash, generate_password_hash

from helpers import apology, login_required, lookup, usd

# Configure application
app = Flask(__name__)

# Ensure templates are auto-reloaded
app.config["TEMPLATES_AUTO_RELOAD"] = True

# Ensure responses aren't cached
@app.after_request
def after_request(response):
    response.headers["Cache-Control"] = "no-cache, no-store, must-revalidate"
    response.headers["Expires"] = 0
    response.headers["Pragma"] = "no-cache"
    return response

# Custom filter
app.jinja_env.filters["usd"] = usd

# Configure session to use filesystem (instead of signed cookies)
app.config["SESSION_FILE_DIR"] = mkdtemp()
app.config["SESSION_PERMANENT"] = False
app.config["SESSION_TYPE"] = "filesystem"
Session(app)

# Configure CS50 Library to use SQLite database
db = SQL("sqlite:///finance.db")

# Make sure API key is set
if not os.environ.get("API_KEY"):
    raise RuntimeError("API_KEY not set")


@app.route("/")
@login_required
def index():
    """Show portfolio of stocks"""
    history = db.execute("SELECT symbol, SUM(shares) AS Shares FROM history WHERE user_id=:user_id GROUP BY symbol HAVING SUM(shares) > 0",
    user_id=session["user_id"])
    cash = db.execute("SELECT cash FROM users WHERE id=:id", id=session["user_id"])
    finaltotal = 0
    for row in history:
        quote = lookup(row["symbol"])
        row["name"] =  quote["name"]
        row["price"] = quote["price"]
        row["total"] = row["price"]*row["Shares"]
        finaltotal = finaltotal + row["total"]
    value = finaltotal + cash[0]["cash"]
    return render_template("index.html", history=history, current_cash=cash[0]["cash"], value=value)

@app.route("/buy", methods=["GET", "POST"])
@login_required
def buy():
    """Buy shares of stock"""

    if request.method == "GET":
        return render_template("buy.html")

    else:
        shares = request.form.get("shares")
        if not request.form.get("symbol") or not shares:
            return apology("All fields required!", 400)

        quote = lookup(request.form.get("symbol"))
        if quote == None:
            return apology("Symbol does not exist", 403)

        try:
            if int(shares) <= 0:
                return apology("Shares must be more than zero!", 403)
        except:
            return apology("Shares must be a positive integer!", 403)

        price = quote["price"]
        total_price = price * int(shares)
        cash = db.execute("SELECT cash FROM users WHERE id=:user_id",
                          user_id = session["user_id"])

        if cash[0]["cash"] < total_price:
            return apology("Sorry it seems you cannot afford these many shares at the current price!", 403)

        else:
            db.execute("UPDATE users SET cash = cash - :price WHERE id=:user_id",
                        price=total_price,
                        user_id=session["user_id"])
            db.execute("INSERT INTO history(user_id, symbol, shares, price) VALUES (:user_id, :symbol, :shares, :price)",
                        user_id=session['user_id'],
                        symbol=request.form.get("symbol"),
                        shares=int(shares),
                        price=price)

        flash("Bought!")

        return redirect("/")

@app.route("/history")
@login_required
def history():
    """Show history of transactions"""

    history = db.execute("SELECT symbol, shares, price, time FROM history WHERE user_id=:user_id",
    user_id=session["user_id"])
    return render_template("history.html", history=history)

@app.route("/login", methods=["GET", "POST"])
def login():
    """Log user in"""

    # Forget any user_id
    session.clear()

    # User reached route via POST (as by submitting a form via POST)
    if request.method == "POST":

        # Ensure username was submitted
        if not request.form.get("username"):
            return apology("must provide username", 403)

        # Ensure password was submitted
        elif not request.form.get("password"):
            return apology("must provide password", 403)

        # Query database for username
        rows = db.execute("SELECT * FROM users WHERE username = :username",
                          username=request.form.get("username"))

        # Ensure username exists and password is correct
        if len(rows) != 1 or not check_password_hash(rows[0]["hash"], request.form.get("password")):
            return apology("invalid username and/or password", 403)

        # Remember which user has logged in
        session["user_id"] = rows[0]["id"]

        # Redirect user to home page
        return redirect("/")

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("login.html")


@app.route("/logout")
def logout():
    """Log user out"""

    # Forget any user_id
    session.clear()

    # Redirect user to login form
    return redirect("/")


@app.route("/quote", methods=["GET", "POST"])
@login_required
def quote():
    """Get stock quote."""
    if request.method == "GET":
        return render_template("symbol.html")
    else:
        quote = lookup(request.form.get("symbol"))
        if quote == None:
            return apology("Symbol does not exist", 403)

        return render_template("quote.html", quote=quote)


@app.route("/register", methods=["GET", "POST"])
def register():
    """Register user"""
    if request.method == "GET":
        return render_template("register.html")
    else:
        # Ensure username was submitted
        if not request.form.get("username"):
            return apology("must provide username", 403)

        # Ensure password was submitted
        elif not request.form.get("password"):
            return apology("must provide password", 403)

        elif not request.form.get("confirmPassword"):
            return apology("must provide confirmation password", 403)

        # Query database for username
        rows = db.execute("SELECT * FROM users WHERE username = :username",
                          username=request.form.get("username"))

        if len(rows) != 0:
            return apology("username already exists!", 403)

        if request.form.get("password") != request.form.get("confirmPassword"):
            return apology("password and confirm password do not match!", 403)
        else:
            new_user = db.execute("INSERT INTO users (username, hash) VALUES (:username, :hash_password)",
                    username=request.form.get("username"),
                    hash_password = generate_password_hash(request.form.get("password")))

        # Remember which user has logged in
        session["user_id"] = new_user

        # Display a flash message
        flash("Registered!")

        return redirect("/")

@app.route("/sell", methods=["GET", "POST"])
@login_required
def sell():
    """Sell shares of stock"""
    if request.method == "GET":
        Symbol = db.execute("SELECT DISTINCT symbol FROM history WHERE user_id=:user_id", user_id=session["user_id"])
        return render_template("sell.html", Symbol=Symbol)
    else:
        symbol = request.form.get("symbol")
        shares_sold = request.form.get("shares")
        if not symbol:
            return apology("Select a symbol!", 403)
        if not shares_sold:
            return apology("Provide shares to be sold!", 403)
        try:
            if int(shares_sold) <= 0:
                return apology("shares must be an positive integer!", 403)
        except:
            return apology("shares enetered must be an integer!", 403)
        rows = db.execute("SELECT SUM(shares) AS shares_owned FROM history WHERE symbol=:symbol and user_id=:id",
                            symbol=symbol,
                            id=session["user_id"])
        if not rows or rows[0]["shares_owned"] == 0:
            return apology("you do not own shares in this company!", 400)
        if rows[0]["shares_owned"] < int(shares_sold):
            return apology("not enough shares owned!", 400)

        quote = lookup(symbol)
        price = quote["price"]
        total_price = int(shares_sold)*price
        shares = 0 - int(shares_sold)

        db.execute("UPDATE users SET cash = cash + :price WHERE id=:user_id",
                        price=total_price,
                        user_id=session["user_id"])
        db.execute("INSERT INTO history(user_id, symbol, shares, price) VALUES (:user_id, :symbol, :shares, :price)",
                        user_id=session['user_id'],
                        symbol=request.form.get("symbol"),
                        shares=shares,
                        price=price)

        flash("Sold!")

        return redirect("/")

def errorhandler(e):
    """Handle error"""
    if not isinstance(e, HTTPException):
        e = InternalServerError()
    return apology(e.name, e.code)


# Listen for errors
for code in default_exceptions:
    app.errorhandler(code)(errorhandler)
