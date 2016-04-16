package application;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class CustomImage extends AppTools implements Serializable, Cloneable {

	private static final long serialVersionUID = -3494215914051909344L;
	
	private String path;
	private double[][] imageData;
	
	@Override
	public CustomImage clone() {
		return new CustomImage(path, imageData);
	}
	
	public CustomImage(BufferedImage bi) {
		this.imageData = bufferedImageTo2DArray(bi);
	}
	
	public CustomImage(String filepath) {
		this.path = filepath;
		this.imageData = new double[0][0];
	}
	
	public CustomImage(String filepath, double[][] imageData) {
		this.path = filepath;
		this.imageData = imageData;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public double[][] getImageData() {
		return imageData;
	}

	public void setImageData(double[][] imageData) {
		this.imageData = imageData;
	}

}
