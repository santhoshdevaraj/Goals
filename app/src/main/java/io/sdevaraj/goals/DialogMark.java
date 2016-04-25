package io.sdevaraj.goals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import io.sdevaraj.goals.adapters.CompleteListener;

/**
 * Created by sdevaraj on 4/19/16.
 */
public class DialogMark extends DialogFragment {

    private ImageButton mBtnClose;
    private Button mBtnCompleted;
    private int mPosition;
    private CompleteListener mListener;


    /**
     * Implements the onClickListener interface. Gets the bundle arguments passed
     * from ActivityMain when the fragment 'Mark Completed' is shown.
     */
    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListener != null && v.getId() == R.id.btn_completed) {
                Bundle arguments = getArguments();
                mPosition = arguments.getInt("POSITION");
                mListener.onComplete(mPosition);
            }
            dismiss();
        }
    };

    /**
     * Called before the fragment is created to apply the styles. Refer the fragment life cycle.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_mark, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnClose = (ImageButton) view.findViewById(R.id.btn_close);
        mBtnCompleted = (Button) view.findViewById(R.id.btn_completed);
        mBtnClose.setOnClickListener(mBtnClickListener);
        mBtnCompleted.setOnClickListener(mBtnClickListener);
    }

    /**
     * Setter for receiving the CompleteListener from ActivityMain.
     */
    public void setCompleteListener(CompleteListener completeListener) {
        mListener = completeListener;
    }
}
