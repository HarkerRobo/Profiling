
/**
 * @author joel
 *
 */
public class PositionTracker {
	
	private final double width; // distance between the left and right wheels
	private double x, y, theta; // state variables
	
	/**
	 * @param width
	 * @param x
	 * @param y
	 * @param theta
	 */
	public PositionTracker(double width, double x, double y, double theta) {
		super();
		this.width = width;
		this.x = x;
		this.y = y;
		this.theta = theta;
	}
	
	public void update(double left, double right) {
		double dTheta = (left - right) / width;
		if(dTheta == 0) {
			x += left * Math.cos(theta);
			y += left * Math.sin(theta);
		} else {
			double radius = (left + right) / (2.0 * dTheta);
			double thetaPrime = theta + dTheta;
			double xPrime = x + radius * (Math.sin(thetaPrime) - Math.sin(theta));
			double yPrime = y - radius * (Math.cos(thetaPrime) - Math.cos(theta));
			x = xPrime;
			y = yPrime;
			theta = thetaPrime;
		}
	}
	
	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}
	
	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * @return the theta
	 */
	public double getTheta() {
		return theta;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PositionTracker [width=" + width + ", x=" + x + ", y=" + y + ", theta=" + theta + "]";
	}
}
