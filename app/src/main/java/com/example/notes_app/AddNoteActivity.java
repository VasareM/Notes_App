package com.example.notes_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    private EditText edNoteContent;
    private EditText edNoteTitle;
    private Spinner spStorage;
    private StorageType selectedStorage;
    private StorageType storageType;
    private SharedPrefsHelper prefsHelper;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        spStorage = findViewById(R.id.spStorage);
        edNoteTitle = findViewById(R.id.edNoteTitle);
        edNoteContent = findViewById(R.id.edNoteContent);

        prefsHelper = new SharedPrefsHelper(this);
        dbHelper = new DatabaseHelper(this);

        // Populate storage spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"SHARED_PREFS", "SQLITE"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStorage.setAdapter(adapter);

        // Default selection (optional)
        spStorage.setSelection(0);

        spStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStorage = StorageType.valueOf((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedStorage = StorageType.SHARED_PREFS;
            }
        });
    }

    public void onBtnSaveAndCloseClick(View view) {
        Log.d("AddNoteActivity", "onBtnSaveAndCloseClick called");
        String content = edNoteContent.getText().toString().trim();
        String title = edNoteTitle.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Title can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.isEmpty()) {
            Toast.makeText(this, "Fill in the content", Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note(title, content);

        if (storageType == StorageType.SHARED_PREFS) {
            prefsHelper.saveNote(note);
        } else {
            dbHelper.addNote(note);
        }

        Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
