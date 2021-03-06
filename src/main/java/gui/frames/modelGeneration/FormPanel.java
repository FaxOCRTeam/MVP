package gui.frames.modelGeneration;

import static org.bytedeco.javacpp.opencv_core.CV_32FC1;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateMat;
import static org.bytedeco.javacpp.opencv_core.cvNot;
import static org.bytedeco.javacpp.opencv_core.cvScalarAll;
import static org.bytedeco.javacpp.opencv_core.cvSetZero;
import static org.bytedeco.javacpp.opencv_imgproc.CV_INTER_LINEAR;
import static org.bytedeco.javacpp.opencv_imgproc.CV_WARP_FILL_OUTLIERS;
import static org.bytedeco.javacpp.opencv_imgproc.cv2DRotationMatrix;
import static org.bytedeco.javacpp.opencv_imgproc.cvWarpAffine;
import gui.interfaces.DisplayCoordinatesInterface;
import gui.interfaces.FormPanelInterface;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;

import com.recognition.software.jdeskew.ImageDeskew;

public class FormPanel extends JPanel implements FormPanelInterface {

	
	private static final long serialVersionUID = -3544461806228328538L;

	FormPanel thisObj;
	Rectangle rect;
	Rectangle originalRect;
	Rectangle[] resizingRect;

	Point _mouseStart;
	Rectangle _rectStart;

	BufferedImage image;
	BufferedImage originImage;

	double scale = 1.0;

	List<DisplayCoordinatesInterface> coNotifierList = new ArrayList<DisplayCoordinatesInterface>();

	int resizingDirection = -1;

	enum Status {
		initialing, resizing, relocating, silence
	}

	Status status;


