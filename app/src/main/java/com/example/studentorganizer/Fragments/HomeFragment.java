package com.example.studentorganizer.Fragments;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.studentorganizer.Activities.EditEventActivity;
import com.example.studentorganizer.R;
import com.example.studentorganizer.Tools.EventAdapter;
import com.example.studentorganizer.Tools.EventContract;
import com.example.studentorganizer.Tools.EventDBHelper;

import java.util.Calendar;

public final class HomeFragment extends Fragment {

    public static final String EXTRA_ID = "EXTRA_ID";
    private SQLiteDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        EventDBHelper dbHelper = new EventDBHelper(getContext());
        database = dbHelper.getReadableDatabase();
        RecyclerView recyclerView = view.findViewById(R.id.homeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        EventAdapter adapter = new EventAdapter(getContext(), getTodayItems());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, int position) {
                openEditActivity((long) v.getTag());
            }
        });
        adapter.swapCursor(getTodayItems());
        return view;
    }

    private void openEditActivity(long id) {
        Intent intent = new Intent(getContext(), EditEventActivity.class);
        intent.putExtra(EXTRA_ID, id);
        startActivity(intent);
    }

    private Cursor getTodayItems() {
        final Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        return database.query(
                EventContract.EventEntry.TABLE_NAME,
                null, EventContract.EventEntry.COLUMN_SELECTED_DAY + " = " + today,
                null, null, null, EventContract.EventEntry.COLUMN_START_HOUR
                        + "," + EventContract.EventEntry.COLUMN_START_MINUTE);
    }
}
