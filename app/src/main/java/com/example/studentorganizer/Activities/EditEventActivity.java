package com.example.studentorganizer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.studentorganizer.Fragments.EditEventFragment;
import com.example.studentorganizer.Fragments.EventsFragment;
import com.example.studentorganizer.Fragments.HomeFragment;
import com.example.studentorganizer.R;
import com.example.studentorganizer.Tools.EventContract;
import com.example.studentorganizer.Tools.EventDBHelper;

public final class EditEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        getSupportActionBar().setTitle("Edit Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        long id = intent.getLongExtra(EventsFragment.EXTRA_ID, 0);

        Bundle bundle = new Bundle();
        bundle.putString("id", String.valueOf(id));
        EditEventFragment editEventFragment = new EditEventFragment();
        editEventFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_edit_container,
                editEventFragment).commit();
    }
}