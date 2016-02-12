package application;

public class PrincipalComponent implements Comparable<PrincipalComponent> {

	public double eigenValue;
	public double[] eigenVector;
	
	public PrincipalComponent(double eigenValue, double[] eigenVector){
		this.eigenValue = eigenValue;
		this.eigenVector = eigenVector;
	}
	
	@Override
	public int compareTo(PrincipalComponent subject) {
		if(eigenValue > subject.eigenValue)
		{
			return -1;
		}
		else if(eigenValue < subject.eigenValue)
		{
			return 1;
		}
		return 0;
	}
}