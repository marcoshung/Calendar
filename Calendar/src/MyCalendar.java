import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * @author marcoshung
 * imported base code from hw2
 * This is the model for the GUI
 */
public class MyCalendar {
	private HashSet<Event> allEvents;
	private HashSet<Event> singleEvents;
	private HashMap<LocalDate, ArrayList<Event>> events;
	private ArrayList<ChangeListener> listeners;

	private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
	private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private Scanner console = new Scanner(System.in);
	private File eventFile = new File("events.txt");
	private File outputFile = new File("output.txt");
	//this is the actual current date
	private LocalDateTime actualCurrentDate = LocalDateTime.now();
	//this is the pointer that points to the current date the user is using
	private LocalDateTime current = LocalDateTime.now();

	/**
	 * Constructor - initializes all instance variables
	 */
	public MyCalendar() {
		events = new HashMap<LocalDate,ArrayList<Event>>();
		allEvents = new HashSet<Event>();
		singleEvents = new HashSet<Event>();
		listeners = new ArrayList<ChangeListener>();
	}
	/**
	 * Adds event to events list
	 * @param e - Event to be added
	 */
	public void addEvent(Event e) {
		if(events.containsKey(e.getDate())) {
			events.get(e.getDate()).add(e);
		}else {
			events.put(e.getDate(), new ArrayList<Event>());
			events.get(e.getDate()).add(e);
		}
		allEvents.add(e);
	}
	
	/**
	 * deletes a single occurring event from the calendar
	 */
	
	public void deleteOneTime() {
		System.out.println("Enter the date of the event you wish to cancel in mm/dd/yyyy format");
		String dateString = console.nextLine();
		while(!checkDate(dateString)) {
			System.out.println("Invalid date format. Try again");
			dateString = console.nextLine();
		}
		LocalDate date = LocalDate.parse(dateString,dateFormat);

		HashSet<Event> eventsOnDate = new HashSet<Event>();
		for(Event e : getSingleEvents()) {
			if(e.getDate().equals(date)) {
				eventsOnDate.add(e);
				System.out.println(e.getStartingTime() + " - " + e.getEndingTime() + " " + e.getName());
			}
		}
		if(eventsOnDate.size() == 0) {
			System.out.println("No events on this day");
			return;
		}
		System.out.println("Enter the name of the event you want to cancel");
		String target = console.nextLine().toLowerCase();
		boolean deleted = false;
		String eventName = "";
		for(Event e : eventsOnDate) {
			if(e.getName().toLowerCase().equals(target)) {
				getSingleEvents().remove(e);
				deleteEvent(e);
				deleted = true;
				eventName = e.getName();
			}
		}
		if(deleted) {
			System.out.println(eventName + " has been deleted");
		}else {
			System.out.println("No event with that name on this day");
		}
	}
	
	/**
	 * delete events from the main event list in calendar
	 * @param e - Event
	 * @param identifier - String identifier to find the event in the file
	 */
	
