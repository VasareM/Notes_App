package com.example.notes_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DeleteNoteActivity extends AppCompatActivity {

    private Spinner spNotes;
    private StorageType storageType;
    private SharedPrefsHelper prefsHelper;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DeleteNoteActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        spNotes = findViewById(R.id.spNotes);
        prefsHelper = new SharedPrefsHelper(this);
        dbHelper = new DatabaseHelper(this);

        String storage = getIntent().getStringExtra("storage");
        storageType = StorageType.valueOf(storage);

        loadNotes();
    }

    private void loadNotes() {
        Log.d("DeleteNoteActivity", "loadNotes called");
        ArrayList<Note> notes = (storageType == StorageType.SHARED_PREFS)
                ? prefsHelper.getAllNotes() : dbHelper.getAllNotes();
        ArrayAdapter<Note> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, notes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNotes.setAdapter(adapter);
    }

    public void onDeleteClick(View view) {
        Log.d("DeleteNoteActivity", "onDeleteClick called");
        Note selected = (Note) spNotes.getSelectedItem();
        if (selected != null) {
            if (storageType == StorageType.SHARED_PREFS)
                prefsHelper.deleteNoteByName(selected.getName());
            else
                dbHelper.deleteNoteByName(selected.getName());
            Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show();
            loadNotes();
        }
    }
}
