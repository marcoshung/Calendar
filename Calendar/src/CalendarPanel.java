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
	
	public void setButtonListeners() {
		removeButtonListener(prevButton);
		removeButtonListener(nextButton);
		if(isCalendarView) {
			prevButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					calendar.getCalendar().goToPrevMonth();
				}
			});
			nextButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					calendar.getCalendar().goToNextMonth();
				}
			});
		}else {
			prevButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					calendar.getCalendar().goToPrevDay();
				}
			});
			
			nextButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					calendar.getCalendar().goToNextDay();
				}
			});
			
			bp.setInputDateBox(model.transformDate(model.getCurrentMonth().getValue() + "/" + model.getCurrentDay() + "/" + model.getCurrentYear()));
			repaint();
		}
	}
	
	public void removeButtonListener(JButton b) {
		for(ActionListener l : b.getActionListeners()) {
			b.removeActionListener(l);
		}
	}
	
	public void setIsCalendarView(boolean b) {
		this.isCalendarView = b;
	}
	
	public boolean getIsCalendarView() {
		return this.isCalendarView;
	}
}
