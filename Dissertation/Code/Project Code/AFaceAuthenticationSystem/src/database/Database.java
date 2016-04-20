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

import javax.imageio.ImageIO;

public class Database {
	
	private ArrayList<String> list = new ArrayList<String>();
	private String filepath = "images/TestFDB/";
	private int imageSetSize;
	public int imageWidth;
	public int imageHeight;
	public long imageSize;
	private Person[] imageSet;
	private static File dir;
	private String[] extensions;
	
	// constructor
	public void setUpDatabase(){
		loadImageLibrary();
		//getFilepathList();
		//loadTestLibrary(8);
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
			int filecount = 0;
			File[] files = dir.listFiles(imageFilter);
			imageSet = new Person[files.length];
			for (File f : files) {
				BufferedImage image = null;
				try {
					// read next file in folder
					image = ImageIO.read(f);
					// create person
					Person person = new Person(image, f.getName(), filecount, image.getWidth(), image.getHeight(), f.length());
                    // add person to set
					imageSet[filecount] = person;
					
                    // debug
					//System.out.println("Image Number: " + filecount);
                    //System.out.println("image: " + f.getName());
                    //System.out.println(" width : " + image.getWidth());
                    //System.out.println(" height: " + image.getHeight());
                    //System.out.println(" size  : " + f.length());
                    
                    filecount++;
                } catch (final IOException e) {
                    e.printStackTrace();
                }
			}
		}
		this.imageSetSize = imageSet.length;
	}
	
	public void loadTestLibrary(int imageSetSize) {
		
		// set image set array size
		imageSet = new Person[imageSetSize];
		for(int i=0; i<imageSetSize ; i++) {			
			// populate each persons details in the image set array
			try {
				BufferedImage bi = ImageIO.read(new FileInputStream(filepath + list.get(i)));
				imageSet[i] = new Person(bi, "new person", i, bi.getWidth(), bi.getHeight(), 1);
				this.imageHeight = bi.getHeight();
				this.imageWidth = bi.getWidth();
				this.imageSetSize = i;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
			e.printStackTrace();
		} catch (IOException e) {
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
