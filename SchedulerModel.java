/**
COPYRIGHT (C) 2013 team flour. All Rights Reserved.
Model for scheduler project (part of MVC design).
Controller updates data from model which then sends data
to views to represent the data.
Solves CS151 project

@author David Hsu, Phu Truong,  Minh Cong Nguyen
@version 1.00 11/19/2013
*/
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SchedulerModel
{
	/**
	 * constructor that instantiates a map of events that automatically sorts dates.
	 * @throws ParseException
	 */
	public SchedulerModel() throws ParseException
	{
		eventlists = new TreeMap<Date, List<Event>>(new Comparator<Date>()
		{
			public int compare(Date d1, Date d2)
			{
				return d1.compareTo(d2);
			}
		});

		//set selected date to today's date
		date = new Date();
		listeners = new ArrayList<ChangeListener>();
	}

	/**
	 * attach a new change listener to list of listeners of this model.
	 * When an update method is called, each of those listeners will be notified
	 * of the change and they will repaint themselves with the new data.
	 * @param listener the listener to add
	 */
	public void attach(ChangeListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * deletes events from given date.
	 * @param date date to delete events
	 */
	public void deleteEventsFromDay(Date date)
	{
		if (eventlists.containsKey(date))
			eventlists.remove(date);
	}

	/**
	 * attempts to add an event and then informs views of the change.
	 * @param date date to affect
	 * @param events events to add to date
	 */
	public boolean updateEvents(String name, Date date, Time start, Time end)
	{
		Event event = new Event(name, date, start, end);
		if (!eventDoesConflict(event))
		{
			if (!eventlists.containsKey(date))
			{
				eventlists.put(date, new ArrayList<Event>());
				eventlists.get(date).add(event);
			}
			else
			{
				eventlists.get(date).add(event);
				Collections.sort(eventlists.get(date), new Comparator<Event>()
				{
					public int compare(Event e1, Event e2)
					{
						return e1.getStartTime().compareTo(e2.getStartTime());
					}
				});
			}
		}
		else
			return false;
		for (ChangeListener listener : listeners)
		{
			listener.stateChanged(new ChangeEvent(this));
		}
		return true;
	}

	/**
	 * update the selected date.
	 * @param month month to set
	 * @param day day to set
	 * @param year year to set
	 */
	public void updateSelectedDate(int month, int day, int year)
	{
		date = new Date(month, day, year);
		for (ChangeListener listener : listeners)
			listener.stateChanged(new ChangeEvent(this));
	}

	/**
	 * Checks if an event conflicts in the system.
	 * @param event event to check
	 */
	public boolean eventDoesConflict(Event event)
	{
		if (eventlists.containsKey(event.getDate()))
			for (Event savedevent : eventlists.get(event.getDate()))
				if ((savedevent.getStartTime().before(event.getEndTime()) && savedevent
					.getEndTime().after(event.getEndTime()))
					|| (savedevent.getEndTime().after(event.getStartTime()) && savedevent
						.getEndTime().before(event.getEndTime()))
					|| (savedevent.getStartTime().equals(event.getStartTime()) && savedevent
						.getEndTime().equals(event.getEndTime())))
					return true;
		return false;
	}

	/**
	 * returns data of model containing all events.
	 * @return a clone of the event lists for all the dates.
	 */
	@SuppressWarnings("unchecked")
	public TreeMap<Date, List<Event>> getEventlists()
	{
		return (TreeMap<Date, List<Event>>) (eventlists.clone());
	}

	/**
	 * Gets date from data.
	 * @return date from data
	 */
	public Date getDate()
	{
		return date;
	}

	private List<ChangeListener> listeners;
	//data fields
	private TreeMap<Date, List<Event>> eventlists;
	private Date date;
}
