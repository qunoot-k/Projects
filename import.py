from sys import argv, exit
from cs50 import SQL
import csv

# if incorrect execution of program
if len(argv) != 2:
    print("Wrong usage of command line arguments !")
    exit(1)

# check if it is a csv file
if not (argv[1].endswith(".csv")):
    print("The file should be a csv !")
    exit(1)

db = SQL("sqlite:///students.db")

# read data from file to dict
with open(argv[1], 'r') as file:
    reader = csv.DictReader(file)
    character = []
    for row in reader:
        character.append(row)

# insert from dict to table
for row in character:
    name = row["name"].split(' ')
    if len(name) == 2:
        db.execute("INSERT INTO students (first, last, house, birth) VALUES(?, ?, ?, ?)", name[0], name[1], row['house'], row['birth'])
    if len(name) == 3:
         db.execute("INSERT INTO students (first, middle, last, house, birth) VALUES(?, ?, ?, ?, ?)", name[0], name[1], name[2], row['house'], row['birth'])