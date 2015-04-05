package gui;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ShowPanel extends JPanel {


	private static final long serialVersionUID = 1L;
	
	  private JButton button1 =null;
	  private JButton button2 =null;
	  private JLabel label = null;
	 
	  private JTextField field1 = null;
	  private JTextField field2 = null;
	  private JTextField field3 = null;
	  private JTextField field4 = null;
	 
	 public ShowPanel()
	 {
		 		 
		 button1=new JButton("Ok");
		 button2=new JButton("Cancel");
		 field1=new JTextField();
		 field2=new JTextField();
		 field3=new JTextField();
		 field4=new JTextField();
	     label=new JLabel();
	    
	     
	     this.setLayout(null);
	     
	     button1.setBounds(470, 180, 50,20);
	     button2.setBounds(470, 210, 50,20);
	     field1.setBounds(470, 10, 80,20);
	     field2.setBounds(470, 40, 80,20);
	     field3.setBounds(470, 70, 80,20);
	     field4.setBounds(470, 100, 80,20);
	     label.setBounds(10, 10, 300, 200);
	          
	     add(button1);
	     add(button2);
	     add(field1);
	     add(field2);
	     add(field3);
	     add(field4);
	     add(label);
	     
	     
	     button2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				field1.setText(null);
				field2.setText(null);
				field3.setText(null);
				field4.setText(null);
				label.setIcon(null);
			}
	     });
	     
	   	     
	 }
	 
	 public JTextField getField1()
	 {
		 return field1;
	 }
	 public JTextField getField2()
	 {
		 return field2;
	 }
	 public JTextField getField3()
	 {
		 return field3;
	 }
	 public JTextField getField4()
	 {
		 return field4;
	 }
	 
	 public JLabel getLabel()
	 {
		 return label;
	 }
	 
	 
	 public static BufferedImage getScreenImage(int x, int y, int w, int h) throws AWTException, InterruptedException {
	     Robot robot = new Robot();
	     BufferedImage screen = robot.createScreenCapture(new Rectangle(x, y, w, h));
		 return screen;
	 }
	 
}
