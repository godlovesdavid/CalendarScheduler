/**
COPYRIGHT (C) 2013 team flour. All Rights Reserved.
Time as part of the event scheduler system.
Solves CS151 homework assignment #3

@author David Hsu, Phu Truong,  Minh Cong Nguyen
@version 1.00 10/19/2013
*/
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Time
{
	private Calendar calendar;

	/**
	 * Constructs a Time object with today's time.
	 */
	public Time()
	{
		calendar = new GregorianCalendar();
	}

	/**
	 * Constructs a new Time object with specs using 24 hour clock.
	 * @param hour hour
	 * @param minute minute
	 */
	public Time(int hour, int minute)
	{
		this();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
	}

	/**
	 * Constructs a Time object with today's time.
	 */
	public Time(int hour, int minute, boolean isPM)
	{
		this();
		calendar.set(Calendar.HOUR, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.AM_PM, isPM ? 1 : 0);
		//invert am/pm if hour given is 12, because of the way Calendar is
		if (hour == 12)
			calendar
				.set(Calendar.AM_PM, calendar.get(Calendar.AM_PM) == 1 ? 0 : 1);
	}

	/**
	 * Parses a string to a time object. Format: HH:MM
	 * @param str the string to parse to time
	 * @return a time object from given string
	 */
	public static Time parseTime(String str)
	{
		//take out spaces
		str = str.replaceAll(" ", "");

		//change to lower case
		str = str.toLowerCase();

		//12 hour format
		if (str.contains("pm") || str.contains("am"))
		{
			str = str.substring(0, str.indexOf("m") - 1);
			//has hour and minute
			if (str.contains(":"))
			{
				int hour = Integer.parseInt(str.substring(0, str.indexOf(":")));
				int minute =
					Integer.parseInt(str.substring(str.indexOf(":") + 1, str
						.length()));
				return new Time(hour, minute, str.contains("pm"));
			}
			else
			{
				//has only hour
				int hour = Integer.parseInt(str.substring(0, str.length()));
				return new Time(hour, 0, str.contains("pm"));
			}
		}
		else
		//24 hour format
		{
			//has hour and minute
			if (str.contains(":"))
			{
				int hour = Integer.parseInt(str.substring(0, str.indexOf(":")));
				int minute =
					Integer.parseInt(str.substring(str.indexOf(":") + 1, str
						.length()));
				return new Time(hour, minute);
			}
			else
			{
				//has only hour
				int hour = Integer.parseInt(str.substring(0, str.length()));
				return new Time(hour, 0);
			}
		}
	}

	/**
	 * Tests this time object with another time object.
	 * @param t2 time to test
	 * @return int compared
	 */
	public int compareTo(Time t2)
	{
		if (getHourOfDay() == t2.getHourOfDay())
			return (int) getMinute() - t2.getMinute();
		else
			return (int) getHourOfDay() - t2.getHourOfDay();
	}

	/**
	 * Gets hour of day. This is the hour of the 24 hour time clock.
	 * @return hour of day, between 1 and 24, inclusive
	 */
	public int getHourOfDay()
	{
		/* 24th hour of calendar HOUR_OF_DAY is represented by 0, so change it
		 * if needed
		 */
		return calendar.get(Calendar.HOUR_OF_DAY) == 0 ? 24 : calendar
			.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * gets minute.
	 * @return minute
	 */
	public int getMinute()
	{
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * gets hour in 12 hour time clock.
	 * @return hour, between 1 and 12 (inclusive).
	 */
	public int getHour()
	{
		/* 12th hour of calendar HOUR is represented by 0, so change it
		 * if needed
		 */
		return calendar.get(Calendar.HOUR) == 0 ? 12 : calendar
			.get(Calendar.HOUR);
	}

	/**
	 * returns whether the time is PM.
	 * @return true if PM, false if AM.
	 */
	public boolean isPM()
	{
		return calendar.get(Calendar.AM_PM) == 1 ? true : false;
	}

	/**
	 * Checks if this time is before another time.
	 * @param t2 the time to test with
	 * @return whether this time is before the other time
	 */
	public boolean before(Time t2)
	{
		return this.compareTo(t2) < 0;
	}

	/**
	 * Checks if this time is after another time.
	 * @param t2 the time to test with
	 * @return whether this time is after the other time
	 */
	public boolean after(Time t2)
	{
		return this.compareTo(t2) > 0;
	}

	/**
	 * checks if this time equals another time.
	 * @param t2 other time to test
	 * @return true if same time, false if not.
	 */
	public boolean equals(Time t2)
	{
		return this.compareTo(t2) == 0;
	}

	/**
	 * Overrides the toString() method and prints the time.
	 * @return the string representation of this time object
	 */
	@Override
	public String toString()
	{
		//one digit numbers must have 0 appended to it
		String minute = (getMinute() < 10 ? "0" : "") + getMinute();
		return getHour() + ":" + minute + (isPM() ? " PM" : " AM");
	}
}
