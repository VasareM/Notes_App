package com.example.notes_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DeleteNoteActivity extends AppCompatActivity {

    private Spinner spNotes;
    private Spinner spStorageDel;
    private StorageType selectedStorage;
    private StorageType storageType;
    private SharedPrefsHelper prefsHelper;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        spStorageDel = findViewById(R.id.spStorageDel);
        spNotes = findViewById(R.id.spNotes);

        prefsHelper = new SharedPrefsHelper(this);
        dbHelper = new DatabaseHelper(this);

        // Populate storage spinner
        ArrayAdapter<String> storageAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"SHARED_PREFS", "SQLITE"});
        storageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStorageDel.setAdapter(storageAdapter);
        spStorageDel.setSelection(0);

        spStorageDel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStorage = StorageType.valueOf((String) parent.getItemAtPosition(position));
                loadNotes(); // refresh notes for the selected storage
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedStorage = StorageType.SHARED_PREFS;
                loadNotes();
            }
        });
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
