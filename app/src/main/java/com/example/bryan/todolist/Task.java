package com.example.bryan.todolist;

import java.util.Calendar;

import io.realm.RealmObject;

/**
 * Created by bryan on 8/16/15.
 */
public class Task extends RealmObject {

    private String note;
    private Calendar alarm;

    public void setNote(String myNote) {
        note = myNote;
    }

    public String getNote() {
        return note;
    }
}
