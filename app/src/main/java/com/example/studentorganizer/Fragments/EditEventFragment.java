package com.example.studentorganizer.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studentorganizer.Activities.MainActivity;
import com.example.studentorganizer.R;
import com.example.studentorganizer.Tools.AlertReceiver;
import com.example.studentorganizer.Tools.DayConverter;
import com.example.studentorganizer.Tools.EventContract;
import com.example.studentorganizer.Tools.EventDBHelper;
import com.example.studentorganizer.Tools.ReminderHourConverter;

import java.text.DateFormat;
import java.util.Calendar;

public final class EditEventFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private long id;
    private int reminderId;
    private EditText editTextLessonName;
    private EditText editTextProfessorName;
    private Spinner daySpinner;
    private Spinner reminderSpinner;
    private EditText editTextStartTime;
    private EditText editTextEndTime;
    private SQLiteDatabase database;
    private String selectedDay;
    private String selectedReminderHours;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_events, container, false);
        String stringId = getArguments().getString("id");
        id = Long.parseLong(stringId);

        EventDBHelper dbHelper = new EventDBHelper(getContext());
        database = dbHelper.getReadableDatabase();


        editTextLessonName = view.findViewById(R.id.edit_lesson_name);
        editTextProfessorName = view.findViewById(R.id.edit_professor);
        daySpinner = view.findViewById(R.id.edit_lesson_day);
        final ArrayAdapter<CharSequence> daySpinnerAdapter =
                ArrayAdapter.createFromResource(getContext(), R.array.days,
                        android.R.layout.simple_spinner_item);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(daySpinnerAdapter);
        daySpinner.setOnItemSelectedListener(this);
        editTextStartTime = view.findViewById(R.id.edit_start_time);
        editTextEndTime = view.findViewById(R.id.edit_end_time);
        reminderSpinner = view.findViewById(R.id.edit_reminder_hour);
        final ArrayAdapter<CharSequence> reminderSpinnerAdapter =
                ArrayAdapter.createFromResource(getContext(), R.array.reminder_hours,
                        android.R.layout.simple_spinner_item);
        reminderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderSpinner.setAdapter(reminderSpinnerAdapter);
        reminderSpinner.setOnItemSelectedListener(this);
        Button confirmButton = view.findViewById(R.id.edit_confirm_button);
        Button deleteButton = view.findViewById(R.id.edit_delete_button);

        editTextStartTime.setInputType(InputType.TYPE_NULL);
        editTextEndTime.setInputType(InputType.TYPE_NULL);

        Cursor cursor = getItem(id);
        setEditTexts(cursor);

        editTextStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(editTextStartTime);
            }
        });

        editTextEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(editTextEndTime);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem(id);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(id);
            }
        });

        return view;
    }

    private Cursor getItem(long id) {
        return database.query(
                EventContract.EventEntry.TABLE_NAME,
                null, EventContract.EventEntry._ID + " = " + id,
                null, null, null, null);
    }

    private void setEditTexts(Cursor cursor) {
        setLessonName(cursor);
        setProfessorName(cursor);
        setLessonDay(cursor);
        setStartTime(cursor);
        setEndTime(cursor);
        setReminderHour(cursor);
        setReminderId(cursor);
        cursor.close();
    }

    private void setLessonName(Cursor cursor) {
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String data = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_LESSON));
                editTextLessonName.setText(data);
                cursor.moveToNext();
            }
        }
    }

    private void setProfessorName(Cursor cursor) {
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String data = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_PROFESSOR));
                editTextProfessorName.setText(data);
                cursor.moveToNext();
            }
        }
    }

    private void setLessonDay(Cursor cursor) {
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                daySpinner.setSelection(cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_SELECTED_DAY)));
                cursor.moveToNext();
            }
        }
    }

    private void setStartTime(Cursor cursor) {
        String startHour = setStartHour(cursor);
        String startMinute = setStartMinute(cursor);
        editTextStartTime.setText(startHour + ":" + startMinute);
    }

    private void setEndTime(Cursor cursor) {
        String endHour = setEndHour(cursor);
        String endMinute = setEndMinute(cursor);
        editTextEndTime.setText(endHour + ":" + endMinute);
    }

    private String setStartHour(Cursor cursor) {
        String startHour = "";
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                startHour = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_START_HOUR));
                cursor.moveToNext();
            }
        }
        return startHour;
    }

    private String setStartMinute(Cursor cursor) {
        String startMinute = "";
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                startMinute = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_START_MINUTE));
                cursor.moveToNext();
            }
        }
        return startMinute;
    }

    private String setEndHour(Cursor cursor) {
        String endHour = "";
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                endHour = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_END_HOUR));
                cursor.moveToNext();
            }
        }
        return endHour;
    }

    private void setReminderHour(Cursor cursor) {
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                reminderSpinner.setSelection(cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_SELECTED_REMINDER_HOUR)));
                cursor.moveToNext();
            }
        }
    }

    private void setReminderId(Cursor cursor) {
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                reminderId = cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_REMINDER_ID));
                cursor.moveToNext();
            }
        }
    }

    private void timePicker(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        TimePickerDialog picker = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        editText.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()));
                    }
                }, hour, minutes, true);
        picker.show();
    }

    private String setEndMinute(Cursor cursor) {
        String endMinute = "";
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                endMinute = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_END_MINUTE));
                cursor.moveToNext();
            }
        }
        return endMinute;
    }

    private String breakTime(String type, String inputTime) {
        String time;

        if (type.equals("h"))
            time = String.valueOf(inputTime.charAt(0)) + inputTime.charAt(1);
        else
            time = String.valueOf(inputTime.charAt(3)) + inputTime.charAt(4);

        return time;
    }

    private void updateItem(long id) {
        String startHour = breakTime("h", String.valueOf(editTextStartTime.getText()));
        String startMinute = breakTime("m", String.valueOf(editTextStartTime.getText()));
        String endHour = breakTime("h", String.valueOf(editTextEndTime.getText()));
        String endMinute = breakTime("m", String.valueOf(editTextEndTime.getText()));
        int selectedDay = DayConverter.stringToNumber(this.selectedDay);

        ContentValues contentValues = new ContentValues();
        contentValues.put(EventContract.EventEntry.COLUMN_LESSON, String.valueOf(editTextLessonName.getText()));
        contentValues.put(EventContract.EventEntry.COLUMN_PROFESSOR, String.valueOf(editTextProfessorName.getText()));
        contentValues.put(EventContract.EventEntry.COLUMN_SELECTED_DAY, selectedDay);
        contentValues.put(EventContract.EventEntry.COLUMN_START_HOUR, startHour);
        contentValues.put(EventContract.EventEntry.COLUMN_START_MINUTE, startMinute);
        contentValues.put(EventContract.EventEntry.COLUMN_END_HOUR, endHour);
        contentValues.put(EventContract.EventEntry.COLUMN_END_MINUTE, endMinute);
        contentValues.put(EventContract.EventEntry.COLUMN_SELECTED_REMINDER_HOUR, ReminderHourConverter.convertString(selectedReminderHours));
        database.update(EventContract.EventEntry.TABLE_NAME, contentValues,
                EventContract.EventEntry._ID + "=" + id, null);

        int reminderHour = ReminderHourConverter.setReminderHour(Integer.parseInt(startHour), selectedReminderHours);
        int selectedReminderDay = setReminderDay(selectedDay, reminderHour, Integer.parseInt(startHour));
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.set(Calendar.DAY_OF_WEEK, selectedReminderDay);
        alarmCalendar.set(Calendar.HOUR_OF_DAY, reminderHour);
        alarmCalendar.set(Calendar.MINUTE, Integer.parseInt(startMinute));
        alarmCalendar.set(Calendar.SECOND, 0);
        startUpdatedAlarm(alarmCalendar);

        Toast.makeText(getContext(), "Event Changed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    private void deleteItem(long id) {
        database.delete(EventContract.EventEntry.TABLE_NAME,
                EventContract.EventEntry._ID + "=" + id, null);
        cancelAlarm();
        Toast.makeText(getContext(), "Event Deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    private int setReminderDay(int selectedDay, int reminderHour, int startHour) {
        if (reminderHour > startHour)
            if (selectedDay == 1)
                selectedDay = 7;
            else
                selectedDay = selectedDay - 1;

        return selectedDay;
    }

    private void startUpdatedAlarm(Calendar alarmCalendar) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        String message = "You have " + editTextLessonName.getText() + " at " + editTextStartTime.getText();
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        intent.putExtra("message", message);
        intent.putExtra("reminderId", reminderId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),
                reminderId, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), reminderId, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner1 = (Spinner) parent;
        Spinner spinner2 = (Spinner) parent;
        if (spinner1.getId() == R.id.edit_lesson_day) {
            selectedDay = parent.getItemAtPosition(position).toString();
        }
        if (spinner2.getId() == R.id.edit_reminder_hour) {
            selectedReminderHours = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
