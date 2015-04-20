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
