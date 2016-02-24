package com.zq1451349.game2048;

import android.app.Activity;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProgressInfo implements Comparable<ProgressInfo>{
    String progressName;
    String saveTime;
    Matrix matrix = new Matrix();
    int score;
    int stepCanceled;
    int stepCancelable;

    public static ArrayList<ProgressInfo> createList(Activity activity, File path) {
        if (!path.exists() || path.list().length == 0) {
            return null;
        }
        ArrayList<ProgressInfo> itemInfoList = new ArrayList<>();
        try {
            for (String fileName : path.list()) {
                ProgressInfo progressInfo = new ProgressInfo();
                FileInputStream fis = new FileInputStream(new File(path, fileName));
                DataInputStream dis = new DataInputStream(fis);
                progressInfo.progressName = fileName;
                progressInfo.saveTime = dis.readUTF();
                progressInfo.score = dis.readInt();
                progressInfo.stepCanceled = dis.readInt();
                progressInfo.stepCancelable = dis.readInt();
                for (int row = 0; row < 4; row++) {
                    for (int column = 0; column < 4; column++) {
                        progressInfo.matrix.elements[row][column] = dis.readInt();
                    }
                }
                itemInfoList.add(progressInfo);
                dis.close();
            }
        } catch (IOException e) {
            activity.finish();
            Toast.makeText(activity, R.string.load_progress_failed, Toast.LENGTH_SHORT).show();
        }
        return itemInfoList;
    }

    @Override
    public int compareTo(ProgressInfo another) {
        if (score > another.score || score == another.score && stepCanceled < another.stepCanceled) {
            return -1;
        }else {
            return 1;
        }
    }
}
