/**
COPYRIGHT (C) 2013 David Hsu. All Rights Reserved.
Makes a new Event Scheduler and runs it.
Solves CS151 project

@author David Hsu, Phu Truong,  Minh Cong Nguyen
@version 1.00 11/19/2013
*/
import java.awt.GridLayout;
import java.io.IOException;
import java.text.ParseException;
import javax.swing.JFrame;

public class Calendar
{
	public static void main(String[] args) throws ParseException, IOException
	{
		//make model
		SchedulerModel model = new SchedulerModel();

		//make view1
		EventsView eventsview = new EventsView(model);
		model.attach(eventsview);

		//make view2
		CalendarView calendarview = new CalendarView(model);
		model.attach(calendarview);

		//put views in frame
		JFrame frame = new JFrame("Event Scheduler");
		frame.setLayout(new GridLayout());
		frame.add(calendarview);
		frame.add(eventsview);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
