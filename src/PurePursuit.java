import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author joel
 *
 */
public class PurePursuit {
	
	private static final boolean[][] check = new boolean[][] {
			{ true, true, true, true, true, true, true, true, true },
			{ true, false, false, false, false, true, false, true, true },
			{ true, false, false, false, true, true, true, true, true },
			{ true, false, false, false, true, false, true, true, false },
			{ true, false, true, true, false, true, false, true, true },
			{ true, true, true, false, true, false, true, true, false },
			{ true, false, true, true, false, true, false, false, false },
			{ true, true, true, true, true, true, false, false, false },
			{ true, true, true, false, true, false, false, false, false }
			};
	
	// tracker width = pursuit lookahead
	private PositionTracker tracker;
	private TankDrive drive;
	private double lookahead;
	
	private List<Waypoint> waypoints;
	
	public PurePursuit(TankDrive drive, PositionTracker tracker, double lookahead, List<Waypoint> waypoints) {
		this.drive = drive;
		this.tracker = tracker;
		this.lookahead = lookahead;
		this.waypoints = waypoints;
	}
	
	public PurePursuit(TankDrive drive, PositionTracker tracker, double lookahead, Waypoint... waypoints) {
		this(drive, tracker, lookahead, Arrays.asList(waypoints));
	}
	
	public void drive() {
		Waypoint target = find();
		PositionTrackerTest.showWaypoint = target;
		double angle = Math.atan2(target.getY() - tracker.getY(), target.getX() - tracker.getX());
		angle = (angle - tracker.getTheta());
		double distance = Math.sqrt(Math.pow(target.getX() - tracker.getX(), 2) + Math.pow(target.getY() - tracker.getY(), 2));
		travelTo(angle, distance);
	}
	
	public Waypoint find() {
		Waypoint prev = waypoints.get(waypoints.size() - 1);
		int pPrev = classify(prev);
		double r2 = Math.pow(lookahead, 2);
		for(int i = waypoints.size() - 2; i >= 0; i--) {
			Waypoint current = waypoints.get(i);
			int pCurrent = classify(current);
			if(true || check[pPrev][pCurrent]) {
				System.out.println("encountered checkable path: " + current + ", " + prev);
				double dx = prev.getX() - current.getX();
				double dy = prev.getY() - current.getY();
				double dr2 = Math.pow(dx, 2) + Math.pow(dy, 2);
				double D = (current.getX() - tracker.getX()) * (prev.getY() - tracker.getY()) - (current.getY() - tracker.getY()) * (prev.getX() - tracker.getX());
				double D2 = Math.pow(D, 2);
				double discriminant = r2 * dr2 - D2;
				System.out.println("Discriminant: " + discriminant);
				double sqrtdiscrim = Math.sqrt(discriminant);
				if(discriminant >= 0) {
					System.out.println("had a valid discriminant");
					Waypoint a = new Waypoint(tracker.getX() + (D * dy + Math.signum(dy) * dx * sqrtdiscrim)/dr2, tracker.getY() - (D * dx + dy * sqrtdiscrim)/dr2);
					Waypoint b = new Waypoint(tracker.getX() + (D * dy - Math.signum(dy) * dx * sqrtdiscrim)/dr2, tracker.getY() - (D * dx - dy * sqrtdiscrim)/dr2);
					PositionTrackerTest.showA = a;
					PositionTrackerTest.showB = b;
					System.out.println("a: " + a + ", " + b);
					if(a.distanceSquared(prev) < b.distanceSquared(prev)){
						if(between(a, current, prev)) {
//							for(int j = 0; j < i; j++) {
//								waypoints.remove(0);
//							}
							return a;
						} else if(between(b, current, prev)) {
//							for(int j = 0; j < i; j++) {
//								waypoints.remove(0);
//							}
							return b;
						}
					} else {
						if(between(b, current, prev)) {
//							for(int j = 0; j < i; j++) {
//								waypoints.remove(0);
//							}
							return b;
						} else if(between(a, current, prev)) {
//							for(int j = 0; j < i; j++) {
//								waypoints.remove(0);
//							}
							return a;
						}
					}
				}
			}
			prev = current;
			pPrev = pCurrent;
		}
		System.out.println("no intersection found");
		return waypoints.size() > 0 ? waypoints.get(1) : waypoints.get(0);
	}
	
	/*
	 * return a between b and c
	 */
	public boolean between(Waypoint a, Waypoint b, Waypoint c) {
		return a.distanceSquared(c) <= b.distanceSquared(c) && a.distance(b) <= c.distanceSquared(b);
	}
	
	/*
	 * 123
	 * 405
	 * 678
	 */
	public int classify(Waypoint p) {
		return (p.getX() < tracker.getX() - lookahead ? 0 : p.getX() < tracker.getX() + lookahead ? 1 : 2)
				+ (p.getY() < tracker.getX() - lookahead ? 0 : p.getY() < tracker.getY() + lookahead ? 3 : 6);
	}
	
	/*
	 * -pi/2 < angle < pi/2 (as a heading)
	 */
	public void travelTo(double angle, double distance) {
//		if(Math.abs(angle) > Math.PI/2) {
//			if(angle < 0) {
//				drive.drive(-drive.getMaxSpeed(), drive.getMaxSpeed());
//			} else {
//				drive.drive(drive.getMaxSpeed(), -drive.getMaxSpeed());
//			}
//		} else {
		PositionTrackerTest.showAngle = angle;
		PositionTrackerTest.showDistance = distance;
		double theta = 2.0 * angle;
		double radius = distance / (2.0 * Math.sin(angle));
		double left = (radius + tracker.getWidth() / 2.0) * theta;
		double right = (radius - tracker.getWidth() / 2.0) * theta;
		double max = Math.max(Math.abs(left), Math.abs(right));
		drive.drive(left * drive.getMaxSpeed() / max, right * drive.getMaxSpeed() / max);
//		}
	}
}
