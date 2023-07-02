package com.example.studentorganizer.Tools;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public final class InputEventValidator {

    private TextInputLayout textInputLayoutLesson;
    private TextInputLayout textInputLayoutProfessor;
    private TextInputLayout textInputLayoutStartTime;
    private TextInputLayout textInputLayoutEndTime;
    private TextView dayTextLabel;
    private TextView reminderTextLabel;
    private TextView daySpinnerErrorMessage;
    private TextView reminderSpinnerErrorMessage;
    private String selectedDay;
    private String selectedReminderHour;

    public InputEventValidator(TextInputLayout textInputLayoutLesson,
                               TextInputLayout textInputLayoutProfessor,
                               TextInputLayout textInputLayoutStartTime,
                               TextInputLayout textInputLayoutEndTime,
                               TextView dayTextLabel, TextView reminderTextLabel,
                               TextView daySpinnerErrorMessage,
                               TextView reminderSpinnerErrorMessage, String selectedDay,
                               String selectedReminderHour) {
        this.textInputLayoutLesson = textInputLayoutLesson;
        this.textInputLayoutProfessor = textInputLayoutProfessor;
        this.textInputLayoutStartTime = textInputLayoutStartTime;
        this.textInputLayoutEndTime = textInputLayoutEndTime;
        this.dayTextLabel = dayTextLabel;
        this.reminderTextLabel = reminderTextLabel;
        this.daySpinnerErrorMessage = daySpinnerErrorMessage;
        this.reminderSpinnerErrorMessage = reminderSpinnerErrorMessage;
        this.selectedDay = selectedDay;
        this.selectedReminderHour = selectedReminderHour;
    }

    public boolean validateInputs() {
        return (!validateLesson() | !validateProfessor() | !validateDay()
                | !validateStartTime() | !validateEndTime() | !validateReminder());
    }

    private boolean validateLesson() {
        String lessonInput = textInputLayoutLesson.getEditText().getText().toString().trim();

        if (lessonInput.isEmpty()) {
            textInputLayoutLesson.setError("Type the name of the lesson!");
            return false;
        } else {
            textInputLayoutLesson.setError(null);
            return true;
        }
    }

    private boolean validateProfessor() {
        String professorInput = textInputLayoutProfessor.getEditText().getText().toString().trim();

        if (professorInput.isEmpty()) {
            textInputLayoutProfessor.setError("Type the name of the professor!");
            return false;
        } else {
            textInputLayoutProfessor.setError(null);
            return true;
        }
    }

    private boolean validateDay() {
        if (selectedDay.equals("Select Day")) {
            daySpinnerErrorMessage.setVisibility(View.VISIBLE);
            dayTextLabel.setTextColor(Color.RED);
            return false;
        } else {
            daySpinnerErrorMessage.setVisibility(View.INVISIBLE);
            dayTextLabel.setTextColor(Color.BLACK);
            return true;
        }
    }

    private boolean validateStartTime() {
        String startTimeInput = textInputLayoutStartTime.getEditText().getText().toString().trim();

        if (startTimeInput.isEmpty()) {
            textInputLayoutStartTime.setError("Pick a time!");
            return false;
        } else {
            textInputLayoutStartTime.setError(null);
            return true;
        }
    }

    private boolean validateEndTime() {
        String lessonInput = textInputLayoutEndTime.getEditText().getText().toString().trim();

        if (lessonInput.isEmpty()) {
            textInputLayoutEndTime.setError("Pick a time!");
            return false;
        } else {
            textInputLayoutEndTime.setError(null);
            return true;
        }
    }

    private boolean validateReminder() {
        if (selectedReminderHour.equals("Select Hours")) {
            reminderSpinnerErrorMessage.setVisibility(View.VISIBLE);
            reminderTextLabel.setTextColor(Color.RED);
            return false;
        } else {
            reminderSpinnerErrorMessage.setVisibility(View.INVISIBLE);
            reminderTextLabel.setTextColor(Color.BLACK);
            return true;
        }
    }
}
