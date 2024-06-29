package assignment_2;

// Visitor interface for implementing the Visitor pattern
public interface Visitor {
    void visit(User user);
    void visit(UserGroup group);
}


