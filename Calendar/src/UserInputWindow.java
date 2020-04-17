import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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

	
	public UserInputWindow(MyCalendar c) {
		this.calendar = c;
		
		frame = new JFrame("Create New Event");

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
		
		submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String eventName = eventNameBox.getText();
				String date = dateBox.getText();
				String startTime = startTimeBox.getText();
				String endTime = endTimeBox.getText();
				if(eventName.length() == 0 || date.length() == 0 || startTime.length() == 0 || endTime.length() == 0) {
					 JFrame emptyTextError = new ErrorFrame("Please Enter in all parts!");
				}else if(!calendar.checkDate(date) || !calendar.checkTime(startTime) || !calendar.checkTime(endTime)) {
					 JFrame wrongFormatError = new ErrorFrame("Please make sure your date and times are in the correct format!");
				}else {
					Event newEvent = calendar.createEvent(eventName, date, startTime, endTime);
					try {
						calendar.writeEventToFile(newEvent);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					dispose();
				}
			}
			});
		add(submit);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
	    setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
	    pack();
		setVisible(true);
	}
	
	public void setDateBox(String date) {
		dateBox.setEditable(false);
		dateBox.setText(date);
	}
}
