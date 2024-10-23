package zeotap.project;

import java.util.Stack;

public class RuleParser {

   
    public Node createRule(String rule) {
        
        if (rule == null || rule.trim().isEmpty()) {
            throw new InvalidRuleException("Rule string cannot be null or empty.");
        }

        
        String[] tokens = tokenizeRule(rule);
        return parseTokens(tokens);
    }

    
    private String[] tokenizeRule(String rule) {
        
        return rule.replaceAll("([()])", " $1 ").trim().split("\\s+");
    }

   
    private Node parseTokens(String[] tokens) {
        Stack<Node> stack = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (token.equals("(")) {
               
                operators.push(token);
            } else if (token.equals(")")) {
               
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    Node right = stack.pop();
                    Node left = stack.pop();
                    String operator = operators.pop();
                    stack.push(new Node("operator", left, right, operator));
                }
                if (operators.isEmpty() || !operators.peek().equals("(")) {
                    throw new InvalidRuleException("Mismatched parentheses.");
                }
                operators.pop(); 
            } else if (token.equalsIgnoreCase("AND") || token.equalsIgnoreCase("OR")) {
               
                operators.push(token);
            } else {
                
                String operand = token;

              
                if (i + 2 < tokens.length && isValidOperator(tokens[i + 1])) {
                    operand += " " + tokens[i + 1] + " " + tokens[i + 2];
                    i += 2; 
                } else {
                    throw new InvalidRuleException("Invalid operand structure: " + token);
                }

                stack.push(new Node("operand", null, null, operand)); 
            }
        }

       
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
        return token.equals(">") || token.equals("<") || token.equals("="); 
    }
}
