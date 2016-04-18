package io.sdevaraj.goals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import io.realm.Realm;
import io.sdevaraj.goals.beans.Drop;


/**
 * Created by sdevaraj on 4/11/16.
 */
public class DialogAdd extends DialogFragment {

    private ImageButton mBtnClose;
    private EditText mInputWhat;
    private DatePicker mInputWhen;
    private Button mBtnAdd;
    public static final String TAG = "Santhosh";

    /**
     * Event listener for below events:
     *  a) When a new drop is added from the fragment.
     *  b) When the fragment is closed.
     */
    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_add:
                    addAction();
                    break;
            }
            dismiss();
        }
    };

    /**
     * Called when a drop is added. Inserts a new row to the realm DB.
     * TODO: Investigate if the realm changes can happen through an interface.
     * TODO: Investigate if transaction can be wrapped as a decorator.
     */
    private void addAction() {
        String what = mInputWhat.getText().toString();
        long now = System.currentTimeMillis();

        Realm realm = Realm.getDefaultInstance();
        Drop drop = new Drop(what, now, 0, false);

        realm.beginTransaction();
        realm.copyToRealm(drop);
        realm.commitTransaction();
        realm.close();
    }

    /**
     * Called to create the fragment view.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // gets the fragment view from xml
        View view = inflater.inflate(R.layout.dialog_add, container, false);

        // gets the view objects from xml widgets
        mInputWhat = (EditText) view.findViewById(R.id.et_drop);
        mBtnClose = (ImageButton) view.findViewById(R.id.btn_close);
        mInputWhen = (DatePicker) view.findViewById(R.id.bpv_date);
        mBtnAdd = (Button) view.findViewById(R.id.btn_add);

        // adds the event handlers for add and close
        mBtnClose.setOnClickListener(mBtnClickListener);
        mBtnAdd.setOnClickListener(mBtnClickListener);

        return view;
    }
}
