import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class CalendarPanel extends JPanel{
	CalendarGUI calendar;
	
	public CalendarPanel(CalendarGUI c) {
		this.calendar = c;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		calendar.draw(g2);
	}
}
