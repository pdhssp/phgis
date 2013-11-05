/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.ejb;

import gov.sp.health.data.Sex;
import gov.sp.health.data.Title;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Singleton;
import java.util.TimeZone;

/**
 *
 *
 */
@Singleton
public class CommonFunctions {

    public static Date guessDob(String docStr) {
        System.out.println("year string is " + docStr);
        try {
            int years = Integer.valueOf(docStr);
            System.out.println("int year is " + years);
            Calendar now = Calendar.getInstance(TimeZone.getTimeZone("IST"));
            System.out.println("now before is " + now);
            now.add(Calendar.YEAR, -years);
            System.out.println("now after is " + now);
            System.out.println("now time is " + now.getTime());
            return now.getTime();
        } catch (Exception e) {
            System.out.println("Error is " + e.getMessage());
            return Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime();

        }
    }

    public static Date firstDateOfYear(Date dateConcern) {
        Calendar oc = Calendar.getInstance();
        Calendar ic = Calendar.getInstance();
        ic.setTime(dateConcern);
        oc.set(Calendar.YEAR, ic.get(Calendar.YEAR));
        oc.set(Calendar.MONTH , 0);
        oc.set(Calendar.DAY_OF_MONTH  , 1);
        return getStartOfDay(oc.getTime());
    }
    
      public static Date lastDateOfYear(Date dateConcern) {
        Calendar oc = Calendar.getInstance();
        Calendar ic = Calendar.getInstance();
        ic.setTime(dateConcern);
        oc.set(Calendar.YEAR, ic.get(Calendar.YEAR));
        oc.set(Calendar.MONTH , 11);
        oc.set(Calendar.DAY_OF_MONTH  , 31);
        return getEndOfDay(oc.getTime());
    }

    public String calculateAge(Date dob, Date toDate) {
        if (dob == null || toDate == null) {
            return "";
        }
        long ageInDays;
        ageInDays = (toDate.getTime() - dob.getTime()) / (1000 * 60 * 60 * 24);
        System.out.println("Age in days " + ageInDays);
        if (ageInDays < 60) {
            return ageInDays + " days";
        } else if (ageInDays < 366) {
            return (ageInDays / 30) + " months";
        } else {
            return (ageInDays / 365) + " years";
        }
    }

    public long calculateAgeInDays(Date dob, Date toDate) {
        if (dob == null || toDate == null) {
            return 0l;
        }
        long ageInDays;
        ageInDays = (toDate.getTime() - dob.getTime()) / (1000 * 60 * 60 * 24);
        return ageInDays;
    }

    public long calculateDurationTime(Date dob, Date toDate) {
        if (dob == null || toDate == null) {
            return 0;
        }
        long durationHours;
        durationHours = (toDate.getTime() - dob.getTime()) / (1000 * 60 * 60);
        return durationHours;
    }

    public long calculateDurationMin(Date dob, Date toDate) {
        if (dob == null || toDate == null || dob.getTime() > toDate.getTime()) {
            return 0;
        }
        long durationHours;
        durationHours = (toDate.getTime() - dob.getTime()) / (1000 * 60);
        return durationHours;
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
//        calendar.set(year, month, day+1, 0, 0, 0);
//        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    public static Date removeTime(Date date) {
        Calendar cal;
        cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Boolean checkAgeSex(Date dob, Sex sex, Title title) {
        Boolean result = true;
        Date toDate = Calendar.getInstance().getTime();

        long age;

        age = ((toDate.getTime() - dob.getTime()) / (1000 * 60 * 60 * 24)) / 365;

        if (title.toString().equals("Baby")) {
            if (age > 5) {
                result = false;
            }
        } else if (title.toString().equals("Master")) {
            if (age > 12) {
                result = false;
            }
        }


        if (title.toString().equals("Mrs")
                || title.toString().equals("Ms")
                || title.toString().equals("Miss")
                || title.toString().equals("DrMrs")
                || title.toString().equals("DrMs")
                || title.toString().equals("DrMiss")) {

            if (sex.toString().equals("Male")) {
                result = false;
            }
        }

        if (title.toString().equals("Mr")
                || title.toString().equals("Master")
                || title.toString().equals("Dr")) {

            if (sex.toString().equals("Female")) {
                result = false;
            }
        }


        return result;
    }
}
