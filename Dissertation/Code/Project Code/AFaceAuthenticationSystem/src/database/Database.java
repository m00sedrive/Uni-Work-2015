package database;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Database {
	
	private ArrayList<String> list = new ArrayList<String>();
	private String filepath = "images/xm2vts/";
	private int imageSetSize = 8;
	public int imageWidth;
	public int imageHeight;
	public long imageSize;
	private List<Person> imageSet1;
	private Person[] imageSet;
	private static File dir;
	private String[] extensions;
	
	// constructor
	public void setUpDatabase(){
		loadImageLibrary();
	}
	
	// read image library into memory
	public void loadImageLibrary(){
		
		// set file path
		dir = new File(filepath);
		// set file extension compatibility
		extensions = new String[] {"gif", "png", "bmp"};
		// filter to identify images based on extension
		FilenameFilter imageFilter = new FilenameFilter() {
			 @Override
		        public boolean accept(final File dir, final String name) {
		            for (final String ext : extensions) {
		                if (name.endsWith("." + ext)) {
		                    return (true);
		                }
		            }
		            return (false);
		        }
		};
		
		// is dir a directory
		if(dir.isDirectory()) {
			int fileCount = 1;
			for(File f: dir.listFiles(imageFilter)) {
				BufferedImage image = null;
				try {
                    // create image set person array
					imageSet1 = new ArrayList<Person>(dir.listFiles().length);
					// read next file in folder
					image = ImageIO.read(f);
					// set image stats
                    this.imageWidth = image.getWidth();
                    this.imageHeight = image.getHeight();
                    this.imageSize = f.length();
					// create person
					Person person = new Person(image, f.getName(), fileCount, image.getWidth(), image.getHeight(), f.length());
                    // add person to set
					imageSet1.add(person);
					
                    // debug
					System.out.println("Image Number: " + fileCount);
                    System.out.println("image: " + f.getName());
                    System.out.println(" width : " + image.getWidth());
                    System.out.println(" height: " + image.getHeight());
                    System.out.println(" size  : " + f.length());
                    
                    fileCount++;
                } catch (final IOException e) {
                    e.printStackTrace();
                }
			}
		}
		
		// get list of file paths
		//getFilepathList();
		// read and store image set data into memory
		//initializePersonImageSet(imageSetSize);
	}
	/*
	private Person[] initializeImageSet(int imageSetSize) {
		// set image set array size
		imageSet = new Person[imageSetSize];
		for(int i=0; i<=imageSetSize-1 ; i++) {			
			// populate each persons details in the image set array
			imageSet[i] = new Person();
			try {
				// load images
				imageSet[i].setImage(ImageIO.read(new FileInputStream(filepath + list.get(i))));
				// set person details
				imageSet[i].setPersonName("something");
				imageSet[i].setImageNum(i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// normalize image set before storing
		
		return imageSet;
	}
	*/
	
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
		return imageSet;
	}

	public void setPersonImageSet(Person[] personImageSet) {
		this.imageSet = personImageSet;
	}
	
	public Person getPerson(int num) {
		return imageSet[num];
	}
	
	public void setPerson(Person person, int numPersonInImageSet) {
		this.imageSet[numPersonInImageSet] = person;
	}

	public int getImageSetSize() {
		return imageSetSize;
	}

}
