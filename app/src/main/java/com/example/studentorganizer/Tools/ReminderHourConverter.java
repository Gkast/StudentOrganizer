package com.example.studentorganizer.Tools;

public final class ReminderHourConverter {

    public static int setReminderHour(int startHour, String stringReminderHour) {
        int reminderHour = convertString(stringReminderHour);

        if (reminderHour > startHour) {
            reminderHour = 24 - (reminderHour - startHour);
        } else {
            reminderHour = startHour - reminderHour;
        }

        return reminderHour;
    }

    public static int convertString(String stringReminderHour){
        int reminderHour;

        switch (stringReminderHour) {
            case "1 Hour Before":
                reminderHour = 1;
                break;
            case "2 Hours Before":
                reminderHour = 2;
                break;
            case "3 Hours Before":
                reminderHour = 3;
                break;
            case "4 Hours Before":
                reminderHour = 4;
                break;
            case "5 Hours Before":
                reminderHour = 5;
                break;
            case "6 Hours Before":
                reminderHour = 6;
                break;
            case "7 Hours Before":
                reminderHour = 7;
                break;
            default:
                reminderHour = 8;
        }

        return reminderHour;
    }
}
