<<<<<<< HEAD
package gui.frames.modelPreview;

import gui.models.FormField;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

public class ModelPreviewPanel extends JPanel {
	private static final long serialVersionUID = -118888159147806811L;

	public ModelPreviewPanel(BufferedImage image, List<FormField> model) {
		super();
		this.image = image;
		this.model = model;
		Dimension d = new Dimension(image.getWidth(), image.getHeight());
		setSize(d);
		setPreferredSize(d);
	}

	BufferedImage image;
	List<FormField> model;

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, this);
		g.setColor(Color.red);
		for (FormField ff : model) {
			g.drawRect(ff.getRectConfig()[0], ff.getRectConfig()[1], ff.getRectConfig()[2],
					ff.getRectConfig()[3]);
		}
	}
}
=======
package gui.frames.modelPreview;

import gui.models.FormField;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

public class ModelPreviewPanel extends JPanel {
	private static final long serialVersionUID = -118888159147806811L;
	public double scale; 
	BufferedImage image;
	List<FormField> model;
	
	public ModelPreviewPanel(BufferedImage image, List<FormField> model) {
		super();
		this.image = image;
		this.model = model;
		this.scale = 600.0/image.getWidth();
		int width_scaled = (int) Math.ceil(image.getWidth()*scale);
		int height_scaled = (int) Math.ceil(image.getHeight()*scale);
		Dimension d = new Dimension(width_scaled, height_scaled);
		setSize(d);
		setPreferredSize(d);
	}

	
	@Override
	protected void paintComponent(Graphics g) {
		int width_scaled = (int) Math.ceil(image.getWidth()*scale);
		int height_scaled = (int) Math.ceil(image.getHeight()*scale);
		Image image_scaled = this.image.getScaledInstance(width_scaled, height_scaled, Image.SCALE_FAST);
		repaint();
		g.drawImage(image_scaled, 0, 0, width_scaled, height_scaled, this);
		
		g.setColor(Color.red);
		for (FormField ff : model) {
			int a1 = (int) Math.ceil(ff.getRectConfig()[0]*scale);
			int a2 = (int) Math.ceil(ff.getRectConfig()[1]*scale);
			int a3 = (int) Math.ceil(ff.getRectConfig()[2]*scale);
			int a4 = (int) Math.ceil(ff.getRectConfig()[3]*scale);
			g.drawRect(a1, a2, a3, a4);
			//g.drawRect(ff.getRectConfig()[0]*this.scale, ff.getRectConfig()[1]*this.scale, ff.getRectConfig()[2]*this.scale,
			//		ff.getRectConfig()[3]*this.scale);
		}
	}
}
>>>>>>> refs/remotes/origin/master
