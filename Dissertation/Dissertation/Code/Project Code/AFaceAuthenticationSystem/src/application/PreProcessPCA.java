package application;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class PreProcessPCA {
	
	private int numOfImg = 8;
	private ArrayList<String> list;
	private BufferedImage[] images;
	private String filepath = "C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\";
	
	// constructor
	public PreProcessPCA(){
		// load image library
		//loadImageLibrary();
	}
	
	// read image library into memory
	public void loadImageLibrary(){
		// get list of file paths
		getFilepaths();
		
		for(int i=0; i<numOfImg; i++) {
			try {
				images[i] = ImageIO.read(new File(filepath + list.get(i)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// Source: http://www.answers.opencv.org/question/28348/converting-bufferedimage-to-mat-in-java/
	// Convert image to Mat
	public Mat matify(BufferedImage im) {
	    // Convert INT to BYTE
	    //im = new BufferedImage(im.getWidth(), im.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
	    // Convert bufferedimage to byte array
	    byte[] pixels = ((DataBufferByte) im.getRaster().getDataBuffer())
	            .getData();

	    // Create a Matrix the same size of image
	    Mat image = new Mat(im.getHeight(), im.getWidth(), CvType.CV_64FC1);
	    // Fill Matrix with image values
	    image.put(0, 0, pixels);

	    return image;
	}
	
	// source: http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
    /**
	 * @param 
	 * @return The output can be assigned either to BufferedImage or to Image
	 */
	public BufferedImage Mat2BufferedImage(Mat m){
	    int type = BufferedImage.TYPE_BYTE_GRAY;
	    if ( m.channels() > 1 ) {
	        type = BufferedImage.TYPE_3BYTE_BGR;
	    }
	    int bufferSize = m.channels()*m.cols()*m.rows();
	    byte [] b = new byte[bufferSize];
	    m.get(0,0,b); // get all the pixels
	    BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
	    final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	    System.arraycopy(b, 0, targetPixels, 0, b.length);  
	    return image;
	}
		
	public void getFilepaths() {
		Scanner s = null;
		try {
			s = new Scanner(new File("C:\\Users\\user\\workspace\\AFaceAuthenticationSystem\\src\\application\\filepaths.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list = new ArrayList<String>();
		while (s.hasNextLine()){
		    list.add(s.nextLine());
		}
		s.close();
		
	}
}
