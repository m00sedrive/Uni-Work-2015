package database;

import java.awt.image.BufferedImage;

public class Person {

	private String name = null;
	private BufferedImage image;
	private int personID = 0;
	private int imageNum = 0;
	
	public Person() {
	}
	
	public int getPersonID() {
		return personID;
	}

	public void setPersonID(int personID) {
		this.personID = personID;
	}

	public int getImageNum() {
		return imageNum;
	}

	public void setImageNum(int imageNum) {
		this.imageNum = imageNum;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	
	
}
