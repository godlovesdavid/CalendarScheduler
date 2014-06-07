/**
COPYRIGHT (C) 2013  team flour. All Rights Reserved.
Events view is a model data change listening panel that draws 
menus for the user, allowing to view and change events for dates.
Solves CS151 project

@author David Hsu, Phu Truong,  Minh Cong Nguyen
@version 1.00 11/19/2013
*/
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EventsView extends JPanel implements ChangeListener
{
	private static final long serialVersionUID = 6741709934815312820L;
	private SchedulerModel model;
	private JPanel createpanel, daypanel, weekpanel, monthpanel, agendapanel,
		importpanel;
	private JTextArea monthtext, daytext, weektext;
	private TreeMap<Date, List<Event>> data;
	private GregorianCalendar calendar = new GregorianCalendar();
	private final static String newLine = System.getProperty("line.separator");
	private JTextField fromdate;
	private JTextField todate;
	JTextField starttext;
	JTextField endtext;
	JTextField datetext;

	/**
	 * constructor for an event scheduling system.
	 * @throws ParseException 
	 */
	public EventsView(SchedulerModel model) throws ParseException
	{
		this.model = model;

		//make a tabbed pane containing all the panels we want to make
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setPreferredSize(new Dimension(400, 400));
		makeDayPanel();
		tabbedPane.addTab("day", daypanel);
		makeWeekPanel();
		tabbedPane.addTab("week", weekpanel);
		makeMonthPanel();
		tabbedPane.addTab("month", monthpanel);
		makeAgendaPanel();
		tabbedPane.addTab("agenda", agendapanel);
		makeCreatePanel();
		tabbedPane.addTab("create", createpanel);
		makeImportPanel();
		tabbedPane.addTab("from file", importpanel);
		add(tabbedPane);

		//get data from model and repaint self
		stateChanged(new ChangeEvent(model));
	}

	/**
	 * makes the day view panel.
	 */
	private void makeDayPanel()
	{
		daypanel = new JPanel();
		daypanel.setLayout(new BorderLayout());
		daytext = new JTextArea();
		daypanel.add(new JScrollPane(daytext));
	}

	/**
	 * makes the week view panel.
	 * @throws ParseException 
	 */
	private void makeWeekPanel() throws ParseException
	{
		weekpanel = new JPanel();
		weekpanel.setLayout(new BorderLayout());
		weektext = new JTextArea();
		weekpanel.add(new JScrollPane(weektext));
	}

	/**
	 * makes the month view panel.
	 * @throws ParseException 
	 */
	private void makeMonthPanel() throws ParseException
	{
		monthpanel = new JPanel();
		monthpanel.setLayout(new BorderLayout());
		monthtext = new JTextArea();
		monthpanel.add(new JScrollPane(monthtext));
	}

	/**
	 * make the Agenda panel that allows you to query for events within dates.
	 */
	private void makeAgendaPanel()
	{
		agendapanel = new JPanel();
		agendapanel.setLayout(new BorderLayout());

		fromdate = new JTextField(model.getDate().toString(), 15);
		todate = new JTextField(model.getDate().toString(), 15);
		final JTextArea eventstext = new JTextArea();
		JButton okbutton = new JButton("View Events");

		setPreferredSize(new Dimension(450, 400));

		eventstext.setEditable(false);
		okbutton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String dateinfo[] = fromdate.getText().split("/");
				Date d1 =
					new Date(Integer.parseInt(dateinfo[0]), Integer
						.parseInt(dateinfo[1]), Integer.parseInt(dateinfo[2]));
				String dateinfo2[] = todate.getText().split("/");
				Date d2 =
					new Date(Integer.parseInt(dateinfo2[0]), Integer
						.parseInt(dateinfo2[1]), Integer.parseInt(dateinfo2[2]));
				eventstext.setText(eventsToString(d1, d2));
			}
		});
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.Y_AXIS));
		aPanel.add(new JLabel("from date"));
		aPanel.add(fromdate);
		aPanel.add(new JLabel("to date"));
		aPanel.add(todate);
		aPanel.add(okbutton);
		aPanel.add(new JLabel("format: mm/dd/yyyy"));
		agendapanel.add(aPanel, BorderLayout.PAGE_START);
		agendapanel.add(new JScrollPane(eventstext), BorderLayout.CENTER);
	}

	/**
	 * Make the From File panel that allows you to import events from a file into program.
	 */
	private void makeImportPanel()
	{
		importpanel = new JPanel();
		importpanel.setLayout(new BoxLayout(importpanel, BoxLayout.Y_AXIS));
		final JTextField result = new JTextField();
		JButton importButton = new JButton("Import Events");
		importButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					importEvents(".\\src\\input.txt");
					result.setText("Events added");
				}
				catch (IOException ioe)
				{
					result.setText("There's no input.txt file");
				}

			}
		});
		importpanel.add(importButton);
		importpanel.add(result);

		/**
		* import events from a file to the 
		*/
	}

	private void importEvents(String aFile) throws IOException
	{
		String[] allDaysOfWeek =
		{ "S", "M", "T", "W", "H", "F", "A" };
		List<String> lines;
		Charset ENCODING = StandardCharsets.UTF_8;
		Path path = Paths.get(aFile);
		lines = Files.readAllLines(path, ENCODING);
		for (String line : lines)
		{
			String[] lineArray = line.split(";");
			String[] anArray = lineArray[4].split("");
			String[] daysOfWeek = new String[anArray.length - 1];
			System.arraycopy(anArray, 1, daysOfWeek, 0, daysOfWeek.length);
			Time startTime = new Time(Integer.parseInt(lineArray[5]), 0);
			Time endTime = new Time(Integer.parseInt(lineArray[6]), 0);
			calendar.set(GregorianCalendar.MONTH,
				Integer.parseInt(lineArray[2]) - 1);
			calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
			calendar.set(GregorianCalendar.YEAR, Integer.parseInt(lineArray[1]));

			while (calendar.get(GregorianCalendar.MONTH) <= Integer
				.parseInt(lineArray[3]) - 1)
			{
				for (String dayOfWeek : daysOfWeek)
				{
					if (calendar.get(GregorianCalendar.DAY_OF_WEEK) == Arrays
						.asList(allDaysOfWeek).indexOf(dayOfWeek) + 1)
					{
						Date date =
							new Date(calendar.get(GregorianCalendar.MONTH) + 1,
								calendar.get(GregorianCalendar.DAY_OF_MONTH), calendar
									.get(GregorianCalendar.YEAR));
						model.updateEvents(lineArray[0], date, startTime, endTime);
					}
				}
				calendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
			}
		}
	}

	/**
	 * makes the create an event panel that allows you to add events to program.
	 * 
	 */

	private void makeCreatePanel()
	{
		createpanel = new JPanel();
		createpanel.setLayout(new BoxLayout(createpanel, BoxLayout.PAGE_AXIS));
		final JTextField titletext = new JTextField("Title");
		datetext = new JTextField("Date"); //MM/dd/yyyy
		starttext = new JTextField("1:00"); //hh:mm
		endtext = new JTextField("2:00");

		createpanel.add(new JLabel("Add New Event:"));
		createpanel.add(new JLabel("Title"));
		createpanel.add(titletext);
		createpanel.add(new JLabel("Date"));
		createpanel.add(datetext);
		createpanel.add(new JLabel("Start time"));
		createpanel.add(starttext);
		createpanel.add(new JLabel("End time"));
		createpanel.add(endtext);

		JButton okbutton = new JButton();
		okbutton.setText("Add Event");
		okbutton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Time time1 = Time.parseTime(starttext.getText());
				Time time2 = Time.parseTime(endtext.getText());
				if (time2.before(time1))
					throw new IllegalArgumentException(
						"start time must be before end time");
				String dateinfo[] = datetext.getText().split("/");
				if (!model.updateEvents(titletext.getText(), new Date(Integer
					.parseInt(dateinfo[0]), Integer.parseInt(dateinfo[1]), Integer
					.parseInt(dateinfo[2])), time1, time2))
					System.out.println("Error with adding event");
				else
					System.out.println("Added event");

			}
		});

		createpanel.add(okbutton);
		createpanel.add(new JLabel("Date format: mm/dd/yyyy"));
		createpanel.add(new JLabel("Time format: hh:mm in 24-hr time clock"));
	}

	/**
	 * Get a string of events within dates.
	 * @param datefrom starting date
	 * @param dateend ending date
	 * @return a string of events between the dates
	 * @throws IllegalArgumentException if start date is not before end date
	 */
	public String eventsToString(Date datefrom, Date dateend)
	{
		if (dateend.before(datefrom))
			throw new IllegalArgumentException(
				"start date must be before end date");
		String str = "Events from " + datefrom + " to " + dateend + newLine;

		int currentyear = 0;
		int currentmonth = 0;
		int currentday = 0;

		//for each entry of data as date => eventlist)
		for (Map.Entry<Date, List<Event>> entry : data.entrySet())
		{
			Date date = entry.getKey();
			List<Event> eventlist = entry.getValue();

			// if (date >= datefrom && date <= dateend)
			if (date.compareTo(datefrom) >= 0 && date.compareTo(dateend) <= 0)
			{
				if (currentyear != date.getYear())
				{
					str += newLine + "     ";
					currentyear = date.getYear();
					str += newLine + Integer.toString(currentyear);
				}
				if (currentmonth != date.getMonth())
				{
					str += newLine + "     ";
					currentmonth = date.getMonth();
					str +=
						newLine + "     "
							+ CalendarView.MONTH_NAMES[date.getMonth() - 1];
				}
				if (currentday != date.getDay())
				{
					str += newLine + "     ";
					currentday = date.getDay();
					str +=
						newLine + "     " + date.getDay() + " : "
							+ CalendarView.DAY_NAMES[date.getDayOfWeek() - 1];
				}
				for (Event event : eventlist)
				{
					str +=
						newLine + "     " + event.getStartTime() + "-"
							+ event.getEndTime() + ": " + event.getName();
				}
			}
		}
		return str;
	}

	/**
	 * lists events from given date.
	 * @param date date to list events
	 */
	public String eventsToString(Date date)
	{
		//make a string with date asked for
		String str = date + "\r\n";

		//try to get events for this date from map (data)
		if (data.containsKey(date))
		{
			for (Event event : data.get(date))
				//append event to string
				str +=
					event.getStartTime() + "-" + event.getEndTime() + " "
						+ event.getName() + "\r\n";
		}
		return str;
	}

	/**
	 * updates when state of model has changed.
	 */
	public void stateChanged(ChangeEvent e)
	{
		//get data from model
		data = model.getEventlists();//update events
		Date selecteddate = model.getDate();

		//set the day's event's text to selected date's events
		daytext.setText(eventsToString(selecteddate));

		//same for week
		weektext.setText(eventsToString(Date.getWeekStart(selecteddate), Date
			.getWeekEnd(selecteddate)));

		//same for month
		monthtext.setText(eventsToString(Date.getMonthStart(selecteddate), Date
			.getMonthEnd(selecteddate)));

		fromdate.setText(model.getDate().toString());
		todate.setText(model.getDate().toString());
		datetext.setText(model.getDate().toString());

		repaint();
		revalidate();
		updateUI();
	}
}
