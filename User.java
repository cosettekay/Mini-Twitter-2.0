package assignment_2;

import java.util.ArrayList;
import java.util.List;

// User class representing an individual user in the mini Twitter application
public class User extends UserComponent implements Observer, Visitable {
    private String userID;          // Unique ID for the user
    private List<User> followers;   // List of followers
    private List<User> followings;  // List of users this user is following
    private List<String> newsFeed;  // List of tweets in the user's news feed
    private List<String> messages;  // List of messages
    private long creationTime;      // Timestamp of the user creation time
    private long lastUpdateTime;    // Timestamp of the last update

    // Constructor to initialize the user with a unique ID
    public User(String userID) {
        this.userID = userID;
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.newsFeed = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.creationTime = System.currentTimeMillis(); // Record creation time
        this.lastUpdateTime = System.currentTimeMillis(); // Record lastUpdated time
    }

    // Getter for user ID
    public String getUserID() {
        return userID;
    }

    // Getter for news feed
    public List<String> getNewsFeed() {
        return newsFeed;
    }

    // Getter for followings
    public List<User> getFollowings() {
        return followings;
    }

    // Getter for followers
    public List<User> getFollowers(){
        return followers;
    }

    // Getter for messages
    public List<String> getMessages() {
        return messages;
    }

    // Getter for creation time
    public long getCreationTime() {
        return creationTime;
    }

    // Getter for last update time
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    // Method to follow another user
    public void follow(User user) {
        if (!followings.contains(user)) {
            followings.add(user);
            user.addFollower(this);
        }
    }

    // Method to add a follower to this user
    public void addFollower(User user) {
        if (!followers.contains(user)) {
            followers.add(user);
        }
    }

    // Method to post a tweet
    public void postTweet(String tweet) {
        newsFeed.add(tweet);
        notifyFollowers(tweet);
        lastUpdateTime = System.currentTimeMillis();
    }

    // Notify all followers about a new tweet
    private void notifyFollowers(String tweet) {
        for (User follower : followers) {
            follower.update(tweet);
        }
    }

    // Update news feed with a new tweet
    @Override
    public void update(String tweet) {
        newsFeed.add(tweet);
        lastUpdateTime = System.currentTimeMillis();
    }

     // Method to add a message
     public void addMessage(String message) {
        messages.add(message);
        lastUpdateTime = System.currentTimeMillis(); // Update the last update time
    }

    // Accept a visitor
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    @Override
    public String toString() {
        return userID;
    }
}
