package com.zq1451349.game2048;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    MatrixView matrixView = null;
    TextView tvStepCanceled = null;
    TextView tvStepCancelable = null;
    TextView tvScore = null;
    private Matrix matrix;
    private View.OnTouchListener slide = new View.OnTouchListener() {
        private float x1 = 0, y1 = 0, x2 = 0, y2 = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                x1 = event.getX();
                y1 = event.getY();
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                x2 = event.getX();
                y2 = event.getY();
                if ((y2 - y1) <= -(x2 - x1) && (y2 - y1) > (x2 - x1))
                    if (matrix.move(Matrix.MOVE_LEFT) != 0) {
                        gameRefresh();
                    }
                if ((y2 - y1) >= -(x2 - x1) && (y2 - y1) < (x2 - x1))
                    if (matrix.move(Matrix.MOVE_RIGHT) != 0) {
                        gameRefresh();
                    }
                if ((y2 - y1) <= (x2 - x1) && (y2 - y1) < -(x2 - x1))
                    if (matrix.move(Matrix.MOVE_UP) != 0) {
                        gameRefresh();
                    }
                if ((y2 - y1) >= (x2 - x1) && (y2 - y1) > -(x2 - x1))
                    if (matrix.move(Matrix.MOVE_DOWN) != 0) {
                        gameRefresh();
                    }
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        matrixView = (MatrixView) findViewById(R.id.matrixView);
        tvStepCanceled = (TextView) findViewById(R.id.tvStepCanceled);
        tvStepCancelable = (TextView) findViewById(R.id.tvStepCancelable);
        tvScore = (TextView) findViewById(R.id.tvScore);
        matrix = new Matrix(this);
        matrixView.setMatrix(matrix);
        matrixView.setOnTouchListener(slide);
        String progress = getIntent().getStringExtra("progress");
        if (!progress.equals(".DO_NOT_LOAD")) {
            if (!loadProgress(progress))
                Toast.makeText(GameActivity.this, R.string.load_progress_failed, Toast.LENGTH_SHORT).show();
        }
        gameRefresh();
        findViewById(R.id.btnCancel).setOnClickListener(this);
        findViewById(R.id.btnSaveProgress).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                cancel();
                break;
            case R.id.btnSaveProgress:
                dialogToSave();
                break;
            default:
                break;
        }
    }

    public boolean cancel() {
        if (matrix.stepCancelable <= 0 || matrix.matrixRecord[0].equals(matrix))
            return false;
        matrix.stepCanceled++;
        matrix.stepCancelable--;
        matrix.copy(matrix.matrixRecord[0]);
        for (int i = 0; i < matrix.matrixRecord.length - 1; i++) {
            matrix.matrixRecord[i].copy(matrix.matrixRecord[i + 1]);
        }
        gameRefresh();
        return true;
    }

    private void dialogToSave() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle(R.string.select_or_input_progress_name);
        File path = new File(getFilesDir().getPath() + "/save");
        if (!path.exists()) {
            path.mkdirs();
        }
        final String[] items = path.list();
        if (items != null && items.length != 0) {
            builder.setItems(items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (saveProgress("save/" + items[which])) {
                        Toast.makeText(GameActivity.this, getString(R.string.success_to_save), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GameActivity.this, getString(R.string.failed_to_save), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        final EditText editText = new EditText(this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        editText.setHint(R.string.apply_default_name);
        final Button button = new Button(this);
        button.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setText(R.string.ok);
        linearLayout.addView(editText);
        linearLayout.addView(button);
        builder.setView(linearLayout);
        final Dialog dialog = builder.show();
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String progressName = editText.getText().toString();
                if (progressName.isEmpty()) {
                    if (saveProgress(null)){
                        Toast.makeText(GameActivity.this, getString(R.string.success_to_save), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GameActivity.this, getString(R.string.failed_to_save), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                } else if (progressName.contains("/")) {
                    Toast.makeText(GameActivity.this, R.string.wrong_progress_name, Toast.LENGTH_SHORT).show();
                } else {
                    if (saveProgress("save/" + progressName)){
                        Toast.makeText(GameActivity.this, getString(R.string.success_to_save), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GameActivity.this, getString(R.string.failed_to_save), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            }
        });
    }

    public void gameRefresh() {
        matrixView.redraw();
        tvScore.setText(getString(R.string.current_score) + String.valueOf(matrix.score));
        tvStepCanceled.setText(getString(R.string.canceled) + String.valueOf(matrix.stepCanceled));
        tvStepCancelable.setText(getString(R.string.cancelable) + String.valueOf(matrix.stepCancelable));
        if (!matrix.canMove()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.game_over);
            builder.setMessage(getString(R.string.final_score) + matrix.score);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity.this.finish();
                    new File(getFilesDir().getPath(), ".AUTO_SAVED").delete();
                }
            });
            builder.setCancelable(false);
            builder.show();
            saveProgress("top/" + System.currentTimeMillis());
            ArrayList<ProgressInfo> progressInfoList
                    = ProgressInfo.createList(this,new File(getFilesDir() + "/top"));
            if (progressInfoList != null && progressInfoList.size() != 0) {
                Collections.sort(progressInfoList);
                while (progressInfoList.size() > 10) {
                    new File(getFilesDir(), "top/" + progressInfoList.get(10).progressName).delete();
                    progressInfoList.remove(10);
                }
            }
        }
    }

    public boolean saveProgress(String progressName) {
        String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        if (progressName == null)
            progressName = "save/" + timeNow;
        File file = new File(getFilesDir(), progressName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeUTF(timeNow);
            dos.writeInt(matrix.score);
            dos.writeInt(matrix.stepCanceled);
            dos.writeInt(matrix.stepCancelable);
            for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 4; column++) {
                    dos.writeInt(matrix.elements[row][column]);
                }
            }
            for (int i = 0; i < matrix.matrixRecord.length; i++) {
                for (int row = 0; row < 4; row++) {
                    for (int column = 0; column < 4; column++) {
                        dos.writeInt(matrix.matrixRecord[i].elements[row][column]);
                    }
                }
            }
            dos.flush();
            dos.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean loadProgress(String progressName) {
        try {
            FileInputStream fis = new FileInputStream(new File(getFilesDir(), progressName));
            DataInputStream dis = new DataInputStream(fis);
            dis.readUTF();
            matrix.score = dis.readInt();
            matrix.stepCanceled = dis.readInt();
            matrix.stepCancelable = dis.readInt();
            for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 4; column++) {
                    matrix.elements[row][column] = dis.readInt();
                }
            }
            for (int i = 0; i < matrix.matrixRecord.length; i++) {
                for (int row = 0; row < 4; row++) {
                    for (int column = 0; column < 4; column++) {
                        matrix.matrixRecord[i].elements[row][column] = dis.readInt();
                    }
                }
            }
            dis.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void finish() {
        saveProgress(".AUTO_SAVED");
        super.finish();
    }
}
