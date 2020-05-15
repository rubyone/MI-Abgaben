import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Abgabe implements ActionListener{

    int value = 0;
    JLabel label;


    public Abgabe() {

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JButton btn = new JButton("FUCK ME");
        label = new JLabel( "Number of Fucks given: 0");

        btn.addActionListener(this);

//        panel.setBorder(BorderFactory.createEmptyBorder(50,50,10,30));
//        panel.setSize(10,10);
//        panel.setLayout(new GridLayout(0,1));
        panel.add(btn);
        panel.add(label);

        frame.setPreferredSize(new Dimension(300,300));


        frame.add(panel, BorderLayout.PAGE_END);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Test");
        frame.pack();
        frame.setVisible(true);
    }

    public void paint(Graphics g){

        g.drawLine(10, 10, 200, 300);
    }


    public  static void main(String[] args) {
        new Abgabe();

    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        value++;
        label.setText("Number of Fucks given: 0" + value);
    }
}

