import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
//this creates a frame displaying the corresponding error message
public class ErrorFrame extends JFrame{
	private String message;
	private int width = 400;
	private int height = 400;
	
	/**
	 * @param message passed to indicate the error
	 */
	public ErrorFrame(String message) {
		super("Error");
		this.message = message;
		getContentPane().add(new JLabel(this.message));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new GridLayout());
		setSize(width, height);
		setVisible(true);
	}
}
