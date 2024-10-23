package zeotap.project;

import java.util.*;

public class RuleEngine {

    private final Set<String> validAttributes = Set.of("age", "department", "salary", "experience");

    public Node createRule(String rule) {
        if (rule == null || rule.trim().isEmpty()) {
            throw new InvalidRuleException("Rule string cannot be null or empty.");
        }
        String[] tokens = tokenizeRule(rule);
        validateRuleTokens(tokens);
        return parseTokens(tokens);
    }

    public boolean evaluateRuleAST(Node node, Map<String, Object> data) {
        if (node == null) {
            throw new InvalidRuleException("Node cannot be null");
        }
        return new RuleEvaluator().evaluateRule(node, data);
    }

    private void validateRuleTokens(String[] tokens) {
        Stack<String> parentheses = new Stack<>();
        for (String token : tokens) {
            if (token.equals("(")) parentheses.push(token);
            if (token.equals(")")) {
                if (parentheses.isEmpty()) throw new InvalidRuleException("Mismatched parentheses.");
                parentheses.pop();
            }
        }
        if (!parentheses.isEmpty()) throw new InvalidRuleException("Mismatched parentheses.");
    }

    private String[] tokenizeRule(String rule) {
        return rule.replaceAll("([()])", " $1 ").trim().split("\\s+");
    }

    private Node parseTokens(String[] tokens) {
        Stack<Node> stack = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
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
                
                String[] operandParts = token.split(" ");
                if (operandParts.length == 3) {
                    String attribute = operandParts[0];
                    String operator = operandParts[1];
                    String value = operandParts[2].replace("'", ""); 

                    if (!validAttributes.contains(attribute)) {
                        throw new InvalidRuleException("Invalid attribute: " + attribute);
                    }

                    stack.push(new Node("operand", null, null, token)); 
                } else {
                    throw new InvalidRuleException("Invalid operand structure: " + token);
                }
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

    public Node combineRules(List<Node> rules) {
        if (rules.isEmpty()) {
            throw new InvalidRuleException("No rules to combine.");
        }
        Node combined = rules.get(0);
        for (int i = 1; i < rules.size(); i++) {
            combined = new Node("operator", combined, rules.get(i), "AND");
        }
        return combined;
    }
}
