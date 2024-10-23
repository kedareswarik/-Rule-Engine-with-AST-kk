package zeotap.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RuleTest {

    public static void main(String[] args) {
        RuleEngine engine = new RuleEngine();
        RuleParser parser = new RuleParser();
        Scanner scanner = new Scanner(System.in);

        // Sample rules
        String rule1 = "(age > 30 AND department = 'Sales')";
        String rule2 = "(age < 25 AND department = 'Marketing')";
        
        Node rule1AST = parser.createRule(rule1);
        Node rule2AST = parser.createRule(rule2);

        // Combine rules
        List<Node> rules = List.of(rule1AST, rule2AST);
        Node combinedAST = engine.combineRules(rules);

        // Print the combined AST for verification (implement a toString method in Node for easy printing)
        System.out.println("Combined AST: " + combinedAST); // Optional: Print the structure of the combined AST

        // Loop to continuously ask for user input
        while (true) {
            // Prompt user for input
            System.out.println("Enter user details for evaluation (or type 'exit' to quit):");
            System.out.print("Age: ");
            String ageInput = scanner.nextLine();
            if (ageInput.equalsIgnoreCase("exit")) break; // Exit condition

            System.out.print("Department: ");
            String department = scanner.nextLine();
            if (department.equalsIgnoreCase("exit")) break; // Exit condition

            System.out.print("Salary: ");
            String salaryInput = scanner.nextLine();
            if (salaryInput.equalsIgnoreCase("exit")) break; // Exit condition

            System.out.print("Experience: ");
            String experienceInput = scanner.nextLine();
            if (experienceInput.equalsIgnoreCase("exit")) break; // Exit condition

            // Parse the inputs safely
            try {
                int age = Integer.parseInt(ageInput);
                double salary = Double.parseDouble(salaryInput);
                int experience = Integer.parseInt(experienceInput);

                // Sample user data
                Map<String, Object> userData = new HashMap<>();
                userData.put("age", age);
                userData.put("department", department);
                userData.put("salary", salary);
                userData.put("experience", experience);

                // Evaluate the combined AST with the sample data
                boolean result = engine.evaluateRuleAST(combinedAST, userData);
                System.out.println("Rule evaluation result: " + result); // Outputs true or false based on the rules
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter numeric values for age, salary, and experience.");
            } catch (InvalidRuleException e) {
                System.err.println("Error evaluating rule: " + e.getMessage());
            }
        }

        scanner.close(); // Close the scanner
    }
}
