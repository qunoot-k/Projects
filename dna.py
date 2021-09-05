from sys import argv, exit
import csv

# if incorrect execution of program
if len(argv) != 3:
    print("Missing command line arguments !")
    exit(1)

# open the CSV file and read its contents into memory
with open(argv[1], 'r') as file:
    reader = csv.DictReader(file)
    STRs = reader.fieldnames
    # store sequence names
    STRs = STRs[1:]
    dict_list = []
    for line in reader:
        dict_list.append(line)

# dict for storing no of times a sequence is repeated
num_of_occurance = {STR_pattern: 0 for STR_pattern in STRs}

# open the DNA sequence and read its contents into memory.
sequence = open(argv[2], "r")
data = sequence.readline()
sequence.close()
seq_length = len(data)

# count the max number of times a sequence is consecituively repeated
for STR in STRs:
    max_occurance = 0
    # find the first occurance of STR
    i = data.find(STR)
    if i == -1:
        continue
    while i in range(seq_length):
        occurance = 0
        while data[i:i+len(STR)] == STR:
            occurance += 1
            i += len(STR)
        if occurance > max_occurance:
            max_occurance = occurance
        index = data.find(STR, i, seq_length)
        if i == -1:
            break
        i = index
    num_of_occurance[STR] = max_occurance

# Find a match
for name in dict_list:
    dna_match = 0
    for STR in STRs:
        temp = int(name[STR])
        if temp != num_of_occurance[STR]:
            break
        dna_match += 1
    # Match has been found
    if dna_match == len(STRs):
        print(f"{name['name']}")
        exit(0)

# If no match found
print("No match")
