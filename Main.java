package assignment_2;

// Main class to start the mini Twitter application
public class Main {
    public static void main(String[] args) {
        // Launch the Admin Control Panel
        AdminControlPanel controlPanel = AdminControlPanel.getInstance();
        controlPanel.setVisible(true);
    }
}
