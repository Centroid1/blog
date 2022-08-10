package com.blog.domain;

import java.time.LocalDateTime;

public class Timer {
	static String getTime() {
		LocalDateTime now = LocalDateTime.now();
		
		int year = now.getYear();
		int month = now.getMonthValue();
		int day = now.getDayOfMonth();
		int hour = now.getHour();
		int minute = now.getMinute();
		int second = now.getSecond();
		
		String answer = new String("");
		answer += String.valueOf(year);
		
		answer += "-";
		if(month < 10) answer += "0";
		answer += String.valueOf(month);
		
		answer += "-";
		if(day < 10) answer += "0";
		answer += String.valueOf(day);
		
		answer += " ";
		if(hour < 10) answer += "0";
		answer += String.valueOf(hour);
		
		answer += ":";
		if(minute < 10) answer += "0";
		answer += String.valueOf(minute);
		
		answer += ":";
		if(second < 10) answer += "0";
		answer += String.valueOf(second);
		
		return answer;
	}
}
