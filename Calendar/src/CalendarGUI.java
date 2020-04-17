import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComponent;

public class CalendarGUI extends JComponent{
	private MyCalendar calendar;
	private Font titleFont = new Font("SansSerif", Font.BOLD, 24);
	private Font weekFont = new Font("SansSerif", Font.BOLD, 15);
	private Font dayFont = new Font("SansSerif", Font.PLAIN, 12);
	private HashMap<Rectangle2D, Integer> dayBoxes;
	
	private int CALENDAR_FRAME_WIDTH = 1000;
	private int CALENDAR_FRAME_LENGTH = 800;
	private int TITLE_HEIGHT = 100;
	private int NUM_OF_WEEKS_DISPLAYED = 6;
	private int DAYS_IN_WEEK = 7;
	private double WEEK_DIVIDER_SPACE = (CALENDAR_FRAME_LENGTH - TITLE_HEIGHT)/ (double)NUM_OF_WEEKS_DISPLAYED;
	private double DAY_DIVIDER_SPACE = (double)CALENDAR_FRAME_WIDTH/ DAYS_IN_WEEK;
	
	
	public CalendarGUI(MyCalendar c) {
		this.calendar = c;
		dayBoxes = new HashMap<Rectangle2D, Integer>();
	}
	
	public void drawMonthView(Graphics2D g2) {
		Rectangle2D.Double body = new Rectangle2D.Double(0, 0, CALENDAR_FRAME_WIDTH, CALENDAR_FRAME_LENGTH);
		g2.draw(body);
		
		g2.setFont(titleFont);
		g2.drawString(calendar.getCurrentMonth() + " " + calendar.getCurrentYear(), 25, TITLE_HEIGHT/2);
		drawBody(g2);
		
		
	}
	
	public void drawBody(Graphics2D g2) {
		
		String[] daysOfWeek = calendar.getDaysOfWeek();
		int firstWeekDayOfMonth = calendar.getFirstDayOfWeek();
		int dayCount = 1;
		int daysInMonth = calendar.getCurrentMonth().length(calendar.isLeapYear(calendar.getCurrentYear()));
		//draws weeks
		for(int i = 0; i < NUM_OF_WEEKS_DISPLAYED;i++) {
			//draws days
			for(int j = 0; j < DAYS_IN_WEEK; j++) {
			//writes in the days of the week
				g2.setFont(weekFont);
				g2.drawString(daysOfWeek[j], (int) (DAY_DIVIDER_SPACE * j + 5), TITLE_HEIGHT - 5);
						
				//draws the individual boxes for days
				double xPos = DAY_DIVIDER_SPACE * j;
				double yPos = TITLE_HEIGHT + WEEK_DIVIDER_SPACE * i;
				Rectangle2D.Double dayBox = new Rectangle2D.Double(xPos, yPos, DAY_DIVIDER_SPACE, WEEK_DIVIDER_SPACE);
				g2.draw(dayBox);
				
				Rectangle2D.Double dayLabelBox = new Rectangle2D.Double(xPos, yPos, DAY_DIVIDER_SPACE/5, WEEK_DIVIDER_SPACE/5);
				g2.draw(dayLabelBox);
				int dayTextSpace = dayFont.getSize();
				if(i == 0) {
					if(j >= firstWeekDayOfMonth) {
						g2.setFont(dayFont);
						g2.drawString(dayCount + " ", (int) xPos + dayTextSpace , (int) (yPos) + dayTextSpace);
						dayBoxes.put(dayBox, dayCount);
						dayCount++;
					}
				}else {
					if(dayCount <= daysInMonth) {
						g2.setFont(dayFont);
						g2.drawString(dayCount + " ", (int) xPos + dayTextSpace, (int) (yPos) + dayTextSpace);
						dayBoxes.put(dayBox, dayCount);
						dayCount++;
					}
				}
			}
		}
	}
	
	public void drawDay(Graphics2D g2) {
		int spacing = TITLE_HEIGHT/2;
		g2.setFont(titleFont);
		g2.drawString(calendar.getCurrentMonth() + " " + calendar.getCurrentDay(), spacing, spacing);
		LocalDate date = LocalDate.of(calendar.getCurrentYear(), calendar.getCurrentMonth(), calendar.getCurrentDay());
		ArrayList<String> events = calendar.getDayEvents(date);
		
		g2.setFont(weekFont);
		int count = 2;
		for(String event : events) {
			g2.drawString(event, spacing, spacing * count);
			count++;
		}
	}
	
	public HashMap<Rectangle2D,Integer> getDayBoxes(){
		return this.dayBoxes;
	}
	
	
	public int getTitleHeight() {
		return this.TITLE_HEIGHT;
	}
	
	public MyCalendar getCalendar() {
		return this.calendar;
	}
}
