import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class UserInputWindow extends JFrame{
	JFrame frame;
	JButton submit;
	MyCalendar calendar;
	int WINDOW_WIDTH = 200;
	int WINDOW_HEIGHT = 500;
	
	JTextField eventNameBox = new JTextField();
	JTextField dateBox = new JTextField();
	JTextField startTimeBox = new JTextField();
	JTextField endTimeBox = new JTextField();

	/**
	 * constructor 
	 * @param c as the model to access the data
	 * @param label to indicate whether this is the create or delete input for the user
	 */
	public UserInputWindow(MyCalendar c, String label) {
		this.calendar = c;
		
		frame = new JFrame("Create New Event");

		//creates the input boxes for the user
		eventNameBox.setSize(WINDOW_WIDTH,WINDOW_HEIGHT/5);
		eventNameBox.setText("Enter Event Name");
		add(eventNameBox);
		
		dateBox.setSize(WINDOW_WIDTH,WINDOW_HEIGHT/5);
		dateBox.setText("Starting Date (mm/dd/yyyy)");
		add(dateBox);
		
		startTimeBox.setSize(WINDOW_WIDTH,WINDOW_HEIGHT/5);
		startTimeBox.setText("Enter Starting Time (HH:mm)");
		add(startTimeBox);
		
		endTimeBox.setSize(WINDOW_WIDTH,WINDOW_HEIGHT/5);
		endTimeBox.setText("Enter Ending Time (HH:mm)");
		add(endTimeBox);
		
		submit = new JButton();
		

		//checks in the label is to create
		if(label.toLowerCase().equals("create")) {
			submit.setText("Create");
			submit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//gets the data from the boxes
					String eventName = eventNameBox.getText();
					String date = dateBox.getText();
					String startTime = startTimeBox.getText();
					String endTime = endTimeBox.getText();
					
					//makes sure all input fields are filled
					if(eventName.length() == 0 || date.length() == 0 || startTime.length() == 0 || endTime.length() == 0) {
						 JFrame emptyTextError = new ErrorFrame("Please Enter in all parts!");
					//makes sure data is in correct format
					}else if(!calendar.checkDate(date) || !calendar.checkTime(startTime) || !calendar.checkTime(endTime)) {
						 JFrame wrongFormatError = new ErrorFrame("Please make sure your date and times are in the correct format!");
					}else {
						//creates events and writes to the file
						Event newEvent = calendar.createEvent(eventName, date, startTime, endTime);
						if(newEvent != null) {
							try {
								calendar.writeEventToFile(newEvent);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
					//closes window
					dispose();
				}
			});
		//checks if the label is delete
		}else if(label.toLowerCase().equals("delete")) {
			submit.setText("Delete");
			submit.addActionListener(new ActionListener() {
			
				@Override
				public void actionPerformed(ActionEvent e) {
					//gets the data from the boxes

					String eventName = eventNameBox.getText();
					String date = dateBox.getText();
					String startTime = startTimeBox.getText();
					String endTime = endTimeBox.getText();
					
					//makes sure all input fields are filled
					if(eventName.length() == 0 || date.length() == 0 || startTime.length() == 0 || endTime.length() == 0) {
						 JFrame emptyTextError = new ErrorFrame("Please Enter in all parts!");
					//makes sure data is in correct format
					}else if(!calendar.checkDate(date) || !calendar.checkTime(startTime) || !calendar.checkTime(endTime)) {
						 JFrame wrongFormatError = new ErrorFrame("Please make sure your date and times are in the correct format!");
					}else {
						HashSet<Event> events = calendar.getAllEvents();
						Event targetEvent = null;
						//finds the event to delete
						for(Event event : events) {
;							if(event.getName().toLowerCase().equals(eventName.toLowerCase()) && calendar.transformIdentifier(event.getIdentifer()).equals(calendar.transformDate(date) + " " + startTime + " " + endTime)) {
								targetEvent = event;
							}
						}
						//if the event is not found it will send an error
						if(targetEvent == null) {
							 JFrame wrongFormatError = new ErrorFrame("No Event with those specifiers!");
						}else {
						//deletes the event
							calendar.deleteEvent(targetEvent);
						}
					}
					//closes window
					dispose();
				}
			});
		}else {
			System.out.println("Button Error");
			return;
		}
		add(submit);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
	    setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
	    pack();
		setVisible(true);
	}
	
	/**
	 * @param date to set the date text field and make sure the user cant edit it
	 */
	public void setDateBox(String date) {
		dateBox.setEditable(false);
		dateBox.setText(date);
	}
}
