package zeotap.project;

public class Node {
    String type; // "operator" or "operand"
    Node left;   // Left child node
    Node right;  // Right child node
    String value; // The value or operator (like ">", "<=", etc.)

    public Node(String type, Node left, Node right, String value) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.value = value;
    }
}
