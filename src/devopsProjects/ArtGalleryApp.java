package devopsProjects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ArtGalleryApp {
    // Lists to store image paths and details
    private static final List<String> IMAGE_PATHS = new ArrayList<>();
    private static final List<String> ART_DETAILS = new ArrayList<>();

    // Default images (initial images)
    private static final String[] DEFAULT_IMAGE_PATHS = {
            "resources/art1.jpg", "resources/art2.jpg", "resources/art3.jpg",
            "resources/art4.jpg", "resources/art5.jpg", "resources/art6.jpg",
            "resources/art7.jpg", "resources/art8.jpg", "resources/art9.jpg"
    };

    // Default art details
    private static final String[] DEFAULT_ART_DETAILS = {
            "Art 1: A beautiful painting that captivates the viewer.\nArtist: John Doe\nPrice: $1000",
            "Art 2: A stunning landscape showcasing nature's beauty.\nArtist: Jane Smith\nPrice: $1200",
            "Art 3: A captivating portrait that tells a story.\nArtist: Alex Johnson\nPrice: $1500",
            "Art 4: An abstract piece filled with emotion and meaning.\nArtist: Emily Davis\nPrice: $800",
            "Art 5: A vibrant cityscape brimming with life.\nArtist: Michael Brown\nPrice: $950",
            "Art 6: A serene nature scene that promotes tranquility.\nArtist: Sarah Wilson\nPrice: $1100",
            "Art 7: A dynamic sculpture that challenges perceptions.\nArtist: David Taylor\nPrice: $2000",
            "Art 8: A historic artwork that reflects a bygone era.\nArtist: Laura Moore\nPrice: $1600",
            "Art 9: A modern interpretation that pushes boundaries.\nArtist: Chris White\nPrice: $1400"
    };

    // URL for the background image
    private static final String BACKGROUND_IMAGE_URL = "https://i.pinimg.com/736x/86/db/24/86db242b0197c7d03d1166090f33cb9f.jpg";

    public static void main(String[] args) {
        // Load default images and details
        for (String path : DEFAULT_IMAGE_PATHS) {
            IMAGE_PATHS.add(path);
        }
        for (String details : DEFAULT_ART_DETAILS) {
            ART_DETAILS.add(details);
        }

        createAndShowGUI();
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Art Gallery");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Set classic background image
        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage;

            {
                // Load the background image
                try {
                    backgroundImage = new ImageIcon(new URL(BACKGROUND_IMAGE_URL)).getImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        backgroundPanel.setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(0, 3)); // 3 columns for images

        // Add images to the panel
        refreshGallery(imagePanel);

        JButton addButton = new JButton("Add Art");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addArt(imagePanel); // Pass the imagePanel to refresh it later
            }
        });

        backgroundPanel.add(addButton, BorderLayout.NORTH);
        backgroundPanel.add(new JScrollPane(imagePanel), BorderLayout.CENTER);
        frame.add(backgroundPanel);
        frame.setVisible(true);
    }

    private static void refreshGallery(JPanel imagePanel) {
        imagePanel.removeAll(); // Clear previous images

        // Create labels for each image
        for (int i = 0; i < IMAGE_PATHS.size(); i++) {
            final int index = i; // Create a final variable to be used in the mouse listener

            ImageIcon imageIcon = new ImageIcon(IMAGE_PATHS.get(i));
            Image image = imageIcon.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH); // Resize the image
            JLabel imageLabel = new JLabel(new ImageIcon(image));

            // Adding action listener to display details on image click
            imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showArtDetails(IMAGE_PATHS.get(index), ART_DETAILS.get(index), index, imagePanel);
                }
            });

            imagePanel.add(imageLabel); // Add the image label to the panel
        }

        imagePanel.revalidate(); // Refresh the image panel
        imagePanel.repaint(); // Repaint to show updated images
    }

    private static void addArt(JPanel imagePanel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose an Image to Add");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "jpeg", "png"));

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String destinationPath = "resources/" + selectedFile.getName();

            // Prompt for artist and price details
            String artist = JOptionPane.showInputDialog("Enter the artist's name:");
            String price = JOptionPane.showInputDialog("Enter the price:");

            if (artist != null && price != null) {
                String details = "Artist: " + artist + "\nPrice: $" + price;
                try {
                    // Create resources directory if it doesn't exist
                    File resourcesDir = new File("resources");
                    if (!resourcesDir.exists()) {
                        resourcesDir.mkdir(); // Create the directory
                    }

                    // Copy the selected file to the resources directory
                    Files.copy(selectedFile.toPath(), new File(destinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    IMAGE_PATHS.add(destinationPath); // Add the image path to the list
                    ART_DETAILS.add(details); // Add the details to the list
                    JOptionPane.showMessageDialog(null, "Image added successfully!");
                    refreshGallery(imagePanel); // Refresh the gallery with the new image
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error adding image: " + e.getMessage());
                }
            }
        }
    }

    private static void showArtDetails(String imagePath, String details, int index, JPanel imagePanel) {
        JFrame detailsFrame = new JFrame("Art Details");
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Get screen dimensions to set image size to half of display size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int halfWidth = (int) (screenSize.width / 2);
        int halfHeight = (int) (screenSize.height / 2);

        detailsFrame.setSize(halfWidth, halfHeight);
        detailsFrame.setLayout(new BorderLayout());

        // Load the image
        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image image = imageIcon.getImage().getScaledInstance(halfWidth, halfHeight - 100, Image.SCALE_SMOOTH); // Resize the image
        JLabel imageLabel = new JLabel(new ImageIcon(image));

        JTextArea detailsArea = new JTextArea(details);
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setMargin(new Insets(10, 10, 10, 10));

        // Button to remove the image
        JButton removeButton = new JButton("Remove Image");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this image?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    IMAGE_PATHS.remove(index);
                    ART_DETAILS.remove(index);
                    detailsFrame.dispose(); // Close the details window
                    refreshGallery(imagePanel); // Refresh the gallery to reflect the removal
                }
            }
        });

        detailsFrame.add(imageLabel, BorderLayout.CENTER);
        detailsFrame.add(new JScrollPane(detailsArea), BorderLayout.SOUTH);
        detailsFrame.add(removeButton, BorderLayout.NORTH);
        detailsFrame.setVisible(true);
    }
}
