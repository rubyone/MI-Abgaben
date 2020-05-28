package at.imagelibrary;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * Samlung von Methoden fuer Verarbeitung von Bilder<br><br>
 * 
 * <b>Lizenz:</b><br>
 * Diese Klasse wird kostenfrei verbreitet und darf geaendert werden.<br><br>
 * 
 * <b>Copyright:</b><br>
 * by Aleksej Tokarev 2011 <br>
 *
 * <br><b>Date:</b><br>
 * 08.07.2011
 * 
 * @author Aleksej Tokarev<br>
 * <a href="http://atoks.bplaced.net">{@link http://atoks.bplaced.net}</a>
 * @version 1.1
 */

/* 
 * Neuerungen zu Version 1.0
 * - Rundngsfehler im adaptImage werden beseitig, in dem Integer werden mit Float ersetzt
 * - Eingefuegte Methoden
 *		- getBufferedImage(InputStream is)
 *		- getBufferedImage(URL url)
 *		- getBufferedImage(String str)
 */

public class ImageTool {
	// http://www.javalobby.org/articles/ultimate-image/
	
	// Anaglyph Modi
	public static final int ANAGLYPH_RAD_CYAN_COLOR		=  1;
	public static final int ANAGLYPH_RAD_CYAN_GRAY		= -1;
	public static final int ANAGLYPH_RAD_GREEN_COLOR	=  2;
	public static final int ANAGLYPH_RAD_GREEN_GRAY		= -2;
	public static final int ANAGLYPH_RAD_BLUE_COLOR		=  3;
	public static final int ANAGLYPH_RAD_BLUE_GRAY		= -3;
	public static final int ANAGLYPH_YELLOW_BLUE_COLOR	=  4;
	public static final int ANAGLYPH_YELLOW_BLUE_GRAY	= -4;
	
	// Color Mix
	private static final int ANAGLYPH_RAD_CYAN		=  1;
	private static final int ANAGLYPH_RAD_GREEN		=  2;
	private static final int ANAGLYPH_RAD_BLUE		=  3;
	private static final int ANAGLYPH_YELLOW_BLUE	=  4;
	
	/**
	 * Methode gibt eine BufferedImage aus InputStream
	 * @param is InputStream mit Image
	 * @return BufferedImage
	 * @throws ImageToolException
	 */
	public static BufferedImage getBufferedImage(InputStream is) throws ImageToolException{
		try {
			return ImageIO.read(is);
		} catch (IOException e) {
			throw new ImageToolException("UPS: "+e.getMessage());
		}
	}
	/**
	 * Methode gibt eine BufferedImage aus URL
	 * @param url URL mit Image
	 * @return BufferedImage
	 * @throws ImageToolException
	 */
	public static BufferedImage getBufferedImage(URL url) throws ImageToolException{
		try {
			return ImageIO.read(url);
		} catch (IOException e) {
			throw new ImageToolException("UPS: "+e.getMessage());
		}
	}
	/**
	 * Methode gibt eine BufferedImage aus String adresse. Benutzt new File(str)
	 * @param str String welche wird als Parameter fuer File genomen
	 * @return BufferedImage
	 * @throws ImageToolException
	 */
	public static BufferedImage getBufferedImage(String str) throws ImageToolException{
		try {
			return ImageIO.read(new File(str));
		} catch (IOException e) {
			throw new ImageToolException("UPS: "+e.getMessage());
		}
	}
	
	/**
	 * Methode erstelt eine Anaglyphbild
	 * @param left Linkebild
	 * @param right Rechtebild
	 * @param anaglyph_type Typ von Anaglyphbild<br>
	 * <b>Moegliche Typen:</b><br>
	 * ANAGLYPH_RAD_CYAN_COLOR (defined in ImageTool)<br>
	 * ANAGLYPH_RAD_CYAN_GRAY (defined in ImageTool)<br>
	 * ANAGLYPH_RAD_GREEN_COLOR (defined in ImageTool)<br>
	 * ANAGLYPH_RAD_GREEN_GRAY (defined in ImageTool)<br>
	 * ANAGLYPH_RAD_BLUE_COLOR (defined in ImageTool)<br>
	 * ANAGLYPH_RAD_BLUE_GRAY (defined in ImageTool)<br>
	 * ANAGLYPH_YELLOW_BLUE_COLOR (defined in ImageTool)<br>
	 * ANAGLYPH_YELLOW_BLUE_GRAY (defined in ImageTool)<br>
	 * 
	 * @return Zusammen gesetzte Anaglyphbild
	 */
	public static BufferedImage getAnaglyphImage(BufferedImage left, BufferedImage right, int anaglyph_type){
		
		int width = Math.min(left.getWidth(), right.getWidth());
		int height = Math.min(left.getHeight(), right.getHeight());
		
		BufferedImage anaglyphImage = new BufferedImage(width, height, left.getType());
		
		// Wenn Type auf Grau gesetzt, dann Quellbilder Grau machen
		if(anaglyph_type<0){
			left  = getGrayImage(left);
			right = getGrayImage(right);
		}
		
		// Anaglyphbild erstellen
		for(int w=0; w<width; w++){
			for(int h=0; h<height; h++){
				anaglyphImage.setRGB(w, h, multyplayColor(new Color(left.getRGB(w,h)), new Color(right.getRGB(w,h)), Math.abs(anaglyph_type)).getRGB());			
			}
		}
		
		return anaglyphImage;
	}
	/**
	 * Methode erstelt eine Anaglyphbild
	 * @param left Linkebild
	 * @param right Rechtebild
	 * @param leftMatrix Multiplexer fuer linke Bild
	 * @param rightMatrix Multiplexer fuer rechte Bild
	 * @return Zusammen gesetzte Anaglyphbild
	 * @throws ImageToolException 
	 */
	public static BufferedImage getAnaglyphImage(BufferedImage left, BufferedImage right, double[][] leftMatrix, double[][] rightMatrix) throws ImageToolException{
		int width = Math.min(left.getWidth(), right.getWidth());
		int height = Math.min(left.getHeight(), right.getHeight());
		
		BufferedImage anaglyphImage = new BufferedImage(width, height, left.getType());
		// Anaglyphbild erstellen
		for(int w=0; w<width; w++){
			for(int h=0; h<height; h++){
				Color pxL = new Color(left.getRGB(w, h));
				Color pxR = new Color(right.getRGB(w, h));
				
				int r = (int)Math.round(((leftMatrix[0][0] * pxL.getRed()) + (leftMatrix[0][1] * pxL.getGreen()) + (leftMatrix[0][2] * pxL.getBlue())) + ((rightMatrix[0][0] * pxR.getRed()) + (rightMatrix[0][1] * pxR.getGreen()) + (rightMatrix[0][2] * pxR.getBlue())));
				int g = (int)Math.round(((leftMatrix[1][0] * pxL.getRed()) + (leftMatrix[1][1] * pxL.getGreen()) + (leftMatrix[1][2] * pxL.getBlue())) + ((rightMatrix[1][0] * pxR.getRed()) + (rightMatrix[1][1] * pxR.getGreen()) + (rightMatrix[1][2] * pxR.getBlue())));
				int b = (int)Math.round(((leftMatrix[2][0] * pxL.getRed()) + (leftMatrix[2][1] * pxL.getGreen()) + (leftMatrix[2][2] * pxL.getBlue())) + ((rightMatrix[2][0] * pxR.getRed()) + (rightMatrix[2][1] * pxR.getGreen()) + (rightMatrix[2][2] * pxR.getBlue())));				
				
				try{
					anaglyphImage.setRGB(w, h, new Color(r,g,b).getRGB());	
				}catch(IllegalArgumentException e){
					throw new ImageToolException("UPS: "+e.getMessage());
				}
			}
		}
		return anaglyphImage;
	}
	
	/**
	 * Methode wandelt Bild in graue Stuffen
	 * @param img Bildquelle
	 * @return Schwarzweise Bild
	 */
	public static BufferedImage getGrayImage(BufferedImage img){
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY); 
		ColorConvertOp op = new ColorConvertOp(cs, null);  
		return op.filter(img, null);
	}
	/**
	 * Methode Aendert bild Groesse
	 * @param img Bildquelle
	 * @param width Neue Breite
	 * @param height Neue Hoehe
	 * @return Skalierte Bild
	 */
	public static BufferedImage resizeImage(BufferedImage img, int width, int height){
		
		BufferedImage resizedCopyImage = new BufferedImage(width, height, img.getType());
		
		Graphics2D g = resizedCopyImage.createGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.dispose();
		
		return resizedCopyImage; 
	}
	/**
	 * Methode Aendert bild Groesse so das Proportionen werde beibechalten
	 * @param img Bildquelle
	 * @param width Breite an welche Bild adaptiert werden muss
	 * @param height Hoehe an welche Bild adaptiert werden muss
	 * @return Skalierte (Adaptierte) Bild
	 * @throws ImageToolException 
	 */
	public static BufferedImage adaptImage(BufferedImage img, int width, int height) throws ImageToolException{
		// Proportionen ausrechnen fuer Hoehe
		// An Breite anpassen
		float newWidth  = width;
		float newHeight = 0;
		
		float prozent = (100F * width / img.getWidth()); // Aenderungsprozent ausrechnen
		newHeight = prozent * img.getHeight() / 100F; // Neue Hoehe ausrechnen
		
		// Testen ob neue Hoehe Past in gewuenschte
		// Wenn nicht Bild an Hoehe anpassen
		if(newHeight > height){
			// Proportionen ausrechnen fuer Hoehe
			newHeight = height;
			
			prozent = (100F * height / img.getHeight()); // Aenderungsprozent ausrechnen
			newWidth = prozent * img.getWidth() / 100F; // Neue Breite ausrechnen
		}
		BufferedImage resizedCopyImage = null;
		try{
		resizedCopyImage = new BufferedImage(Math.round(newWidth), Math.round(newHeight), img.getType());
		}catch(OutOfMemoryError e){
			throw new ImageToolException("UPS: "+e.getMessage());
		}
		
		Graphics2D g = resizedCopyImage.createGraphics();
		g.drawImage(img, 0, 0, Math.round(newWidth), Math.round(newHeight), null);
		g.dispose();
		
		return resizedCopyImage; 
	}
	
	/**
	 * Methode schneidet eine Teil von Bild aus
	 * @param img Bildquelle
	 * @param x Startposition X
	 * @param y Startposition Y
	 * @param width Breite von Ausschnit
	 * @param height Hoehe von Ausschnit
	 * @return Ausgeschnitene Bild
	 */
	public static BufferedImage cutPartOfImage(BufferedImage img, int x, int y, int width, int height){
		return img.getSubimage(x, y, width, height);
	}
	/**
	 * Methode dreht Bild in degree Grad rum
	 * @param img Bildquelle
	 * @param degree Rotationsgrad
	 * @return Gedrate Bild
	 */
	public static BufferedImage rotateImage(BufferedImage img, double degree){
		double sin = Math.abs(Math.sin(Math.toRadians(degree)));
		double cos = Math.abs(Math.cos(Math.toRadians(degree)));
		int width  = img.getWidth();
		int height = img.getHeight();
		int newWidth  = (int)Math.floor(width*cos+height*sin);
		int newHeight = (int)Math.floor(height*cos+width*sin);

		BufferedImage rotadetCopyImage = new BufferedImage(newWidth, newHeight, Transparency.TRANSLUCENT);

		Graphics2D g = rotadetCopyImage.createGraphics();
		g.translate((newWidth-width)/2, (newHeight-height)/2);
		g.rotate(Math.toRadians(degree), width/2, height/2);
		g.drawRenderedImage(img, null);
		g.dispose();
		
		return rotadetCopyImage;
	}
	/**
	 * Methode fuegt ein Wasserzeichen zu Hauptbild
	 * @param img Hauptbildquelle
	 * @param watermark Wasserzeichenquelle
	 * @param x X-Position an welche mus Wasserzeichen gesetzt werden
	 * @param y Y-Position an welche mus Wasserzeichen gesetzt werden
	 * @param transperancy Duersichtlichkeit von Wasserzeichen
	 * @return Origenalbild mit Wasserzeichen
	 */
	public static BufferedImage addWatermarkToImage(BufferedImage img, BufferedImage watermark, int x, int y,  float transperancy){
		Graphics2D g = img.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transperancy));
		g.drawImage(watermark, x, y, watermark.getWidth(), watermark.getHeight(), null);
		g.dispose();
		
		return img; 
	}
	/**
	 * Methode spigeld Bild Horizontal
	 * @param img Bildquelle
	 * @return Horizontal gespiegelte Bild
	 */
	public static BufferedImage reflectHorizontalImage(BufferedImage img){
		int width  = img.getWidth();
		int height = img.getHeight();
		
		BufferedImage reflectCopyImage = new BufferedImage(width, height, img.getType());
		
		Graphics2D g = reflectCopyImage.createGraphics();
		g.drawImage(img, 0, 0, width, height, width, 0, 0, height,  null); 
		g.dispose();
		
		return reflectCopyImage; 
	}
	/**
	 * Methode spigeld Bild Vertikal
	 * @param img Bildquelle
	 * @return Vertikal gespiegelte Bild
	 */
	public static BufferedImage reflectVerticalImage(BufferedImage img){
		int width  = img.getWidth();
		int height = img.getHeight();
		
		BufferedImage reflectCopyImage = new BufferedImage(width, height, img.getType());
		
		Graphics2D g = reflectCopyImage.createGraphics();
		g.drawImage(img, 0, 0, width, height, 0, height, width, 0, null);  
		g.dispose();
		
		return reflectCopyImage; 
	}
	/**
	 * Methode macht Bild duerchsichtig
	 * @param img Bildquelle 
	 * @param transperancy Duersichtigkeitstaerke
	 * @return Duersichtige Bild
	 */
	public static BufferedImage makeTransparentImage(BufferedImage img, float transperancy){
		int width  = img.getWidth();
		int height = img.getHeight();
		
		BufferedImage transparentCopyImage = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
		
		Graphics2D g = transparentCopyImage.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transperancy));  
		g.drawImage(img, 0, 0, null);  
		g.dispose();
		
		return transparentCopyImage; 
	}
	
	//#######################################################################
	//################           PRIVATE HILFSMETHODEN        ###############
	//#######################################################################
	
	/*
	 * Methode vermischt 2 Farben zu Anagliph Farbe
	 * @param l Color von linke  Bild
	 * @param r Color von rechte Bild
	 * @param type Type von Mixen z.B (rot/cyan), (gelb/blau)
	 * Moegliche Typen:
	 * ANAGLYPH_RAD_CYAN
	 * ANAGLYPH_RAD_GREEN
	 * ANAGLYPH_RAD_BLUE
	 * ANAGLYPH_YELLOW_BLUE
	 * 
	 * @return gemischte Farbe
	 */
	private static Color multyplayColor(Color l, Color r, int type){
		
		double[] leftMatrix  = {0,0,0}; // R, G, B
		double[] rightMatrix = {0,0,0}; // R, G, B
		
		if(type==ANAGLYPH_RAD_CYAN){
			leftMatrix  = new double[] {1,0,0}; // R, G, B
			rightMatrix = new double[] {0,1,1}; // R, G, B
		}
		
		if(type==ANAGLYPH_RAD_GREEN){
			leftMatrix  = new double[] {1,0,0}; // R, G, B
			rightMatrix = new double[] {0,1,0}; // R, G, B
		}
		
		if(type==ANAGLYPH_RAD_BLUE){
			leftMatrix  = new double[] {1,0,0}; // R, G, B
			rightMatrix = new double[] {0,0,1}; // R, G, B
		}
		
		if(type==ANAGLYPH_YELLOW_BLUE){
			leftMatrix  = new double[] {1,1,0}; // R, G, B
			rightMatrix = new double[] {0,0,1}; // R, G, B
		}
		
		// Color Anaglyphs
		int rC = (int)Math.round(((leftMatrix[0] * l.getRed()))   +  ((rightMatrix[0] * r.getRed())));
		int gC = (int)Math.round(((leftMatrix[1] * l.getGreen())) +  ((rightMatrix[1] * r.getGreen())));
		int bC = (int)Math.round(((leftMatrix[2] * l.getBlue()))  +  ((rightMatrix[2] * r.getBlue())));
		
		
		return new Color(rC, gC, bC);
	}
}
