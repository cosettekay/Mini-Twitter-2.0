package assignment_2;

import java.util.List;
import java.util.ArrayList;

// UserGroup class representing a group of users in the mini Twitter application
public class UserGroup extends UserComponent {
    private String groupID;              // Unique ID for the group
    private List<UserComponent> userComponents;  // List of users and sub-groups

    // Constructor to initialize the group with a unique ID
    public UserGroup(String groupID) {
        this.groupID = groupID;
        this.userComponents = new ArrayList<>();
    }

    // Getter for group ID
    public String getGroupID() {
        return groupID;
    }

    // Method to add a user or sub-group to the group
    public void addUserComponent(UserComponent userComponent) {
        userComponents.add(userComponent);
    }

    // Getter for list of user components
    public List<UserComponent> getUserComponents() {
        return userComponents;
    }
    
    // Method to get total user count including all nested components
    public int getTotalUserCount() {
        int count = 0;
        for (UserComponent component : userComponents) {
            if (component instanceof User) {
                count++;
            } else if (component instanceof UserGroup) {
                count += ((UserGroup) component).getTotalUserCount();
            }
        }
        return count;
    }

    // Method to get total message count including all nested components
    public int getTotalMessageCount() {
        int count = 0;
        for (UserComponent component : userComponents) {
            if (component instanceof User) {
                count += ((User) component).getNewsFeed().size();
            } else if (component instanceof UserGroup) {
                count += ((UserGroup) component).getTotalMessageCount();
            }
        }
        return count;
    }

    // Accept a visitor
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        for (UserComponent component : userComponents) {
            component.accept(visitor);
        }
    }
    @Override
    public String toString() {
        return groupID;
    }
}
