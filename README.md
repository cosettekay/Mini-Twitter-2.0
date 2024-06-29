# Mini-Twitter 2.0

## Overview

Mini-Twitter 2.0 is a Java-based application with a graphical user interface (GUI) developed using Java Swing. It allows users to create accounts, post tweets, follow other users, and manage user groups. This version includes enhanced features like user/group ID validation, creation timestamps, last update timestamps, and identifying the last user to update.

## Features

### Admin Control Panel
- **Create Users and Groups**: Add new users and user groups with unique IDs.
- **Verify IDs**: Validate all user and group IDs for uniqueness and absence of spaces.
- **View Tree Structure**: Display the hierarchical structure of users and groups.
- **Statistics and Analysis**: Output the total number of users, groups, tweet messages, and analyze the percentage of positive tweets.

### User View
- **Follow Users**: Follow other users by entering their ID.
- **Post Tweets**: Share short messages with followers.
- **News Feed**: View tweets from followed users.
- **Display Timestamps**: Show creation time and last update time for users.

### Additional Features (Mini-Twitter 2.0)
- **ID Validation**: Check uniqueness and space-free criteria for all IDs used.
- **Creation Timestamp**: Display creation time for users.
- **Last Update Timestamp**: Update and display last update time whenever a tweet is posted.
- **Identify Last Updater**: Output the ID of the user who most recently updated.

## Design Patterns Used
- **Singleton**: Ensures a single instance of the admin control panel.
- **Observer**: Updates followers' news feeds in real-time.
- **Visitor**: Performs operations like counting users, groups, and tweets.
- **Composite**: Manages hierarchical structures of users and user groups.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or later

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/minitwitter2.0.git
   cd minitwitter
2. Compile the project
```bash
javac -d bin src/**/*.java
```
3. Run the application
```bash
java -cp bin your.main.package.Main
```

## Usage (of the new features)
- Open the AdminControlPanel to access the new Validate IDs and LastUpdatedUser buttons
<img width="439" alt="img1" src="https://github.com/cosettekay/Mini-Twitter-2.0/assets/71306558/3204e6b4-cc03-4561-8a6b-498ca85b25ea">
<img width="300" alt="img2" src="https://github.com/cosettekay/Mini-Twitter-2.0/assets/71306558/c66f481c-dfaf-47ae-8d51-fa2d389861d5">
<img width="300" alt="img3" src="https://github.com/cosettekay/Mini-Twitter-2.0/assets/71306558/407059f1-a49a-4dc9-b59e-6c1d72a21e7c">
<img width="300" alt="img5" src="https://github.com/cosettekay/Mini-Twitter-2.0/assets/71306558/fcfe730c-fd4e-42ef-a4d1-ac9d69449439">

- Access User Views to see the creation time of each user and the last updated time of their most recent tweets.
<img width="439" alt="img4" src="https://github.com/cosettekay/Mini-Twitter-2.0/assets/71306558/9f978038-9c40-4909-a380-cac501e7fee3">

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any bugs or feature requests.
