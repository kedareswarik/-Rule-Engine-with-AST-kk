package zeotap.project;

import java.util.Stack;

public class RuleParser {

    // Function to create AST from rule string
    public Node createRule(String rule) {
        // Validate the rule string
        if (rule == null || rule.trim().isEmpty()) {
            throw new InvalidRuleException("Rule string cannot be null or empty.");
        }

        // Handle parentheses and tokenization
        String[] tokens = tokenizeRule(rule);
        return parseTokens(tokens);
    }

    // Function to tokenize the rule string, handling parentheses
    private String[] tokenizeRule(String rule) {
        // This regex will split the rule while keeping parentheses intact
        return rule.replaceAll("([()])", " $1 ").trim().split("\\s+");
    }

    // Function to parse tokens and build the AST
    private Node parseTokens(String[] tokens) {
        Stack<Node> stack = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (token.equals("(")) {
                // Open parentheses, push a placeholder for it
                operators.push(token);
            } else if (token.equals(")")) {
                // Closing parentheses, process the expression inside
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    Node right = stack.pop();
                    Node left = stack.pop();
                    String operator = operators.pop();
                    stack.push(new Node("operator", left, right, operator));
                }
                if (operators.isEmpty() || !operators.peek().equals("(")) {
                    throw new InvalidRuleException("Mismatched parentheses.");
                }
                operators.pop(); // Remove the opening parentheses
            } else if (token.equalsIgnoreCase("AND") || token.equalsIgnoreCase("OR")) {
                // Push the operator to the operator stack
                operators.push(token);
            } else {
                // Operand, handle the whole expression for attributes
                String operand = token;

                // Check if the next tokens are part of the operand
                if (i + 2 < tokens.length && isValidOperator(tokens[i + 1])) {
                    operand += " " + tokens[i + 1] + " " + tokens[i + 2];
                    i += 2; // Move the index to skip over the operator and value
                } else {
                    throw new InvalidRuleException("Invalid operand structure: " + token);
                }

                stack.push(new Node("operand", null, null, operand)); // Push the entire expression as the value
            }
        }

        // Process remaining operators
        while (!operators.isEmpty()) {
            String operator = operators.pop();
            if (operator.equals("(")) {
                throw new InvalidRuleException("Mismatched parentheses.");
            }
            Node right = stack.pop();
            Node left = stack.pop();
            stack.push(new Node("operator", left, right, operator));
        }

        if (stack.size() != 1) {
            throw new InvalidRuleException("Invalid rule structure.");
        }

        return stack.pop();
    }

    private boolean isValidOperator(String token) {
        return token.equals(">") || token.equals("<") || token.equals("="); // Add more operators if needed
    }
}
