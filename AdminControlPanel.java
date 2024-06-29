package assignment_2;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

// Singleton class for the admin control panel
public class AdminControlPanel extends JFrame {
    private static AdminControlPanel instance = null;  // Singleton instance
    private JTree userTree;                            // Tree view of users and groups
    private DefaultTreeModel treeModel;                // Model for the tree view
    private DefaultMutableTreeNode rootNode;           // Root node of the tree

    // Private constructor for singleton pattern
    private AdminControlPanel() {
        // Set up the main frame
        setTitle("Admin Control Panel");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the root node and tree model
        rootNode = new DefaultMutableTreeNode("Root");
        treeModel = new DefaultTreeModel(rootNode);
        userTree = new JTree(treeModel);
        JScrollPane treeView = new JScrollPane(userTree);
        
        // Set up control panel with GridBagLayout
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // User and group ID input fields and buttons
        JTextField userIDField = new JTextField(10);
        JButton addUserButton = new JButton("Add User");
        JTextField groupIDField = new JTextField(10);
        JButton addGroupButton = new JButton("Add Group");
        JButton openUserViewButton = new JButton("Open User View");
        JButton validateIDsButton = new JButton("Validate IDs");  // New button for ID validation
        JButton findLastUpdatedUserButton = new JButton("Find Last Updated User");  // New button for last updated user

        // Layout for user ID input
        gbc.gridx = 0;
        gbc.gridy = 0;
        controlPanel.add(new JLabel("User ID:"), gbc);

        gbc.gridx = 1;
        controlPanel.add(userIDField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        controlPanel.add(addUserButton, gbc);

        // Layout for group ID input
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        controlPanel.add(new JLabel("Group ID:"), gbc);

        gbc.gridx = 1;
        controlPanel.add(groupIDField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        controlPanel.add(addGroupButton, gbc);
        
        // Panel for statistical buttons
        JPanel statsPanel = new JPanel(new GridLayout(2, 2));
        JButton showUserTotalButton = new JButton("Show User Total");
        JButton showGroupTotalButton = new JButton("Show Group Total");
        JButton showMessageTotalButton = new JButton("Show Message Total");
        JButton showPositivePercentageButton = new JButton("Show Positive Percentage");

        statsPanel.add(showUserTotalButton);
        statsPanel.add(showGroupTotalButton);
        statsPanel.add(showMessageTotalButton);
        statsPanel.add(showPositivePercentageButton);
        statsPanel.add(validateIDsButton);  // Adding the new button to the stats panel
        statsPanel.add(findLastUpdatedUserButton);  // Adding the new button to the stats panel
        
        // Add panels to the main frame
        add(treeView, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);
        add(openUserViewButton, BorderLayout.SOUTH);
        add(statsPanel, BorderLayout.EAST);

        // Action listener to add a new user
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userIDField.getText().trim();
                if (userID.isEmpty() || userID.contains(" ")) {
                    JOptionPane.showMessageDialog(AdminControlPanel.this, "User ID cannot be empty or contains spaces.");
                    return;
                }

                // Check if user already exists
                if (findUserByID(userID) != null) {
                   JOptionPane.showMessageDialog(AdminControlPanel.this, "User ID already exists.");
                   return;
                }

                DefaultMutableTreeNode selectedNode = getSelectedNode();
                if (selectedNode == null) {
                    selectedNode = rootNode; // Add user to root node as default
                } else if (!(selectedNode.getUserObject() instanceof UserGroup)) {
                    selectedNode = (DefaultMutableTreeNode) selectedNode.getParent(); // Move up to parent if selected node is a user
                }

                // Create a new User object and add to the tree
                User newUser = new User(userID);
                DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(newUser);
                selectedNode.add(userNode);
                treeModel.reload(selectedNode);

                // Select the root node after adding the user
                userTree.setSelectionPath(new TreePath(rootNode.getPath()));

                // Provide feedback to the user
                JOptionPane.showMessageDialog(AdminControlPanel.this, "User added successfully: " + userID);
                userIDField.setText("");
            }
        });

        // Action listener to add a new group
        addGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String groupID = groupIDField.getText().trim();
                if (groupID.isEmpty()) {
                    JOptionPane.showMessageDialog(AdminControlPanel.this, "Group ID cannot be empty.");
                    return;
                }

                DefaultMutableTreeNode selectedNode = getSelectedNode();
                if (selectedNode == null) {
                    selectedNode = rootNode;
                }

                UserGroup newGroup = new UserGroup(groupID);
                DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(newGroup);
                selectedNode.add(groupNode);
                treeModel.reload(selectedNode);

                // Select the root node after adding the group
                userTree.setSelectionPath(new TreePath(rootNode.getPath()));

