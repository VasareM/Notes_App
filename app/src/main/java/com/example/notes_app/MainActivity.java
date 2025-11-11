package com.example.notes_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
        Spinner spStorageMain = findViewById(R.id.spStorageMain);

        prefsHelper = new SharedPrefsHelper(this);
        dbHelper = new DatabaseHelper(this);

        ArrayAdapter<String> storageAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"SHARED_PREFS", "SQLITE"});
        storageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStorageMain.setAdapter(storageAdapter);

        spStorageMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentStorage = StorageType.valueOf((String) parent.getItemAtPosition(position));
                refreshNotesList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnAddNote.setOnClickListener(v -> {
            startActivity(new Intent(this, AddNoteActivity.class));
        });

        btnDeleteNote.setOnClickListener(v -> {
            startActivity(new Intent(this, DeleteNoteActivity.class));
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

        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, notes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);
                Note note = getItem(position);
                text1.setText(note.getName());
                text2.setText(note.getContent());
                return view;
            }
        };
        lvNotes.setAdapter(adapter);
        setTitle("Storage: " + currentStorage.name());
    }
}