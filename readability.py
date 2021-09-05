from cs50 import get_string

word = 0
sentence = 0
letter = 0
# get text from user
text = get_string("Text: ")
# length of the text
length = len(text)

for i in range(length):
    # if it is an alphabet
    if text[i] >= 'A' and text[i] <= 'Z' or text[i] >= 'a' and text[i] <= 'z':
        # increase letter count
        letter += 1
        # If it is the first word
        if (i == 0):
            # increase word count
            word += 1
        # startimg of a word
        elif text[i - 1] == ' ' or text[i - 1] == '"':
            word += 1

    # marks the end of sentence
    elif text[i] == '.' or text[i] == '!' or text[i] == '?':
        # increase sentence count
        sentence += 1

# average letters per 100 word
avgLetters = (letter / word) * 100
# average letters per 100 word
avgSentences = (sentence / word) * 100
# calculate grade
# round function converts float to the nearest integer
index = round(0.0588 * avgLetters - 0.296 * avgSentences - 15.8)


# print Grade
if index < 1:
    print("Before Grade 1")

elif index < 16:
    # casting float to int
    print("Grade", int(index))

else:
    print("Grade 16+")