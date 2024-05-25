import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShuttleApp {
    private JFrame frame;
    private JPanel panel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel registrationTrackerLabel;
    private JButton registerButton;
    private JButton clearRegistrationButton;
    private static final int MAX_USERS = 5;
    private String[] usernames = new String[MAX_USERS];
    private String[] passwords = new String[MAX_USERS];
    private int currentUserCount = 0;
    private String currentLocation;
    private String destination;
    private JLabel mapLabel;

    public ShuttleApp() {
        frame = new JFrame("BUP Shuttle Companion");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel(new BorderLayout());

        JLabel headlineLabel = new JLabel("BUP Shuttle Companion", SwingConstants.CENTER);
        headlineLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(headlineLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        clearRegistrationButton = new JButton("Clear Registration");
        registrationTrackerLabel = new JLabel("");

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(loginButton);
        formPanel.add(registerButton);
        formPanel.add(new JLabel());
        formPanel.add(registrationTrackerLabel);
        formPanel.add(new JLabel());
        formPanel.add(clearRegistrationButton);

        panel.add(formPanel, BorderLayout.CENTER);

        // Add a placeholder panel to center the "Clear Registration" button at the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(clearRegistrationButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(panel, BorderLayout.CENTER);

        clearRegistrationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < MAX_USERS; i++) {
                    usernames[i] = null;
                    passwords[i] = null;
                }
                currentUserCount = 0; // Reset user count

                registrationTrackerLabel.setText("Registration data cleared.");

                // Enable register button after clearing registrations
                registerButton.setEnabled(true);
            }
        });

        // Register button action listener
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                // Check for empty username or password
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter both username and password.");
                    return;
                }

                // Check if maximum user count has been reached
                if (currentUserCount < MAX_USERS) {
                    // Store the new user's credentials
                    usernames[currentUserCount] = username;
                    passwords[currentUserCount] = password;
                    currentUserCount++;

                    registrationTrackerLabel.setText("Registration successful for " + username);
                } else {
                    registrationTrackerLabel.setText("Maximum user registrations reached.");
                }

                // Clear fields after registration attempt
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        // Login button action listener
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                // Check if the entered credentials match any of the registered users
                boolean loginSuccess = false;
                for (int i = 0; i < currentUserCount; i++) {
                    if (username.equals(usernames[i]) && password.equals(passwords[i])) {
                        loginSuccess = true;
                        break;
                    }
                }

                if (loginSuccess) {
                    JOptionPane.showMessageDialog(frame, "Login successful! Welcome, " + username + "!");
                    openNextPage(); // Open the next page upon successful login
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password. Please try again.");
                }

                // Clear fields after login attempt
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        frame.setVisible(true);
    }

    private void openNextPage() {
        JFrame nextPageFrame = new JFrame("Enjoy BUP Shuttle");
        nextPageFrame.setSize(600, 400);
        nextPageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel nextPagePanel = new JPanel(new BorderLayout(10, 10));
        JPanel controlPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JButton selectLocationButton = new JButton("Select Your Location");
        JLabel currentLocationLabel = new JLabel("");
        JButton selectDestinationButton = new JButton("Select Destination");
        JLabel destinationLabel = new JLabel("");
        JButton discountButton = new JButton("Discount");
       
        JButton takeRideButton = new JButton("Take the Ride");
        JTextField urlField = new JTextField();
        JButton submitUrlButton = new JButton("Submit URL");

        controlPanel.add(selectLocationButton);
        controlPanel.add(currentLocationLabel);
        controlPanel.add(selectDestinationButton);
        controlPanel.add(destinationLabel);
        controlPanel.add(discountButton);
        JLabel discountLabel = new JLabel("");
        controlPanel.add(discountLabel);
        
        controlPanel.add(takeRideButton);

        // Create a new panel for the URL field and submit button to place it in the center
        JPanel urlPanel = new JPanel(new BorderLayout(2, 1));
        urlPanel.add(new JLabel("Enter Map URL:"), BorderLayout.WEST);
        urlPanel.add(urlField, BorderLayout.CENTER);
        urlPanel.add(submitUrlButton, BorderLayout.SOUTH);

        // Add the URL panel to the main panel
        nextPagePanel.add(controlPanel, BorderLayout.NORTH);
        nextPagePanel.add(urlPanel, BorderLayout.CENTER);

        // Panel to display the map
        JPanel mapPanel = new JPanel(new BorderLayout());
        mapLabel = new JLabel();
        mapPanel.add(mapLabel, BorderLayout.CENTER);

        nextPagePanel.add(mapPanel, BorderLayout.SOUTH);

        selectLocationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openMap();
            }
        });

        discountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] friendsOptions = {"1", "2", "3", "4", "5"};
                String friends = (String) JOptionPane.showInputDialog(
                        nextPageFrame,
                        "Select number of friends you want to take with you:",
                        "Discount",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        friendsOptions,
                        friendsOptions[0]);

                if (friends != null) {
                    int numFriends = Integer.parseInt(friends);
                    int discount = numFriends * 5;
                    discountLabel.setText(discount + "% discount applied");
                }
            }
        });

        takeRideButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRideAnimation();
            }
        });

        submitUrlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                if (!url.isEmpty()) {
                    extractLocationsFromUrl(url);
                    currentLocationLabel.setText(currentLocation);
                    destinationLabel.setText(destination);
                    displayMap(url);
                    JOptionPane.showMessageDialog(nextPageFrame, "Locations set!\nCurrent Location: " + currentLocation + "\nDestination: " + destination + "\nYour destination is set now, you can take the ride.");
                } else {
                    JOptionPane.showMessageDialog(nextPageFrame, "Please enter a URL.");
                }
            }
        });

        nextPageFrame.add(nextPagePanel);
        nextPageFrame.setVisible(true);
    }

    private void showRideAnimation() {
        try {
            JFrame animationFrame = new JFrame("BUP Shuttle is Coming (: ");
            animationFrame.setSize(800, 600); // Adjust size as needed
            animationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            // Create a panel to hold the message and the GIF
            JPanel animationPanel = new JPanel(new BorderLayout());
            
            // Create and add the message label
            JLabel messageLabel = new JLabel("BUP Shuttle is coming!", SwingConstants.CENTER);
            messageLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Adjust font size as needed
            animationPanel.add(messageLabel, BorderLayout.NORTH);
            
            // Create and add the GIF label
            JLabel gifLabel = new JLabel(new ImageIcon("C:\\Users\\Lenovo\\eclipse-workspace\\BUP SHUTTLE PROJECT APP/car1.gif"));
            animationPanel.add(gifLabel, BorderLayout.CENTER);
            
            // Add the animation panel to the frame
            animationFrame.add(animationPanel);
            
            animationFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openMap() {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI("https://www.google.com/maps/dir/?api=1&origin=BUP+Academic+Building,+Dhaka&destination=Mirpur-12+Bus+Stand,+Dhaka+1216"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extractLocationsFromUrl(String url) {
        try {
            Pattern pattern = Pattern.compile("dir/([^/]*)/([^/@]*)");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                currentLocation = matcher.group(1).replace("+", " ");
                destination = matcher.group(2).replace("+", " ");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid URL format.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error extracting locations from URL.");
        }
    }

    private void displayMap(String url) {
        try {
            String mapHtml = "<html><iframe src=\"" + url + "\" width=\"500\" height=\"400\" frameborder=\"0\" style=\"border:0\" allowfullscreen></iframe></html>";
            mapLabel.setText(mapHtml);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error displaying the map.");
        }
    }

    public static void main(String[] args) {
        new ShuttleApp();
    }
    }
