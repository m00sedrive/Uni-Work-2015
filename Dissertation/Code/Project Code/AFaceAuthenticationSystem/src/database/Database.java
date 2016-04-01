package database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Database {
	
	private ArrayList<String> list = new ArrayList<String>();
	private String filepath = "images/xm2vts/";
	private int imageSetSize = 8;
	public int imageRows = 55;
	public int imageCols = 51;
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
	
	private Person[] initializePersonImageSet(int imageSetSize) {
		
		// set image set array size
		personImageSet = new Person[imageSetSize];
		for(int i=0; i<=imageSetSize-1 ; i++) {			
			// populate each persons details in the image set array
			personImageSet[i] = new Person();
			try {
				// load images
				personImageSet[i].setImage(ImageIO.read(new FileInputStream(filepath + list.get(i))));
				// set person details
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

	public Person[] getPersonImageSet() {
		return personImageSet;
	}

	public void setPersonImageSet(Person[] personImageSet) {
		this.personImageSet = personImageSet;
	}
	
	public Person getPerson(int num) {
		return personImageSet[num];
	}
	
	public void setPerson(Person person, int numPersonInImageSet) {
		this.personImageSet[numPersonInImageSet] = person;
	}

	public int getImageSetSize() {
		return imageSetSize;
	}

}
