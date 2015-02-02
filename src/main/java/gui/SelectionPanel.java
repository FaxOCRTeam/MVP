package gui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SelectionPanel extends JPanel {

	private static final long serialVersionUID = -3544461806228328538L;

	
	SelectionPanel thisObj;
	Rectangle rect;
	Point[] dragPoint;
	boolean dragging;

	public SelectionPanel(final ShowPanel panel2) {
		thisObj = this;
		dragPoint = new Point[2];
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dragging = true;
				if (null == rect) {
					rect = new Rectangle(e.getX(), e.getY(), 0, 0);
				} else if (rect.contains(e.getPoint())) {
					dragPoint[0] = (Point) e.getPoint().clone();
					dragPoint[1] = (Point) rect.getLocation().clone();
				} else {
					dragging = false;
				}
				super.mouseDragged(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				dragging = false;
				dragPoint[0] = null;
				super.mouseReleased(e);
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (null != rect && dragging) {
					if (null == dragPoint[0])
						rect.setSize((int) (e.getX() - rect.getX()), (int) (e.getY() - rect.getY()));
					else {
						rect.setBounds((int) (dragPoint[1].getX() + (e.getX() - dragPoint[0].getX())),//
								(int) (dragPoint[1].getY() + (e.getY() - dragPoint[0].getY())), //
								(int) rect.getWidth(), (int) rect.getHeight());

					}
				}
				panel2.getField1().setText(Integer.toString((int)rect.getX()));
				panel2.getField2().setText(Integer.toString((int)rect.getY()));
				panel2.getField3().setText(Integer.toString((int)rect.getX()+(int)rect.getWidth()));
				panel2.getField4().setText(Integer.toString((int)rect.getY()+(int)rect.getHeight()));
				
				super.mouseDragged(e);
				repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {

				if (null != rect && rect.contains(e.getX(), e.getY()))
					thisObj.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				else
					thisObj.setCursor(Cursor.getDefaultCursor());
				super.mouseMoved(e);
			}
		});
		
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.red);
		if (null != rect) {
			g2.draw(rect);
		}
	}
}




