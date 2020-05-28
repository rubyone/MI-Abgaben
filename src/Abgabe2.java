
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;

import at.imagelibrary.ImageToolException;
import at.imagelibrary.StereoImage;


public class Abgabe2 {
    public static void main(String[] args){
        JFrame f = new JFrame("StereoImage -1");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(408, 325);

        JFrame f1 = new JFrame("StereoImage 1");
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f1.setSize(408, 325);

        try {




            BufferedImage left = ImageIO.read(new File("src/leftSmall.jpg"));
            BufferedImage right = ImageIO.read(new File("src/rightSmall.jpg"));



            StereoImage stereo = new StereoImage(left, right);

/*
            public static final int ANAGLYPH_RAD_CYAN_COLOR		=  1;
            public static final int ANAGLYPH_RAD_CYAN_GRAY		= -1;
            public static final int ANAGLYPH_RAD_GREEN_COLOR	=  2;
            public static final int ANAGLYPH_RAD_GREEN_GRAY		= -2;
            public static final int ANAGLYPH_RAD_BLUE_COLOR		=  3;
            public static final int ANAGLYPH_RAD_BLUE_GRAY		= -3;
            public static final int ANAGLYPH_YELLOW_BLUE_COLOR	=  4;
            public static final int ANAGLYPH_YELLOW_BLUE_GRAY	= -4;


            double[][] leftFilter = {
                    {1,0,0},
                    {0,0,0},
                    {0,0,0}
            };

            double[][] rightFilter = {
                    {0,0,0},
                    {0,1,0},
                    {0,0,1}
            };*/

            ImageIcon icon = new ImageIcon(stereo.getResultImage(-1  ));
            ImageIcon icon1 = new ImageIcon(stereo.getResultImage(1));
//            ImageIcon icon = new ImageIcon(stereo.getResultImage(leftFilter, rightFilter));

            f.add(new JLabel(icon));
            f1.add(new JLabel(icon1));

            Image img = icon.getImage();
            Image img1 = icon1.getImage();
            ImageIO.write((RenderedImage) img1, "jpg", new File("src/1.jpg"));
            ImageIO.write((RenderedImage) img, "jpg", new File("src/-1.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ImageToolException e) {
            e.printStackTrace();
        }
        f.setVisible(true);
        f1.setVisible(true);
    }
}