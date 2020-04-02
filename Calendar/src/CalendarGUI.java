import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class CalendarGUI extends JComponent{
	private MyCalendar calendar;
	private Font titleFont = new Font("SansSerif", Font.BOLD, 24);
	private Font weekFont = new Font("SansSerif", Font.BOLD, 15);

	private int CALENDAR_FRAME_WIDTH = 1000;
	private int CALENDAR_FRAME_LENGTH = 800;
	private int TITLE_LENGTH = 100;
	private int NUM_OF_WEEKS_DISPLAYED = 6;
	private int DAYS_IN_WEEK = 7;
	private double WEEK_DIVIDER_SPACE = (CALENDAR_FRAME_LENGTH - TITLE_LENGTH)/ (double)NUM_OF_WEEKS_DISPLAYED;
	private double DAY_DIVIDER_SPACE = (double)CALENDAR_FRAME_WIDTH/ DAYS_IN_WEEK;
	
	public CalendarGUI(MyCalendar c) {
		this.calendar = c;
	}
	
	public void draw(Graphics2D g2) {
		Rectangle2D.Double body = new Rectangle2D.Double(0, 0, CALENDAR_FRAME_WIDTH, CALENDAR_FRAME_LENGTH);
		g2.draw(body);
		
		Rectangle2D.Double title = new Rectangle2D.Double(0, 0, CALENDAR_FRAME_WIDTH, TITLE_LENGTH);
		g2.draw(title);
		
		g2.setFont(titleFont);
		g2.drawString(calendar.getCurrentMonth() + " " + calendar.getCurrentYear(), 25, TITLE_LENGTH/2);
		
		String[] daysOfWeek = calendar.getDaysOfWeek();
		//draws weeks
		for(int i = 0; i < NUM_OF_WEEKS_DISPLAYED;i++) {
			//draws boxes
			Rectangle2D.Double weekBox = new Rectangle2D.Double(0, TITLE_LENGTH + WEEK_DIVIDER_SPACE * i, CALENDAR_FRAME_WIDTH, WEEK_DIVIDER_SPACE);
			g2.draw(weekBox);
			
			//draws days
			for(int j = 0; j < DAYS_IN_WEEK; j++) {
				//writes in the days of the week
				g2.setFont(weekFont);
				g2.drawString(daysOfWeek[j], (int) (DAY_DIVIDER_SPACE * j + 5), TITLE_LENGTH - 5);
				
				//draws the individual boxes for days
				Rectangle2D.Double dayBox = new Rectangle2D.Double(DAY_DIVIDER_SPACE * j, 
						TITLE_LENGTH + WEEK_DIVIDER_SPACE * i, DAY_DIVIDER_SPACE, WEEK_DIVIDER_SPACE);
				g2.draw(dayBox);
				
				Rectangle2D.Double dayLabelBox = new Rectangle2D.Double(DAY_DIVIDER_SPACE * j, TITLE_LENGTH + WEEK_DIVIDER_SPACE * i, DAY_DIVIDER_SPACE/5, WEEK_DIVIDER_SPACE/5);
				g2.draw(dayLabelBox);
				
			}
		}
	
	}
	
}
