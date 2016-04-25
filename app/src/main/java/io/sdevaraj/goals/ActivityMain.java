package io.sdevaraj.goals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import io.sdevaraj.goals.adapters.AdapterDrops;
import io.sdevaraj.goals.adapters.AddListener;
import io.sdevaraj.goals.adapters.CompleteListener;
import io.sdevaraj.goals.adapters.Divider;
import io.sdevaraj.goals.adapters.Filter;
import io.sdevaraj.goals.adapters.MarkListener;
import io.sdevaraj.goals.adapters.SimpleTouchCallback;
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
     * Implements the methods for the AddListener interface between the adapter and fragment
     */
    private AddListener mAddListener = new AddListener() {
        @Override
        public void add() {
            showDialogAdd();
        }
    };

    /**
     * Implements the methods for the MarkListener interface between the adapter and fragment
     */
    private MarkListener mMarkListener = new MarkListener() {
        @Override
        public void onMark(int position) {
            showDialogMark(position);
        }
    };

    /**
     * Implements the methods for the CompleteListener interface between the adapter and fragment
     */
    private CompleteListener mCompleteListener = new CompleteListener() {
        @Override
        public void onComplete(int position) {
            mAdapter.markComplete(position);
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
        dialog.show(getSupportFragmentManager(), "Add");
    }

    /**
     * Shows the dialog fragment on clicking the 'drop'.
     * @param position
     */
    private void showDialogMark(int position) {
        // TODO: Investigate if a new instance to be created every time clicked.
        DialogMark dialog = new DialogMark();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        dialog.setArguments(bundle);
        dialog.setCompleteListener(mCompleteListener);
        dialog.show(getSupportFragmentManager(), "Mark");
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

        // gets the db records through an async call
        int filterOption = AppBucketDrops.load(this);
        loadResults(filterOption);

        // gets the view objects from xml widgets
        mButton = (Button) findViewById(R.id.iv_button);
        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEmptyDrop = findViewById(R.id.empty_drops);

        // based on the number of drops hides/displays certain views within activity
        mRecycler.hideIfEmpty(mToolbar);
        mRecycler.showIfEmpty(mEmptyDrop);

        // adds an adapter to the RecyclerView ie to provide a mapping
        // between the data and view
        mAdapter = new AdapterDrops(this, mRealm, mResults, mAddListener, mMarkListener);
        mRecycler.setAdapter(mAdapter);

        // add the decoration ie divider for the recycler view
        mRecycler.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));

        // add the swipe listener and callback
        SimpleTouchCallback callback = new SimpleTouchCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecycler);

        // sets the toolbar
        setSupportActionBar(mToolbar);

        // sets the required event listeners
        mButton.setOnClickListener(mBtnListener);

        // sets the background image through the glide library
        initBackgroundImage();
    }

    /**
     * Loads the results from Realm based on filter option.
     */
    private void loadResults(int filterOption) {
        switch (filterOption) {
            case Filter.NONE:
                mResults = mRealm.where(Drop.class).findAllAsync();
                break;
            case Filter.MOST_TIME_LEFT:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("when", Sort.DESCENDING);
                break;
            case Filter.LEAST_TIME_LEFT:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("when");
                break;
            case Filter.COMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("completed", true).findAllAsync();
                break;
            case Filter.INCOMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("completed", false).findAllAsync();
                break;
        }
        mResults.addChangeListener(mChangeListener);
    }

    /**
     * Converts the xml to view for the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Called when an item is selected from the menu dropdown.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = true;
        int filterOption = Filter.NONE;

        switch (id) {
            case R.id.action_add:
                showDialogAdd();
                return handled;
            case R.id.action_show_complete:
                filterOption = Filter.COMPLETE;
                break;
            case R.id.action_show_incomplete:
                filterOption = Filter.INCOMPLETE;
                break;
            case R.id.action_sort_ascending_date:
                filterOption = Filter.LEAST_TIME_LEFT;
                break;
            case R.id.action_sort_descending_date:
                filterOption = Filter.MOST_TIME_LEFT;
                break;
            default:
                handled = false;
                break;
        }
        AppBucketDrops.save(this, filterOption);
        loadResults(filterOption);

        return handled;
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
}
