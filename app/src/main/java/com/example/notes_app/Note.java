package com.example.notes_app;

import android.util.Log;

public class Note {
    private String name;
    private String content;

    public Note(String name, String content) {
        Log.d("Note", "Constructor called");
        this.name = name;
        this.content = content;
    }

    public String getName() {
        Log.d("Note", "getName called");
        return name;
    }

    public String getContent() {
        Log.d("Note", "getContent called");
        return content;
    }

    @Override
    public String toString() {
        return getName() + ": " + getContent();
    }
}