	public FormPanel() {
		// setPreferredSize(new Dimension(1000,1000));
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
				updateResizingRect();
				repaint();
				super.mouseReleased(e);
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (status == Status.initialing) {
					rect.setSize((int) (e.getX() - rect.getX()),
							(int) (e.getY() - rect.getY()));
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
						} else if (resizingDirection == 3) {
							rect.setSize((int) (e.getX() - rect.getX()),
									(int) (e.getY() - rect.getY()));
						} else if (resizingDirection == 4) {
							rect.setBounds((int) (_rectStart.getX()),//
									(int) (_rectStart.getY() + mouseDeltaY), //
									(int) (rect.getWidth()), (int) (rbY - e.getY()));
						} else if (resizingDirection == 5) {
							rect.setSize((int) (e.getX() - rect.getX()),
									(int) (rect.getHeight()));
						} else if (resizingDirection == 6) {
							rect.setBounds((int) (_rectStart.getX() + mouseDeltaX),//
									(int) (_rectStart.getY()), //
									(int) (rbX - e.getX()), (int) (rect.getHeight()));
						} else if (resizingDirection == 7) {
							rect.setSize((int) (rect.getWidth()),
									(int) (e.getY() - rect.getY()));
						}
					} else if (status == Status.relocating) {
						rect.setBounds((int) (_rectStart.getX() + (mouseDeltaX)),//
								(int) (_rectStart.getY() + (mouseDeltaY)), //
								(int) rect.getWidth(), (int) rect.getHeight());
					}
				}
				updateResizingRect();
				notifyRect();
				super.mouseDragged(e);
				repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				final int[] resizingCursorIndex = new int[] { Cursor.NW_RESIZE_CURSOR,
						Cursor.NE_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR,
						Cursor.SE_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR,
						Cursor.E_RESIZE_CURSOR, Cursor.W_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR };
				if (null != rect) {
					boolean cursorResizing = false;
					if (status == Status.silence) {
						for (int i = 0; i < resizingRect.length; i++) {
							if (resizingRect[i].contains(e.getX(), e.getY())) {
								thisObj.setCursor(Cursor
										.getPredefinedCursor(resizingCursorIndex[i]));
								cursorResizing = true;
								break;
							}
						}
						if (!cursorResizing)
							if (rect.contains(e.getX(), e.getY()))
								thisObj.setCursor(Cursor
										.getPredefinedCursor(Cursor.HAND_CURSOR));
							else
								thisObj.setCursor(Cursor.getDefaultCursor());
					}
				} else
					thisObj.setCursor(Cursor.getDefaultCursor());
				super.mouseMoved(e);
			}
		});
	}
	
	private void updateOriginalSelection() {
		originalRect = new Rectangle((int) Math.ceil(rect.getX() / scale), //
				(int) Math.ceil(rect.getY() / scale), //
				(int) Math.ceil(rect.getWidth() / scale), //
				(int) Math.ceil(rect.getHeight() / scale));
	}
	
	private void updateScaledSelection() {
		if(null == originalRect)
			return;
		rect = new Rectangle((int) Math.ceil(originalRect.getX() * scale), //
				(int) Math.ceil(originalRect.getY() * scale), //
				(int) Math.ceil(originalRect.getWidth() * scale), //
				(int) Math.ceil(originalRect.getHeight() * scale));
	}
	
	private void updateResizingRect() {
		if(null == rect)
			return;
		updateOriginalSelection();
		if (null == resizingRect) {
			resizingRect = new Rectangle[8];
			for (int i = 0; i < resizingRect.length; i++)
				resizingRect[i] = new Rectangle();
		}
		final int resizeSize = 8;
		resizingRect[0].setBounds((int) (rect.getX() - resizeSize / 2),//
				(int) (rect.getY() - resizeSize / 2), resizeSize, resizeSize);
		// x, y
		resizingRect[1].setBounds((int) (rect.getX() + rect.getWidth() - resizeSize / 2),//
				(int) (rect.getY() - resizeSize / 2), resizeSize, resizeSize);
		// x+w, y
		resizingRect[2].setBounds(
				(int) (rect.getX() - resizeSize / 2),//
				(int) (rect.getY() + rect.getHeight() - resizeSize / 2), resizeSize,
				resizeSize);
		// x, y+h
		resizingRect[3].setBounds(
				(int) (rect.getX() + rect.getWidth() - resizeSize / 2),//
				(int) (rect.getY() + rect.getHeight() - resizeSize / 2), resizeSize,
				resizeSize);
		// x+w, y+h
		resizingRect[4].setBounds((int) (rect.getX() + rect.getWidth() / 2 - resizeSize / 2),//
				(int) (rect.getY() - resizeSize / 2), resizeSize, resizeSize);
		// x+w/2, y
		resizingRect[5].setBounds(
				(int) (rect.getX() + rect.getWidth() - resizeSize / 2),//
				(int) (rect.getY() + rect.getHeight() / 2 - resizeSize / 2), resizeSize,
				resizeSize);
		// x+w, y+h/2
		resizingRect[6].setBounds(
				(int) (rect.getX() - resizeSize / 2),//
				(int) (rect.getY() + rect.getHeight() / 2 - resizeSize / 2), resizeSize,
				resizeSize);
		// x, y+h/2
		resizingRect[7].setBounds(
				(int) (rect.getX() + rect.getWidth() / 2 - resizeSize / 2),//
				(int) (rect.getY() + rect.getHeight() - resizeSize / 2), resizeSize,
				resizeSize);
		// x+w/2, y+h
	}

	private void notifyRect() {
		int[] rectCoordinates = new int[] { (int) originalRect.getX(),
				(int) originalRect.getY(), //
				(int) originalRect.getWidth(), (int) originalRect.getHeight() };
		for (DisplayCoordinatesInterface dci : coNotifierList) {
			dci.setCoordinates(rectCoordinates);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if (null != image)
			g2.drawImage(image, 0, 0, this);
		g2.setColor(Color.red);
		if (null != rect) {
			g2.draw(rect);
			if (null != resizingRect) {
				g2.setColor(Color.gray);
				for (Rectangle rr : resizingRect) {
					g2.clearRect((int) rr.getX(), (int) rr.getY(), (int) rr.getWidth(),
							(int) rr.getHeight());
					g2.draw(rr);
				}
			}
		} else {
			g2.clearRect(getX(), getY(), getWidth(), getHeight());
			if (null != image)
				g2.drawImage(image, 0, 0, this);
		}
	}

	@Override
	public void loadImage(File f) {
		try {
			this.image = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/**
		 * not safe, change to new instance
		 */
		originImage = image;

		Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
		this.setSize(dimension);
		this.setPreferredSize(dimension);
		repaint();
	}

	@Override
	public void cancelSelection() {
		originalRect = null;
		rect = null;
		resizingRect = null;
		status = Status.silence;
		_rectStart = null;
		_mouseStart = null;
		repaint();
	}

	@Override
	public void addCoordinatesNotifier(DisplayCoordinatesInterface dci) {
		coNotifierList.add(dci);
	}

	@Override
	public void setSelection(int[] selection) {
		originalRect = new Rectangle(selection[0], selection[1], selection[2], selection[3]);
		updateScaledSelection();
		updateResizingRect();
		notifyRect();
		repaint();

	}

	@Override
	public void zoomOut() {
		scale *= 0.8;
		if (scale <= 0.2) {
			scale = 0.2;
		}
		resizeImage();

	}

	@Override
	public void zoomIn() {
		scale /= 0.8;
		if (scale >= 2) {
			scale = 2;
		}
		resizeImage();
	}

	@Override
	public void originZoom() {
		scale = 1.0;
		resizeImage();
	}

	// @Override
	Map<Double, BufferedImage> scalingCache = new HashMap<Double, BufferedImage>();
	private void resizeImage() {
		if(null == originImage) {
			return;
		}
		updateScaledSelection();
		
		BufferedImage scaledImage = scalingCache.get(scale);
		if(scaledImage == null) {
			int newImageWidth = (int) (originImage.getWidth() * scale);
			int newImageHeight = (int) (originImage.getHeight() * scale);
			if (newImageWidth > 0 && newImageHeight > 0) {
				Image newImage = originImage.getScaledInstance(newImageWidth, newImageHeight,
						Image.SCALE_SMOOTH);
				scaledImage = toBufferedImage(newImage);
				scalingCache.put(scale, scaledImage);
			} else {
				System.out.println("Cannot zoom any more!!");
			}
		}
		image = scaledImage;
		repaint();
		updateResizingRect();
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		// Return the buffered image
		return bimage;
	}
	
	@Override
	public BufferedImage getImage(){
		
		return image;
	}
	
	@Override
	public void deskew(IplImage Ipl){
		BufferedImage bImg = Ipl.getBufferedImage();
		IplImage ret = IplImage.create(Ipl.width(),Ipl.height(), IPL_DEPTH_8U, 1);
		cvSetZero(ret);
		cvNot(ret,ret);
		ImageDeskew deskew = new ImageDeskew(bImg);
		double angle = deskew.getSkewAngle();
		
		CvPoint2D32f my_center = new CvPoint2D32f();
		my_center.put((double)(Ipl.width() / 2), (double)(Ipl.height() / 2));
		int flags = CV_INTER_LINEAR + CV_WARP_FILL_OUTLIERS;
		CvScalar fillval = cvScalarAll(255);
		CvMat map_matrix = cvCreateMat(2, 3, CV_32FC1);
		cv2DRotationMatrix(my_center, angle, 1, map_matrix);
		cvWarpAffine(Ipl, ret, map_matrix, flags, fillval);
		image = ret.getBufferedImage();
		repaint();
		
	}
	
	
}
