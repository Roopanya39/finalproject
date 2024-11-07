package devopsProjects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class InteractiveArtGallerySwing extends JFrame {

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

    private Image[][] artImages;
    private List<String>[][] artComments;
    private HashMap<String, Integer>[][] artRatings;

    public InteractiveArtGallerySwing() {
        setTitle("Interactive Art Gallery");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadArtImages();
        initializeCommentAndRatingStructures();
        CanvasPanel canvas = new CanvasPanel();
        add(canvas);
        
        // Set a random background color at launch
        getContentPane().setBackground(getRandomColor());
    }

    private void loadArtImages() {
        artImages = new Image[memberNames.length][];
        for (int i = 0; i < memberNames.length; i++) {
            artImages[i] = new Image[artTitles[i].length];
            for (int j = 0; j < artTitles[i].length; j++) {
                String imagePath = "resources/art" + (i * 3 + j + 1) + ".jpg";
                artImages[i][j] = new ImageIcon(imagePath).getImage();
            }
        }
    }

    private void initializeCommentAndRatingStructures() {
        artComments = new List[memberNames.length][];
        artRatings = new HashMap[memberNames.length][];
        for (int i = 0; i < memberNames.length; i++) {
            artComments[i] = new List[artTitles[i].length];
            artRatings[i] = new HashMap[artTitles[i].length];
            for (int j = 0; j < artTitles[i].length; j++) {
                artComments[i][j] = new ArrayList<>();
                artRatings[i][j] = new HashMap<>();
            }
        }
    }

    private Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    class CanvasPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < memberNames.length; i++) {
                g.setColor(Color.CYAN);
                g.fillRect(i * 350 + 20, 20, 320, 620);
                g.setColor(Color.BLACK);
                g.drawString(memberNames[i], i * 350 + 120, 40);

                for (int j = 0; j < artTitles[i].length; j++) {
                    g.drawImage(artImages[i][j], i * 350 + 60, j * 200 + 60, 150, 150, this);
                    g.setColor(Color.BLACK);
                    g.drawString(artTitles[i][j], i * 350 + 70, j * 200 + 220);
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1000, 700);
        }
    }

    private void showArtDetails(int memberIndex, int artIndex) {
        JFrame detailsFrame = new JFrame("Artwork Details");
        detailsFrame.setSize(500, 500);
        detailsFrame.setLocationRelativeTo(this);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout());

        JLabel artLabel = new JLabel(new ImageIcon(artImages[memberIndex][artIndex].getScaledInstance(400, 400, Image.SCALE_SMOOTH)));
        detailsPanel.add(artLabel, BorderLayout.NORTH);

        JTextArea detailsArea = new JTextArea(10, 30);
        detailsArea.setText(artDetails[memberIndex][artIndex]);
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsPanel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new GridLayout(0, 1));

        JTextField commentField = new JTextField();
        commentPanel.add(commentField);

        JButton submitCommentButton = new JButton("Submit Comment");
        submitCommentButton.addActionListener(e -> {
            String comment = commentField.getText();
            if (!comment.isEmpty()) {
                artComments[memberIndex][artIndex].add(comment);
                JOptionPane.showMessageDialog(detailsFrame, "Comment added!");
                commentField.setText("");
            }
        });
        commentPanel.add(submitCommentButton);

        JTextField ratingField = new JTextField();
        commentPanel.add(ratingField);

        JButton submitRatingButton = new JButton("Submit Rating");
        submitRatingButton.addActionListener(e -> {
            try {
                int rating = Integer.parseInt(ratingField.getText());
                String username = JOptionPane.showInputDialog("Enter your username:");
                if (username != null && !username.isEmpty()) {
                    artRatings[memberIndex][artIndex].put(username, rating);
                    JOptionPane.showMessageDialog(detailsFrame, "Rating added!");
                    ratingField.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(detailsFrame, "Please enter a valid rating (1-5).");
            }
        });
        commentPanel.add(submitRatingButton);

        detailsPanel.add(commentPanel, BorderLayout.SOUTH);

        detailsFrame.add(detailsPanel);
        detailsFrame.setOpacity(0.0f);
        Timer timer = new Timer(20, e -> {
            float opacity = detailsFrame.getOpacity();
            if (opacity < 1.0f) {
                detailsFrame.setOpacity(opacity + 0.05f);
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();

        detailsFrame.setVisible(true);
    }

    public void run() {
        CanvasPanel canvas = (CanvasPanel) getContentPane().getComponent(0);
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                for (int i = 0; i < memberNames.length; i++) {
                    for (int j = 0; j < artTitles[i].length; j++) {
                        if (x > (i * 350 + 60) && x < (i * 350 + 210) &&
                                y > (j * 200 + 60) && y < (j * 200 + 210)) {
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
