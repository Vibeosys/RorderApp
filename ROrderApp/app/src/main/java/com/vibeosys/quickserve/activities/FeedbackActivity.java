package com.vibeosys.quickserve.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vibeosys.quickserve.MainActivity;
import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.adaptors.FeedbackAdapter;
import com.vibeosys.quickserve.data.FeedBackDTO;
import com.vibeosys.quickserve.data.TableCommonInfoDTO;
import com.vibeosys.quickserve.data.TableDataDTO;
import com.vibeosys.quickserve.data.UploadCustomerFeedback;
import com.vibeosys.quickserve.data.UploadFeedback;
import com.vibeosys.quickserve.util.ConstantOperations;
import com.vibeosys.quickserve.util.NetworkUtils;

import java.util.ArrayList;

/**
 * Created by akshay on 16-02-2016.
 */
public class FeedbackActivity extends BaseActivity {

    private ListView mFeedbackList;
    private TableCommonInfoDTO tableCommonInfoDTO;
    private FeedbackAdapter mFeedbackAdapter;
    private ArrayList<FeedBackDTO> mFeedbacks;
    private TextView txtThank;
    private Context mContext = this;
    private int mTableId;
    private int mTableNo;
    private String mCustId;

    @Override
    protected String getScreenName() {
        return "Feedback";
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_feedback);
        setTitle(getResources().getString(R.string.feedback_activity_name));
        mFeedbackList = (ListView) findViewById(R.id.listFeedback);
        txtThank = (TextView) findViewById(R.id.txtThank);
        mFeedbacks = mDbRepository.getFeedBackList();
        tableCommonInfoDTO = getIntent().getParcelableExtra("tableCustInfo");
        mTableId = tableCommonInfoDTO.getTableId();
        mTableNo = tableCommonInfoDTO.getTableNo();
        mCustId = tableCommonInfoDTO.getCustId();
        mFeedbackAdapter = new FeedbackAdapter(mFeedbacks, getApplicationContext());
        mFeedbackList.setAdapter(mFeedbackAdapter);

        txtThank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                    sendFeedback();
                } else {
                    showMyDialog(mContext);
                }
            }
        });
    }

    private void sendFeedback() {
        ArrayList<UploadFeedback> feedbackDbDTOs = new ArrayList<>();
        for (FeedBackDTO feedBackDTO : mFeedbacks) {
            feedbackDbDTOs.add(new UploadFeedback(feedBackDTO.getmFeedbackId(), feedBackDTO.getmRating()));
        }

        UploadCustomerFeedback customerFeedback = new UploadCustomerFeedback(mCustId, feedbackDbDTOs);

        Gson gson = new Gson();

        String serializedJsonString = gson.toJson(customerFeedback);
        Log.d(TAG, "##" + serializedJsonString);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.CUSTOMER_FEEDBACK, serializedJsonString);
        mServerSyncManager.uploadDataToServer(tableDataDTO);

        Intent iMain = new Intent(getApplicationContext(), MainActivity.class);
        iMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(iMain);
        finish();
    }


}
