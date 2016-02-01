package database;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Database {
	
	private ArrayList<String> list = new ArrayList<String>();
	private BufferedImage[] images;
	private String filepath = "images/xm2vts/";
	private int numOfPeople = 8;
	private int imageSetSize = 8;
	private Person[] personImageSet;
	
	// constructor
	public void setUpDatabase(){
		loadImageLibrary();
	}
	
	// read image library into memory
	public void loadImageLibrary(){
		// get list of file paths
		getFilepathList();
		// read and store image set data into memory
		initializePersonImageSet(imageSetSize);
	}
	
	private Person[] initializePersonImageSet(int numOfPeople) {
		
		// set image set size
		personImageSet = new Person[numOfPeople + 1];
		
		for(int i=0; i<numOfPeople + 1; i++) {
			
			// initialize pe
			personImageSet[i] = new Person();
			try {
				System.out.println(filepath + list.get(i));
				personImageSet[i].setImage(ImageIO.read(new FileInputStream(filepath + list.get(i))));
				personImageSet[i].setPersonID(i);
				personImageSet[i].setImageNum(i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return personImageSet;
	}
	
	public void getFilepathList() {
		
		// Open the file
		FileInputStream fstream;
		try {
			fstream = new FileInputStream("list_of_image_names.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;

			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				// add string line to array list
				list.add(strLine);
			}
			//Close the input stream
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public BufferedImage[] getImages() {
		return images;
	}

	public void setImages(BufferedImage[] images) {
		this.images = images;
	}
}
