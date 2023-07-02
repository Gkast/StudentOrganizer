package com.example.studentorganizer.Fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studentorganizer.R;
import com.example.studentorganizer.Tools.AlertReceiver;
import com.example.studentorganizer.Tools.DayConverter;
import com.example.studentorganizer.Tools.EventContract;
import com.example.studentorganizer.Tools.EventDBHelper;
import com.example.studentorganizer.Tools.InputEventValidator;
import com.example.studentorganizer.Tools.ReminderHourConverter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Random;

public final class AddEventFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentAddEventListener listener;
    private TextInputLayout textInputLayoutLesson;
    private TextInputLayout textInputLayoutProfessor;
    private TextInputLayout textInputLayoutStartTime;
    private TextInputLayout textInputLayoutEndTime;
    private TextInputEditText textInputStartTime;
    private TextInputEditText textInputEndTime;
    private Spinner daySpinner;
    private Spinner reminderSpinner;
    private TextView dayTextLabel;
    private TextView daySpinnerErrorMessage;
    private String selectedDay;
    private TextView reminderTextLabel;
    private TextView reminderSpinnerErrorMessage;
    private String selectedReminderHour;
    private SQLiteDatabase database;
    private int reminderId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        EventDBHelper dbHelper = new EventDBHelper(getContext());
        database = dbHelper.getWritableDatabase();

        textInputLayoutLesson = view.findViewById(R.id.lesson_name);
        textInputLayoutProfessor = view.findViewById(R.id.professor_name);
        textInputLayoutStartTime = view.findViewById(R.id.start_time_layout);
        textInputLayoutEndTime = view.findViewById(R.id.end_time_layout);
        textInputStartTime = view.findViewById(R.id.start_time);
        textInputStartTime.setInputType(InputType.TYPE_NULL);
        textInputEndTime = view.findViewById(R.id.end_time);
        textInputEndTime.setInputType(InputType.TYPE_NULL);

        daySpinner = view.findViewById(R.id.day_spinner);
        final ArrayAdapter<CharSequence> daySpinnerAdapter =
                ArrayAdapter.createFromResource(getContext(), R.array.days,
                        android.R.layout.simple_spinner_item);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(daySpinnerAdapter);
        daySpinner.setOnItemSelectedListener(this);
        dayTextLabel = view.findViewById(R.id.day_text_label);
        daySpinnerErrorMessage = view.findViewById(R.id.day_spinner_error_message);

        reminderSpinner = view.findViewById(R.id.reminder_spinner);
        final ArrayAdapter<CharSequence> reminderSpinnerAdapter =
                ArrayAdapter.createFromResource(getContext(), R.array.reminder_hours,
                        android.R.layout.simple_spinner_item);
        reminderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderSpinner.setAdapter(reminderSpinnerAdapter);
        reminderSpinner.setOnItemSelectedListener(this);
        reminderTextLabel = view.findViewById(R.id.reminder_text_label);
        reminderSpinnerErrorMessage = view.findViewById(R.id.reminder_spinner_error_message);


        textInputStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(textInputStartTime);
            }
        });

        textInputEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(textInputEndTime);
            }
        });

        Button addEventButton = view.findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                if (new InputEventValidator(textInputLayoutLesson, textInputLayoutProfessor,
                        textInputLayoutStartTime, textInputLayoutEndTime, dayTextLabel, reminderTextLabel,
                        daySpinnerErrorMessage, reminderSpinnerErrorMessage, selectedDay,
                        selectedReminderHour).validateInputs()) {
                    return;
                }
                addEvent();
                clearInputs(daySpinnerAdapter, reminderSpinnerAdapter);
                Toast.makeText(getContext(), "Event Added", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void addEvent() {
        String lesson = textInputLayoutLesson.getEditText().getText().toString();
        String professor = textInputLayoutProfessor.getEditText().getText().toString();
        int selectedDay = DayConverter.stringToNumber(this.selectedDay);
        String startHour = breakTime("h", textInputLayoutStartTime.getEditText().getText().toString());
        String startMinute = breakTime("m", textInputLayoutStartTime.getEditText().getText().toString());
        String endHour = breakTime("h", textInputLayoutEndTime.getEditText().getText().toString());
        String endMinute = breakTime("m", textInputLayoutEndTime.getEditText().getText().toString());
        int selectedReminderHour = ReminderHourConverter.convertString(this.selectedReminderHour);
        reminderId = new Random().nextInt(10000);

        ContentValues contentValues = new ContentValues();
        contentValues.put(EventContract.EventEntry.COLUMN_LESSON, lesson);
        contentValues.put(EventContract.EventEntry.COLUMN_PROFESSOR, professor);
        contentValues.put(EventContract.EventEntry.COLUMN_SELECTED_DAY, selectedDay);
        contentValues.put(EventContract.EventEntry.COLUMN_START_HOUR, startHour);
        contentValues.put(EventContract.EventEntry.COLUMN_START_MINUTE, startMinute);
        contentValues.put(EventContract.EventEntry.COLUMN_END_HOUR, endHour);
        contentValues.put(EventContract.EventEntry.COLUMN_END_MINUTE, endMinute);
        contentValues.put(EventContract.EventEntry.COLUMN_SELECTED_REMINDER_HOUR, selectedReminderHour);
        contentValues.put(EventContract.EventEntry.COLUMN_REMINDER_ID, reminderId);

        database.insert(EventContract.EventEntry.TABLE_NAME, null, contentValues);

        int reminderHour = ReminderHourConverter.setReminderHour(Integer.parseInt(startHour), this.selectedReminderHour);
        int selectedReminderDay = setReminderDay(selectedDay, reminderHour, Integer.parseInt(startHour));
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.set(Calendar.DAY_OF_WEEK, selectedReminderDay);
        alarmCalendar.set(Calendar.HOUR_OF_DAY, reminderHour);
        alarmCalendar.set(Calendar.MINUTE, Integer.parseInt(startMinute));
        alarmCalendar.set(Calendar.SECOND, 0);
        startAlarm(alarmCalendar);
    }

    private void clearInputs(ArrayAdapter<CharSequence> daySpinnerAdapter, ArrayAdapter<CharSequence> reminderSpinnerAdapter) {
        textInputLayoutLesson.getEditText().getText().clear();
        textInputLayoutProfessor.getEditText().getText().clear();
        daySpinner.setSelection(daySpinnerAdapter.getPosition("Select Day"));
        textInputLayoutStartTime.getEditText().getText().clear();
        textInputLayoutEndTime.getEditText().getText().clear();
        daySpinnerErrorMessage.setVisibility(getView().INVISIBLE);
        dayTextLabel.setTextColor(Color.BLACK);
        reminderSpinner.setSelection(reminderSpinnerAdapter.getPosition("Select Hours"));
        reminderSpinnerErrorMessage.setVisibility(getView().INVISIBLE);
        reminderTextLabel.setTextColor(Color.BLACK);
    }

    private void timePicker(final TextInputEditText textInputEditText) {
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
                        textInputEditText.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()));
                    }
                }, hour, minutes, true);
        picker.show();
    }

    private String breakTime(String type, String inputTime) {
        String time;

        if (type.equals("h"))
            time = String.valueOf(inputTime.charAt(0)) + inputTime.charAt(1);
        else
            time = String.valueOf(inputTime.charAt(3)) + inputTime.charAt(4);

        return time;
    }

    private int setReminderDay(int selectedDay, int reminderHour, int startHour) {
        if (reminderHour > startHour)
            if (selectedDay == 1)
                selectedDay = 7;
            else
                selectedDay = selectedDay - 1;

        return selectedDay;
    }

    private void startAlarm(Calendar alarmCalendar) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        String message = "You have " + textInputLayoutLesson.getEditText().getText() +
                " at " + textInputLayoutStartTime.getEditText().getText();
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        intent.putExtra("message", message);
        intent.putExtra("reminderId", reminderId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),
                reminderId, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }

    public interface FragmentAddEventListener {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentAddEventListener) {
            listener = (FragmentAddEventListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentAddEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner1 = (Spinner) parent;
        Spinner spinner2 = (Spinner) parent;
        if (spinner1.getId() == R.id.day_spinner) {
            selectedDay = parent.getItemAtPosition(position).toString();
        }
        if (spinner2.getId() == R.id.reminder_spinner) {
            selectedReminderHour = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
