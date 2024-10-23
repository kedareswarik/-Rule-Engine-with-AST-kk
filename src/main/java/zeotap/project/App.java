package zeotap.project;

import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        // Create instances of RuleParser and RuleEvaluator
        RuleParser ruleParser = new RuleParser();
        RuleEvaluator ruleEvaluator = new RuleEvaluator();

        // Define a sample rule string
        String rule = "(age > 30 AND salary < 50000) OR (department = 'HR')";

        // Create a map to hold data for evaluation
        Map<String, Object> data = new HashMap<>();
        data.put("age", 32);
        data.put("salary", 40000);
        data.put("department", "HR");

        try {
            // Parse the rule to create the AST
            Node ast = ruleParser.createRule(rule);
            // Evaluate the rule against the data
            boolean result = ruleEvaluator.evaluateRule(ast, data);
            // Print the evaluation result
            System.out.println("Evaluation Result: " + result);
        } catch (InvalidRuleException e) {
            // Handle any exceptions related to rule parsing or evaluation
            System.err.println("Error: " + e.getMessage());
        }
    }
}
