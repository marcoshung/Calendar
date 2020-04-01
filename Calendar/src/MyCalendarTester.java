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
 * 
 */
public class MyCalendarTester {
	static Scanner console = new Scanner(System.in);
	static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
	static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	static MyCalendar calendar = new MyCalendar();
	static File eventFile = new File("events.txt");
	static File outputFile = new File("output.txt");
	
	/**
	 * Main method
	 * calls methods to print calendar, load events, and begin main menu
	 * @param args
	 */
	public static void main(String[] args) {
		printCalendar();
		try {
			loadEvents();
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
				view();
			}else if(input.equals("c")) {
				create();
			}else if(input.equals("g")){
				goTo();
			}else if(input.equals("e")) {
				seeEventList();
			}else if(input.equals("d")) {
				delete();
			}else if(input.equals("q")) {
				done = true;
				System.out.println("Good Bye");
			}else {
				System.out.println("Not valid input");
			}
		}
	}
	
	
	/**
	 * prints events by single event or recurring events in ascending order
	 */
	private static void seeEventList() {
		ArrayList<Event> singleEvents = getSortedEvents(calendar.getSingleEvents());
		System.out.println("ONE TIME EVENTS");
		for(Event e : singleEvents) {
			System.out.println(e.getDate().getDayOfWeek() + " " + e.getDate().getMonth() + " " + e.getDate().getDayOfMonth() + ", " + e.getDate().getYear() + " " + e.getStartingTime() + "- " + e.getEndingTime() + " " + e.getName());
		}
		
		System.out.println();
		ArrayList<Event> recurringEvents = getSortedEvents(calendar.getRecurringEvents());
		System.out.println("RECURRING EVENTS");
		
		for(Event e : recurringEvents) {
			RecurringEvent re = (RecurringEvent) (e);
			System.out.println(re.getName() + "\n" + re.getOccurringDays() +" " + re.getStartingTime() + " " + re.getEndingTime() +" " + re.getStartDate() + " " +re.getEndDate());
		}
		System.out.println();
	}
	
	/**
	 * handles user interaction for the delete use case
	 */
	
	public static void delete() {
		System.out.println("Choose what type of delete you would like to do\n"
				+ "[S]elected	[A]ll	[DR]ecurring");
		String input = console.nextLine().toLowerCase();
		while(!input.equals("s") && !input.equals("a") && !input.equals("dr")) {
			System.out.println("Invalid input. Try Again");
			input = console.nextLine().toLowerCase();
		}

		if(input.equals("s")) {
			deleteOneTime();
		}else if(input.equals("a")) {
			deleteAll();
		}else if(input.equals("dr")) {
			deleteRecurringEvents();
		}
		
	}
	/**
	 * deletes a single occurring event from the calendar
	 */
	
	public static void deleteOneTime() {
		System.out.println("Enter the date of the event you wish to cancel in mm/dd/yyyy format");
		String dateString = console.nextLine();
		while(!checkDate(dateString)) {
			System.out.println("Invalid date format. Try again");
			dateString = console.nextLine();
		}
		LocalDate date = LocalDate.parse(dateString,dateFormat);

		HashSet<Event> eventsOnDate = new HashSet<Event>();
		for(Event e : calendar.getSingleEvents()) {
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
				calendar.getSingleEvents().remove(e);
				deleteEvent(e, e.getIdentifer());
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
	 * deletes all events on a given date from the calendar
	 */
	
	public static void deleteAll() {
		System.out.println("Enter the date that you want to delete all One Time Events. Please enter in mm/dd/yyyy format");
		String dateString = console.nextLine();
		while(!checkDate(dateString)) {
			System.out.println("Invalid date format. Try again");
			dateString = console.nextLine();
		}
		LocalDate date = LocalDate.parse(dateString,dateFormat);
		Set<Event> events = calendar.getSingleEvents();
		Set<Event> eventsToDelete = new HashSet<Event>();
		for(Event e : events) {
			if(e.getDate().equals(date)) {
				eventsToDelete.add(e);
			}
		}
		if(eventsToDelete.size() != 0) {
			for(Event e : eventsToDelete) {
				calendar.getSingleEvents().remove(e);
				deleteEvent(e, e.getIdentifer());
			}
			System.out.println("All events deleted on " + date.getMonth() + " " + date.getDayOfMonth() + " " + date.getYear());
		}else {
			System.out.println("No events to delete on that date");
		}
	}
	
	/**
	 * deletes all instances of a recurring event from the calendar
	 */
	
	public static void deleteRecurringEvents() {
		System.out.println("Enter the name of the recurring event you want to delete. This will delete all of the specified"
				+ " event in the entire calendar");
		String name = console.nextLine().toLowerCase();
		boolean deleted = false;
		String eventName = "";
		for(Event e: calendar.getRecurringEvents()) {
			if(e.getName().toLowerCase().equals(name)) {
				deleteEvent(e, e.getIdentifer());
				deleted = true;
				eventName = e.getName();
			}
		}
		if(deleted) {
			System.out.println("All " + eventName + " events have been deleted");
		}else {
			System.out.println("No events with that name");
		}
	}
	
	/**
	 * delete events from the main event list in calendar
	 * @param e - Event
	 * @param identifier - String identifier to find the event in the file
	 */
	
	public static void deleteEvent(Event e, String identifier) {
		calendar.getAllEvents().remove(e);
		try {
			deleteEventFromFile(e.getName());
			deleteEventFromFile(identifier);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * deletes specified string from the events file
	 * @param target
	 * @throws IOException
	 */
	
	public static void deleteEventFromFile(String target) throws IOException{
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
	 * handles user interaction for the view use case
	 */

	public static void view() {
		System.out.println("[D]ay view or [M]onth view ?"); 
		String input = console.nextLine().toLowerCase();
		while(!input.equals("d") && !input.equals("m")) {
			System.out.println("Not valid Input. Try Again");
			input = console.nextLine().toLowerCase();
		}
		LocalDateTime current = LocalDateTime.now();
		boolean done = false;
		if(input.equals("d")) {
			while(!done) {
				System.out.println(current.getDayOfWeek() + ", " + current.getMonth() + " " + current.getDayOfMonth());
				printDayEvents(current.toLocalDate());
				System.out.println("[P]revious or [N]ext or [G]o back to the main menu ?");
				input = console.nextLine().toLowerCase();
				
				if(input.equals("g")) {
					done = true;
				}else if(input.equals("p")) {
					current = current.plusDays(-1);
				}else if(input.equals("n")) {
					current = current.plusDays(1);
				}else {
					System.out.println("Invalid input try again");
				}
			}
		}else if(input.equals("m")) {
			while(!done) {
				printMonthEvents(current);
				System.out.println("\n[P]revious or [N]ext or [G]o back to the main menu ?");
				input = console.nextLine().toLowerCase();
				if(input.equals("g")) {
					done = true;
				}else if(input.equals("p")) {
					current = current.plusMonths(-1);
				}else if(input.equals("n")) {
					current = current.plusMonths(1);
				}else {
					System.out.println("Invalid input try again");
				}
			}
		}
	}
	
	/**
	 * handles user interaction for the create use case
	 */
	
	public static void create() {
		System.out.println("Enter the name of your event");
		String eventName = console.nextLine();
		System.out.println("What date is your event on? Please enter in mm/dd/yyyy format");
		String date = console.nextLine();
		while(!checkDate(date)) {
			System.out.println("Invalid date format. Try again");
			date = console.nextLine();
		}
		System.out.println("When does your event start? Please enter in HH:mm format");
		String startTime = console.nextLine();
		while(!checkTime(startTime)) {
			System.out.println("Invalid time format. Try again");
			startTime = console.nextLine();
		}
		System.out.println("When does your event end? Please enter in HH:mm format");
		String endTime = console.nextLine();
		while(!checkTime(endTime)) {
			System.out.println("Invalid time format. Try again");
			endTime = console.nextLine();
		}
		Event e = createEvent(eventName,date,startTime, endTime);
		calendar.addSingleEvent(e);
		if(!(e == null)) {
			System.out.println(eventName + " has been created on " + date + " from " + startTime + " - " + endTime);

			try {
				writeEventToFile(e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * adds event information to event file
	 * @param e - Event
	 * @throws IOException
	 */
	
	public static void writeEventToFile(Event e) throws IOException {
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
	 * handles user interaction for the go to use case
	 */
	
	public static void goTo() {
		System.out.println("Enter the date you want to see in mm/dd/yyy format");
		String input = console.nextLine();
		while(!checkDate(input)) {
			System.out.println("Invalid date format. Try again");
			input = console.nextLine();
		}
		LocalDate date = LocalDate.parse(input,dateFormat);
		printDayEvents(date);
		
	}
	
	/**
	 * 
	 * @param currentMonth
	 * @param year
	 * @return all events for a given month
	 */
	
	private static HashSet<Integer> getEventDaysForMonth(int currentMonth, int year) {
		HashMap<LocalDate, ArrayList<Event>> events = calendar.getEvents();
		HashSet<Integer> days = new HashSet<Integer>();
		for(LocalDate day :events.keySet()) {
			if(day.getMonthValue() == currentMonth && day.getYear() == year) {
				days.add(day.getDayOfMonth());
			}
		}
		return days;
	}
	
	/**
	 * prints the current month view with the current day highlighted in brackets
	 */

	public static void printCalendar() {
		LocalDateTime current = LocalDateTime.now();
		Month month= current.getMonth();
		int currentDayOfMonth = current.getDayOfMonth();
		LocalDateTime firstDayOfMonth = current.withDayOfMonth(1);
		
		System.out.println(current.getMonth() + " " + current.getYear());
		printDaysOfWeek();
		int pos = firstDayOfMonth.getDayOfWeek().getValue();
		setWeekPosition(firstDayOfMonth.getDayOfWeek().getValue());
		for(int i = 1; i <= month.length(isLeapYear(current.getYear())); i ++) {
			if(i == currentDayOfMonth) {
				System.out.printf("[" + "%2s" + "]",i);
			}else {
				System.out.printf("%3s", i);

			}
			if((1 + pos) % 7 == 0) {
				System.out.println();
			}
			pos++;
		}
		System.out.println();
	}
	
	/**
	 * offsets the position of the calendar to get the correct position for the first day of the month
	 * @param value - integer value for the day of the week
	 */
	private static void setWeekPosition(int value) {
		if(value == 7) {
			return;
		}
		for(int i = 0; i < value; i++) {
			System.out.print("   ");
		}
	}
	
	/**
	 * @param year
	 * @return true if the year is a leap year
	 */
	
	public static boolean isLeapYear(int year) {
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
	
	public static void loadEvents() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(eventFile));  
		String line = null;
		while((line = br.readLine())!= null) {
			String eventName = line;
			line = br.readLine();
			String[] eventInfo = line.split(" ");
			if(eventInfo.length == 5) { //recurring event
				HashMap<Character, Integer> daysOfWeek = getWeekSymbols();
				String days = eventInfo[0].toLowerCase();
				HashSet<Integer>occuringDays =  new HashSet<Integer>();
				
				for(int i = 0; i < days.length(); i++) {
					occuringDays.add(daysOfWeek.get(days.charAt(i)));
				}

				LocalDate startDate = LocalDate.parse(transformDate(eventInfo[3]),dateFormat);
				LocalDate endDate = LocalDate.parse(transformDate(eventInfo[4]),dateFormat);
				LocalDate tempDate = startDate;
				
				RecurringEvent event = createRecurringEvent(eventName, eventInfo);
				calendar.addRecurringEvent(event);
				while(tempDate.isBefore(endDate) || tempDate.compareTo(endDate) == 0) {
					if(occuringDays.contains(tempDate.getDayOfWeek().getValue())){
						String dateString = tempDate.getMonthValue() + "/" + tempDate.getDayOfMonth() + "/" + tempDate.getYear();
						createEvent(eventName, dateString, eventInfo[1],eventInfo[2]);
					}
					tempDate = tempDate.plusDays(1);
				}
				
			}else if(eventInfo.length == 3) { //one time event
				Event e = createEvent(eventName, eventInfo[0], eventInfo[1], eventInfo[2]);
				calendar.addSingleEvent(e);
				
			}else {
				System.out.println("Invalid file information");
			}
		}
		br.close();
		System.out.println("Loading is done!\n");
	}
	
	/**
	 * prints all events on a given day in ascending order
	 * @param date
	 */
	
	public static void printDayEvents(LocalDate date) {
		ArrayList<Event> events = getSortedEvents(calendar.getEventsOnDay(date));
		if(!(events.size() == 0)) {
			for(Event e: events) {
				System.out.println(e.getName() + " " + e.getDate() + " " + e.getStartingTime() + "-" + e.getEndingTime());
			}
		}else {
			System.out.println("No events scheduled for today");
		}
	}
	
	/**
	 * prints a monthly calendar view with all days that have events highlighted with brackets
	 * @param current - current Date
	 */
	
	public static void printMonthEvents(LocalDateTime current) {
		System.out.println(current.getMonth() + " " + current.getYear());
		printDaysOfWeek();
		LocalDateTime firstDayOfMonth = current.withDayOfMonth(1);
		
		setWeekPosition(firstDayOfMonth.getDayOfWeek().getValue());
		
		int pos = firstDayOfMonth.getDayOfWeek().getValue();
		HashSet<Integer> eventDays = getEventDaysForMonth(current.getMonthValue(), current.getYear());
		
		for(int i = 1; i <= current.getMonth().length(isLeapYear(current.getYear())); i ++) {
			if(eventDays.contains(i)) {
				System.out.printf("{%s}", i);
			}else {
				System.out.printf("%3s", i);
			}	
			if((pos + 1) % 7 == 0) {
				System.out.println();
			}
			pos++;
		}
	}
	
	/**
	 * @return values mapping to their respective days of the week
	 */
	private static HashMap<Character, Integer> getWeekSymbols() {
		HashMap<Character,Integer> symbols = new HashMap<Character,Integer>();
		symbols.put('m', 1);
		symbols.put('t', 2);
		symbols.put('w', 3);
		symbols.put('r', 4);
		symbols.put('f', 5);
		symbols.put('a', 6);
		symbols.put('7', 7);

		return symbols;
	}
	
	/**
	 * creates an event and stores it in calendar
	 * @param name
	 * @param date
	 * @param startingTime
	 * @param endingTime
	 * @return Event created
	 */
	
	public static Event createEvent(String name, String date,String startingTime, String endingTime) {		
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
		for(Event event: calendar.getAllEvents()) {
			if(e.hasConflict(event)) {
				System.out.println(e.getName() +" will conflict with " + event.getName() + ". "  + e.getName() + " was not added");
				return null;
			}
		}
		calendar.addEvent(e);
		return e;
	}
	
	/**
	 * transforms date string into easily parsed string
	 * @param date
	 * @return string that allows the information to be easily parsed
	 */
	
	public static String transformDate(String date) {
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
	 * @param eventList
	 * @return sorted Events in ascending order
	 */
	
	public static ArrayList<Event> getSortedEvents(Set<Event> eventList){
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
	 * 
	 * @param name
	 * @param eventInfo
	 * @return RecurringEvent with the information given
	 */
	
	public static RecurringEvent createRecurringEvent(String name, String[] eventInfo) {

		LocalDate startDate = LocalDate.parse(transformDate(eventInfo[3]),dateFormat);
		LocalDate endDate = LocalDate.parse(transformDate(eventInfo[4]),dateFormat);
		LocalTime startTime = LocalTime.parse(eventInfo[1], timeFormat);
		LocalTime endTime = LocalTime.parse(eventInfo[2], timeFormat);
		
		TimeInterval t = new TimeInterval(LocalDateTime.of(startDate, startTime), LocalDateTime.of(startDate, endTime));
		
		return new RecurringEvent(name, t,eventInfo[0],startDate, endDate);
	}
	
	/**
	 * 
	 * @param date
	 * @return true if it is a valid date. False if not
	 */
	
	public static boolean checkDate(String date) {
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
	 * 
	 * @param time
	 * @return true if it is a valid time. False if not
	 */
	
	public static boolean checkTime(String time) {
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
	 * prints the header of the calendar with days of the week
	 */
	
	public static void printDaysOfWeek() {
		String[] daysOfWeek = {"Su","Mo", "Tu", "We", "Th", "Fr", "Sa"};
		for(int i = 0; i < 7; i++) {
			System.out.printf("%3s",daysOfWeek[i]);
		}
		System.out.println();
	}
}