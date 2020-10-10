package com.project.mycalculator;
import java.math.BigDecimal;
import java.util.Stack;


public class FormulaUtil {

    private int scale;


    private Stack<BigDecimal> numberStack = null;

    private Stack<Character> symbolStack = null;

    public FormulaUtil(int scale) {
        super();
        this.scale = scale;
    }

    public FormulaUtil() {
        this(13);
    }


    public BigDecimal caculate(String numStr) {
        numStr = removeStrSpace(numStr); // 去除空格

        if (numStr.length() >= 1
                && !"=".equals(numStr.charAt(numStr.length() - 1) + "")) {
            numStr += "=";
        }

        if (!isStandard(numStr)) {
            System.err.println("error");
            return null;
        }

        if (numberStack == null) {
            numberStack = new Stack<BigDecimal>();
        }
        numberStack.clear();
        if (symbolStack == null) {
            symbolStack = new Stack<Character>();
        }
        symbolStack.clear();

        StringBuffer temp = new StringBuffer();

        int symbolNum = 0;
        for (int i = 0; i < numStr.length(); i++) {
            char ch = numStr.charAt(i);
            if (ch == '+' || ch == '-' || ch =='*' || ch =='/') symbolNum++;
            if (isNumber(ch)) {
                temp.append(ch);
            } else {
                String tempStr = temp.toString();
                if (!tempStr.isEmpty()) {

                    BigDecimal num = new BigDecimal(tempStr);
                    numberStack.push(num);
                    temp = new StringBuffer();
                }

                while (!comparePri(ch) && !symbolStack.empty()) {
                    BigDecimal b = numberStack.pop();

                    if ((symbolStack.peek() == '-' || symbolStack.peek() == '+') && (numberStack.isEmpty() || symbolNum != numberStack.size())) {
                        char t = symbolStack.pop();
                        symbolNum--;
                        if(t == '+') numberStack.push(new BigDecimal(0).add(b));
                        else {
                            numberStack.push(new BigDecimal(0).subtract(b));
                        }
                        break;
                    }
                    BigDecimal a = numberStack.pop();

                    symbolNum--;
                    switch ((char) symbolStack.pop()) {
                        case '+':
                            numberStack.push(a.add(b));
                            break;
                        case '-':
                            numberStack.push(a.subtract(b));
                            break;
                        case '*':
                            numberStack.push(a.multiply(b));
                            break;
                        case '/':
                            try {
                                if (b.equals(new BigDecimal(0))) return null;
                                numberStack.push(a.divide(b));
                            } catch (java.lang.ArithmeticException e) {

                                numberStack.push(a.divide(b, this.scale,
                                        BigDecimal.ROUND_HALF_EVEN));
                            }
                            break;
                        default:
                            break;
                    }
                }
                if (ch != '=') {
                    symbolStack.push(new Character(ch));
                    if (ch == ')') {
                        symbolStack.pop();
                        symbolStack.pop();
                    }
                }
            }
        }
        return numberStack.pop();
    }


    private String removeStrSpace(String str) {
        return str != null ? str.replaceAll(" ", "") : "";
    }


    private boolean isStandard(String numStr) {
        if (numStr == null || numStr.isEmpty())
            return false;
        Stack<Character> stack = new Stack<Character>();
        boolean b = false;
        for (int i = 0; i < numStr.length(); i++) {
            char n = numStr.charAt(i);

            if (!(isNumber(n) || "(".equals(n + "") || ")".equals(n + "")
                    || "+".equals(n + "") || "-".equals(n + "")
                    || "*".equals(n + "") || "/".equals(n + "") || "=".equals(n
                    + ""))) {
                return false;
            }

            if ("(".equals(n + "")) {
                stack.push(n);
            }
            if (")".equals(n + "")) {
                if (stack.isEmpty() || !"(".equals((char) stack.pop() + ""))
                    return false;
            }

            if ("=".equals(n + "")) {
                if (b)
                    return false;
                b = true;
            }
        }

        if (!stack.isEmpty())
            return false;

        if (!("=".equals(numStr.charAt(numStr.length() - 1) + "")))
            return false;
        return true;
    }


    private boolean isNumber(char num) {
        if ((num >= '0' && num <= '9') || num == '.')
            return true;
        return false;
    }


    private boolean comparePri(char symbol) {
        if (symbolStack.empty()) {
            return true;
        }



        char top = (char) symbolStack.peek();
        if (top == '(') {
            return true;
        }

        switch (symbol) {
            case '(':
                return true;
            case '*': {
                if (top == '+' || top == '-')
                    return true;
                else
                    return false;
            }
            case '/': {
                if (top == '+' || top == '-')
                    return true;
                else
                    return false;
            }
            case '+':
                return false;
            case '-':
                return false;
            case ')':
                return false;
            case '=':
                return false;
            default:
                break;
        }
        return true;
    }


}