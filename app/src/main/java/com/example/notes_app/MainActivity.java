package com.example.notes_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvNotes;
    private StorageType currentStorage = StorageType.SHARED_PREFS;
    private SharedPrefsHelper prefsHelper;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvNotes = findViewById(R.id.lvNotes);
        Button btnAddNote = findViewById(R.id.btnAddNote);
        Button btnDeleteNote = findViewById(R.id.btnDeleteNote);

        prefsHelper = new SharedPrefsHelper(this);
        dbHelper = new DatabaseHelper(this);

        btnAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNoteActivity.class);
            startActivity(intent);
        });

        btnDeleteNote.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeleteNoteActivity.class);
            startActivity(intent);
        });

        lvNotes.setOnItemClickListener((parent, view, position, id) -> {
            Note note = (Note) parent.getItemAtPosition(position);
            Toast.makeText(this, note.getName(), Toast.LENGTH_LONG).show();
        });

        refreshNotesList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MainActivity", "onCreateOptionsMenu called");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MainActivity", "onOptionsItemSelected called");
        int id = item.getItemId();
        if (id == R.id.menu_add) {
            startActivity(new Intent(this, AddNoteActivity.class)
                    .putExtra("storage", currentStorage.name()));
        } else if (id == R.id.menu_delete) {
            startActivity(new Intent(this, DeleteNoteActivity.class)
                    .putExtra("storage", currentStorage.name()));
        } else if (id == R.id.menu_storage) {
            currentStorage = (currentStorage == StorageType.SHARED_PREFS)
                    ? StorageType.SQLITE : StorageType.SHARED_PREFS;
            refreshNotesList();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNotesList();
    }

    private void refreshNotesList() {
        Log.d("MainActivity", "refreshNotesList called");
        ArrayList<Note> notes = (currentStorage == StorageType.SHARED_PREFS)
                ? prefsHelper.getAllNotes() : dbHelper.getAllNotes();

        ArrayAdapter<Note> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, notes);
        lvNotes.setAdapter(adapter);
        setTitle("Storage: " + currentStorage.name());
    }
}