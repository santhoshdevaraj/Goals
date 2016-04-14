package io.sdevaraj.goals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.sdevaraj.goals.adapters.AdapterDrops;
import io.sdevaraj.goals.beans.Drop;
import io.sdevaraj.goals.widgets.BucketRecyclerView;

public class ActivityMain extends AppCompatActivity {

    Toolbar mToolbar;
    Button eButton;
    BucketRecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Drop> mResults;
    AdapterDrops mAdapter;
    View mEmptyView;
    private String TAG = "Realm";

    private View.OnClickListener mBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialogAdd();
        }
    };

    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
//            Log.d(TAG, "onChange: was called");
            mAdapter.Update(mResults);
        }
    };

    private void showDialogAdd() {
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), "Add");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRealm = Realm.getDefaultInstance();
        mResults = mRealm.where(Drop.class).findAllAsync();
        eButton = (Button) findViewById(R.id.iv_button);
        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEmptyView = findViewById(R.id.empty_drops);
        mRecycler.hideIfEmpty(mToolbar);
        mRecycler.showIfEmpty(mEmptyView);
        mAdapter = new AdapterDrops(this, mResults);
        mRecycler.setAdapter(mAdapter);
        setSupportActionBar(mToolbar);
        eButton.setOnClickListener(mBtnListener);
        initBackgroundImage();
    }

    private void initBackgroundImage() {
        ImageView backgroundImageView = (ImageView) findViewById(R.id.id_background);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(backgroundImageView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mResults.addChangeListener(mChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResults.removeChangeListener(mChangeListener);
    }
}
