package com.example.calculator_app;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {


    private TextView solView, resultView;
    private StringBuilder inputExpression = new StringBuilder();
    private boolean isNewInput = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solView = findViewById(R.id.sol_view);
        resultView = findViewById(R.id.result_view);

        findViewById(R.id.button_0).setOnClickListener(this::onNumberClick);
        findViewById(R.id.button_1).setOnClickListener(this::onNumberClick);
        findViewById(R.id.button_2).setOnClickListener(this::onNumberClick);
        findViewById(R.id.button_3).setOnClickListener(this::onNumberClick);
        findViewById(R.id.button_4).setOnClickListener(this::onNumberClick);
        findViewById(R.id.button_5).setOnClickListener(this::onNumberClick);
        findViewById(R.id.button_6).setOnClickListener(this::onNumberClick);
        findViewById(R.id.button_7).setOnClickListener(this::onNumberClick);
        findViewById(R.id.button_8).setOnClickListener(this::onNumberClick);
        findViewById(R.id.button_9).setOnClickListener(this::onNumberClick);

        findViewById(R.id.button_add).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.button_subtraction).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.button_multi).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.button_Divide).setOnClickListener(this::onOperatorClick);

        findViewById(R.id.button_equal).setOnClickListener(this::onEqualsClick);
        findViewById(R.id.button_C).setOnClickListener(this::onClearClick);
        findViewById(R.id.button_ac).setOnClickListener(this::onClearClick);
        findViewById(R.id.button_dot).setOnClickListener(this::onNumberClick);
        findViewById(R.id.button_openBracket).setOnClickListener(this::onNumberClick);
        findViewById(R.id.button_closeBracket).setOnClickListener(this::onNumberClick);
    }

    public void onNumberClick(View view) {
        Button button = (Button) view;
        if (isNewInput) {
            inputExpression.setLength(0);
            isNewInput = false;
        }
        inputExpression.append(button.getText().toString());
        solView.setText(inputExpression.toString());
    }

    public void onOperatorClick(View view) {
        Button button = (Button) view;
        if (inputExpression.length() > 0) {
            char lastChar = inputExpression.charAt(inputExpression.length() - 1);
            if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
                inputExpression.setCharAt(inputExpression.length() - 1, button.getText().charAt(0));
            } else {
                inputExpression.append(button.getText().toString());
            }
            solView.setText(inputExpression.toString());
        }
    }

    public void onEqualsClick(View view) {
        try {
            double result = evaluateExpression(inputExpression.toString());
            resultView.setText(new DecimalFormat("#.########").format(result));
            isNewInput = true;
        } catch (Exception e) {
            resultView.setText("Error");
        }
    }

    public void onClearClick(View view) {
        inputExpression.setLength(0);
        solView.setText("0");
        resultView.setText("0");
    }

    private double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i++));
                }
                numbers.push(Double.parseDouble(num.toString()));
                continue;
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop();
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
            i++;
        }
        while (!operators.isEmpty()) {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }
        return numbers.pop();
    }

    private int precedence(char operator) {
        if (operator == '+' || operator == '-') return 1;
        if (operator == '*' || operator == '/') return 2;
        return -1;
    }

    private double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;
            default: return 0;
        }
    }
    }