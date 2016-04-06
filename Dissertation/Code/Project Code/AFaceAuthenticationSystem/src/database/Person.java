package database;

import java.awt.image.BufferedImage;

public class Person {

	private BufferedImage image;
	private String personName = null;
	private int imageNum = 0;
	private int imageWidth;
	private int imageHeigth;
	private long imageSize;

	public Person(BufferedImage image, String personName, int imageNum, int imageWidth, int imageHeight,
			long imageSize) {

		this.image = image;
		this.personName = personName;
		this.imageWidth = imageWidth;
		this.imageHeigth = imageHeight;
		this.imageSize = imageSize;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getImageNum() {
		return imageNum;
	}

	public int getImageHeigth() {
		return imageHeigth;
	}

	public long getImageSize() {
		return imageSize;
	}
}
