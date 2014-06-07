/**
COPYRIGHT (C) 2013 team flour. All Rights Reserved.
Date class having simply day, month, year fields.
Solves CS151 project

@author David Hsu, Phu Truong,  Minh Cong Nguyen
@version 1.00 11/19/2013
*/
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Date
{
	private Calendar calendar;

	/**
	 * instantiate new Date object with today's date
	 */
	public Date()
	{
		calendar = new GregorianCalendar();
	}

	/**
	 * make new date object with month, day, and year
	 * @param month
	 * @param day
	 * @param year
	 */
	public Date(int month, int day, int year)
	{
		calendar = new GregorianCalendar(year, month - 1, day);
	}

	/**
	 * set this date to desired date
	 * @param month
	 * @param day
	 * @param year
	 */
	public void set(int month, int day, int year)
	{
		calendar.set(year, month - 1, day);
	}

	/**
	 * add months/days/years to this Date.
	 * @param months
	 * @param days
	 * @param years
	 */
	public void add(int months, int days, int years)
	{
		calendar.add(Calendar.MONTH, months);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		calendar.add(Calendar.YEAR, years);
	}

	/**
	 * get day
	 * @return day number (not index)
	 */
	public int getDay()
	{
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * get month
	 * @return month (not index)
	 */
	public int getMonth()
	{
		return calendar.get(Calendar.MONTH) + 1; //this one is index value
	}

	/**
	 * get year
	 * @return year (not index)
	 */
	public int getYear()
	{
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * override tostring method with simple mm/dd/yyyy format
	 */
	public String toString()
	{
		return getMonth() + "/" + getDay() + "/" + getYear();
	}

	/**
	 * compare method
	 * @param d2 date to compare this date with
	 * @return the result of the comparison. < 0 if this date is
	 *  before other, 0 if equal, > 0 if after.
	 */
	public int compareTo(Date d2)
	{
		if (this.getYear() == d2.getYear())
		{
			if (this.getMonth() == d2.getMonth())
			{
				return (int) (this.getDay() - d2.getDay());
			}
			else
				return (int) (this.getMonth() - d2.getMonth());
		}
		else
			return (int) (this.getYear() - d2.getYear());
	}

	/**
	 * gets the week start date
	 * @param date date to examine
	 * @return date with week start info
	 */
	public static Date getWeekStart(Date date)
	{
		Calendar c =
			new GregorianCalendar(date.getYear(), date.getMonth() - 1, date
				.getDay());
		c.add(Calendar.DATE, -c.get(Calendar.DAY_OF_WEEK) + 1);
		return new Date(c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE), c
			.get(Calendar.YEAR));
	}

	/**
	 * gets the week end date
	 * @param date date to examine
	 * @return date with week end info
	 */
	public static Date getWeekEnd(Date date)
	{
		Calendar c =
			new GregorianCalendar(date.getYear(), date.getMonth() - 1, date
				.getDay());
		c.add(Calendar.DATE, 7 - c.get(Calendar.DAY_OF_WEEK));
		return new Date(c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE), c
			.get(Calendar.YEAR));
	}

	/**
	 * get month start date
	 * @param date date to get info from
	 * @return start of month date
	 */
	public static Date getMonthStart(Date date)
	{
		return new Date(date.getMonth(), 1, date.getYear());
	}

	/**
	 * get month end date
	 * @param date date to get info from
	 * @return end of month date
	 */
	public static Date getMonthEnd(Date date)
	{
		Calendar c =
			new GregorianCalendar(date.getYear(), date.getMonth() - 1, date
				.getDay());
		return new Date(date.getMonth(), c
			.getActualMaximum(Calendar.DAY_OF_MONTH), date.getYear());
	}

	/**
	 * get offset number of days that shows up in each month of a calendar.
	 * e.g. January's offset is 4 days.
	 * @param month month to get offset
	 * @param year year required because leapyear is considered
	 * @return offset num of days
	 */
	public static int getOffsetDaysInMonth(int month, int year)
	{
		GregorianCalendar cal = new GregorianCalendar(year, month - 1, 1);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * tests if this date is before other date.
	 * @param d2 other date
	 * @return true if before, false if not
	 */
	public boolean before(Date d2)
	{
		return this.compareTo(d2) < 0;
	}

	/**
	 * tests if this date is after other date.
	 * @param d2 other date
	 * @return true if before, false if not
	 */
	public boolean after(Date d2)
	{
		return this.compareTo(d2) > 0;
	}

	/**
	 * tests if this date equals other date.
	 * @param d2 other date
	 * @return true if same, false if not
	 */
	public boolean equals(Date d2)
	{
		return this.compareTo(d2) == 0;
	}

	public int getDayOfWeek()
	{
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
}
