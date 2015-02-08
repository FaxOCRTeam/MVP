package gui;

import gui.interfaces.ModelModificationInterface;
import gui.models.FormField;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class ModelPanel extends JPanel implements ModelModificationInterface {

	private static final long serialVersionUID = -1220255140745504900L;
	List<FormField> formData;
	SpringLayout springLayout;
	JPanel cPanel = null;

	public ModelPanel() {
		formData = new ArrayList<FormField>();
		springLayout = new SpringLayout();
		setLayout(springLayout);
		// setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void addField(FormField ff) {
		JPanel ffPanel = new JPanel();
		Dimension cd = new Dimension(200, 44);
		ffPanel.setSize(cd);
		ffPanel.setPreferredSize(cd);

		SpringLayout csl = new SpringLayout();
		ffPanel.setLayout(csl);

		// ffPanel.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		ffPanel.setBorder(BorderFactory.createTitledBorder(//
				BorderFactory.createEtchedBorder(), ff.getName()));

		JLabel[] infoLabels = new JLabel[ff.getRectConfig().length];
		for (int i = 0; i < ff.getRectConfig().length; i++) {
			infoLabels[i] = new JLabel("" + ff.getRectConfig()[i]);
			ffPanel.add(infoLabels[i]);
			csl.putConstraint(SpringLayout.NORTH, infoLabels[i], -3, SpringLayout.NORTH, ffPanel);
			if (i == 0)
				csl.putConstraint(SpringLayout.WEST, infoLabels[0], 8, SpringLayout.WEST, ffPanel);
			else
				csl.putConstraint(SpringLayout.WEST, infoLabels[i - 1], 5, SpringLayout.EAST, infoLabels[i]);
		}
		add(ffPanel);
		springLayout.putConstraint(SpringLayout.WEST, ffPanel, 5, SpringLayout.WEST, this);
		if (cPanel == null)
			springLayout.putConstraint(SpringLayout.NORTH, ffPanel, 5, SpringLayout.NORTH, this);
		else
			springLayout.putConstraint(SpringLayout.NORTH, ffPanel, 5, SpringLayout.SOUTH, cPanel);
		cPanel = ffPanel;
		formData.add(ff);
	}

	@Override
	public void removeField() {
		// TODO Auto-generated method stub

	}

}
