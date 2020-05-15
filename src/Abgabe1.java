import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.*;

public class Abgabe1 extends JFrame{

    public static int value = 20;
    public static int step = 20;

    private boolean isDraw = false;
    private CircleFrame cf;
    private JButton draw;
    private JButton up;
    private JButton down;
    private JButton seen;



    public Abgabe1 (){ // see Java Naming Conventions https://www.geeksforgeeks.org/java-naming-conventions

        super("Making a Circle");
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout bord = new BorderLayout(); //you never use bord
        setLayout(bord);  //BTW BorderLayout is the default for JFrame content pane

        cf = new CircleFrame();
        add(cf, BorderLayout.CENTER);

        JPanel subPanel = new JPanel();
        subPanel.add( draw = new JButton( "Start" ));
        subPanel.add( up = new JButton( "Heller" ));
        subPanel.add( down = new JButton( "Dunkler" ));
        subPanel.add( seen = new JButton( "Veränderung gesehen!" ));

        draw.addActionListener(event ->  toggleDrawErase() );
        up.addActionListener(event ->  setValueUp() );
        down.addActionListener(event ->  setValueDown() );
        seen.addActionListener(event ->  printAnswer() );

        add(subPanel, BorderLayout.PAGE_END);

        pack();
        setVisible(true);

    }

    private void printAnswer() {

        System.out.println("Value: " + Abgabe1.value + " wurde gesehen!");

    }

    private void toggleDrawErase() {

        isDraw = ! isDraw;
        cf.setDraw(isDraw);

        draw.setText(isDraw ?  "Stop" : "Start");
        if (draw.getText().equals("Stop")) {
            draw.setForeground(Color.red);
        } else {
            this.value = 20;
            draw.setForeground(Color.black);
        }
        repaint();
    }

    private void setValueUp() {
        this.value =  this.value > 255 ? 255 : (this.value += step);
        repaint();

    }

    private void setValueDown() {
        this.value =  this.value < 0 ? 0 : (this.value -= step);
        repaint();
    }

    public static void main(String[] arguments){
        new Abgabe1();
    }

}

class CircleFrame extends JPanel{

    private boolean isDraw = false;

    public CircleFrame(){
//        System.out.println(getMaximumSize().height.);
        int w = 1920;
        int h= 1200;
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.black);

    }

    @Override
    public void paintComponent(Graphics comp){

        super.paintComponent(comp);

        if (!isDraw()) {
            comp.setColor(Color.white);
            comp.drawString("Klicken Sie auf Start, wenn Sie den Punkt sehen bzw. nicht sehen klicken Sie solange auf heller/dunkler bis Sie eine Veränderung wahrgenommen haben und klicken dann auf Veränderung gesehen!" , 100 ,200);
            comp.setColor(Color.red);
            comp.drawString("X" , 600 ,350);
        }

        if(! isDraw() ) return;
        Graphics2D comp2D = (Graphics2D) comp;

        int i = Abgabe1.value;

        if (i < 0) {
            i = 0;
        } else if ( i > 255 ) {
            i = 255;
        }

        System.out.println("Value: " + i);

        Color c =  new Color(255, 255, 255, i);
        comp2D.setColor(c);

        Ellipse2D.Float circle = new Ellipse2D.Float(250, 250, 2, 2);
        comp2D.fill(circle);
    }

    boolean isDraw() {  return isDraw;  }

    void setDraw(boolean isDraw) {  this.isDraw = isDraw;   }
}