import java.time.*;
import java.util.*;
/**
 * 
 * @author marcoshung
 *
 */
public class MyCalendar {
	private HashSet<Event> allEvents;
	private HashSet<Event> recurringEvents;
	private HashSet<Event> singleEvents;
	private HashMap<LocalDate, ArrayList<Event>> events;
	/**
	 * Constructor - initializes all instance variables
	 */
	public MyCalendar() {
		events = new HashMap<LocalDate,ArrayList<Event>>();
		allEvents = new HashSet<Event>();
		recurringEvents = new HashSet<Event>();
		singleEvents = new HashSet<Event>();
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
	 * @return a HashMap with Events mapped to the date they are on
	 */
	public HashMap<LocalDate, ArrayList<Event>> getEvents() {
		return this.events;
	}
	
	/**
	 * 
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
	 * @return all events the calendar contains
	 */
	public HashSet<Event> getAllEvents() {
		return this.allEvents;
	}
	
	/**
	 * 
	 * @return all recurring events
	 */
	public HashSet<Event> getRecurringEvents(){
		return this.recurringEvents;
	}
	
	/**
	 * 
	 * @return all one time events
	 */
	public Set<Event> getSingleEvents(){
		return this.singleEvents;
	}
	
	/**
	 * adds Event to the recurring events list
	 * @param e - Event
	 */
	public void addRecurringEvent(RecurringEvent e) {
		this.recurringEvents.add(e);
	}
	
	/**
	 * adds Event to single events list
	 * @param e - Event
	 */
	public void addSingleEvent(Event e) {
		this.singleEvents.add(e);
	}
}
