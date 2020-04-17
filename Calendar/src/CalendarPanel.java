import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CalendarPanel extends JPanel{
	private CalendarGUI calendar;
	private MyCalendar model;
	private ButtonPanel bp;
	private boolean isCalendarView;
	private JButton prevButton;
	private JButton nextButton;
	/**
	 * Constructor that initializes the model and creates the button header panel
	 * @param c which is the GUI portion of the calendar 
	 */
	public CalendarPanel(CalendarGUI c) {
		this.calendar = c;
		model = calendar.getCalendar();
		isCalendarView = true;
		 prevButton = new JButton("<");
		 nextButton = new JButton(">");
		setButtonListeners();

		bp = new ButtonPanel(prevButton, nextButton, model);
		add(bp,BorderLayout.NORTH);
	}
	/**
	 * Paints Panel and sets buttons accordingly
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		if(isCalendarView) {
			calendar.drawMonthView(g2);
			setButtonListeners();
		}else {
			calendar.drawDay(g2);
			setButtonListeners();
		}
	}
	/**
	 * resets buttons' listeners to change the functionality when views switch
	 */
	public void setButtonListeners() {
		removeButtonListener(prevButton);
		removeButtonListener(nextButton);
		
		//if it is the calendar view then the button will move by month
		if(isCalendarView) {
			prevButton.addActionListener(new ActionListener() {
				@Override
				//moves view to previous month
				public void actionPerformed(ActionEvent e) {
					calendar.resetDayBoxes();
					calendar.getCalendar().goToPrevMonth();
				}
			});
			nextButton.addActionListener(new ActionListener() {
				@Override
				//moves view to next month
				public void actionPerformed(ActionEvent e) {
					calendar.resetDayBoxes();
					calendar.getCalendar().goToNextMonth();
				}
			});
		//if not then the button will move by day
		}else {
			prevButton.addActionListener(new ActionListener() {
				@Override
				//moves view to the previous day
				public void actionPerformed(ActionEvent e) {
					calendar.resetDayBoxes();
					calendar.getCalendar().goToPrevDay();
				}
			});
			
			nextButton.addActionListener(new ActionListener() {
				@Override
				//moves view to next day
				public void actionPerformed(ActionEvent e) {
					calendar.resetDayBoxes();
					calendar.getCalendar().goToNextDay();
				}
			});
			
			//This will set the date box in the user input window to only use the current day
			bp.setInputDateBox(model.transformDate(model.getCurrentMonth().getValue() + "/" + model.getCurrentDay() + "/" + model.getCurrentYear()));
		}
		//repaints the panel so changes can be seen immediately
		repaint();
	}
	
	/**
	 * resets the listeners of the buttons. Used to change the buttons' functionality from month to day and viceversa
	 * @param b button to remove listeners from
	 */
	public void removeButtonListener(JButton b) {
		for(ActionListener l : b.getActionListeners()) {
			b.removeActionListener(l);
		}
	}
	
	/**
	 * changes boolean to determine if it is month or day view.
	 * @param b
	 */
	public void setIsCalendarView(boolean b) {
		this.isCalendarView = b;
	}
	/**
	 * @return boolean true if it's month view or false if it's day view.
	 */
	public boolean getIsCalendarView() {
		return this.isCalendarView;
	}
}
