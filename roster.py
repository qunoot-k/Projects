from sys import argv, exit
from cs50 import SQL

# if incorrect execution of program
if len(argv) != 2:
    print("Wrong no. of command line arguments !")
    exit(1)

house = argv[1]

db = SQL("sqlite:///students.db")

# select the rows and store them im a list
char_list = db.execute(f'SELECT first, middle, last, birth FROM students WHERE house = (?) ORDER BY last, first', house)

# print the list
for row in char_list:
    if row["middle"] == None:
        print(f'{row["first"]} {row["last"]}, born {row["birth"]}')
        # If 1st print executed just continue
        continue
    print(f'{row["first"]} {row["middle"]} {row["last"]}, born {row["birth"]}')