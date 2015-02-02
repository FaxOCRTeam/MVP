package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class SelectionPanel extends JPanel {

	private static final long serialVersionUID = -3544461806228328538L;

	SelectionPanel thisObj;
	Rectangle rect;
	Rectangle[] resizingRect;

	Point _mouseStart;
	Rectangle _rectStart;

	int resizingDirection = -1;

	enum Status {
		initialing, resizing, relocating, silence
	}

	Status status;

	public SelectionPanel() {
		thisObj = this;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				status = Status.silence;
				if (null == rect) { // empty
					rect = new Rectangle(e.getX(), e.getY(), 0, 0);
					status = Status.initialing;
				} else { // exists
					_mouseStart = (Point) e.getPoint().clone();
					_rectStart = (Rectangle) rect.clone();

					for (int i = 0; i < resizingRect.length; i++) {
						if (resizingRect[i].contains(e.getX(), e.getY())) {
							status = Status.resizing;
							resizingDirection = i;
							break;
						}
					}
					if (status != Status.resizing)
						if (rect.contains(e.getPoint())) {
							status = Status.relocating;
						} else
							status = Status.silence;
				}
				super.mouseDragged(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				status = Status.silence;
				resizingRect = new Rectangle[4];
				for (int i = 0; i < resizingRect.length; i++)
					resizingRect[i] = new Rectangle();
				updateResizingRect();
				repaint();
				super.mouseReleased(e);
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (status == Status.initialing) {
					rect.setSize((int) (e.getX() - rect.getX()), (int) (e.getY() - rect.getY()));
				} else {
					int mouseDeltaX = (int) e.getX() - (int) _mouseStart.getX();
					int mouseDeltaY = (int) e.getY() - (int) _mouseStart.getY();
					int rbX = (int) _rectStart.getX() + (int) _rectStart.getWidth();
					int rbY = (int) _rectStart.getY() + (int) _rectStart.getHeight();
					if (status == Status.resizing) {
						if (resizingDirection == 0) {
							rect.setBounds((int) (_rectStart.getX() + mouseDeltaX),//
									(int) (_rectStart.getY() + mouseDeltaY), //
									(int) (rbX - e.getX()), (int) (rbY - e.getY()));
						} else if (resizingDirection == 1) {
							rect.setBounds((int) (_rectStart.getX()),//
									(int) (_rectStart.getY() + mouseDeltaY), //
									(int) (e.getX() - rect.getX()), (int) (rbY - e.getY()));
						} else if (resizingDirection == 2) {
							rect.setBounds((int) (_rectStart.getX() + mouseDeltaX),//
									(int) (_rectStart.getY()), //
									(int) (rbX - e.getX()), (int) (e.getY() - rect.getY()));
						} else {
							rect.setSize((int) (e.getX() - rect.getX()), (int) (e.getY() - rect.getY()));
						}

					} else if (status == Status.relocating) {
						rect.setBounds((int) (_rectStart.getX() + (mouseDeltaX)),//
								(int) (_rectStart.getY() + (mouseDeltaY)), //
								(int) rect.getWidth(), (int) rect.getHeight());
					}
					updateResizingRect();
				}
				super.mouseDragged(e);
				repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				final int[] resizingCursorIndex = new int[] { Cursor.NW_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR };
				if (null != rect) {
					boolean cursorResizing = false;
					if (status == Status.silence) {
						for (int i = 0; i < resizingRect.length; i++) {
							if (resizingRect[i].contains(e.getX(), e.getY())) {
								thisObj.setCursor(Cursor.getPredefinedCursor(resizingCursorIndex[i]));
								cursorResizing = true;
								break;
							}
						}
						if (!cursorResizing)
							if (rect.contains(e.getX(), e.getY()))
								thisObj.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
							else
								thisObj.setCursor(Cursor.getDefaultCursor());
					}
				} else
					thisObj.setCursor(Cursor.getDefaultCursor());
				super.mouseMoved(e);
			}
		});
	}

	public void updateResizingRect() {
		final int resizeSize = 8;
		resizingRect[0].setBounds((int) (rect.getX() - resizeSize / 2),//
				(int) (rect.getY() - resizeSize / 2), resizeSize, resizeSize);
		resizingRect[1].setBounds((int) (rect.getX() + rect.getWidth() - resizeSize / 2),//
				(int) (rect.getY() - resizeSize / 2), resizeSize, resizeSize);
		resizingRect[2].setBounds((int) (rect.getX() - resizeSize / 2),//
				(int) (rect.getY() + rect.getHeight() - resizeSize / 2), resizeSize, resizeSize);
		resizingRect[3].setBounds((int) (rect.getX() + rect.getWidth() - resizeSize / 2),//
				(int) (rect.getY() + rect.getHeight() - resizeSize / 2), resizeSize, resizeSize);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.red);
		if (null != rect) {
			g2.draw(rect);
		}
		if (null != resizingRect) {
			g2.setColor(Color.gray);
			for (Rectangle rr : resizingRect) {
				g2.clearRect((int) rr.getX(), (int) rr.getY(), (int) rr.getWidth(), (int) rr.getHeight());
				g2.draw(rr);
			}
		}
	}
}
