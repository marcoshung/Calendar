import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * 
 * @author marcoshung
 * @version 1.00
 *  imported base code from hw2
 */
public class MyCalendarTester {
	static Scanner console = new Scanner(System.in);
	static MyCalendar calendar = new MyCalendar();
	
	/**
	 * Main method
	 * calls methods to print calendar, load events, and begin main menu
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			calendar.loadEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}
		CalendarFrame frame = new CalendarFrame(calendar);
		calendar.addChangeListener(frame);
	}	

}