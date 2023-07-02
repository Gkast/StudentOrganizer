package com.example.studentorganizer.Tools;

import android.provider.BaseColumns;

public final class EventContract {

    private EventContract() {
    }

    public static final class EventEntry implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_LESSON = "lesson";
        public static final String COLUMN_PROFESSOR = "professor";
        public static final String COLUMN_SELECTED_DAY = "selectedDay";
        public static final String COLUMN_START_HOUR = "startHour";
        public static final String COLUMN_START_MINUTE = "startMinute";
        public static final String COLUMN_END_HOUR = "endHour";
        public static final String COLUMN_END_MINUTE = "endMinute";
        public static final String COLUMN_SELECTED_REMINDER_HOUR = "selectedReminderHour";
        public static final String COLUMN_REMINDER_ID = "reminderId";
    }
}
