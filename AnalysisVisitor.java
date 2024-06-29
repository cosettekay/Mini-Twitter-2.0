package assignment_2;

// AnalysisVisitor class for performing analysis on the user components
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays;

public class AnalysisVisitor implements Visitor {
    private int userCount = 0;
    private int groupCount = 0;
    private int tweetCount = 0;
    private int positiveTweetCount = 0;
    private List<String> positiveWords = Arrays.asList("good", "great", "excellent", "awesome", "happy");
    private Set<String> uniqueIDs = new HashSet<>();
    private User lastUpdatedUser;

    // Getter for user count
    public int getUserCount() {
        return userCount;
    }

    // Getter for group count
    public int getGroupCount() {
        return groupCount;
    }

    // Getter for tweet count
    public int getTweetCount() {
        return tweetCount;
    }

    // Getter for the percentage of positive tweets
    public double getPositiveTweetPercentage() {
        return tweetCount == 0 ? 0 : ((double) positiveTweetCount / tweetCount) * 100;
    }

     // Getter for unique IDs
     public Set<String> getUniqueIDs() {
        return uniqueIDs;
    }

    // Getter for the last updated user
    public User getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    // Visit method for User objects
    @Override
    public void visit(User user) {
        userCount++; // Increment user count for each user visited
        tweetCount += user.getNewsFeed().size(); // Increment tweet count
        
        // Check each tweet for positive words
        for (String tweet : user.getNewsFeed()) {
            if (isPositiveTweet(tweet)) {
                positiveTweetCount++; // Increment positive tweet count if positive
            }
         }

        // Check for unique ID
        uniqueIDs.add(user.getUserID());

        // Update the last updated user
        if (lastUpdatedUser == null || user.getLastUpdateTime() > lastUpdatedUser.getLastUpdateTime()) {
            lastUpdatedUser = user;
        }

    }

    // Visit method for UserGroup objects
    @Override
    public void visit(UserGroup group) {
        groupCount++; // Increment group count for the current group

        // Check for unique ID
        uniqueIDs.add(group.getGroupID());

        // Accumulate user and message counts recursively
        userCount += group.getTotalUserCount();
        tweetCount += group.getTotalMessageCount();
        // You can compute positive tweet count percentage here if needed
    }
    // Method to check if a tweet is positive
    private boolean isPositiveTweet(String tweet) {
        String lowerCaseTweet = tweet.toLowerCase();
        // Check if the tweet contains any positive words
        for (String word : positiveWords) {
            if (lowerCaseTweet.contains(word)) {
                return true;
            }
        }
        return false;
    }
}