                // Provide feedback to the user
                JOptionPane.showMessageDialog(AdminControlPanel.this, "Group added successfully: " + groupID);
                groupIDField.setText("");
            }
        });

        // Action listener to open a user view
        openUserViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode selectedNode = getSelectedNode();
                if (selectedNode != null && selectedNode.getUserObject() instanceof User) {
                    User user = (User) selectedNode.getUserObject();
                    new UserView(user);
                } else {
                    JOptionPane.showMessageDialog(AdminControlPanel.this, "Please select a user to view.");
                }
            }
        });

        // Action listener to show total number of users
        showUserTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnalysisVisitor visitor = new AnalysisVisitor();
                AdminControlPanel.this.accept(visitor);
                JOptionPane.showMessageDialog(AdminControlPanel.this, "Total Users: " + visitor.getUserCount());
            }
        });

        // Action listener to show total number of groups
        showGroupTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnalysisVisitor visitor = new AnalysisVisitor();
                AdminControlPanel.this.accept(visitor);
                JOptionPane.showMessageDialog(AdminControlPanel.this, "Total Groups: " + visitor.getGroupCount());
            }
        });

        // Action listener to show total number of messages
        showMessageTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnalysisVisitor visitor = new AnalysisVisitor();
                AdminControlPanel.this.accept(visitor);
                JOptionPane.showMessageDialog(AdminControlPanel.this, "Total Messages: " + visitor.getTweetCount());
            }
        });

        // Action listener to show percentage of positive messages
        showPositivePercentageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnalysisVisitor visitor = new AnalysisVisitor();
                AdminControlPanel.this.accept(visitor);
                JOptionPane.showMessageDialog(AdminControlPanel.this, "Positive Messages Percentage: " + visitor.getPositiveTweetPercentage() + "%");
            }
        });

         // Action listener to validate IDs
         validateIDsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateIDs();
            }
        });

        // Action listener to find the last updated user
        findLastUpdatedUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findLastUpdatedUser();
            }
        });
    }

    // Method to get the singleton instance of AdminControlPanel
    public static synchronized AdminControlPanel getInstance() {
        if (instance == null) {
            instance = new AdminControlPanel();
        }
        return instance;
    }

    // Method to find a user by ID
    public User findUserByID(String userID) {
        DefaultMutableTreeNode node = findNode(rootNode, userID);
        if (node != null && node.getUserObject() instanceof User) {
            return (User) node.getUserObject();
        }
        return null;
    }

    // Method to find a node by user ID
    private DefaultMutableTreeNode findNode(DefaultMutableTreeNode rootNode, String userID) {
        if (rootNode == null) return null;
        if (rootNode.getUserObject() instanceof User && ((User) rootNode.getUserObject()).getUserID().equals(userID)) {
            return rootNode;
        }
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = findNode((DefaultMutableTreeNode) rootNode.getChildAt(i), userID);
            if (node != null) return node;
        }
        return null;
    }

    // Method to accept a visitor
    public void accept(Visitor visitor) {
        traverse(rootNode, visitor);
    }

    // Recursive method to traverse the tree and accept visitor
    private void traverse(DefaultMutableTreeNode node, Visitor visitor) {
        if (node == null) return;

        // Accept the visitor for the current node
        Object userObject = node.getUserObject();
        if (userObject instanceof UserComponent) {
            ((UserComponent) userObject).accept(visitor);
        }

        // Recursively visit child nodes
        for (int i = 0; i < node.getChildCount(); i++) {
            traverse((DefaultMutableTreeNode) node.getChildAt(i), visitor);
        }
    }

    // Helper method to get the selected node from the tree
    private DefaultMutableTreeNode getSelectedNode() {
        TreePath selectedPath = userTree.getSelectionPath();
        if (selectedPath != null) {
            return (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
        }
        return null;
    }

    // Method to validate user and group IDs
    private void validateIDs() {
        Set<String> uniqueIDs = new HashSet<>();
        boolean valid = validateIDsRecursive(rootNode, uniqueIDs);
        if (valid) {
            JOptionPane.showMessageDialog(this, "All IDs are unique.");
        } else {
            JOptionPane.showMessageDialog(this, "Duplicate IDs found.");
        }
    }

    // Recursive method to validate IDs
    private boolean validateIDsRecursive(DefaultMutableTreeNode node, Set<String> uniqueIDs) {
        Object userObject = node.getUserObject();
        if (userObject instanceof User){
            String id = ((User) userObject).getUserID();
            if (uniqueIDs.contains(id)){
                return false;
            }
            uniqueIDs.add(id);
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            if (!validateIDsRecursive((DefaultMutableTreeNode) node.getChildAt(i), uniqueIDs)) {
                return false;
            }
        }
        return true;
    }

    // Method to find the last updated user
    private void findLastUpdatedUser() {
        User lastUpdatedUser = findLastUpdatedUserRecursive(rootNode, null);
        if (lastUpdatedUser != null) {
            JOptionPane.showMessageDialog(this, "Last updated user: " + lastUpdatedUser.getUserID());
        } else {
            JOptionPane.showMessageDialog(this, "No users found.");
        }
    }

    // Recursive method to find the last updated user
    private User findLastUpdatedUserRecursive(DefaultMutableTreeNode node, User lastUpdatedUser) {
        if (node.getUserObject() instanceof User) {
            User user = (User) node.getUserObject();
            if (lastUpdatedUser == null || user.getLastUpdateTime() > lastUpdatedUser.getLastUpdateTime()) {
                lastUpdatedUser = user;
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            lastUpdatedUser = findLastUpdatedUserRecursive((DefaultMutableTreeNode) node.getChildAt(i), lastUpdatedUser);
        }
        return lastUpdatedUser;
    }

    public static void main(String[] args) {
        // Run the admin control panel in the event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AdminControlPanel.getInstance().setVisible(true);
            }
        });
    }
} 
