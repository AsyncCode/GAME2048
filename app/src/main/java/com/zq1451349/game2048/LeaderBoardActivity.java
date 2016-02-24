package com.zq1451349.game2048;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class LeaderBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        ArrayList<ProgressInfo> progressInfoList = ProgressInfo.createList(this,
                new File(getFilesDir() + "/top"));
        if (progressInfoList != null && progressInfoList.size() != 0) {
            Collections.sort(progressInfoList);
            ListView listView = (ListView) findViewById(R.id.progressListView);
            LeaderBoardAdapter leaderBoardAdapter = new LeaderBoardAdapter(this, R.layout.progress_item, progressInfoList);
            listView.setAdapter(leaderBoardAdapter);
        } else {
            finish();
            Toast.makeText(this, R.string.empty_leader_board, Toast.LENGTH_SHORT).show();
        }
    }
}

class LeaderBoardAdapter extends ArrayAdapter<ProgressInfo> {

    private int resource;
    private ArrayList<ProgressInfo> progressInfoList;

    public LeaderBoardAdapter(Context context, int resource, ArrayList<ProgressInfo> progressInfoList) {
        super(context, resource, progressInfoList);
        this.resource = resource;
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
            viewHolder.tvStepCancelableTitle = (TextView) view.findViewById(R.id.tvStepCancelableTitle);
            viewHolder.tvStepCancelableValue = (TextView) view.findViewById(R.id.tvStepCancelableValue);
            viewHolder.btnDeleteThisProgress = (Button) view.findViewById(R.id.btnDeleteThisProgress);
            viewHolder.btnLoadThisProgress = (Button) view.findViewById(R.id.btnLoadThisProgress);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvProgressNameTitle.setText(R.string.rank);
        viewHolder.tvProgressNameValue.setText(String.valueOf(position + 1));
        viewHolder.tvSaveTimeTitle.setText(R.string.achieving_time);
        viewHolder.tvSaveTimeValue.setText(progressInfoList.get(position).saveTime);
        viewHolder.matrixViewInItem.setMatrix(progressInfoList.get(position).matrix);
        viewHolder.tvScoreValue.setText(String.valueOf(progressInfoList.get(position).score));
        viewHolder.tvStepCanceledValue.setText(String.valueOf(progressInfoList.get(position).stepCanceled));
        viewHolder.tvStepCancelableTitle.setVisibility(View.GONE);
        viewHolder.tvStepCancelableValue.setVisibility(View.GONE);
        viewHolder.btnDeleteThisProgress.setVisibility(View.GONE);
        viewHolder.btnLoadThisProgress.setVisibility(View.GONE);
        return view;
    }

    private class ViewHolder {
        TextView tvProgressNameTitle;
        TextView tvProgressNameValue;
        TextView tvSaveTimeTitle;
        TextView tvSaveTimeValue;
        MatrixView matrixViewInItem;
        TextView tvScoreValue;
        TextView tvStepCanceledValue;
        TextView tvStepCancelableTitle;
        TextView tvStepCancelableValue;
        Button btnDeleteThisProgress;
        Button btnLoadThisProgress;
    }
}
