import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.JFrame;


public class Abgabe extends JFrame {

         static int width = 500;
         static int height = 500;
        private static int size = 2;





        public static void main(String[] args) {
                JFrame frame = new MyFrame();

                JButton up = new JButton("Up");
                JButton down = new JButton("Down");

                JPanel panel = new JPanel();
                panel.add(up);
                panel.add(down);
                frame.add(panel);

                frame.setSize(width,height);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setVisible(true);

                up.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        size++;
                        System.out.println("Up!");
                        System.out.println(size);

                    }
                });

                down.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        size--;
                        System.out.println("Down!");
                        System.out.println(size);

                    }
                });


            }


        private static class MyFrame extends JFrame {
            @Override
            public void paint(Graphics g) {
                super.paint(g);




                g.fillRect(0, 0, width, height);
                g.setFont(new Font("Dialog", Font.PLAIN, size));
                g.setColor(Color.white);
                g.drawString("x", width/2,height/2);

            }
        }
    }
