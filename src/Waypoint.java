
/**
 * @author joel
 *
 */
public class Waypoint {
	private final double x, y;

	/**
	 * @param x
	 * @param y
	 */
	public Waypoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
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
	 * Find the distance squared between two waypoints
	 * @param other another waypoint
	 * @return the distance squared
	 */
	public double distanceSquared(Waypoint other) {
		return Math.pow(getX() - other.getX(), 2) + Math.pow(getY() - other.getY(), 2); 
	}
	
	/**
	 * Find the distance between two waypoints
	 * @param other another waypoint
	 * @return the distance
	 */
	public double distance(Waypoint other) {
		return Math.sqrt(distanceSquared(other));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Waypoint [x=" + x + ", y=" + y + "]";
	}
}
