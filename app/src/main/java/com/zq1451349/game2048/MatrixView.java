package com.zq1451349.game2048;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MatrixView extends RelativeLayout {
    Context context;
    private Matrix matrix;
    private ImageView[][] cells;

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        LinearLayout squareField = new LinearLayout(context) {

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                if (MeasureSpec.getSize(widthMeasureSpec) < MeasureSpec.getSize(heightMeasureSpec)) {
                    super.onMeasure(widthMeasureSpec, widthMeasureSpec);
                } else {
                    super.onMeasure(heightMeasureSpec, heightMeasureSpec);
                }
            }
        };
        RelativeLayout.LayoutParams fieldLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        fieldLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        squareField.setLayoutParams(fieldLayoutParams);
        squareField.setOrientation(LinearLayout.VERTICAL);
        squareField.setBackground(ContextCompat.getDrawable(context, R.mipmap.matrix_view_background));
        squareField.setPadding(
                (int) getResources().getDimension(R.dimen.normal_padding),
                (int) getResources().getDimension(R.dimen.normal_padding),
                (int) getResources().getDimension(R.dimen.normal_padding),
                (int) getResources().getDimension(R.dimen.normal_padding));
        this.addView(squareField);
        cells = new ImageView[4][];
        for (int row = 0; row < 4; row++) {
            LinearLayout rowLinearLayout = new LinearLayout(context);
            rowLinearLayout.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
            rowLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            squareField.addView(rowLinearLayout);
            cells[row] = new ImageView[4];
            for (int column = 0; column < 4; column++) {
                cells[row][column] = new ImageView(context);
                cells[row][column].setLayoutParams(
                        new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                cells[row][column].setPadding(
                        (int) getResources().getDimension(R.dimen.small_padding),
                        (int) getResources().getDimension(R.dimen.small_padding),
                        (int) getResources().getDimension(R.dimen.small_padding),
                        (int) getResources().getDimension(R.dimen.small_padding));
                cells[row][column].setScaleType(ImageView.ScaleType.FIT_XY);
                rowLinearLayout.addView(cells[row][column]);
            }
        }
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
        redraw();
    }

    protected void redraw() {
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                String picName = "element_" + matrix.elements[row][column];
                int resId = getResources().getIdentifier(picName, "mipmap", context.getPackageName());
                cells[row][column].setImageDrawable(ContextCompat.getDrawable(context, resId));
            }
        }
    }
}