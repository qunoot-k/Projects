import re

# function to evaluate the inequality of an expression
def correct_signs(exp): 

  # check if it is a valid expression
    for i in exp:
      if not (i.isnumeric() or i in "< >"):
        return False

    # split the numbers and operators
    split_exp = re.split(r'\s', exp)
    exp_length = len(split_exp)
    i = 1

    # evalaute the expression
    # set count of i such that the index is always an operator
    while i < exp_length:
      if (split_exp[i] == '<'):
        value = int(split_exp[i-1]) < int(split_exp[i+1])
      else:
        value = int(split_exp[i-1]) > int(split_exp[i+1])
      if value == False:
        return False
      i += 2
    return True
      
#read an inequality expression from user
expression = input("Enter an inequality expression: ")

# call the correct_signs fuction
sign = correct_signs(expression)

# print the result
print(sign)
