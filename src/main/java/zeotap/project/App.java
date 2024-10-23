package zeotap.project;

import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) {
       
        RuleParser ruleParser = new RuleParser();
        RuleEvaluator ruleEvaluator = new RuleEvaluator();

       
        String rule = "(age > 30 AND salary < 50000) OR (department = 'HR')";

       
        Map<String, Object> data = new HashMap<>();
        data.put("age", 32);
        data.put("salary", 40000);
        data.put("department", "HR");

        try {
            
            Node ast = ruleParser.createRule(rule);
            
            boolean result = ruleEvaluator.evaluateRule(ast, data);
            
            System.out.println("Evaluation Result: " + result);
        } catch (InvalidRuleException e) {
            
            System.err.println("Error: " + e.getMessage());
        }
    }
}
