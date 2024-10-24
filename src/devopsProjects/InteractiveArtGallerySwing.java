package devopsProjects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InteractiveArtGallerySwing extends JFrame {

    // Arrays for art titles, details, and member names
    private String[] memberNames = {"Karishma", "Roopanya", "HemaSree"};
    private String[][] artTitles = {
            {"Artwork A1", "Artwork A2", "Artwork A3"},
            {"Artwork B1", "Artwork B2", "Artwork B3"},
            {"Artwork C1", "Artwork C2", "Artwork C3"}
    };
    private String[][] artDetails = {
            {"Artist: Alice\nMedium: Digital Art\nDescription: A beautiful piece of digital art.",
             "Artist: Alice\nMedium: Oil Painting\nDescription: A vibrant oil painting.",
             "Artist: Alice\nMedium: Watercolor\nDescription: A stunning watercolor artwork."},
            {"Artist: Bob\nMedium: Sculpture\nDescription: An exquisite sculpture.",
             "Artist: Bob\nMedium: Photography\nDescription: A captivating photograph.",
             "Artist: Bob\nMedium: Mixed Media\nDescription: An interesting mixed media piece."},
            {"Artist: Charlie\nMedium: Digital Art\nDescription: A striking digital artwork.",
             "Artist: Charlie\nMedium: Acrylic\nDescription: A colorful acrylic painting.",
             "Artist: Charlie\nMedium: Printmaking\nDescription: A creative printmaking piece."}
    };

    public InteractiveArtGallerySwing() {
        // Set up the main window
        setTitle("Interactive Art Gallery");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create an instance of CanvasPanel and add it to the frame
        CanvasPanel canvas = new CanvasPanel();
        add(canvas);
    }

    // Inner class to draw the art pieces
    class CanvasPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw each member's art pieces
            for (int i = 0; i < memberNames.length; i++) {
                g.setColor(Color.CYAN);
                g.fillRect(i * 250 + 20, 20, 220, 540); // Member section background
                g.setColor(Color.BLACK);
                g.drawString(memberNames[i], i * 250 + 70, 40); // Member name

                for (int j = 0; j < artTitles[i].length; j++) {
                    g.setColor(new Color(255, 127, 80)); // Coral color using RGB
                    g.fillRect(i * 250 + 50, j * 150 + 60, 100, 100); // Art piece
                    g.setColor(Color.BLACK);
                    g.drawString(artTitles[i][j], i * 250 + 55, j * 150 + 180); // Title
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 600);
        }
    }

    // Method to show artwork details
    private void showArtDetails(int memberIndex, int artIndex) {
        JOptionPane.showMessageDialog(this, artDetails[memberIndex][artIndex],
                artTitles[memberIndex][artIndex], JOptionPane.INFORMATION_MESSAGE);
    }

    public void run() {
        // Mouse listener for art piece interaction
        CanvasPanel canvas = (CanvasPanel) getContentPane().getComponent(0);
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                // Check if an art piece is clicked
                for (int i = 0; i < memberNames.length; i++) {
                    for (int j = 0; j < artTitles[i].length; j++) {
                        if (x > (i * 250 + 50) && x < (i * 250 + 150) &&
                                y > (j * 150 + 60) && y < (j * 150 + 160)) {
                            showArtDetails(i, j);
                        }
                    }
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InteractiveArtGallerySwing gallery = new InteractiveArtGallerySwing();
            gallery.run();
        });
    }
}
