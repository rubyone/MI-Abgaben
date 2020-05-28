import at.imagelibrary.ImageToolException;
import at.imagelibrary.StereoImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class shift {

    public static void main(String[] args){
        JFrame f = new JFrame("StereoImage");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(408, 325);

        try {




            BufferedImage left = ImageIO.read(new File("src/leftSmall.jpg"));
            BufferedImage right = ImageIO.read(new File("src/rightSmall.jpg"));



            StereoImage stereo = new StereoImage(left, right);
            BufferedImage anaglyph = stereo.getResultImage(StereoImage.ANAGLYPH_YELLOW_BLUE_GRAY);


            double[][] leftFilter = {
                    {1,0,0},
                    {0,0,0},
                    {0,0,0}
            };

            double[][] rightFilter = {
                    {0,0,0},
                    {0,1,0},
                    {0,0,1}
            };

            ImageIcon icon = new ImageIcon(stereo.getResultImage(leftFilter, rightFilter));

            f.add(new JLabel(icon));

            Image img = icon.getImage();

            ImageIO.write((RenderedImage) img, "jpg", new File("src/output.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ImageToolException e) {
            e.printStackTrace();
        }
        f.setVisible(true);
    }
}
