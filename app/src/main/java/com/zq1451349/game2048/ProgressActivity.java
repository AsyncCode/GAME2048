package com.zq1451349.game2048;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ArrayList<ProgressInfo> progressInfoList = ProgressInfo.createList(this,
                new File(getFilesDir() + "/save"));
        if (progressInfoList != null && progressInfoList.size() != 0) {
            ListView listView = (ListView) findViewById(R.id.progressListView);
            ProgressAdapter progressAdapter = new ProgressAdapter(this, R.layout.progress_item, progressInfoList);
            listView.setAdapter(progressAdapter);
        } else {
            finish();
            Toast.makeText(this, R.string.no_progress_exist, Toast.LENGTH_SHORT).show();
        }
    }
}

class ProgressAdapter extends ArrayAdapter<ProgressInfo> implements View.OnClickListener {

    private int resource;
    private Activity activity;
    private ArrayList<ProgressInfo> progressInfoList;

    public ProgressAdapter(Context context, int resource, ArrayList<ProgressInfo> progressInfoList) {
        super(context, resource, progressInfoList);
        this.resource = resource;
        activity = (Activity) context;
        this.progressInfoList = progressInfoList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            viewHolder = new ViewHolder();
            viewHolder.tvProgressNameTitle = (TextView) view.findViewById(R.id.tvProgressNameTitle);
            viewHolder.tvProgressNameValue = (TextView) view.findViewById(R.id.tvProgressNameValue);
            viewHolder.tvSaveTimeTitle = (TextView) view.findViewById(R.id.tvSaveTimeTitle);
            viewHolder.tvSaveTimeValue = (TextView) view.findViewById(R.id.tvSaveTimeValue);
            viewHolder.matrixViewInItem = (MatrixView) view.findViewById(R.id.matrixViewInItem);
            viewHolder.tvScoreValue = (TextView) view.findViewById(R.id.tvScoreValue);
            viewHolder.tvStepCanceledValue = (TextView) view.findViewById(R.id.tvStepCanceledValue);
            viewHolder.tvStepCancelableValue = (TextView) view.findViewById(R.id.tvStepCancelableValue);
            viewHolder.btnDeleteThisProgress = (Button) view.findViewById(R.id.btnDeleteThisProgress);
            viewHolder.btnLoadThisProgress = (Button) view.findViewById(R.id.btnLoadThisProgress);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvProgressNameTitle.setText(R.string.progress_name);
        viewHolder.tvProgressNameValue.setText(progressInfoList.get(position).progressName);
        viewHolder.tvSaveTimeTitle.setText(R.string.saving_time);
        viewHolder.tvSaveTimeValue.setText(progressInfoList.get(position).saveTime);
        viewHolder.matrixViewInItem.setMatrix(progressInfoList.get(position).matrix);
        viewHolder.tvScoreValue.setText(String.valueOf(progressInfoList.get(position).score));
        viewHolder.tvStepCanceledValue.setText(String.valueOf(progressInfoList.get(position).stepCanceled));
        viewHolder.tvStepCancelableValue.setText(String.valueOf(progressInfoList.get(position).stepCancelable));
        viewHolder.btnDeleteThisProgress.setTag(position);
        viewHolder.btnDeleteThisProgress.setOnClickListener(this);
        viewHolder.btnLoadThisProgress.setTag(position);
        viewHolder.btnLoadThisProgress.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDeleteThisProgress) {
            int position = (Integer) v.getTag();
            new File(activity.getFilesDir(), "save/" + progressInfoList.get(position).progressName).delete();
            progressInfoList.remove(position);
            if (progressInfoList.size() != 0) {
                notifyDataSetChanged();
                Toast.makeText(activity, R.string.success_to_delete, Toast.LENGTH_SHORT).show();
            } else {
                activity.finish();
                Toast.makeText(activity, R.string.no_progress_exist, Toast.LENGTH_SHORT).show();
            }
        }
        else if (v.getId() == R.id.btnLoadThisProgress) {
            int position = (Integer) v.getTag();
            Intent intent = new Intent(activity, GameActivity.class);
            intent.putExtra("progress", "save/" + progressInfoList.get(position).progressName);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    private class ViewHolder {
        TextView tvProgressNameTitle;
        TextView tvProgressNameValue;
        TextView tvSaveTimeTitle;
        TextView tvSaveTimeValue;
        MatrixView matrixViewInItem;
        TextView tvScoreValue;
        TextView tvStepCanceledValue;
        TextView tvStepCancelableValue;
        Button btnDeleteThisProgress;
        Button btnLoadThisProgress;
    }
}
