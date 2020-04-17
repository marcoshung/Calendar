import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * 
 * @author marcoshung
 *This class serves as the view for the GUI
 */
public class CalendarFrame extends JFrame implements ChangeListener, MouseListener{
	private MyCalendar model;
	private CalendarGUI gui;
	private CalendarPanel cp;
	
	/**
	 * constructor. Builds the frame and adds components and panels to it
	 * @param calendar model
	 */
	public CalendarFrame(MyCalendar calendar) {
		super("Calendar");
		this.model = calendar;
		gui = new CalendarGUI(calendar);
		cp = new CalendarPanel(gui);
		add(cp, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setPreferredSize(new Dimension(1000,1000));
	    pack();
		setVisible(true);
		addMouseListener(this);
	}
	@Override
	//repaints the view
	public void stateChanged(ChangeEvent e) {
		cp.repaint();
	}
	
	@Override
	
	public void mouseClicked(MouseEvent e) {}
	
	//handles action when user clicks on the frame. Should only respond when a day box is clicked
	@Override
	public void mousePressed(MouseEvent e) {
		HashMap<Rectangle2D, Integer> dayBoxes = gui.getDayBoxes();
		//gets click location and adjusts for the title
		Point clickLocation = new Point(e.getX(), (e.getY() - gui.getTitleHeight()/5));
		
		if(cp.getIsCalendarView()) {
			//checks to see which box, if any, was clicked
			for(Rectangle2D box : dayBoxes.keySet()) {
				if(box.contains(clickLocation)) {
					LocalDate d = LocalDate.of(model.getCurrentYear(), model.getCurrentMonth().getValue(), dayBoxes.get(box));
					model.setCurrentDate(d.atStartOfDay());
					cp.setIsCalendarView(false);
					JButton monthView = new JButton("Month View");
					monthView.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cp.setIsCalendarView(true);
							repaint();
							cp.remove(monthView);
						}
					});
					cp.add(monthView);
					cp.validate();
					cp.repaint();
				}
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {		}
	@Override
	public void mouseEntered(MouseEvent e) {		}
	@Override
	public void mouseExited(MouseEvent e) {		}
	
	
}
