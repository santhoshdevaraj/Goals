package io.sdevaraj.goals.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.sdevaraj.goals.R;

/**
 * View for the bucket_picker_view.xml file.
 */
public class BucketPickerView extends LinearLayout {
    private TextView mTextMonth;
    private TextView mTextDate;
    private TextView mTextYear;
    private Calendar mCalendar;
    private SimpleDateFormat mFormatter;

    public BucketPickerView(Context context) {
        super(context);
        init(context);
    }

    public BucketPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BucketPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.bucket_picker_view, this);
        mCalendar = Calendar.getInstance();
        mFormatter = new SimpleDateFormat("MMM");
    }


    /**
     * Called as the last phase of inflation, after all child views have been added.
     * Ideal location for adding the code for after the view is rendered.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextDate = (TextView) this.findViewById(R.id.tv_date);
        mTextMonth = (TextView) this.findViewById(R.id.tv_month);
        mTextYear = (TextView) this.findViewById(R.id.tv_year);
        int date = mCalendar.get(Calendar.DATE);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);
        update(date, month, year, 0, 0, 0);
    }

    /**
     * Update the text views for date, month and year with current time stamp.
     */
    private void update(int date, int month, int year, int hour, int minute, int second) {
        mCalendar.set(year, month, date, hour, minute, second);
        mTextDate.setText(date + "");
        mTextMonth.setText(mFormatter.format(mCalendar.getTime()));
        mTextYear.setText(year + "");
    }

    /**
     * Returns the current system time in milliseconds.
     */
    public long getTime() {
        return mCalendar.getTimeInMillis();
    }
}
