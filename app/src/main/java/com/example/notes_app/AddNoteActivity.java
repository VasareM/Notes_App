package com.example.notes_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    private EditText edNoteContent;
    private EditText edNoteTitle;
    private StorageType storageType;
    private SharedPrefsHelper prefsHelper;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("AddNoteActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        edNoteTitle = findViewById(R.id.edNoteTitle);
        edNoteContent = findViewById(R.id.edNoteContent);
        prefsHelper = new SharedPrefsHelper(this);
        dbHelper = new DatabaseHelper(this);

        String storage = getIntent().getStringExtra("storage");
        storageType = StorageType.valueOf(storage);
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
