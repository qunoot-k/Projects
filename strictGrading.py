from fractions import Fraction

# function to return a list of students passed
def who_passed(student_dict):

  #empty list for adding names of students who passed
  passed_students = []

  # iterate over student name 
  # student variable here is the key
  for student in student_dict:
    total = 0

    # iterate over the list of marks for a particular student
    # i is a singlr mark of a student in the marks list of values
    for i in student_dict[student]:

      #convert marks from string to fraction and then float
      #add all the marks togetehr
      total += float(Fraction(i))

    # divide the total marks by number of subjects
    result = total/len(student_dict[student])

    # result is 100% if result = 1
    if result == 1:

      # passed student added to the list
      # here the key is being added to list since key is the student name
      passed_students.append(student)

  #return a list of passed students sorted alphabetically
  return sorted(passed_students)

passed = who_passed({
  "Zach" : ["10/10", "2/4"],
  "Fred" : ["7/9", "2/3"]
})

# print the passed students list returned
passed
