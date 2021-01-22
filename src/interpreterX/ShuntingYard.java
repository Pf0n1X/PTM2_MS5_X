package interpreterX;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import expressionX.Number;
import expressionX.Plus;
import expressionX.BinaryExpression;
import expressionX.Div;
import expressionX.Expression;
import expressionX.Minus;
import expressionX.Mul;

public class ShuntingYard {

	// Data Members
	static int flag = 0;

	// Methods
	public static Double calc(String strexpression, DataManager environment) {
		ArrayList<String> arrayList;
		Queue<String> queue;
		Stack<Expression> stack;
		Expression expression;
		double d;

		try {
			arrayList = SplitStringExpression(strexpression);
			queue = ArrayListStringtoQueueString(arrayList);
			stack = QueueStringtoStackExpression(queue, environment);
			expression = formatExpressionStackToASingleExpression(stack, environment);
		} catch (Exception e) {
			return 0.0;
		}

		d = expression.calculate();
		d *= 100;
		d = Math.floor(d);
		d /= 100;

		return d;
	}

	private static ArrayList<String> SplitStringExpression(String expression) throws Exception {
		expression = expression.replace('�', '-');
		ArrayList<String> arrayList = new ArrayList<>();
		String[] expressionchar = expression.split("");
		String num = "";
		for (int i = 0; i < expressionchar.length; i++) {
			if (IsBineryExpression(expressionchar[i]) && (i == 0 || (!(IsNumber(expressionchar[i - 1])
					|| expressionchar[i - 1].equals("(") || expressionchar[i - 1].equals(")"))))) {
				arrayList.add("(");
				arrayList.add("0");
				flag = 1;
			}
			if (IsNumber(expressionchar[i])) {
				for (; i < expressionchar.length
						&& ((('0' <= expressionchar[i].charAt(0) && expressionchar[i].charAt(0) <= '9')
								|| ('a' <= expressionchar[i].charAt(0) && expressionchar[i].charAt(0) <= 'z')
								|| ('A' <= expressionchar[i].charAt(0) && expressionchar[i].charAt(0) <= 'Z'))
								|| expressionchar[i].charAt(0) == '.'); i++) {
					num += expressionchar[i].charAt(0);
				}
				arrayList.add(num);
				num = "";
				i--;
				if (flag == 1) {
					arrayList.add(")");
					flag = 0;
				}
			} else if (IsBineryExpression(expressionchar[i]) || expressionchar[i].equals("(")
					|| expressionchar[i].equals(")")) {
				if (flag != 0) {
					if (expressionchar[i].equals("(")) {
						flag++;
					} else if (expressionchar[i].equals(")")) {
						flag--;
					}
				}
				arrayList.add(expressionchar[i]);
			} else {
				throw new Exception("Wrong input at index= " + i + " as char= '" + expressionchar[i]
						+ "' for expression='" + expression + "'");
			}
		}
		return arrayList;
	}

	private static Queue<String> ArrayListStringtoQueueString(ArrayList<String> arrayListexpression) throws Exception {
		Stack<String> stack = new Stack<>();
		Queue<String> queue = new LinkedList<>();
		for (String str : arrayListexpression) {
			if (IsNumber(str)) {
				queue.add(str);
			} else if (IsBineryExpression(str)) {
				while ((!stack.isEmpty()) && IsGreaterPrecedence(str, stack.peek())) {
					queue.add(stack.pop());
				}

				stack.push(str);
			} else {
				switch (str) {
				case "(":
					stack.push(str);
					break;
				case ")": {
					boolean flag = false;
					while (!stack.isEmpty()) {
						if (stack.peek().equals("(")) {
							stack.pop();
							flag = true;
							break;
						} else {
							queue.add(stack.pop());
						}
					}
					if (stack.isEmpty() && !flag) {
						throw new Exception("Too many ')' then '('");
					}
					break;
				}
				default:
					throw new Exception("Error= " + str);
				}
			}
		}

		while (!stack.isEmpty()) {
			String string = stack.pop();
			if (string.equals("(")) {
				throw new Exception("Parenthesis parse error.");
			}

			queue.add(string);
		}

		return queue;
	}

	private static boolean IsNumber(String str) {

		if (str.charAt(0) == '-') {

			if (str.length() == 1) {
				return false;
			}

			for (int i = 1; i < str.length(); i++) {
				char curChar = str.charAt(i);

				if (!((('0' <= curChar && curChar <= '9') || ('a' <= curChar && curChar <= 'z')
						|| ('A' <= curChar && curChar <= 'Z')) || curChar == '.')) {
					return false;
				}
			}
		} else {

			for (char curChar : str.toCharArray()) {
				if (!((('0' <= curChar && curChar <= '9') || ('a' <= curChar && curChar <= 'z')
						|| ('A' <= curChar && curChar <= 'Z')) || curChar == '.')) {
					return false;
				}
			}
		}

		return true;
	}

	private static boolean IsBineryExpression(String str) {

		// Check it the expression is either a plus, minus multiply or division
		// expression.
		switch (str) {
		case "+":
		case "-":
		case "*":
		case "/": {
			return true;
		}
		default:
			return false;
		}
	}

	private static Number StringtoNumber(String str, DataManager environment) {
		Number number = new Number(str, environment);

		return number;
	}

	private static BinaryExpression StringtoBineryExpression(String str) {
		BinaryExpression binaryExpression = null;
		switch (str) {
		case "+": {
			binaryExpression = new Plus(null, null);
			break;
		}
		case "-": {
			binaryExpression = new Minus(null, null);
			break;
		}
		case "*": {
			binaryExpression = new Mul(null, null);
			break;
		}
		case "/": {
			binaryExpression = new Div(null, null);
			break;
		}
		default:
			break;
		}
		return binaryExpression;
	}

	private static boolean IsGreaterPrecedence(String binaryExpression, String binaryExpStack) {
		switch (binaryExpStack) {
		case "*":
		case "/": {
			if (binaryExpression.equals("*") || binaryExpression.equals("/")) {
				return true;
			} else if (binaryExpression.equals("+") || binaryExpression.equals("-")) {
				return true;
			}
		}
		case "+":
		case "-": {
			if (binaryExpression.equals("*") || binaryExpression.equals("/")) {
				return false;
			} else if (binaryExpression.equals("+") || binaryExpression.equals("-")) {
				return true;
			}
		}
		default:
			break;
		}

		return false;
	}

	private static Stack<Expression> QueueStringtoStackExpression(Queue<String> queue, DataManager environment) {
		Stack<Expression> expressions = new Stack<>();

		while (!queue.isEmpty()) {
			if (IsNumber(queue.peek())) {
				expressions.push(StringtoNumber(queue.remove(), environment));
			} else if (IsBineryExpression(queue.peek())) {
				expressions.push(StringtoBineryExpression(queue.remove()));
			}
		}

		return expressions;
	}

	private static Expression formatExpressionStackToASingleExpression(Stack<Expression> stack,
			DataManager environment) {

		// If the stack is empty simply return a "0" number response.
		if (stack.isEmpty()) {
			return new Number("0", environment);
		}

		Expression expression = stack.pop();

		if (expression.getClass() != Number.class) {
			((BinaryExpression) expression).setRight(formatExpressionStackToASingleExpression(stack, environment));
			((BinaryExpression) expression).setLeft(formatExpressionStackToASingleExpression(stack, environment));
		}

		return expression;
	}
}