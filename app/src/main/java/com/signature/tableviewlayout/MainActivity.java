package com.signature.tableviewlayout;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    private EditText inputNumber;
    public TextView showResult;

    private boolean isOperatorAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNumber = findViewById(R.id.newNumber);
        showResult = findViewById(R.id.showResult);
        inputNumber.setShowSoftInputOnFocus(false);
        showResult.setText("");
    }

    private void updateText(String strToAdd) {
        try {
            String oldString = inputNumber.getText().toString();
            int cursorPos = inputNumber.getSelectionStart();
            String leftString = oldString.substring(0, cursorPos);
            String rightString = oldString.substring(cursorPos);

            Pattern pattern = Pattern.compile("[\\+|\\-|\\×|\\/|\\^|)]$");
            Matcher matcher = pattern.matcher(leftString);

            String resultStr = "";
            if (strToAdd.matches("^[\\+|\\-|\\×|\\/|\\^]$") && matcher.find()) {
                resultStr = leftString.replaceAll("[\\+|\\-|\\×|\\/|\\^]$", strToAdd).replaceAll("\\)$", ")".concat(strToAdd));
                if (resultStr.length() > leftString.length()) {
                    cursorPos += 1;
                }
                resultStr = resultStr.concat(rightString);
            } else {
                resultStr = leftString.concat(strToAdd).concat(rightString);
                cursorPos += 1;
            }

            inputNumber.setText(resultStr);
            inputNumber.setSelection(cursorPos);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void zeroBTN(View view) {
        updateText("0");
        calculate();
    }

    public void oneBTN(View view) {
        updateText("1");
        calculate();
    }

    public void twoBTN(View view) {
        updateText("2");
        calculate();
    }

    public void threeBTN(View view) {
        updateText("3");
        calculate();
    }

    public void fourBTN(View view) {
        updateText("4");
        calculate();
    }

    public void fiveBTN(View view) {
        updateText("5");
        calculate();
    }

    public void sixBTN(View view) {
        updateText("6");
        calculate();
    }

    public void sevenBTN(View view) {
        updateText("7");
        calculate();
    }

    public void eightBTN(View view) {
        updateText("8");
        calculate();
    }

    public void nineBTN(View view) {
        updateText("9");
        calculate();
    }

    public void addBTN(View view) {
        if (!inputNumber.getText().toString().isEmpty()) {
            updateText("+");
            isOperatorAvailable = true;
        }
    }

    public void subtractBTN(View view) {
        if (!inputNumber.getText().toString().isEmpty()) {
            updateText("-");
            isOperatorAvailable = true;
        }
    }

    public void multiplyBTN(View view) {
        if (!inputNumber.getText().toString().isEmpty()) {
            updateText("×");
            isOperatorAvailable = true;
        }
    }

    public void divideBTN(View view) {
        if (!inputNumber.getText().toString().isEmpty()) {
            updateText("/");
            isOperatorAvailable = true;
        }
    }

    public void plusMinusBTN(View view) {
        String value = inputNumber.getText().toString();
        if (value.length() == 0)
        {
            updateText("-");
        }
//        else {
//            try {
//                Double doubleValue = Double.valueOf(value);
//                doubleValue *= -1;
//                inputNumber.setText(doubleValue.toString());
//            } catch (NumberFormatException e)
//            {
//                inputNumber.setText("");
//            }
//        }
        isOperatorAvailable = true;
    }

    public void decimalBTN(View view) {
        updateText(".");
    }

    public void equalBTN(View view) {
        inputNumber.setText(showResult.getText());
        int textLen = inputNumber.getText().length();
        inputNumber.setSelection(textLen);
        showResult.setText("");



    }

    public void clearBTN(View view) {
        inputNumber.setText("");
        showResult.setText("");
        isOperatorAvailable=false;
    }

    public void paraBTN(View view) {
        int cursorPos = inputNumber.getSelectionStart();
        int openPara = 0;
        int closePara = 0;
        int textLen = inputNumber.getText().length();

        for (int i = 0; i < cursorPos; i++) {
            if (inputNumber.getText().toString().substring(i, i + 1).equals("(")) {
                openPara += 1;
            }

            if (inputNumber.getText().toString().substring(i, i + 1).equals(")")) {
                closePara += 1;
            }
        }

        if (openPara == closePara || inputNumber.getText().toString().substring(textLen - 1, textLen).equals("(")) {
            updateText("(");
        } else if (closePara < openPara && !inputNumber.getText().toString().substring(textLen - 1, textLen).equals("(")) {
            updateText(")");
        }

        inputNumber.setSelection(cursorPos + 1);
    }

    public void powerBTN(View view) {
        if (!inputNumber.getText().toString().isEmpty())
        {
            updateText("^");
            isOperatorAvailable = true;
        }
    }

    public void backSpaceBTN(View view) {
        int cursorPos = inputNumber.getSelectionStart();
        int textLen = inputNumber.getText().length();

        if (cursorPos != 0 && textLen != 0) {
            SpannableStringBuilder selection = (SpannableStringBuilder) inputNumber.getText();
            selection.replace(cursorPos - 1, cursorPos, "");
            inputNumber.setText(selection);
            inputNumber.setSelection(cursorPos - 1);
        }

        if (textLen <= 0) {
            clearBTN(null);
        }

    }

    private void calculate() {
        if (isOperatorAvailable) {
            String postfixExp = infixToPostfix(inputNumber.getText().toString());
            if (postfixExp.equals("Invalid Expression")) {
                showResult.setText(postfixExp);
            } else {
                double result = evaluatePostfix(postfixExp);
                if (String.valueOf(result).substring(String.valueOf(result).lastIndexOf(".") + 1).matches("0*")) {
                    showResult.setText(String.format(Locale.US,"%.0f", result));
                } else {
                    showResult.setText(String.valueOf(result));
                }
            }
        }
    }

    private String infixToPostfix(String expression) {
        // initializing empty String for result
        StringBuilder result = new StringBuilder("");

        // initializing empty stack
        Stack<String> stack = new Stack<>();

        StringBuilder val = new StringBuilder("");

        for (int i = 0; i<expression.length(); ++i)
        {
            char c = expression.charAt(i);

            // If the scanned character is an
            // operand, add it to output.
            if (Character.isLetterOrDigit(c) || c == '.') {
                if (i == 0) {
                    val.append("(").append(c);
                } else {
                    if (Character.isLetterOrDigit(expression.charAt(i-1)) || expression.charAt(i-1) == '.') {
                        val.append(c);
                    } else {
                        val.append("(").append(c);
                    }
                }

                if (i+1 == expression.length()) {
                    if (val.indexOf("(") != -1) {
                        val.append(")");
                        result.append(val);
                        val.delete(0, val.length());
                    }
                } else if (!Character.isLetterOrDigit(expression.charAt(i+1)) && expression.charAt(i+1) != '.') {
                    if (val.indexOf("(") != -1) {
                        val.append(")");
                        result.append(val);
                        val.delete(0, val.length());
                    }
                }
            } else if (c == '(') {
                // If the scanned character is an '(',
                // push it to the stack.
                stack.push(String.valueOf(c));
            } else if (c == ')') {
                //  If the scanned character is an ')',
                // pop and output from the stack
                // until an '(' is encountered.
                while (!stack.isEmpty() &&
                        !stack.peek().equals("("))
                    result.append(stack.pop());

                stack.pop();
            } else {
                // an operator is encountered
                while (!stack.isEmpty() && precedence(c)
                        <= precedence(stack.peek().charAt(0))){

                    result.append(stack.pop());
                }
                stack.push(String.valueOf(c));
            }
            System.out.println("val : " + val.toString());
            System.out.println("result : " + result.toString());
        }

        // pop all the operators from the stack
        while (!stack.isEmpty()){
            if(stack.peek().equals("("))
                return "Invalid Expression";
            result.append(stack.pop());
        }
        return result.toString();
    }

    private int precedence(char symbol) {
        if (symbol == '^') {
            return 3;
        } else if (symbol == '×' || symbol == '/') {
            return 2;
        } else if (symbol == '+' || symbol == '-') {
            return 1;
        } else {
            return 0;
        }
    }

    private Double evaluatePostfix(String exp)
    {
        //create a stack
        Stack<Double> stack=new Stack<>();

        StringBuilder number = new StringBuilder("");

        // Scan all characters one by one
        for(int i=0;i<exp.length();i++)
        {
            char c=exp.charAt(i);

            // If the scanned character is an operand (number here),
            // push it to the stack.
            if (c == '(') {
                //do nothing
            } else if(Character.isDigit(c)) {
                number.append(c);
//                stack.push((double) (c - '0'));
            } else if (c == ')') {
                stack.push(Double.parseDouble(number.toString()));
                number.delete(0, number.length());
            } else {
                //  If the scanned character is an operator, pop two
                // elements from stack apply the operator
                double val1 = stack.pop();
                double val2 = stack.pop();

                switch(c) {
                    case '+':
                        stack.push(val2+val1);
                        break;

                    case '-':
                        stack.push(val2-val1);
                        break;

                    case '/':
                        stack.push(val2/val1);
                        break;

                    case '×':
                        stack.push(val2*val1);
                        break;
                    case '^':
                        stack.push(Math.pow(val2,val1));
                        break;
                }
            }
        }
        return stack.pop();
    }

}