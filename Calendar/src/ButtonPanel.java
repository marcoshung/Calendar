import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel{
	NavigationButtonPanel nbp;
	JButton quit;
	JButton create;
	JButton delete;
	MyCalendar calendar;
	UserInputWindow createInput;
	UserInputWindow deleteInput;
	/**
	 * Constructor 
	 * @param prevButton 
	 * @param nextButton
	 * @param c this is the calendar model
	 */
	public ButtonPanel(JButton prevButton, JButton nextButton, MyCalendar c) {
		this.calendar = c;
		nbp = new NavigationButtonPanel(prevButton, nextButton);
		quit = new JButton("Exit");
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}
		);
		
		//Creates the "+" button which creates and adds events to the calendar
		create = new JButton("+");
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createInput = new UserInputWindow(calendar, "Create");
			}
		});
		
		//creates the "Delete" Button which deletes events from the calendar
		delete = new JButton("Delete");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteInput = new UserInputWindow(calendar, "Delete");
			}
		});
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		add(quit, BorderLayout.EAST);
		add(nbp, BorderLayout.SOUTH);
		add(create);
		add(delete);
		nbp.paintComponent(g);
	}
	
	/**
	 * @param date to set the input box with
	 */
	public void setInputDateBox(String date) {
		if(createInput != null) {
			createInput.setDateBox(date);
		}
	}
}
