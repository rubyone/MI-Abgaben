package at.imagelibrary;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Klasse zerlegt MPO Datei in JPEG Bildern<br><br>
 * <b>Lizenz:</b><br>
 * Diese Klasse wird kostenfrei verbreitet und darf geaendert werden.<br><br>
 * 
 * <b>Copyright:</b><br>
 * by Aleksej Tokarev 2011<br>
 * 
 * <br><b>Date:</b><br>
 * 28.05.2011
 * 
 * @author Aleksej Tokarev<br>
 * <a href="http://atoks.bplaced.net">{@link http://atoks.bplaced.net}</a>
 * @version 1.1
 */
/* 
 * Neuerungen zu Version 1.0
 * - Konstanten werden überarbeitet
 * - Schließen von Streamers wird in finally Block ausgeliefert
 * - Vector wird mit ArrayList ersetzt
 * - Alle Exeptions werden auf ImageToolException reduziert
 * - Debugausgaben werden rausgenommen
 */
public class MPOSplitter {
	
	public static final int SOM   = 255; // Start of Marke (0xFF)
	public static final int SOI   = 216; // Start of Image (0xD8)
	public static final int EOI   = 217; // Ende of Image  (0xD9)
	public static final int APP0  = 224; // Application  0 (0xE0)
	public static final int APP1  = 225; // Application  1 (0xE1)
	public static final int APP2  = 226; // Application  2 (0xE2)
	public static final int APP3  = 227; // Application  3 (0xE3)
	public static final int APP4  = 228; // Application  4 (0xE4)
	public static final int APP5  = 229; // Application  5 (0xE5)
	public static final int APP6  = 230; // Application  6 (0xE6)
	public static final int APP7  = 231; // Application  7 (0xE7)
	public static final int APP8  = 232; // Application  8 (0xE8)
	public static final int APP9  = 233; // Application  9 (0xE9)
	public static final int APP10 = 234; // Application 10 (0xEA)
	public static final int APP11 = 235; // Application 11 (0xEB)
	public static final int APP12 = 236; // Application 12 (0xEC)
	public static final int APP13 = 237; // Application 13 (0xED)
	public static final int APP14 = 238; // Application 14 (0xEE)
	public static final int APP15 = 239; // Application 15 (0xEF)
	
	
	private int offset = -1;
	
	private ArrayList<Integer> starts = new ArrayList<Integer>();
	private ArrayList<Integer> ends   = new ArrayList<Integer>();
	
	private FileInputStream sucher = null;
	
	private File file = null;

	/**
	 * Methode setzt Quelldatei
	 * @param src
	 * @throws ImageToolException 
	 */
	public synchronized void setSRC(String src) throws ImageToolException{
		setSRC(new File(src));
	}
	/**
	 * Methode setzt Quelldatei
	 * @param file
	 * @throws ImageToolException 
	 */
	public synchronized void setSRC(File file) throws ImageToolException{
		if(!file.exists()){
			throw new ImageToolException("UPS: File "+file+" is not found");
		}
		this.file = file;
	}
	/**
	 * Methode gibt Bildgroesse zurueck
	 * @return
	 */
	public synchronized Dimension getDimension(){
		try {
			BufferedImage bi = ImageIO.read(file);
			return new Dimension(bi.getWidth(), bi.getHeight());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Methode gibt Dateigroesse zurueck
	 * @return
	 */
	public synchronized long getSize(){
		return file.length();
	}
	/**
	 * Methode gibt Anzahl von gefundenen Bilder
	 * @return
	 */
	public synchronized int getImageCount(){
		int sL = starts.size();
		int eL = ends.size();
		return (sL < eL) ? sL : eL;
	}
	/**
	 * Methode gibt eine InputStream von gewuenste Bild mit Nr im <b>param nr</b> <br>
	 * dise Methode muss nach searchImgs() ausgerufen werden
	 * @param nr Bildnummer
	 * @return InputStream Stream mit gewuenste Bild
	 * @throws ImageToolException 
	 */
	public synchronized InputStream getInputStreamImageNr(int nr) throws ImageToolException{
		if(starts.size() > nr && ends.size() > nr){
			int start = starts.get(nr);
			int length  = (ends.get(nr)-start)+1; // +1 weil Letzte Byte muss auch gespeichert werden
			
			// Input stream erstellen
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(this.file);
			} catch (FileNotFoundException e) {
				throw new ImageToolException("UPS: File \""+this.file+"\" not found. "+e.getMessage());
			}
			try {
				fis.skip(start);
			} catch (IOException e) {
				if(fis!=null){
					try {
						fis.close();
					} catch (IOException e1) {}
				}
				throw new ImageToolException("UPS: Exception by skip("+start+")"+e.getMessage());
			}
			
			byte[] byteArray = new byte[length];
			try {
				fis.read(byteArray);
			} catch (IOException e) {
				if(fis!=null){
					try {
						fis.close();
					} catch (IOException e1) {}
				}
				throw new ImageToolException("UPS: Exception by read(new byte["+length+"])"+e.getMessage());
			}
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					throw new ImageToolException("UPS: Exception by closing"+e.getMessage());
				}
			}
			return new ByteArrayInputStream(byteArray);
		}else{
			throw new ImageToolException("UPS: Image with ID "+nr+" not exists. Images count = "+getImageCount());
		}
	}
	
	/**
	 * Methode gibt eine BufferedImage von gewuenste Bild mit Nr im <b>param nr</b> <br>
	 * dise Methode muss nach searchImgs() ausgerufen werden
	 * @param nr Bildnummer
	 * @return BufferedImage gewuenste Bild
	 * @throws IOException
	 * @throws ImageToolException 
	 */
	public synchronized BufferedImage getImageNr(int nr) throws ImageToolException{
		if(starts.size() > nr && ends.size() > nr){
			// Stream mit Bild in BufferedImage umwandeln
			try {
				return ImageIO.read(getInputStreamImageNr(nr));
			} catch (IOException e) {
				throw new ImageToolException("UPS: Exception by ImageIO.read()"+e.getMessage());
			}
		}else{
			throw new ImageToolException("UPS: Image with ID "+nr+" not exists. Images count = "+getImageCount());
		}
	}
	
	/**
	 * Methode speichert bilder in Datei<br>
	 * dise Methode muss nach searchImgs() ausgerufen werden
	 * @param nr Bildnummer
	 * @param out Datei in welche wird extrairte Bild gespeichert
	 * @throws ImageToolException 
	 */
	public synchronized void saveImageNr(int nr, File out) throws ImageToolException{

		if(starts.size() > nr && ends.size() > nr){
			int start = starts.get(nr);
			int length  = (ends.get(nr)-start)+1; // +1 weil Letzte Byte muss auch gespeichert werden

			FileInputStream fis = null;
			FileOutputStream fos = null;
			try{
				fis = new FileInputStream(this.file);
				fis.skip(start);
				
				fos = new FileOutputStream(out);
				
				byte[] buff;
				int buffSize = 1024;
		        while (length > 0) {
		        	if(length>buffSize){
		        		buff = new byte[buffSize];
		        	}else{
		        		buff = new byte[length];
		        	}
		        	fis.read(buff);
		            fos.write(buff);
		            length-=buff.length;
		        }     
			}catch(IOException e){
				throw new ImageToolException(e.getMessage());
			}finally{
				if(fos!=null){
					try {
						fos.flush();
					} catch (IOException e) {}
					try {
						fos.close();
					} catch (IOException e) {}
				}
				if(fis!=null){
					try {
						fis.close();
					} catch (IOException e) {}
				}
			}
		}else{
			throw new ImageToolException("UPS: Image with ID "+nr+" not exists. Images count = "+getImageCount());
		}
	}
	
	/**
	 * Methode sucht Bilder im MPO datei
	 * @return Anzahl von gefundenen Bilder
	 * @throws ImageToolException 
	 * @throws IOException 
	 */
	public synchronized int searchImgs() throws ImageToolException{
		
		if(file == null){
			throw new ImageToolException("No File: "+file);
		}
		try{
			sucher = new FileInputStream(file);
			
			starts = new ArrayList<Integer>();
			ends = new ArrayList<Integer>();
			offset = -1;
			
			int b;
			while((b = read1byte())!=-1){
				// Start of Marke
				if(b == 0xFF){
					b = read1byte();
					
					// Start of Image
					if(b == SOI){
						starts.add(offset-1); // Start von Bild speichern
					}
					
					// End of Image
					if(b == EOI){
						ends.add(offset); // Ende von Bild speichern
					}
					
					// APP0
					if(b == APP0){
						ignoreApp();
					}
					
					// APP1
					if(b == APP1){
						ignoreApp();
					}
					
					// APP2
					if(b == APP2){
						ignoreApp();
					}
					
					// APP3
					if(b == APP3){
						ignoreApp();
					}
					
					// APP4
					if(b == APP4){
						ignoreApp();
					}
					
					// APP5
					if(b == APP5){
						ignoreApp();
					}
					
					// APP6
					if(b == APP6){
						ignoreApp();
					}
					
					// APP7
					if(b == APP7){
						ignoreApp();
					}
					
					// APP8
					if(b == APP8){
						ignoreApp();
					}
					
					// APP9
					if(b == APP9){
						ignoreApp();
					}
					
					// APP10
					if(b == APP10){
						ignoreApp();
					}
					
					// APP11
					if(b == APP11){
						ignoreApp();
					}
					
					// APP12
					if(b == APP12){
						ignoreApp();
					}
					
					// APP13
					if(b == APP13){
						ignoreApp();
					}
					
					// APP14
					if(b == APP14){
						ignoreApp();
					}
					
					// APP15
					if(b == APP15){
						ignoreApp();
					}
				}
			}
			return this.getImageCount();
		}catch(IOException e){
			throw new ImageToolException(e.getMessage());
		}finally{
			if(sucher!=null){
				try {
					sucher.close();
				} catch (IOException e) {}
			}
		}
	}
	
	// Methode fuer Sucher
	private int read1byte() throws IOException{
		int r = sucher.read();
		if(r!=-1){
			offset++;
		}
		return r;
	}
	// Methode fuer Sucher
	private int read2byte() throws IOException{
		int b1 = read1byte();
		int b2 = -1;
		if(b1!=-1){
			b2 = read1byte();
			if(b1!=-1){
				return (b1 << 8) + (b2);
			}
		}
		return -1;
	}
	// Methode fuer Sucher
	private void jump(int len) throws IOException{
		offset+=sucher.skip(len); // Damit geht wesentlich schneller
	}
	// Methode fuer Sucher
	private void ignoreApp() throws IOException{
		// Lesen folgende 2 Byte und Erstellen aus Beiden laenge von APP
		int length = read2byte(); 	
		// Wenn Laenge von APP vorchanden ist
		if(length!=-1){
			// Anzahl von Byts eilesen ohne Verarbeitung
			jump(length-3); // Minus 3 um auf vorhaerige Bayt von FF-Marke zu kommen
		}
	}
}
