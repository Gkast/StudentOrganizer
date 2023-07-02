package com.example.studentorganizer.Tools;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentorganizer.R;

public final class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    private Cursor cursor;
    private OnItemClickListener listener;

    public EventAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    public interface OnItemClickListener {
        void onItemClicked(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView lessonNameText;
        public TextView professorNameText;
        public TextView selectedDayText;
        public TextView startHourText;
        public TextView startMinuteText;
        public TextView endHourText;
        public TextView endMinuteText;

        public EventViewHolder(@NonNull final View itemView) {
            super(itemView);

            lessonNameText = itemView.findViewById(R.id.lesson_name_item);
            professorNameText = itemView.findViewById(R.id.professor_name_item);
            selectedDayText = itemView.findViewById(R.id.lesson_day_item);
            startHourText = itemView.findViewById(R.id.start_hour_item);
            startMinuteText = itemView.findViewById(R.id.start_minute_item);
            endHourText = itemView.findViewById(R.id.end_hour_item);
            endMinuteText = itemView.findViewById(R.id.end_minute_item);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onItemClicked(v, getLayoutPosition());
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        if (!cursor.moveToPosition(position))
            return;

        long id = cursor.getLong(cursor.getColumnIndex(EventContract.EventEntry._ID));
        String lesson = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_LESSON));
        String professor = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_PROFESSOR));
        int selectedDay = cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_SELECTED_DAY));
        String startHour = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_START_HOUR));
        String startMinute = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_START_MINUTE));
        String endHour = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_END_HOUR));
        String endMinute = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_END_MINUTE));

        holder.itemView.setTag(id);
        holder.lessonNameText.setText(lesson);
        holder.professorNameText.setText(professor);
        holder.selectedDayText.setText(DayConverter.numberToString(selectedDay));
        holder.startHourText.setText(startHour);
        holder.startMinuteText.setText(startMinute);
        holder.endHourText.setText(endHour);
        holder.endMinuteText.setText(endMinute);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null)
            cursor.close();

        cursor = newCursor;

        if (newCursor != null)
            notifyDataSetChanged();
    }
}
