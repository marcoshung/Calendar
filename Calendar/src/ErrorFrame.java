import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ErrorFrame extends JFrame{
	private String messsage;
	private int width = 400;
	private int height = 400;
	public ErrorFrame(String message) {
		super("Error");
		this.messsage = message;
		getContentPane().add(new JLabel(message));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new GridLayout());
		setSize(width, height);
		setVisible(true);
	}
}
