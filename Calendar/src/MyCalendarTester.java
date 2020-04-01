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
		calendar.printCalendar();
		try {
			calendar.loadEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		startMainMenu();
	}
	
	/**
	 * Method for starting home page and handling user interaction
	 */
	public static void startMainMenu() {
		
		boolean done = false;
		while(!done) {
			System.out.println("Select one of the following main menu options:\n" + 
					"[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
			String input = console.nextLine().toLowerCase();
			if(input.equals("v")) {
				calendar.view();
			}else if(input.equals("c")) {
				calendar.create();
			}else if(input.equals("g")){
				calendar.goTo();
			}else if(input.equals("e")) {
				calendar.seeEventList();
			}else if(input.equals("d")) {
				calendar.delete();
			}else if(input.equals("q")) {
				done = true;
				System.out.println("Good Bye");
			}else {
				System.out.println("Not valid input");
			}
		}
	}
	

}