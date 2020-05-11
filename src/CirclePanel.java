import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import javax.swing.JButton;


public class CirclePanel extends JPanel implements ActionListener {

    Timer tm = new Timer(100, this);

    boolean grow = true;

    int XDiameter = 20;
    int YDiameter = 20;

    public CirclePanel() {
        tm.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillOval(50, 50, XDiameter, YDiameter);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SuperSizeCircle();
        repaint();
    }

    public void SuperSizeCircle() {

        if (XDiameter >= 20) {
            grow = false;
        }
        if (XDiameter <= 1) {
            grow = true;
        }

        if (grow) {
            XDiameter += 1;
            YDiameter += 1;
            System.out.println("x" + XDiameter);
            System.out.println("y" + YDiameter);
        } else {
            XDiameter -= 1;
            YDiameter -= 1;
            System.out.println("x" +XDiameter);
            System.out.println("y" + YDiameter);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Circle enlarger");
                CirclePanel co = new CirclePanel();
                frame.add(co);


                JButton button1 = new JButton("Ja, ich sehe den Punkt.");
                co.add(button1);

                JButton button2 = new JButton("Nein, ich sehe den Punkt nicht");
                button2.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent event){
                        System.out.println("Punkt nicht gesehen");
                    }
                });
                co.add(button2);

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1000, 500);
                frame.setVisible(true);
            }
        });
    }
}