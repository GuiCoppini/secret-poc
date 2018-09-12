package client.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ConnectFour1 {

    public static void main(String[] args) {
        new ConnectFour1();
    }

    public ConnectFour1() {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }

            JFrame frame = new JFrame("Testing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GamePane());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public enum Player {

        RED, BLUE, NONE;
    }

    public class GamePane extends JPanel {

        private BoardPane boardPane;
        private JLabel label;
        private Player player = null;

        public GamePane() {
            setLayout(new BorderLayout());
            boardPane = new BoardPane();
            add(boardPane);

            label = new JLabel("...");
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setForeground(Color.WHITE);
            label.setOpaque(true);
            label.setBorder(new EmptyBorder(10, 10, 10, 10));

            add(label, BorderLayout.NORTH);

            updatePlayer();

            boardPane.addChangeListener(e -> updatePlayer());
        }

        protected void updatePlayer() {
            String text = "...";
            Color color = null;
            if(player == null || player.equals(Player.BLUE)) {
                player = Player.RED;
                text = "Red";
                color = Color.RED;
            } else if(player.equals(Player.RED)) {
                player = Player.BLUE;
                text = "Blue";
                color = Color.BLUE;
            }

            label.setText(text);
            label.setBackground(color);
            boardPane.setPlayer(player);
        }
    }

    public class BoardPane extends JPanel {

        private Player[][] board;
        private Player player;

        private int hoverColumn = -1;

        public BoardPane() {
            board = new Player[7][7];
            for(int row = 0; row < board.length; row++) {
                for(int col = 0; col < board[row].length; col++) {
                    board[row][col] = Player.NONE;
                }
            }

            MouseAdapter mouseHandler = new MouseAdapter() {

                @Override
                public void mouseMoved(MouseEvent e) {
                    hoverColumn = getColumnAt(e.getPoint());
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hoverColumn = -1;
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if(hoverColumn > -1) {
                        addPieceTo(hoverColumn);
                        repaint();
                    }
                }
            };

            addMouseMotionListener(mouseHandler);
            addMouseListener(mouseHandler);
        }

        public void addChangeListener(ChangeListener listener) {
            listenerList.add(ChangeListener.class, listener);
        }

        public void removeChangeListener(ChangeListener listener) {
            listenerList.add(ChangeListener.class, listener);
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        protected void addPieceTo(int col) {
            boolean added = false;
            if(col >= 0 && col < board[0].length) {
                for(int row = 0; row < board.length; row++) {
                    if(board[row][col] != Player.NONE) {
                        if(row >= 0) {
                            board[row - 1][col] = player;
                            added = true;
                        }
                        break;
                    }
                }
            }
            if(!added) {
                if(board[0][col] == Player.NONE) {
                    board[board.length - 1][col] = player;
                    added = true;
                }
            }
            if(added) {
                fireStateChanged();
            }
            repaint();
        }

        protected void fireStateChanged() {
            ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);
            if(listeners != null && listeners.length > 0) {
                ChangeEvent evt = new ChangeEvent(this);
                for(ChangeListener listener : listeners) {
                    listener.stateChanged(evt);
                }
            }
        }

        protected int getColumnAt(Point p) {

            int size = Math.min(getWidth() - 1, getHeight() - 1);

            int xOffset = (getWidth() - size) / 2;
            int yOffset = (getHeight() - size) / 2;

            int padding = getBoardPadding();

            int diameter = (size - (padding * 2)) / 8;

            int xPos = p.x - xOffset;
            int column = xPos / diameter;

            return Math.min(Math.max(0, column), board[0].length - 1);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 600);
        }

        protected int getBoardPadding() {
            return 10;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            int size = Math.min(getWidth() - 1, getHeight() - 1);

            int xOffset = (getWidth() - size) / 2;
            int yOffset = (getHeight() - size) / 2;

            g2d.fill(new RoundRectangle2D.Double(xOffset, yOffset, size, size, 20, 20));

            int padding = getBoardPadding();

            int diameter = (size - (padding * 2)) / 8;

            for(int row = 0; row < board.length; row++) {
                int yPos = (yOffset + padding) + (diameter * row);
                for(int col = 0; col < board[row].length; col++) {
                    int xPos = (xOffset + padding) + (diameter * col);
                    switch(board[row][col]) {
                        case RED:
                            g2d.setColor(Color.RED);
                            break;
                        case BLUE:
                            g2d.setColor(Color.BLUE);
                            break;
                        default:
                            g2d.setColor(getBackground());
                            break;
                    }
                    g2d.fill(new Ellipse2D.Double(xPos, yPos, diameter, diameter));
                }
            }

            if(hoverColumn > -1) {
                int yPos = (yOffset + padding) + (diameter * 0);
                int xPos = (xOffset + padding) + (diameter * hoverColumn);
                if(player != null) {
                    switch(player) {
                        case RED:
                            g2d.setColor(Color.RED);
                            break;
                        case BLUE:
                            g2d.setColor(Color.BLUE);
                            break;
                        default:
                            g2d.setColor(getBackground());
                            break;
                    }
                    g2d.fill(new Ellipse2D.Double(xPos, yPos, diameter, diameter));
                }
            }

            g2d.dispose();
        }
    }
}