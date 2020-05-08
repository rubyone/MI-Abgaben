import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DrawingOnJLabel extends JFrame
{
    private CustomLabel label;
    private int flag = 1;
    private JPanel contentPane;

    public DrawingOnJLabel()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        label = new CustomLabel(200, 200);
        label.setLabelText("A");
        label.setValues(50, 50, 100, 100, 240, 60);

        final JButton button = new JButton("CLEAR");
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        if (flag == 1)
                        {
                            label.setFlag(flag);
                            flag = 0;
                            button.setText("REPAINT");
                            contentPane.revalidate();
                            contentPane.repaint();
                        }
                        else if (flag == 0)
                        {
                            label.setFlag(flag);
                            flag = 1;
                            button.setText("CLEAR");
                            contentPane.revalidate();
                            contentPane.repaint();
                        }
                    }
                });
            }
        });

        contentPane.add(label);

        add(contentPane, BorderLayout.CENTER);
        add(button, BorderLayout.PAGE_END);
        setSize(300, 300);
        setVisible(true);
    }

    public static void main(String... args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new DrawingOnJLabel();
            }
        });
    }
}

class CustomLabel extends JLabel
{
    private int sizeX;
    private int sizeY;
    private int x, y, width, height, startAngle, arcAngle;
    private int flag = 0;
    private String text;

    public CustomLabel(int sX, int sY)
    {
        sizeX = sX;
        sizeY = sY;
    }

    // Simply call this or any set method to paint on JLabel.
    public void setValues(int x, int y, int width, int height, int startAngle, int arcAngle)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startAngle = startAngle;
        this.arcAngle = arcAngle;
        repaint();
    }

    public void setFlag(int value)
    {
        flag = value;
        repaint();
    }

    public Dimension getPreferredSize()
    {
        return (new Dimension(sizeX, sizeY));
    }

    public void setLabelText(String text)
    {
        super.setText(text);
        this.text = text;
        flag = 0;
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        if (flag == 0)
        {
            g.setColor(Color.RED);
            g.drawString(text, 20, 20);
            g.setColor(Color.BLUE);
            g.drawOval(x, y, width, height);
            g.fillOval(x + 20, y + 20, 15, 15);
            g.fillOval(x + 65, y + 20, 15, 15);
            g.fillRect(x + 40, y + 40, 5, 20);
            g.drawArc(x + 20, y + 30, 55, 55, startAngle, arcAngle);
        }
        else if (flag == 1)
        {
            g.clearRect(x, y, width, height);
        }
    }
}