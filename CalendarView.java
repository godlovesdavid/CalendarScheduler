/**
COPYRIGHT (C) 2013  team flour. All Rights Reserved.
Calendar View is a model data change listening panel that draws a calendar.
Solves CS151 project

@author David Hsu, Phu Truong,  Minh Cong Nguyen
@version 1.00 11/19/2013
*/
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CalendarView extends JPanel implements ChangeListener
{
	static final String DAY_NAMES[] =
	{ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
		"Saturday" };
	static final String MONTH_NAMES[] =
	{ "January", "February", "March", "April", "May", "June", "July", "August",
		"September", "October", "November", "December" };
	static final JLabel weekLabel =
		new JLabel(
			"      Sun            Mon            Tue             Wed             Thu              Fri              Sat");
	private SchedulerModel model;
	private JPanel calendarPanel, showMonthPanel, monthPanel;
	private JLabel monthLabel;
	private JButton selectedButton;
	//current date has only month and year, which change when hitting back and forth buttons
	private Date currentdate;
	private static final long serialVersionUID = 196188863987669295L;

	/**
	 * Constructor of new CalendarView object.
	 * @param model the model to associate with.
	 */
	public CalendarView(SchedulerModel model)
	{
		setPreferredSize(new Dimension(600, 600));

		this.model = model;

		//instantiate calendar to a new calendar instance with today's date
		currentdate = new Date();

		makeCalendarPanel();

		makeShowMonthPanel();
		//add what we just made to calendar panel
		calendarPanel.add(showMonthPanel, BorderLayout.PAGE_START);

		makeMonthPanel();
		//set initial selected button to the button with model's date
		Date modeldate = model.getDate();
		selectedButton =
			(JButton) monthPanel.getComponents()[modeldate.getDay()
				- 1
				+ Date.getOffsetDaysInMonth(modeldate.getMonth(), modeldate
					.getYear()) - 1];
		//add what we just made to calendar panel
		calendarPanel.add(monthPanel, BorderLayout.PAGE_END);

		//add what we just made to view
		add(calendarPanel);
	}

	/**
	 * a show month panel has arrows for going back and forth between months and month labels for year and name
	 */
	private void makeShowMonthPanel()
	{
		showMonthPanel = new JPanel();

		//make month label showing current month name and add it to month panel
		monthLabel =
			new JLabel(MONTH_NAMES[currentdate.getMonth() - 1] + " "
				+ String.valueOf(currentdate.getYear()), JLabel.CENTER);
		monthLabel.setPreferredSize(new Dimension(150, 50));
		showMonthPanel.add(monthLabel);

		//left arrow to go to next month and add it to month panel
		JButton leftarrow = new JButton("<");
		leftarrow.setFont(new Font("sansserif", Font.BOLD, 18));
		leftarrow.setPreferredSize(new Dimension(50, 40));
		leftarrow.addActionListener(getMonthChangeListener(false));
		showMonthPanel.add(leftarrow);

		//right arrow to go to next month and add it to month panel
		JButton rightarrow = new JButton(">");
		rightarrow.setFont(new Font("sansserif", Font.BOLD, 18));
		rightarrow.setPreferredSize(new Dimension(50, 40));
		rightarrow.addActionListener(getMonthChangeListener(true));
		showMonthPanel.add(rightarrow);
	}

	/**
	 * makes the calendar panel that has back and forth buttons and label for 
	 * month, year, etc.
	 */
	private void makeCalendarPanel()
	{
		calendarPanel = new JPanel(new BorderLayout());
		calendarPanel.add(weekLabel, BorderLayout.CENTER);
	}

	/**
	 * makes a month panel that contains selectable date buttons.
	 */
	private void makeMonthPanel()
	{
		//make month panel having buttons with date number
		monthPanel = new JPanel(new GridLayout(0, 7));

		//offset number of days each month
		for (int offset = 0; offset < Date.getOffsetDaysInMonth(currentdate
			.getMonth(), currentdate.getYear()) - 1; offset++)
			monthPanel.add(Box.createGlue()); //spacer

		//make day buttons for this panel based on current date from calendar object
		JButton button;
		//make (max num of days for current month) date buttons
		for (int dayindex = 0; dayindex < Date.getMonthEnd(currentdate).getDay(); dayindex++)
		{
			button = new JButton(Integer.toString(dayindex + 1));
			button.setFont(new Font("sansserif", Font.BOLD, 12));
			button.setOpaque(false);
			button.setContentAreaFilled(false);
			button.setBorderPainted(false);
			button.setPreferredSize(new Dimension(60, 30));
			button.addActionListener(getDateChangeListener());
			button.setFocusable(false);

			/** if this month has the selected button that 
			 *  was previously chosen, disable the button.
			*/
			Date modeldate = model.getDate();
			if (currentdate.getYear() == modeldate.getYear()
				&& currentdate.getMonth() == modeldate.getMonth()
				&& Integer.parseInt(button.getText()) == modeldate.getDay())
			{
				button.setEnabled(false);
				selectedButton = button;
			}

			//add the date button to month panel
			monthPanel.add(button);
		}
	}

	/**
	 * Factory method that returns a 
	 * new ActionListener object for month changes.
	 * @param next whether the listener is for changing to next month. False if 
	 * going previous month.
	 * @return ActionListener for month change action.
	 */
	public ActionListener getMonthChangeListener(final boolean next)
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//when clicked, either go back or forth in month
				if (next)
					currentdate.add(1, 0, 0);
				else
					currentdate.add(-1, 0, 0);

				//set month label's text to new month name and year
				monthLabel.setText(MONTH_NAMES[currentdate.getMonth() - 1] + " "
					+ String.valueOf(currentdate.getYear()));

				//remove the panel with dates
				calendarPanel.remove(monthPanel);

				//put new buttons in the panel based on new month
				makeMonthPanel();

				//re-add the panel with dates
				calendarPanel.add(monthPanel, BorderLayout.PAGE_END);

				//refresh
				revalidate();
				repaint();
			}
		};
	}

	/**
	 * Date listener factory method that returns an object that 
	 * updates model's selected date.
	 * @return an ActionListener object for the date buttons
	 */
	public ActionListener getDateChangeListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//enable previously selected button
				selectedButton.setEnabled(true);

				//set selected button to pressed one
				selectedButton = (JButton) e.getSource();

				//and disable it
				selectedButton.setEnabled(false); //disable button

				//update model's selected date information
				model.updateSelectedDate(currentdate.getMonth(), Integer
					.parseInt((selectedButton).getText()), currentdate.getYear());
			}
		};
	}

	/**
	 * get new data from model and repaint self.
	 */
	public void stateChanged(ChangeEvent e)
	{
		revalidate();
		repaint();
	}
}
