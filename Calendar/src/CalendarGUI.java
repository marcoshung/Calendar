import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JComponent;
//This is the Component that will hold the calendar portion of the GUI displayed

public class CalendarGUI extends JComponent{
	//model
	private MyCalendar calendar;
	//fonts that will be used to write to the GUI
	private Font titleFont = new Font("SansSerif", Font.BOLD, 24);
	private Font weekFont = new Font("SansSerif", Font.BOLD, 15);
	private Font dayFont = new Font("SansSerif", Font.PLAIN, 12);
	private Font boldDayFont =new Font("SansSerif", Font.BOLD, 13);
	//This hold all the rectangles that are actual days of the month. So empty boxes are not included.
	private HashMap<Rectangle2D, Integer> dayBoxes;
	
	private int CALENDAR_FRAME_WIDTH = 1000;
	private int CALENDAR_FRAME_LENGTH = 800;
	private int TITLE_HEIGHT = 100;
	private int NUM_OF_WEEKS_DISPLAYED = 6;
	private int DAYS_IN_WEEK = 7;
	private double WEEK_DIVIDER_SPACE = (CALENDAR_FRAME_LENGTH - TITLE_HEIGHT)/ (double)NUM_OF_WEEKS_DISPLAYED;
	private double DAY_DIVIDER_SPACE = (double)CALENDAR_FRAME_WIDTH/ DAYS_IN_WEEK;
	
	/**
	 * constructor
	 * @param c which is the calendar model.
	 */
	public CalendarGUI(MyCalendar c) {
		this.calendar = c;
		dayBoxes = new HashMap<Rectangle2D, Integer>();
	}
	
	/**
	 * draws month view
	 * @param g2
	 */
	public void drawMonthView(Graphics2D g2) {
		Rectangle2D.Double body = new Rectangle2D.Double(0, 0, CALENDAR_FRAME_WIDTH, CALENDAR_FRAME_LENGTH);
		g2.draw(body);
		//writes title 
		g2.setFont(titleFont);
		g2.drawString(calendar.getCurrentMonth() + " " + calendar.getCurrentYear(), 25, TITLE_HEIGHT/2);
		
		drawBody(g2);
	}
	
	/**
	 * draws all the day and week boxes
	 * @param g2
	 */
	public void drawBody(Graphics2D g2) {
		String[] daysOfWeek = calendar.getDaysOfWeek();
		int firstWeekDayOfMonth = calendar.getFirstDayOfWeek();
		int dayCount = 1;
		int daysInMonth = calendar.getCurrentMonth().length(calendar.isLeapYear(calendar.getCurrentYear()));
		HashSet<Integer> monthEvents = this.calendar.getEventDaysForMonth(calendar.getCurrentMonth().getValue(), calendar.getCurrentYear());

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
				//checks if its the first week and if it is, it will not label the box unless J is in the correct spot
				if(i != 0 || j >= firstWeekDayOfMonth) {
					//checks if it has reached the end of the month
					if(dayCount <= daysInMonth) {
						g2.setFont(dayFont);
						if((dayCount == calendar.getAcutalCurrentDate().getDayOfMonth()) && (calendar.getCurrentMonth().getValue() == calendar.getAcutalCurrentDate().getMonth().getValue())) {
							g2.setFont(boldDayFont);
						}
						String label = dayCount +"";
						//months with events are marked by the * symbol
						if(monthEvents.contains(dayCount)) {
							label += "*";
						}
						g2.drawString(label, (int) xPos + dayTextSpace -5, (int) (yPos) + dayTextSpace + 5);
						dayBoxes.put(dayBox, dayCount);
						dayCount++;
					}
				}
			}
		}
	}
	
	/**
	 * draws the day view of the gui
	 * @param g2
	 */
	public void drawDay(Graphics2D g2) {
		int spacing = TITLE_HEIGHT/2;
		g2.setFont(titleFont);
		g2.drawString(calendar.getCurrentMonth() + " " + calendar.getCurrentDay(), spacing, spacing);
		//gets current date of the day
		LocalDate date = LocalDate.of(calendar.getCurrentYear(), calendar.getCurrentMonth(), calendar.getCurrentDay());
		
		g2.setFont(weekFont);
		//count is 2 because the title already takes the 1st spot. So we use two to account for the spacing
		int count = 2;
		
		//writes all the events
		ArrayList<String> events = calendar.getDayEvents(date);
		for(String event : events) {
			g2.drawString(event, spacing, spacing * count);
			count++;
		}
	}
	
	/**
	 * @return all the boxes that have days
	 */
	public HashMap<Rectangle2D,Integer> getDayBoxes(){
		return this.dayBoxes;
	}
	
	
	/**
	 * @return title height
	 */
	public int getTitleHeight() {
		return this.TITLE_HEIGHT;
	}
	
	/**
	 * @return the calendar model
	 */
	public MyCalendar getCalendar() {
		return this.calendar;
	}
	
	/**
	 * resets the day box. Used to avoid issue of day boxes continuing onto the next month with same functionality
	 */
	public void resetDayBoxes() {
		this.dayBoxes = new HashMap<Rectangle2D, Integer>();
	}

}
