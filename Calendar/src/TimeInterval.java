import java.time.LocalDateTime;
/**
 * 
 * @author marcoshung
 * imported base code from hw2
 */
public class TimeInterval {
	LocalDateTime startingTime;
	LocalDateTime endingTime;
	/**
	 * 
	 * @param startingTime
	 * @param endingTime
	 */
	public TimeInterval(LocalDateTime startingTime, LocalDateTime endingTime) {
		this.startingTime = startingTime;
		this.endingTime = endingTime;
	}
	
	/**
	 * 
	 * @return the starting time for this interval
	 */
	public LocalDateTime getStartingTime() {
		return this.startingTime;
	}
	
	/**
	 * 
	 * @return the ending time for this interval
	 */
	public LocalDateTime getEndingTime() {
		return this.endingTime;
	}
}
