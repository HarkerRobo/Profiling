import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * @author joel
 *
 */
public class PositionTrackerTest extends JPanel {
	
	private static double l, r;
	private PositionTracker tracker;
	private static JSlider rightSlide, leftSlide;
	private static List<Waypoint> waypoints;
	public static double showAngle = 0, showDistance = 0;
	public static Waypoint showWaypoint = new Waypoint(0, 0), showA = new Waypoint(0, 0), showB = new Waypoint(0, 0);
	private static boolean started = false;
	private static PurePursuit pp;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		PositionTrackerTest test = new PositionTrackerTest(new PositionTracker(30, 500, 500, 0));
		waypoints = new ArrayList<Waypoint>();
		test.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				waypoints.add(new Waypoint(e.getX(), e.getY()));
				System.out.println("Waypoint added");
				frame.repaint();
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		// leftSlide = new JSlider(JSlider.VERTICAL, -90, 90, 1);
		// rightSlide = new JSlider(JSlider.VERTICAL, 30, 200, 30);
		JButton go = new JButton("Start");
		frame.add(test);
		// frame.add(leftSlide, BorderLayout.WEST);
		// frame.add(rightSlide, BorderLayout.EAST);
		frame.add(go, BorderLayout.SOUTH);
		frame.setSize(1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		go.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!started) {
					started = true;
					new Thread(() -> {
						pp = new PurePursuit(new TankDrive() {
							
							@Override
							public void drive(double left, double right) {
								System.out.println("Speed: " + left + ", " + right);
								test.tracker.update(left, right);
							}
							
							@Override
							public double getMaxSpeed() {
								return 10;
							}
						}, test.tracker, test.tracker.getWidth() * 2, waypoints);
						System.out.println("Started");
						//					while(true) {
						//						try {
						//							Thread.sleep(150);
						//							pp.drive();
						//							frame.repaint();
						//						} catch(InterruptedException e1) {
						//							e1.printStackTrace();
						//						}
						//					}
						
					}).start();
				} else {
					pp.drive();
					frame.repaint();
				}
			}
			
		});
		// frame.addKeyListener(new KeyListener() {
		//
		// @Override
		// public void keyTyped(KeyEvent e) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void keyPressed(KeyEvent e) {
		// if(e.getKeyCode() == KeyEvent.VK_Q)
		// l = 2;
		// else if(e.getKeyCode() == KeyEvent.VK_A)
		// l = 1;
		// else if(e.getKeyCode() == KeyEvent.VK_P)
		// r = 2;
		// else if(e.getKeyCode() == KeyEvent.VK_L)
		// r = 1;
		// }
		//
		// @Override
		// public void keyReleased(KeyEvent e) {
		// if(e.getKeyCode() == KeyEvent.VK_Q)
		// l = 0;
		// else if(e.getKeyCode() == KeyEvent.VK_A)
		// l = 0;
		// else if(e.getKeyCode() == KeyEvent.VK_P)
		// r = 0;
		// else if(e.getKeyCode() == KeyEvent.VK_L)
		// r = 0;
		// }
		//
		// });
		// while(true) {
		// try {
		// Thread.sleep(25);
		//// System.out.println("Update: " + l + ", " + r);
		//// test.tracker.update(leftSlide.getValue()/100.0,
		// rightSlide.getValue()/100.0);
		//// System.out.println(test.tracker);
		// frame.repaint();
		// } catch(InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
	}
	
	public PositionTrackerTest(PositionTracker tracker) {
		this.tracker = tracker;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		double x = tracker.getX(), y = tracker.getY(), t = tracker.getTheta(), t2 = t + Math.PI / 2;
		g.drawPolygon(
				new int[] { (int) Math.round(x + 15 * Math.cos(t) + 10 * Math.cos(t2)),
						(int) Math.round(x + 15 * Math.cos(t) - 10 * Math.cos(t2)),
						(int) Math.round(x - 15 * Math.cos(t) - 10 * Math.cos(t2)),
						(int) Math.round(x - 15 * Math.cos(t) + 10 * Math.cos(t2)) },
				new int[] { (int) Math.round(y + 15 * Math.sin(t) + 10 * Math.sin(t2)),
						(int) Math.round(y + 15 * Math.sin(t) - 10 * Math.sin(t2)),
						(int) Math.round(y - 15 * Math.sin(t) - 10 * Math.sin(t2)),
						(int) Math.round(y - 15 * Math.sin(t) + 10 * Math.sin(t2)) },
				4);
		g.setColor(Color.RED);
		for(Waypoint wp : waypoints) {
			g.drawOval((int) wp.getX() - 3, (int) wp.getY() - 3, 6, 6);
		}
		g.drawOval((int) (tracker.getX() - tracker.getWidth() * 2), (int) (tracker.getY() - tracker.getWidth() * 2), (int) (tracker.getWidth() * 4), (int) (tracker.getWidth() * 4));
		g.setColor(Color.PINK);
		g.drawLine((int) tracker.getX(), (int) tracker.getY(), (int) (tracker.getX() + showDistance * Math.cos(tracker.getTheta() - showAngle)), (int) (tracker.getY() + showDistance * Math.sin(tracker.getTheta() - showAngle)));
		g.drawOval((int) showWaypoint.getX() - 3, (int) showWaypoint.getY() - 3, 6, 6);
		g.setColor(Color.GREEN);
		g.drawOval((int) showA.getX() - 2, (int) showA.getY() - 2, 4, 4);
		g.drawOval((int) showB.getX() - 2, (int) showB.getY() - 2, 4, 4);
		// g.setColor(Color.RED);
		// g.drawLine((int) Math.round(tracker.getX()), (int)
		// Math.round(tracker.getY()),
		// (int) Math.round(tracker.getX() + 10 * Math.cos(tracker.getTheta())),
		// (int) Math.round(tracker.getY() + 10 *
		// Math.sin(tracker.getTheta())));
		// g.setColor(Color.GREEN);
		// g.drawLine((int) Math.round(tracker.getX()), (int)
		// Math.round(tracker.getY()),
		// (int) Math.round(tracker.getX() + rightSlide.getValue() *
		// Math.cos(tracker.getTheta() + leftSlide.getValue() * Math.PI / 180)),
		// (int) Math.round(tracker.getY() + rightSlide.getValue() *
		// Math.sin(tracker.getTheta() + leftSlide.getValue() * Math.PI /
		// 180)));
		// g.setColor(Color.PINK);
		// double theta = 2 * leftSlide.getValue() * Math.PI / 180;
		// try {
		// double radius = rightSlide.getValue() / (2 * Math.sin(theta/2));
		// g.drawOval((int) Math.round(tracker.getX() - radius),
		// (int) Math.round(tracker.getY()), (int) (radius * 2), (int) (radius *
		// 2));
		// } catch (ArithmeticException e) {
		//
		// }
	}
}
