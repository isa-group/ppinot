package es.us.isa.ppinot.model;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;

/**
 * Schedule
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class Schedule implements Cloneable{

    private int beginDay;
    private int endDay;
    private LocalTime beginTime;
    private LocalTime endTime;

    public Schedule(int beginDay, int endDay, LocalTime beginTime, LocalTime endTime) {
        this.beginDay = beginDay;
        this.endDay = endDay;
        this.beginTime = beginTime;
        this.endTime = endTime;

        if (beginDay < DateTimeConstants.MONDAY || beginDay > DateTimeConstants.SUNDAY || endDay < DateTimeConstants.MONDAY || endDay > DateTimeConstants.SUNDAY) {
            throw new RuntimeException("Invalid day of week");
        }
    }

    public int getBeginDay() {
        return beginDay;
    }

    public int getEndDay() {
        return endDay;
    }

    public LocalTime getBeginTime() {
        return beginTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean dayOfWeekExcluded(int dayOfWeek) {
        return (dayOfWeek < getBeginDay() || dayOfWeek > getEndDay());
    }
    
    public Schedule clone(){
    	
    	final Schedule clone;
    	
    	try{
    		
    		clone = (Schedule)super.clone();
    		//los valores "int" y "localTime" no están clonados.
    		return clone;
    		
    	}catch(Exception e){
    		System.out.println("\t!>>>> Excepción en Schedule - clone()\n~~~" + e.getMessage());
    		return null;
    	}
    	
    }
}
