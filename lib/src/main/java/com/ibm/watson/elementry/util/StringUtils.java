package com.ibm.watson.elementry.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StringUtils
{
	public static final DateFormat iso8601Format = initDateFormat();

	public static boolean isNotEmpty( final String s )
	{
		return null != s && s.length() > 0;
	}

	public static boolean isEmpty( final String s )
	{
		return null == s || s.length() == 0;
	}

	public static String toDateString( Date date )
	{
		return iso8601Format.format( date );
	}

	public static Date fromDateString( String date ) throws ParseException
	{
		return iso8601Format.parse( date );
	}

	private static DateFormat initDateFormat()
	{
		DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US );
		dateFormat.setTimeZone( TimeZone.getTimeZone( "UTC" ) );

		return  dateFormat;
	}

}
