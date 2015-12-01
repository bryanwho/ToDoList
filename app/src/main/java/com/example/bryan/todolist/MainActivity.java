package com.example.bryan.todolist;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private ListView listView;
    private RealmResults<Task> tasks;
    private TaskRowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getInstance(this);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemLongClickListener(onItemLongClickListener);
        listView.setAdapter(adapter);

        populateListView();
    }

    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            realm.beginTransaction();
            tasks.remove(position);
            realm.commitTransaction();
            adapter.notifyDataSetChanged();
            return false;
        }
    };

    private void saveTask(String message) {

        realm.beginTransaction();
        Task task = realm.createObject(Task.class);
        task.setNote(message);
        realm.commitTransaction();

        startAlarm();
        populateListView();
    }

    private void populateListView() {

        tasks = realm.where(Task.class).findAll();

        if(tasks != null) {
            adapter = new TaskRowAdapter(this, tasks);
            listView.setAdapter(adapter);
        }
    }

    private void startAlarm() {//TODO pass in Task Object
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        Calendar calendar =  new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), 10, 30, 20, 8);

//        Calendar calendar = Calendar.getInstance();

        Log.d("flow","ERA: " + calendar.get(Calendar.ERA));
        Log.d("flow", "YEAR: " + calendar.get(Calendar.YEAR));
        Log.d("flow", "MONTH: " + calendar.get(Calendar.MONTH));
        Log.d("flow", "WEEK_OF_YEAR: " + calendar.get(Calendar.WEEK_OF_YEAR));
        Log.d("flow", "WEEK_OF_MONTH: " + calendar.get(Calendar.WEEK_OF_MONTH));
        Log.d("flow", "DATE: " + calendar.get(Calendar.DATE));
        Log.d("flow", "DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
        Log.d("flow", "DAY_OF_YEAR: " + calendar.get(Calendar.DAY_OF_YEAR));
        Log.d("flow", "DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
        Log.d("flow", "DAY_OF_WEEK_IN_MONTH: "
                + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        Log.d("flow", "AM_PM: " + calendar.get(Calendar.AM_PM));
        Log.d("flow", "HOUR: " + calendar.get(Calendar.HOUR));
        Log.d("flow", "HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
        Log.d("flow", "MINUTE: " + calendar.get(Calendar.MINUTE));
        Log.d("flow", "SECOND: " + calendar.get(Calendar.SECOND));
        Log.d("flow", "MILLISECOND: " + calendar.get(Calendar.MILLISECOND));


        Log.d("flow","CurrentTime: " + calendar.get(Calendar.HOUR_OF_DAY));

//        calendar.add(Calendar.MINUTE, 1);
//
        Log.d("flow","Time set to: " + calendar.get(Calendar.HOUR_OF_DAY));

        long when = calendar.getTimeInMillis();         // notification time
        Intent intent = new Intent(this, ReminderService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC, when, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC, when, pendingIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addTask) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Create Task");
            builder.setMessage("What do you want to do?");
            final EditText inputField = new EditText(this);
            builder.setView(inputField);
            builder.setNegativeButton("Cancel", null);
            builder.setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //code inside this method will be executed whenever the positive
                    //button is clicked
                    saveTask(inputField.getText().toString());

                }
            });

            builder.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
