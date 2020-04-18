import java.io.IOException;

/** 
 * @author marcoshung
 * @version 2.17
 *  imported base code from hw2 to serve as model for GUI
 */

public class SimpleCalendar {
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		MyCalendar calendar = new MyCalendar();

		try {
			calendar.loadEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}
		CalendarFrame frame = new CalendarFrame(calendar);
		calendar.addChangeListener(frame);
	}	

}