import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
/**
 * 
 * @author marcoshung
 * imported base code from hw2
 */
public class Event {
	private String name;
	private TimeInterval timeInterval;
	
	/**
	 * Constructor
	 * @param name - name of event
	 * @param time - starting time to ending time
	 */
	
	public Event(String name, TimeInterval time) {
		this.name = name;
		this.timeInterval = time;
	}
	
	/**
	 * @return the start date for this event 
	 */
	public LocalDate getDate() {
		return timeInterval.getStartingTime().toLocalDate();
	}
	
	/**
	 * 
	 * @return the name of this event
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return the starting time of this event
	 */
	public LocalTime getStartingTime() {
		return this.timeInterval.getStartingTime().toLocalTime();
	}
	
	/**
	 * @return the ending time for this event
	 */
	public LocalTime getEndingTime() {
		return this.timeInterval.getEndingTime().toLocalTime();
	}
	
	/**
	 * @return the full date and time for the starting point of this event
	 */
	public LocalDateTime getStartingDateAndTime() {
		return this.timeInterval.getStartingTime();
	}
	
	/**
	 * @return the full date and time for the ending point of this event
	 */
	public LocalDateTime getEndingDateAndTime() {
		return this.timeInterval.getEndingTime();
	}
	
	/**
	 * 
	 * @param e - an event to see if it conflicts with this events time
	 * @return true if there is a conflict in time. False if not
	 */
	public boolean hasConflict(Event e) {
		if(e.getEndingDateAndTime().isAfter(this.getStartingDateAndTime()) && (e.getEndingDateAndTime().isBefore(this.getEndingDateAndTime())) || e.getEndingDateAndTime().compareTo(this.getEndingDateAndTime()) == 0) {
			return true;
		}
		if(this.getEndingDateAndTime().isAfter(e.getStartingDateAndTime()) && this.getEndingDateAndTime().isBefore(e.getEndingDateAndTime())) {
			return true;
		}
		return false;
	}
	/**
	 * @return the string that is used to identify the event in the text file
	 */
	public String getIdentifer() {
		String s;
		LocalDate date = this.getDate();
		String year = date.getYear() + "";
		s = date.getMonthValue() + "/" + date.getDayOfMonth()+ "/" + year.substring(2) + " " + this.getStartingTime().toString() + " " + this.getEndingTime().toString();
		return s;
	}
}
