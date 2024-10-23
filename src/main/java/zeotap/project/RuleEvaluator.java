package zeotap.project;

import java.util.Map;

public class RuleEvaluator {
    public boolean evaluateRule(Node node, Map<String, Object> data) {
        if (node == null) {
            throw new InvalidRuleException("Node cannot be null");
        }

        if (node.type.equals("operand")) {
            // Split the operand string into parts (e.g., "age > 30")
            String[] parts = node.value.split(" ");
            if (parts.length != 3) {
                throw new InvalidRuleException("Invalid operand structure: " + node.value);
            }
            String attribute = parts[0];
            String operator = parts[1];
            String value = parts[2].replace("'", ""); // Handle string literals

            // Get the actual value from data
            Object actualValue = data.get(attribute);
            
            // Ensure the actual value is not null and perform the comparison
            if (actualValue == null) {
                throw new InvalidRuleException("Invalid operand structure: " + attribute);
            }

            return compareValues(actualValue, operator, value);
        } else if (node.type.equals("operator")) {
            boolean leftResult = evaluateRule(node.left, data);
            boolean rightResult = evaluateRule(node.right, data);
            
            if (node.value.equalsIgnoreCase("AND")) {
                return leftResult && rightResult;
            } else if (node.value.equalsIgnoreCase("OR")) {
                return leftResult || rightResult;
            }
        }

        throw new InvalidRuleException("Invalid node structure");
    }

    private boolean compareValues(Object actualValue, String operator, String value) {
        switch (operator) {
            case ">":
                return ((Number) actualValue).doubleValue() > Double.parseDouble(value);
            case "<":
                return ((Number) actualValue).doubleValue() < Double.parseDouble(value);
            case "=":
                return actualValue.toString().equals(value);
            default:
                throw new InvalidRuleException("Unknown operator: " + operator);
        }
    }
}
