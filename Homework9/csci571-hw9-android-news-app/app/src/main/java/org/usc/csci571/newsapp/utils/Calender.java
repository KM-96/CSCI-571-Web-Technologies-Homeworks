package org.usc.csci571.newsapp.utils;

public class Calender {
    private static class Constants {
        static String JAN = "Jan";
        static String FEB = "Feb";
        static String MAR = "Mar";
        static String APR = "Apr";
        static String MAY = "May";
        static String JUN = "Jun";
        static String JUL = "Jul";
        static String AUG = "Aug";
        static String SEP = "Sep";
        static String OCT = "Oct";
        static String NOV = "Nov";
        static String DEC = "Dec";
    }

    public static String getMonthName(int month) {
        String monthName = "";
        switch (month) {
            case 1:
                monthName = Constants.JAN;
                break;
            case 2:
                monthName = Constants.FEB;
                break;
            case 3:
                monthName = Constants.MAR;
                break;
            case 4:
                monthName = Constants.APR;
                break;
            case 5:
                monthName = Constants.MAY;
                break;
            case 6:
                monthName = Constants.JUN;
                break;
            case 7:
                monthName = Constants.JUL;
                break;
            case 8:
                monthName = Constants.AUG;
                break;
            case 9:
                monthName = Constants.SEP;
                break;
            case 10:
                monthName = Constants.OCT;
                break;
            case 11:
                monthName = Constants.NOV;
                break;
            case 12:
                monthName = Constants.DEC;
                break;
        }
        return monthName;
    }
}
