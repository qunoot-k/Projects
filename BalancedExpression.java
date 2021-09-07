import java.util.Scanner;
import java.util.Stack;

public class BalancedExpression {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		String expression = new String();
		System.out.print("Enter an Algebraic expression : ");
		expression = input.nextLine();
		boolean check = checkExpression(expression);
		if (check == false)
			System.out.println("\nThe expression is not balanced !");
		else
			System.out.println("\nThe expression is balanced :D");
		input.close();
	}

	public static boolean checkExpression(String expression) {

		Scanner brackets = new Scanner(expression);
		brackets.useDelimiter("\\[\\]\\{\\}\\(\\)");
		char parantheses1;
		char parantheses2;
		Stack<Character> stack = new Stack<Character>();
		int i = 0;
		while ((brackets.hasNextLine()) && (i < expression.length())) {
			parantheses1 = expression.charAt(i);
			if (parantheses1 == '{' || parantheses1 == '(' || parantheses1 == '[')
				stack.push(parantheses1);
			if (parantheses1 == '}' || parantheses1 == ')' || parantheses1 == ']') {
				if (stack.isEmpty())
					return false;
				parantheses2 = stack.peek();
				if (parantheses1 == '}' && parantheses2 == '{' || parantheses1 == ')' && parantheses2 == '('
						|| parantheses1 == ']' && parantheses2 == '[')
					stack.pop();
				else
					return false;
			}
			i++;
		}

		return stack.isEmpty() ? true : false;
	}
}
