import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

public class DraftGUI implements MouseListener {

    private static final String IMAGE_URL = "http://images.paramountbusinessjets.com/space/spaceshuttle.jpg";
    private JPanel jpPack;
    private JPanel jpCards;
    private JPanel jpInfo;
    private JPanel jpChat;
    private TextField tf;
    private StyledDocument doc;
    private JTextPane tp;
    private JScrollPane sp;

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    public DraftGUI() throws MalformedURLException {

        final JFrame frame = new JFrame("Draft");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set the content pane, we'll add everything to it and then add it to the frame
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());

        // creates some panels with some default values for now
        JPanel jpCards = new JPanel(new BorderLayout());
        jpCards.setBackground(Color.BLUE);

        JPanel jpInfo = new JPanel();
        jpInfo.setBackground(Color.GREEN);

        JPanel jpPack = new JPanel(new GridBagLayout());
        jpPack.setBackground(Color.RED);

        // grab some info to make the JTextPane and make it scroll
        tf = new TextField();
        doc = new DefaultStyledDocument();
        tp = new JTextPane(doc);
        tp.setEditable(false);
        sp = new JScrollPane(tp);

        // adding the quantities to the chat panel
        JPanel jpChat = new JPanel();
        jpChat.setLayout(new BorderLayout());
        jpChat.add("North", tf);
        jpChat.add("Center", sp);

        // a new GridBagConstraints used to set the properties/location of the panels
        GridBagConstraints c = new GridBagConstraints();

        // adding some panels to the content pane
        // set it to start from the top left of the quadrant if it's too small
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH; // set it to fill both vertically and horizontally
        c.gridx = 0; // set it to quadrant x=0 and
        c.gridy = 0; // set it to quadrant y=0
        c.weightx = 0.7;
        c.weighty = 0.3;
        contentPane.add(jpCards, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.3;
        c.weighty = 0.3;
        contentPane.add(jpInfo, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.7;
        c.weighty = 0.7;
        contentPane.add(jpPack, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.3;
        c.weighty = 0.7;
        contentPane.add(jpChat, c);

        // set some necessary values
        frame.setContentPane(contentPane);
        frame.setLocationByPlatform(true);

        // This code works for adding an Image
        // need to learn how to specify a path not dependent on the specific users's machine
        // this is not a high priority for now though
        GridBagConstraints d = new GridBagConstraints();
        d.weightx = 1.0;
        d.weighty = 1.0;
        d.fill = GridBagConstraints.BOTH;
        d.gridx = 0;
        d.gridy = 0;
        ImageLabel imageLabel1 = new ImageLabel(new ImageIcon(new URL(IMAGE_URL)));
        jpPack.add(imageLabel1, d);
        ImageLabel imageLabel2 = new ImageLabel(new ImageIcon(new URL(IMAGE_URL)));
        d.gridx++;
        jpPack.add(imageLabel2, d);

        ImageLabel imageLabel3 = new ImageLabel(new ImageIcon(new URL(IMAGE_URL)));
        d.gridy++;
        jpPack.add(imageLabel3, d);

        imageLabel1.addMouseListener(this);
        imageLabel2.addMouseListener(this);
        imageLabel3.addMouseListener(this);
        frame.pack();
        // 223, 310 are the aspect values for a card image, width, height
        // these need to be maintained as the GUI size changes
            frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new DraftGUI();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static class ImageLabel extends JPanel {
        private static int counter = 1;
        private ImageIcon icon;
        private int id = counter++;

        public ImageLabel(ImageIcon icon) {
            super();
            setOpaque(false);
            this.icon = icon;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(icon.getIconWidth(), icon.getIconHeight());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            double zoom = Math.min((double) getWidth() / icon.getIconWidth(), (double) getHeight() / icon.getIconHeight());
            int width = (int) (zoom * icon.getIconWidth());
            int height = (int) (zoom * icon.getIconHeight());
            g.drawImage(icon.getImage(), (getWidth() - width) / 2, (getHeight() - height) / 2, width, height, this);
            g.setFont(g.getFont().deriveFont(36.0f));
            g.drawString(String.valueOf(id), getWidth() / 2, getHeight() / 2);
        }
    }
}