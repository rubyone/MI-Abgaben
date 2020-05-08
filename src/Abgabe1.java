import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Abgabe1 extends JFrame{

    public static int value;
    public static int step = 20;

    private boolean isDraw = false;
    private CircleFrame cf;
    private JButton draw;
    private JButton up;
    private JButton down;



    public Abgabe1 (){ // see Java Naming Conventions https://www.geeksforgeeks.org/java-naming-conventions

        super("Making a Circle");
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout bord = new BorderLayout(); //you never use bord
        setLayout(bord);  //BTW BorderLayout is the default for JFrame content pane




        //you do not want to add a new panel with every button click so it
        //should NOT be triggered by the action listener
        cf = new CircleFrame();
        add(cf, BorderLayout.CENTER);

        //creating a button
//        draw = new JButton("Draw");
//        up = new JButton("Up");



        //you also need to add the button

//        add(draw, BorderLayout.PAGE_END);/**/


        JPanel subPanel = new JPanel();

        subPanel.add( up = new JButton( "Up" ));
        subPanel.add( draw = new JButton( "Draw" ));
        subPanel.add( down = new JButton( "Down" ));

        draw.addActionListener(event ->  toggleDrawErase() );
        up.addActionListener(event ->  setValueUp() );
        down.addActionListener(event ->  setValueDown() );

        add(subPanel, BorderLayout.PAGE_END);

        pack();
        setVisible(true);

    }



    private void down() {
        System.out.println("Down");
    }

    private void up() {
//        repaint();
        System.out.println("Up");
    }


    private void toggleDrawErase() {
        isDraw = ! isDraw;
        cf.setDraw(isDraw);

        draw.setText(isDraw ?  "Erase" : "Draw");
        repaint();
    }



    private void setValueUp() {
        this.value +=step;
        repaint();

    }

    private void setValueDown() {


            this.value -=step;
            repaint();

    }

    public int getValue() {
//        if (value <= 0) {
//             value = 0;
//
//        } else if ( value >= 255) {
//             value = 255;
//        }
        return value;
    }


    public static void main(String[] arguments){
        new Abgabe1();
    }


}

class CircleFrame extends JPanel{

    private boolean isDraw = false;



    public CircleFrame(){

        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.black);

    }

    @Override
    public void paintComponent(Graphics comp){


        super.paintComponent(comp);
        if(! isDraw() ) return;
        Graphics2D comp2D = (Graphics2D) comp;


        int i = Abgabe1.value;

        System.out.println("Value: " + i);
        if (i <= 0) {
            i = 0;
        } else if ( i >= 255) {
            i = 255;
        }
        Color c =  new Color(255, 255, 255, i);



        comp2D.setColor(c);

        comp2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Ellipse2D.Float circle = new Ellipse2D.Float(250, 250, 20, 20);
        comp2D.fill(circle);
    }

    boolean isDraw() {  return isDraw;  }

    void setDraw(boolean isDraw) {  this.isDraw = isDraw;   }
}