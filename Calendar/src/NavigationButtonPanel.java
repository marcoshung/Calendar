import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JPanel;

public class NavigationButtonPanel extends JPanel{
	JButton prevButton;
	JButton nextButton;
	
	public NavigationButtonPanel(JButton prevButton, JButton nextButton) {
		this.prevButton = prevButton;
		this.nextButton = nextButton;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		add(prevButton, BorderLayout.EAST);
		add(nextButton, BorderLayout.WEST);
	}
}
