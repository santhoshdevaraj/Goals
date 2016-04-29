package io.sdevaraj.goals.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.sdevaraj.goals.R;

/**
 * View for the bucket_picker_view.xml file.
 */
public class BucketPickerView extends LinearLayout implements View.OnTouchListener {
    private TextView mTextMonth;
    private TextView mTextDate;
    private TextView mTextYear;
    private Calendar mCalendar;
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;

    private SimpleDateFormat mFormatter;
    public static final String TAG = "Santhosh";

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
        mTextDate.setOnTouchListener(this);
        mTextMonth.setOnTouchListener(this);
        mTextYear.setOnTouchListener(this);
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

    /**
     * Called when the event is triggered from any of the touch listeners.
     * Based on the view and event type, appropriate action is called for.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_date:
                processEventsFor(mTextDate, event);
                break;
            case R.id.tv_month:
                processEventsFor(mTextMonth, event);
                break;
            case R.id.tv_year:
                processEventsFor(mTextYear, event);
                break;
        }
        return true;
    }

    /**
     * Based on the events and the drawable location, triggers the appropriate text view updates.
     */
    private void processEventsFor(TextView textView, MotionEvent event) {
        // Returns an array of left, top, right and bottom drawables for the given
        // text view and the area occupied by each
        Drawable[] compoundDrawables = textView.getCompoundDrawables();
        if (hasDrawableTop(compoundDrawables) && hasDrawableBottom(compoundDrawables)) {
            Rect topBounds = compoundDrawables[TOP].getBounds();
            Rect bottomBounds = compoundDrawables[BOTTOM].getBounds();
            float x = event.getX();
            float y = event.getY();
            if (topDrawableHit(textView, topBounds.height(), x, y)) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modifyViewValues(textView.getId(), 1);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
            } else if (bottomDrawableHit(textView, bottomBounds.height(), x, y)) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modifyViewValues(textView.getId(), -1);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
            } else {
                Log.d(TAG, "Pressed outside the drawable");
            }
        }
    }

    /**
     * Verify if the text view has a drawable top
     */
    private boolean hasDrawableTop(Drawable[] drawables) {
        return drawables[TOP] != null;
    }

    /**
     * Verify if the text view has a drawable bottom
     */
    private boolean hasDrawableBottom(Drawable[] drawables) {
        return drawables[BOTTOM] != null;
    }

    /**
     * Check if the action up, down, etc,. is within the bounds of the drawable.
     */
    private boolean topDrawableHit(TextView textView, int drawableHeight, float x, float y) {
        int xMin = textView.getPaddingLeft();
        int xMax = textView.getWidth() - textView.getPaddingRight();
        int yMin = textView.getPaddingTop();
        int yMax = textView.getPaddingTop() + drawableHeight;

        return x > xMin && x < xMax && y > yMin && y < yMax;
    }

    /**
     * Check if the action up, down, etc,. is within the bounds of the drawable.
     */
    private boolean bottomDrawableHit(TextView textView, int drawableHeight, float x, float y) {
        int xMin = textView.getPaddingLeft();
        int xMax = textView.getWidth() - textView.getPaddingRight();
        int yMax = textView.getHeight() - textView.getPaddingBottom();
        int yMin = yMax - drawableHeight;

        return x > xMin && x < xMax && y > yMin && y < yMax;
    }

    /**
     * Modifies the values of the calendar to update the text view.
     */
    private void modifyViewValues(int id, int operand) {
        switch (id) {
            case R.id.tv_date:
                mCalendar.add(Calendar.DATE, operand);
                break;
            case R.id.tv_month:
                mCalendar.add(Calendar.MONTH, operand);
                break;
            case R.id.tv_year:
                mCalendar.add(Calendar.YEAR, operand);
                break;
        }
        updateTextView(mCalendar);
    }

    /**
     * Updates the text views to reflect the latest value of calendar.
     */
    private void updateTextView(Calendar calendar) {
        int date = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);

        mTextDate.setText(date + "");
        mTextYear.setText(year + "");
        mTextMonth.setText(mFormatter.format(mCalendar.getTime()));
    }
}
