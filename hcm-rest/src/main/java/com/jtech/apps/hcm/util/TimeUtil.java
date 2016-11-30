package com.jtech.apps.hcm.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtil {
	
	public static String getTimeStamp(){
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
		return timeStamp;
	}

}
