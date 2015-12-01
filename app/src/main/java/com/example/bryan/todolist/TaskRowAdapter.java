package com.example.bryan.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import io.realm.RealmResults;

/**
 * Created by bryan on 8/16/15.
 */
public class TaskRowAdapter extends BaseAdapter {

    Context context;
    RealmResults<Task> tasks;

    public TaskRowAdapter(Context context, RealmResults<Task> tasks){
        this.context = context;
        this.tasks = tasks;
    }

    private static class ViewHolder {
        TextView user_task;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.task_row, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.user_task = (TextView) convertView.findViewById(R.id.user_task);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.user_task.setText(tasks.get(position).getNote());

        return convertView;
    }
}
