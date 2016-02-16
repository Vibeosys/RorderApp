package com.vibeosys.rorderapp.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.FeedbackAdapter;
import com.vibeosys.rorderapp.data.FeedBackDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 16-02-2016.
 */
public class FeedbackActivity extends BaseActivity {

    private ListView mFeedbackList;
    private FeedbackAdapter mFeedbackAdapter;
    private ArrayList<FeedBackDTO> mFeedbacks;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_feedback);
        setTitle(getResources().getString(R.string.feedback_activity_name));
        mFeedbackList = (ListView) findViewById(R.id.listFeedback);
        mFeedbacks = mDbRepository.getFeedBackList();
        mFeedbackAdapter = new FeedbackAdapter(mFeedbacks, getApplicationContext());
        mFeedbackList.setAdapter(mFeedbackAdapter);
    }


}
