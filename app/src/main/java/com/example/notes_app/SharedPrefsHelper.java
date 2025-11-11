package com.example.notes_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SharedPrefsHelper {
    private static final String PREFS_NAME = "NotesPrefs";
    private static final String KEY_NOTES = "Notes";
    private final SharedPreferences sharedPreferences;

    public SharedPrefsHelper(Context context) {
        Log.d("SharedPrefsHelper", "Constructor called");
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveNote(Note note) {
        Log.d("SharedPrefsHelper", "saveNote called");
        ArrayList<Note> notes = getAllNotes();
        notes.add(note);
        saveAll(notes);
    }

    public void deleteNoteByName(String name) {
        Log.d("SharedPrefsHelper", "deleteNoteByName called");
        ArrayList<Note> notes = getAllNotes();
        notes.removeIf(n -> n.getName().equals(name));
        saveAll(notes);
    }

    public ArrayList<Note> getAllNotes() {
        Log.d("SharedPrefsHelper", "getAllNotes called");
        ArrayList<Note> notes = new ArrayList<>();
        String json = sharedPreferences.getString(KEY_NOTES, "[]");

        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                notes.add(new Note(obj.getString("name"), obj.getString("content")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notes;
    }

    private void saveAll(ArrayList<Note> notes) {
        Log.d("SharedPrefsHelper", "saveAll called");
        JSONArray arr = new JSONArray();
        for (Note n : notes) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", n.getName());
                obj.put("content", n.getContent());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arr.put(obj);
        }
        sharedPreferences.edit().putString(KEY_NOTES, arr.toString()).apply();
    }
}