	public void deleteEvent(Event e) {
		getAllEvents().remove(e);
		getEvents().get(e.getDate()).remove(e);
		if(getEvents().get(e.getDate()).size() == 0) {
			getEvents().remove(e.getDate());
		}
		try {
			deleteEventFromFile(e.getName());
			deleteEventFromFile(e.getIdentifer());
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * deletes specified string from the events file
	 * @param target
	 * @throws IOException
	 */
	
	public void deleteEventFromFile(String target) throws IOException{
		File initial = eventFile;
		File newFile = new File("myTempFile.txt");

		BufferedReader reader = new BufferedReader(new FileReader(initial));
		BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
		String currentLine = null;
		while((currentLine = reader.readLine()) != null) {
		    String line = currentLine.trim().toLowerCase();
		    if(line.equals(target.toLowerCase())) {
		    	continue;
		    }
		    writer.write(currentLine + "\n");
		}
		writer.close(); 
		reader.close(); 
		newFile.renameTo(initial);
	}
	/**
	 * @return a HashMap with Events mapped to the date they are on
	 */
	public HashMap<LocalDate, ArrayList<Event>> getEvents() {
		return this.events;
	}
	
	/**
	 * @param date
	 * @return All events that are on the given date
	 */
	public HashSet<Event> getEventsOnDay(LocalDate date){
		if(events.get(date) == null) {
			return new HashSet<Event>();
		}
		return new HashSet<Event> (events.get(date));
	}
	/**
	 * 
	 * 
	 * @param currentMonth
	 * @param year
	 * @return all events for a given month
	 */

	public HashSet<Integer> getEventDaysForMonth(int currentMonth, int year) {
		HashMap<LocalDate, ArrayList<Event>> events = getEvents();
		HashSet<Integer> days = new HashSet<Integer>();
		for(LocalDate day :events.keySet()) {
			if(day.getMonthValue() == currentMonth && day.getYear() == year) {
				days.add(day.getDayOfMonth());
			}
		}
		return days;
	}
	
	/**
	 * @return all events the calendar contains
	 */
	public HashSet<Event> getAllEvents() {
		return this.allEvents;
	}
	
	/**
	 * @return all one time events
	 */
	public Set<Event> getSingleEvents(){
		return this.singleEvents;
	}
	
	/**
	 * adds Event to single events list
	 * @param e - Event
	 */
	public void addSingleEvent(Event e) {
		this.singleEvents.add(e);
	}
	
	/**
	 * @param eventList
	 * @return sorted Events in ascending order
	 */
	
	public ArrayList<Event> getSortedEvents(Set<Event> eventList){
		ArrayList<Event> sorted = new ArrayList<Event>();
		
		if(eventList.size() == 0) {
			return sorted;
		}
		
		for(Event e: eventList) {
			if(sorted.size() == 0) {
				sorted.add(e);
			}else {
				int count = 0;
				while(count != sorted.size() &&!(e.getStartingDateAndTime().compareTo(sorted.get(count).getStartingDateAndTime()) <= 0)) {
					count++;
				}
				sorted.add(count, e);
			}	
		}
		return sorted;
	}
	
	/**
	 * adds event information to event file
	 * @param e - Event
	 * @throws IOException
	 */
	public void writeEventToFile(Event e) throws IOException {
		FileWriter eventFileOut = new FileWriter(eventFile, true);
		FileWriter outputOut = new FileWriter(outputFile ,true);
		eventFileOut.write(e.getName() +"\n");
		eventFileOut.write(e.getIdentifer()+"\n");
		eventFileOut.close();
		outputOut.write(e.getName() +"\n");
		outputOut.write(e.getIdentifer()+"\n");
		outputOut.close();
	}
	
	/**
	 * @return int representing the first day of of the week for the current month.
	 */
	public int getFirstDayOfWeek() {
		LocalDateTime firstDayOfMonth = current.withDayOfMonth(1);
		return firstDayOfMonth.getDayOfWeek().getValue();
	}
	
	/**
	 * @param year
	 * @return true if the year is a leap year
	 */
	public boolean isLeapYear(int year) {
		if(year % 4 == 0) {
			if(year % 100 == 0) {
				if(year % 400 == 0) {
					return true;
				}
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * reads events from the event file and creates events to be stored in calendar
	 * @throws IOException
	 */
	
	public void loadEvents() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(eventFile));  
		String line = null;
		while((line = br.readLine())!= null) {
			String eventName = line;
			line = br.readLine();
			String[] eventInfo = line.split(" ");
			if(eventInfo.length == 3) { //one time event
				createEvent(eventName, eventInfo[0], eventInfo[1], eventInfo[2]);		
			}else {
				System.out.println("Invalid file information");
			}
		}
		br.close();
		System.out.println("Loading is done!\n");
	}
	
	/**
	 * returns all events on a given day in ascending order
	 * @param date
	 */ 	
	public ArrayList<String> getDayEvents(LocalDate date) {
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<Event> events = getSortedEvents(getEventsOnDay(date));
		if(!(events.size() == 0)) {
			for(Event e: events) {
				results.add(e.getName() + " " + e.getDate() + " " + e.getStartingTime() + "-" + e.getEndingTime());
			}
		}else {
			results.add("No events scheduled for today");
		}
		
		return results;
	}
	
	/**
	 * creates an event and stores it in calendar
	 * @param name
	 * @param date
	 * @param startingTime
	 * @param endingTime
	 * @return Event created
	 */	
	public Event createEvent(String name, String date,String startingTime, String endingTime) {		
		date = transformDate(date);
		LocalDate eventDate = LocalDate.parse(date,dateFormat);
		LocalTime startTime = LocalTime.parse(startingTime, timeFormat);
		LocalTime endTime = LocalTime.parse(endingTime, timeFormat);
		
		LocalDateTime startingPoint = LocalDateTime.of(eventDate,startTime);
		if(endTime.isBefore(startTime)){//this means the event goes into the next day
			eventDate.plusDays(1);
		}
		LocalDateTime endingPoint = LocalDateTime.of(eventDate, endTime);
		Event e = new Event(name, new TimeInterval(startingPoint, endingPoint));
		for(Event event: getAllEvents()) {
			if(e.hasConflict(event)) {
				ErrorFrame error = new ErrorFrame(e.getName() +" will conflict with " + event.getName() + ". "  + e.getName() + " was not added");
				return null;
			}
		}
		addEvent(e);
		return e;
	}
	
	/**
	 * transforms date string into easily parsed string
	 * @param date
	 * @return string that allows the information to be easily parsed
	 */
	public String transformDate(String date) {
		String[] info = date.split("/");
		if(info.length < 3) {
			info = date.split("-");
		}
		if(info[0].length() < 2) {
			info[0] = "0" +info[0];
		}
		if(info[1].length() <2) {
			info[1] = "0" +info[1];
		}
		if(info[2].length() < 4) {
			info[2] = "20" + info[2];
		}
		return info[0] + "/" + info[1] +"/" + info[2];
	}
	/**
	 * 
	 * @param identifer
	 * @return String that allows comparison to user input
	 */
	public String transformIdentifier(String identifer) {
		String[] info = identifer.split(" ");
		if(info.length != 3) {
			System.out.println("Incorrect indentifier format");
			return "";
		}
		return transformDate(info[0]) + " " + info[1] +" " +info[2];
	}
	
	/**
	 * @param date
	 * @return true if it is a valid date. False if not
	 */	
	public boolean checkDate(String date) {
		String[] info = date.split("/");
		if(info.length != 3) {
			return false;
		}
		if(info[0].length() != 2 || (info[1].length() != 2)|| (info[2].length() != 4)) {
			return false;
		}
		return true;
	}
	
	/**
	 * @param time
	 * @return true if it is a valid time. False if not
	 */
	public boolean checkTime(String time) {
		String[] info = time.split(":");
		if(info.length != 2) {
			return false;
		}
		if(info[0].length() == 2 && info[1].length() == 2) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return the symbols of the days of the week in an array with the corresponding index value
	 */
	public String[] getDaysOfWeek() {
		String[] daysOfWeek = {"Su","Mo", "Tu", "We", "Th", "Fr", "Sa"};
		return daysOfWeek;
	}
	
	public int getCurrentDay() {
		return this.current.getDayOfMonth();
	}
	
	public Month getCurrentMonth() {
		return this.current.getMonth();
	}
	
	public int getCurrentYear() {
		return this.current.getYear();
	}
	
	public void goToNextMonth() {
		current = current.plusMonths(1);
		executeStateChanges(new ChangeEvent(this));
	}
	
	public void goToPrevMonth() {
		current = current.plusMonths(-1);
		executeStateChanges(new ChangeEvent(this));
	}
	
	public void goToNextDay() {
		current = current.plusDays(1);
		executeStateChanges(new ChangeEvent(this));
	}
	
	public void goToPrevDay() {
		current = current.plusDays(-1);
		executeStateChanges(new ChangeEvent(this));
	}
	
	public void addChangeListener(ChangeListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * activates all change listeners in the model
	 * @param event
	 */
	private void executeStateChanges(ChangeEvent event) {
		for(ChangeListener l:listeners) {
			l.stateChanged(event);
		}
	}
	
	/**
	 * @param newDate to set the current date
	 */
	public void setCurrentDate(LocalDateTime newDate) {
		this.current = newDate;
	}
	
	public LocalDate getAcutalCurrentDate() {
		return this.actualCurrentDate.toLocalDate();
	}
}
