package gui;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 全屏显示的窗口, 按 Alt +　F4 退出
 * @author pengranxiang
 */
public class ScreenWindow extends JFrame {
	private static final long serialVersionUID = -3758062802950480258L;
	
	private Image image;
	private JLabel imageLabel;
	
	private int x, y, xEnd, yEnd;	//用于记录鼠标点击开始和结束的坐标

	public ScreenWindow() throws AWTException, InterruptedException {
		//取得屏幕尺寸
		Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
		//取得全屏幕截图
		image = GraphicsUtils.getScreenImage(0, 0, screenDims.width, screenDims.height);
		//用于展示截图
		imageLabel = new JLabel(new ImageIcon(image));
		//当鼠标在imageLabel上时，展示为 十字形
		imageLabel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		
		createAction();
		
		this.getContentPane().add(imageLabel);
		
		this.setUndecorated(true);	//去掉窗口装饰
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);	//窗口最大化
	}
	
	/**
	 * 实现监听动作
	 */
	private void createAction() {
		imageLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				x = e.getX();
				y = e.getY();
			}

			public void mouseReleased(MouseEvent e) {
				xEnd = e.getX();
				yEnd = e.getY();
				
				//鼠标弹起时，取得鼠标起始两点组成的矩形区域的图像
				try {
					//因为 xEnd 可能比  x 小 （由右网左移动）起始坐标取其中较小值，xEnd - x 取其绝对值， 同样处理y
					image = GraphicsUtils.getScreenImage(Math.min(x, xEnd), Math.min(y, yEnd), Math.abs(xEnd - x), Math.abs(yEnd - y));
				} catch (AWTException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				//为了查看截图效果，将区域截图的部分代替全屏的截图展示
				imageLabel.setIcon(new ImageIcon(image));
			}
		});
	}
	
	public static void main(String[] args) throws AWTException, InterruptedException {
		new ScreenWindow();
	}
}

class GraphicsUtils {
	/**
     * 截图屏幕中制定区域的图片
     * @param x
     * @param y
     * @param w
     * @param h
     * @return 被截部分的BufferedImage对象
     * @throws AWTException
     * @throws InterruptedException
     */
    public static BufferedImage getScreenImage(int x, int y, int w, int h) throws AWTException, InterruptedException {
		Robot robot = new Robot();
		BufferedImage screen = robot.createScreenCapture(new Rectangle(x, y, w, h));
		return screen;
	}
}