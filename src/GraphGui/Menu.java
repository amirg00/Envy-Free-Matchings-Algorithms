package GraphGui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;


/**
 * Class details: this class suppose to be the entrance login panel, before starting the game.
 */
public class Menu extends JPanel {

    private static boolean isPlayButtonPressed;
    private Graphics2D g2d;

    Menu() {
        setPlayButtonState(false);
        this.setPreferredSize(new Dimension(1100,750));
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(500, 0, 0, 0);
        gbc.ipadx = 0;
        gbc.ipady = 0;


        JButton btn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/PlayButton_d.gif"))));;
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setRolloverIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/frame_b.gif"))));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btn, gbc);
        btn.addActionListener(e -> {
            setPlayButtonState(true);
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1100,750);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g.create();
        // Background:
        Image background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GraphGui/Icons/Login.jpg"));
        g2d.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
        g2d.dispose();
    }

    public boolean getPlayButtonState(){
        return isPlayButtonPressed;
    }

    public void setPlayButtonState(boolean b){isPlayButtonPressed = b;}

    public static void main(String[] args) {
        new Menu();
    }
}

