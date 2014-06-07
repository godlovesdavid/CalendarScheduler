
/**
COPYRIGHT (C) 2013 team flour. All Rights Reserved.
Event as part of the event scheduler system.
Solves CS151 homework assignment #3

@author David Hsu, Phu Truong,  Minh Cong Nguyen
@version 1.00 10/19/2013
*/

public class Event
{
	private String name;
	private Date date;
	private Time start;
	private Time end;

	/**
	 * Constructs a new event.
	 * @param name event name
	 * @param date event date
	 * @param start event time start
	 * @param end event time end
	 */
	public Event(String name, Date date, Time start, Time end)
	{
		this.name = name;
		this.date = date;
		this.start = start;
		this.end = end;
	}

	/**
	   Gets the name of this event.
	   @return the name of this event
	 */
	public String getName()
	{
		return this.name;

	}

	/**
		Gets the date of this event.
	   @return the date of this event
	 */
	public Date getDate()
	{
		return this.date;
	}

	/**
	 * Gets this event's start time.
	 * @return the start time
	 */
	public Time getStartTime()
	{
		return this.start;
	}

	/**
	 * Gets this event's end time.
	 * @return the end time
	 */
	public Time getEndTime()
	{
		return this.end;
	}
}
