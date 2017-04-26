package com.ifca.uicommon.formview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ifca.uicommon.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rex on 2017/3/17.
 * 功能：表格控件
 *
 *   List<String> data=new ArrayList<>();
     for (int i=0;i<28;i++){
        data.add(i+"");
     }
    FormView formView=(FormView) findViewById(R.id.form_view);
    formView.setTableTitleColor(R.drawable.grid_titleview);
    formView.setOrientation(FormView.VERTICAL);
     formView.drawForm(this,data,7);
 */

public class FormView extends HorizontalScrollView {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int orientationData = HORIZONTAL;           //数据排序方向
    private int tableBackground = 0xffffffff;           //表头颜色
    private int cellBackground=R.drawable.grid_titleview;
    private Context mContext;
    private List<String> formData;        //表格内容
    private int columnCount;         //列数
    private int rowCount;            //行数
    private int layoutPaddingPx;


    public FormView(Context context) {
        super(context);
    }

    public FormView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FormView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * @param context     传入上下文
     * @param data        传入表格填充数据
     * @param formColumns 表格的列数
     */
    public void drawForm(Context context, List<String> data, int formColumns) {
        mContext = context;
        formData = data;
        columnCount = formColumns;
        layoutPaddingPx = dpTopx(5);
        rowCount = ((formData.size()) / columnCount);
        if (orientationData == HORIZONTAL) {
            drawHorizontal();
        } else {
            drawVertital();
        }
        this.invalidate();
    }

    private void drawHorizontal() {
        int maxHeight = 0;
        int maxWidth = 0;
        List<Integer> listWidth = new ArrayList<>();
        List<Integer> listHeight = new ArrayList<>();

        //动态计算对应的每一行中，统一的最高单元格的高度
        for (int row = 0; row < rowCount; row++) {
            maxHeight = 0;
            for (int column = 0; column < columnCount; column++) {
                TextView textView = getCellTextView();
                textView.setText(formData.get(row * columnCount + column));
                int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                textView.measure(spec, spec);
                maxHeight = textView.getMeasuredHeight() > maxHeight ? textView.getMeasuredHeight() : maxHeight;
            }
            listHeight.add(maxHeight);
        }

        //动态计算对应的每一行中，统一的最高单元格的宽度
        for (int cloumn = 0; cloumn < columnCount; cloumn++) {
            maxWidth = 0;
            for (int row = 0; row < rowCount; row++) {
                TextView textView = getCellTextView();
                textView.setText(formData.get(row * columnCount + cloumn));
                int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                textView.measure(spec, spec);
                maxWidth = textView.getMeasuredWidth() > maxWidth ? textView.getMeasuredWidth() : maxWidth;
            }
            listWidth.add(maxWidth);
        }

        LinearLayout tablayout = new LinearLayout(mContext);
        tablayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams tablayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tablayout.setLayoutParams(tablayoutParams);

        for (int keyCount = 0; keyCount < rowCount; keyCount++) {
            LinearLayout rowLayout = new LinearLayout(mContext);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rowLayout.setLayoutParams(rowParams);
            for (int count = 0; count < columnCount; count++) {
                TextView tvTableCell = getCellTextView();
                tvTableCell.setText(formData.get(keyCount * columnCount + count));
                tvTableCell.setLayoutParams(new ViewGroup.LayoutParams(listWidth.get(count), listHeight.get(keyCount)));
                if (keyCount == 0) {
                    tvTableCell.setBackgroundResource(tableBackground);
                }
                rowLayout.addView(tvTableCell);
            }
            tablayout.addView(rowLayout);
        }
        this.addView(tablayout);
    }

    private void drawVertital() {
        int maxHeight = 0;
        int maxWidth = 0;
        List<Integer> listWidth = new ArrayList<>();
        List<Integer> listHeight = new ArrayList<>();

        //动态计算对应的每一行中，统一的最高单元格的高度
        for (int row = 0; row < rowCount; row++) {
            maxHeight = 0;
            for (int column = 0; column < columnCount; column++) {
                TextView textView = getCellTextView();
                textView.setText(formData.get(column * rowCount + row));
                int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                textView.measure(spec, spec);
                maxHeight = textView.getMeasuredHeight() > maxHeight ? textView.getMeasuredHeight() : maxHeight;
            }
            listHeight.add(maxHeight);
        }

        //动态计算对应的每一行中，统一的最高单元格的宽度
        for (int cloumn = 0; cloumn < columnCount; cloumn++) {
            maxWidth = 0;
            for (int row = 0; row < rowCount; row++) {
                TextView textView = getCellTextView();
                textView.setText(formData.get(row * columnCount + row));
                int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                textView.measure(spec, spec);
                maxWidth = textView.getMeasuredWidth() > maxWidth ? textView.getMeasuredWidth() : maxWidth;
            }
            listWidth.add(maxWidth);
        }

        LinearLayout tablayout = new LinearLayout(mContext);
        tablayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams tablayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tablayout.setLayoutParams(tablayoutParams);

        for (int keyCount = 0; keyCount < columnCount; keyCount++) {
            LinearLayout rowLayout = new LinearLayout(mContext);
            rowLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rowLayout.setLayoutParams(rowParams);
            for (int count = 0; count < rowCount; count++) {
                TextView tvTableCell = getCellTextView();
                tvTableCell.setText(formData.get(keyCount * rowCount + count));
                tvTableCell.setLayoutParams(new ViewGroup.LayoutParams(listWidth.get(keyCount), listHeight.get(count)));
                if (count == 0) {
                    tvTableCell.setBackgroundResource(tableBackground);
                }
                rowLayout.addView(tvTableCell);
            }
            tablayout.addView(rowLayout);
        }
        this.addView(tablayout);

    }

    /**
     * @param orientation HORIZONTAL 表示数据的顺序是从第一行的左到右的顺序添加的，
     *                    Vertital 表示数据的添加顺序是从第一列的上到下顺序添加的
     */
    public void setOrientation(int orientation) {
        orientationData = orientation;
    }

    /**
     * @param res 设置表格表头标题的背景Id
     */
    public void setTableTitleColor(int res) {
        tableBackground = res;

    }

    /**
     * @param res 设置表格内容背景*/
    public void setCellBackground(int res){
        cellBackground=res;
    }

    /**
     * 表格单元格样式
     */
    private TextView getCellTextView() {
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTextColor(0xff333333);
        textView.setPadding(layoutPaddingPx, layoutPaddingPx, layoutPaddingPx, layoutPaddingPx);
        textView.setMaxWidth(dpTopx(250));
        textView.setBackgroundResource(cellBackground);
        return textView;
    }

    private int dpTopx(int dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
