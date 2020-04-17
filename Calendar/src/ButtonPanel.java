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
	MyCalendar calendar;
	UserInputWindow input;
	
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
		
		create = new JButton("+");
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input = new UserInputWindow(calendar);
			}
		});
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		add(quit, BorderLayout.EAST);
		add(nbp, BorderLayout.SOUTH);
		add(create);
		nbp.paintComponent(g);
	}
	
	public void setInputDateBox(String date) {
		if(input != null) {
			input.setDateBox(date);
		}
	}
}
