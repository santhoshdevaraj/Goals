package io.sdevaraj.goals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

/**
 * Main activity on application start.
 */
public class ActivityMain extends AppCompatActivity {

    Toolbar mToolbar;
    Button mButton;
    BucketRecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Drop> mResults;
    AdapterDrops mAdapter;
    View mEmptyDrop;
    public static final String TAG = "Santhosh";

    /**
     * Listener for 'add drop' button click event
     */
    private View.OnClickListener mBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialogAdd();
        }
    };

    /**
     * Listener for changes to the DB ie RealmResults object change events like update, insert, delete
     */
    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            mAdapter.Update(mResults);
        }
    };

    /**
     * Shows the dialog fragment on clicking the 'Add drop' button.
     */
    private void showDialogAdd() {
        // TODO: Investigate if a new instance to be created every time clicked.
        DialogAdd dialog = new DialogAdd();
        Log.d(TAG, "showDialogAdd is called");
        dialog.show(getSupportFragmentManager(), "Add");
    }

    /**
     * Called when the activity is started. Used for creating views, DB calls, previous instance
     * states, etc,.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // sets the content view on activity start
        setContentView(R.layout.activity_main);

        // gets the default instance realm object
        mRealm = Realm.getDefaultInstance();
        Log.d(TAG, "realm in oncreate is " + mRealm);

        // gets the db records through an async call
        mResults = mRealm.where(Drop.class).findAllAsync();

        // gets the view objects from xml widgets
        mButton = (Button) findViewById(R.id.iv_button);
        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEmptyDrop = findViewById(R.id.empty_drops);

        // TODO: Add comments for below
        mRecycler.hideIfEmpty(mToolbar);
        mRecycler.showIfEmpty(mEmptyDrop);

        // adds an adapter to the RecyclerView ie to provide a mapping
        // between the data and view
        mAdapter = new AdapterDrops(this, mResults);
        mRecycler.setAdapter(mAdapter);

        // sets the toolbar
        setSupportActionBar(mToolbar);

        // sets the required event listeners
        mButton.setOnClickListener(mBtnListener);
        mResults.addChangeListener(mChangeListener);

        // sets the background image through the glide library
        initBackgroundImage();
    }

    /**
     * Sets the background image using the glide library.
     */
    private void initBackgroundImage() {
        ImageView backgroundImageView = (ImageView) findViewById(R.id.id_background);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(backgroundImageView);
    }

//    /**
//     * Called when the activity is visible to the user.
//     * TODO: Investigate why the listener should be added on every onStart activity.
//     * TODO: Investigate if the results/view can be refreshed without doing this.
//     */
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mResults.addChangeListener(mChangeListener);
//    }
//
//    /**
//     * Called when the activity is visible to the user.
//     */
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mResults.removeChangeListener(mChangeListener);
//    }

    /**
     * Called when the activity is destroyed. Used for clean up.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mResults.removeChangeListener(mChangeListener);
    }
}
