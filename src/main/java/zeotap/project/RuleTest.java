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

      
        String rule1 = "(age > 30 AND department = 'Sales')";
        String rule2 = "(age < 25 AND department = 'Marketing')";
        
        Node rule1AST = parser.createRule(rule1);
        Node rule2AST = parser.createRule(rule2);

      
        List<Node> rules = List.of(rule1AST, rule2AST);
        Node combinedAST = engine.combineRules(rules);

        
        System.out.println("Combined AST: " + combinedAST); 
        
        while (true) {
          
            System.out.println("Enter user details for evaluation (or type 'exit' to quit):");
            System.out.print("Age: ");
            String ageInput = scanner.nextLine();
            if (ageInput.equalsIgnoreCase("exit")) break; 

            System.out.print("Department: ");
            String department = scanner.nextLine();
            if (department.equalsIgnoreCase("exit")) break; 

            System.out.print("Salary: ");
            String salaryInput = scanner.nextLine();
            if (salaryInput.equalsIgnoreCase("exit")) break; 

            System.out.print("Experience: ");
            String experienceInput = scanner.nextLine();
            if (experienceInput.equalsIgnoreCase("exit")) break; 

            
            try {
                int age = Integer.parseInt(ageInput);
                double salary = Double.parseDouble(salaryInput);
                int experience = Integer.parseInt(experienceInput);

                
                Map<String, Object> userData = new HashMap<>();
                userData.put("age", age);
                userData.put("department", department);
                userData.put("salary", salary);
                userData.put("experience", experience);

               
                boolean result = engine.evaluateRuleAST(combinedAST, userData);
                System.out.println("Rule evaluation result: " + result); 
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter numeric values for age, salary, and experience.");
            } catch (InvalidRuleException e) {
                System.err.println("Error evaluating rule: " + e.getMessage());
            }
        }

        scanner.close(); 
    }
}
