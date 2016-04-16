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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.dialog_add, container, false);
        mInputWhat = (EditText) fragmentView.findViewById(R.id.et_drop);
        mBtnClose = (ImageButton) fragmentView.findViewById(R.id.btn_close);
        mInputWhen = (DatePicker) fragmentView.findViewById(R.id.bpv_date);
        mBtnAdd = (Button) fragmentView.findViewById(R.id.btn_add);

        mBtnClose.setOnClickListener(mBtnClickListener);
        mBtnAdd.setOnClickListener(mBtnClickListener);

        return fragmentView;
    }
}
