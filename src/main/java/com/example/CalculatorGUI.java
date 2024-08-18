package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class CalculatorGUI extends JFrame {

        private JTextField inputField;
        private JButton[] button;
        private JLabel Result;

        public CalculatorGUI() {

            setTitle("Basic Calculator");
            setSize(300, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            inputField = new JTextField();
            inputField.setFont(new Font("Calisto MT", Font.PLAIN, 25));
            inputField.setHorizontalAlignment(JTextField.RIGHT);
            inputField.setEditable(false);
            add(inputField, BorderLayout.NORTH);


            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));
            button = new JButton[20];

            String[] buttonLabels = {
                    "7", "8", "9", "/",
                    "4", "5", "6", "*",
                    "1", "2", "3", "-",
                    "0", "(", ")", "+",
                    "C", "=", ".", " "
            };

            for (int i = 0; i < 20; i++) {
                button[i] = new JButton(buttonLabels[i]);
                button[i].setFont(new Font("Calisto MT", Font.BOLD, 18));
                button[i].setFocusPainted(false);

                if (buttonLabels[i].matches("[0-9]")) {
                    button[i].setForeground(Color.ORANGE);
                } else if (buttonLabels[i].matches("[+\\-*/()]")) {
                    button[i].setForeground(Color.PINK);
                }

                button[i].addActionListener(new ButtonClickListener());
                buttonPanel.add(button[i]);
            }

            add(buttonPanel, BorderLayout.CENTER);

            Result = new JLabel("Result: ");
            Result.setFont(new Font("Calisto MT", Font.BOLD, 24));
            Result.setHorizontalAlignment(JLabel.RIGHT);
            add(Result, BorderLayout.SOUTH);
            setVisible(true);
        }

        private class ButtonClickListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                String buttonText = button.getText();

                if (buttonText.equals("=")) {
                    String expression = inputField.getText();
                    try {
                        int result = evaluate(expression);
                        Result.setText("Result: " + result);
                    } catch (Exception ex) {
                        Result.setText("Error: Invalid Expression");
                    }
                } else if (buttonText.equals("C")) {
                    inputField.setText("");
                    Result.setText("Result: ");
                } else {
                    inputField.setText(inputField.getText() + buttonText);
                }
            }
        }

        private int evaluate(String expression) {
            Stack<Integer> values = new Stack<>();
            Stack<Character> operators = new Stack<>();
            for (int i = 0; i < expression.length(); i++) {
                char ch = expression.charAt(i);
                if (ch == ' ') continue;
                if (ch >= '0' && ch <= '9') {
                    StringBuilder sb = new StringBuilder();
                    while (i < expression.length() && expression.charAt(i) >= '0' && expression.charAt(i) <= '9') {
                        sb.append(expression.charAt(i++));
                    }
                    values.push(Integer.parseInt(sb.toString()));
                    i--;
                } else if (ch == '(') {
                    operators.push(ch);
                } else if (ch == ')') {
                    while (operators.peek() != '(') {
                        values.push(applyOp(operators.pop(), values.pop(), values.pop()));
                    }
                    operators.pop();
                } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                    while (!operators.isEmpty() && hasPrecedence(ch, operators.peek())) {
                        values.push(applyOp(operators.pop(), values.pop(), values.pop()));
                    }
                    operators.push(ch);
                }
            }
            while (!operators.isEmpty()) {
                values.push(applyOp(operators.pop(), values.pop(), values.pop()));
            }
            return values.pop();
        }

        private boolean hasPrecedence(char op1, char op2) {
            if (op2 == '(' || op2 == ')') return false;
            if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
            return true;
        }

        private int applyOp(char op, int b, int a) {
            switch (op) {
                case '+':
                    return a + b;
                case '-':
                    return a - b;
                case '*':
                    return a * b;
                case '/':
                    if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                    return a / b;
            }
            return 0;
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(CalculatorGUI::new);
        }
    }

