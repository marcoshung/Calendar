import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 
 * @author marcoshung
 * imported base code from hw2
 */
public class RecurringEvent extends Event{
	private String occurringDays;
	private LocalDate startDate;
	private LocalDate endDate;
	
	/**
	 * Constructor
	 * @param eventName
	 * @param t - Time interval with starting time and ending time
	 * @param ocurringDays - the days of the week this event occurs on
	 * @param start - start date
	 * @param end - end date
	 */
	public RecurringEvent(String eventName, TimeInterval t, String ocurringDays, LocalDate start, LocalDate end) {
		super(eventName, t);
		this.occurringDays = ocurringDays;
		this.startDate = start;
		this.endDate = end;
	}
	
	/**
	 * 
	 * @return the days of the week this event occurs on
	 */
	public String getOccurringDays() {
		return this.occurringDays;
	}
	
	/**
	 * 
	 * @return the start date of this event
	 */
	public LocalDate getStartDate() {
		return this.startDate;
	}
	
	/**
	 * 
	 * @return the end date for this event
	 */
	public LocalDate getEndDate() {
		return this.endDate;
	}
	
	/**
	 * overrides Event class's getIdentifer()
	 * @return the string that is used to identify the event in the text file
	*/
	@Override
	public String getIdentifer() {
		String s;
		String startingYear = this.getStartDate().getYear() + "";
		String startingDate = this.getStartDate().getMonthValue() + "/" +this.getStartDate().getDayOfMonth() +"/" + startingYear.substring(2);
		String endingYear = this.getEndDate().getYear() + "";
		String endingDate = this.getEndDate().getMonthValue() + "/" +this.getEndDate().getDayOfMonth() +"/" + endingYear.substring(2);
		s = occurringDays.toUpperCase() + " " + this.getStartingTime() +" " + this.getEndingTime() + " " + startingDate + " " + endingDate;
		return s;
	}
}
