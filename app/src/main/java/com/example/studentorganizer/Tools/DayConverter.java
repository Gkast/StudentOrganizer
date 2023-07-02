package com.example.studentorganizer.Tools;

public final class DayConverter {

    public static String numberToString(int numDay) {
        String day;

        switch (numDay) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            default:
                day = "Saturday";
        }

        return day;
    }

    public static int stringToNumber(String stringDay) {
        int day;

        switch (stringDay) {
            case "Sunday":
                day = 1;
                break;
            case "Monday":
                day = 2;
                break;
            case "Tuesday":
                day = 3;
                break;
            case "Wednesday":
                day = 4;
                break;
            case "Thursday":
                day = 5;
                break;
            case "Friday":
                day = 6;
                break;
            default:
                day = 7;
        }

        return day;
    }
}
