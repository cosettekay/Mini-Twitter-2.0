package assignment_2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Date;

// User view class to display and manage a user's news feed and followings
public class UserView extends JFrame {
    private User user; // The user associated with this view
    private DefaultListModel<String> newsFeedModel;  // Model for the news feed list
    private DefaultListModel<String> followingsModel;  // Model for the followings list
    private DefaultListModel<String> followersModel;  // Model for the followers list
    // Add JLabels for displaying creation time and last update time
    private JLabel creationTimeLabel;
    private JLabel lastUpdateTimeLabel;

    // Constructor to initialize the user view
    public UserView(User user) {
        this.user = user;

        // Set up the frame
        setTitle("User View: " + user.getUserID());
        // Set the size of the frame
        setSize(600, 400);
         // Dispose on close to free up resources
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initialize the list models
        newsFeedModel = new DefaultListModel<>();
        followingsModel = new DefaultListModel<>();
        followersModel = new DefaultListModel<>();

        creationTimeLabel = new JLabel("Creation Time: " + new Date(user.getCreationTime()));
        lastUpdateTimeLabel = new JLabel("Last Update Time: " + new Date(user.getLastUpdateTime()));

        // Create JList components using the models
        JList<String> newsFeedList = new JList<>(newsFeedModel);
        JList<String> followingsList = new JList<>(followingsModel);
        JList<String> followersList = new JList<>(followersModel);

        // Wrap JList components in JScrollPane for scrolling
        JScrollPane newsFeedScrollPane = new JScrollPane(newsFeedList);
        JScrollPane followingsScrollPane = new JScrollPane(followingsList);
        JScrollPane followersScrollPane = new JScrollPane(followersList);

        // Create post panel with a text field and a button
        JPanel postPanel = new JPanel(new BorderLayout());
        JTextField postField = new JTextField();
        JButton postButton = new JButton("Post");

        postPanel.add(postField, BorderLayout.CENTER);
        postPanel.add(postButton, BorderLayout.EAST);
       
        // Create follow panel with a text field and a button
        JPanel followPanel = new JPanel(new BorderLayout());
        JTextField followField = new JTextField();
        JButton followButton = new JButton("Follow");

        followPanel.add(followField, BorderLayout.CENTER);
        followPanel.add(followButton, BorderLayout.EAST);

        // Create titled panels for tweets, followings, and followers
        JPanel tweetsPanel = new JPanel(new BorderLayout());
        tweetsPanel.setBorder(BorderFactory.createTitledBorder("Tweets"));
        tweetsPanel.add(newsFeedScrollPane, BorderLayout.CENTER);

        JPanel followingsPanel = new JPanel(new BorderLayout());
        followingsPanel.setBorder(BorderFactory.createTitledBorder("Followings"));
        followingsPanel.add(followingsScrollPane, BorderLayout.CENTER);

        JPanel followersPanel = new JPanel(new BorderLayout());
        followersPanel.setBorder(BorderFactory.createTitledBorder("Followers"));
        followersPanel.add(followersScrollPane, BorderLayout.CENTER);

        // Create a center panel with a grid layout and add scroll panes
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(newsFeedScrollPane);
        centerPanel.add(followingsScrollPane);

        // Add components to the frame
        add(centerPanel, BorderLayout.CENTER);
        add(postPanel, BorderLayout.SOUTH);
        add(followPanel, BorderLayout.NORTH);
        add(followersPanel, BorderLayout.WEST);

         // Add labels to the frame
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(creationTimeLabel);
        infoPanel.add(lastUpdateTimeLabel);
        add(infoPanel, BorderLayout.NORTH);

        // Action listener to post a new tweet
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tweet = postField.getText();
                if (!tweet.isEmpty()) {
                    user.postTweet(tweet);  // Post the tweet through the user object
                    newsFeedModel.addElement(tweet); // Add tweet to the news feed list
                    lastUpdateTimeLabel.setText("Last Update Time: " + new Date(user.getLastUpdateTime()));
                }
            }
        });

        // Action listener to follow another user
        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = followField.getText();
                if (!userID.isEmpty()) {
                    User targetUser = AdminControlPanel.getInstance().findUserByID(userID);
                    if (targetUser != null) {
                        user.follow(targetUser); // Follow the target user
                        followingsModel.addElement(userID); // Add userID to followings list
                        followField.setText(""); // Clear the text field

                        // Add follower to the current user's followers list
                        if (!targetUser.getFollowers().contains(user)) {
                            targetUser.addFollower(user);
                            followersModel.addElement(user.getUserID());
                        }
                    } else {
                        JOptionPane.showMessageDialog(UserView.this, "User not found!");
                    }
                }
            }
        });

        // Initialize the followings list
        List<User> followings = user.getFollowings();
        for (User following : followings) {
            followingsModel.addElement(following.getUserID());
            // Add initial tweets of the followed users to the news feed
            List<String> tweets = following.getNewsFeed();
            for (String tweet : tweets) {
                newsFeedModel.addElement(tweet + " (From: " + following.getUserID() + ")");
            }
        }

        // Initialize the followers list
        List<User> followers = user.getFollowers();
        for (User follower : followers) {
            followersModel.addElement(follower.getUserID());
        }

        // Initialize the news feed list
        List<String> newsFeed = user.getNewsFeed();
        for (String tweet : newsFeed) {
            newsFeedModel.addElement(tweet);
        }
        // Make the frame visible
        SwingUtilities.invokeLater(() ->setVisible(true));
    }


    public static void main(String[] args) {
        // Example usage:
        User user = new User("exampleUser");
        user.addMessage("This is a message."); // Example to update last update time
        new UserView(user);
    }
}
