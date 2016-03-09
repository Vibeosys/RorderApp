package com.vibeosys.rorderapp.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;

/**
 * Created by akshay on 08-03-2016.
 */
public class FragmentTakeAway extends BaseFragment {

    private TextView mTxtTotalCount;
    private GridView mGridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.take_away_content, container, false);
        ImageButton fab = (ImageButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //callWaitingIntent();
                //Show waiting dialog
                sendEventToGoogle("Action", "Float Waiting list");
                showWaitingDialog(savedInstanceState);
            }
        });
        return view;
    }

    private void showWaitingDialog(Bundle savedInstanceState) {
        final Dialog dlg = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = getLayoutInflater(savedInstanceState).inflate(R.layout.dialog_add_take_away, null);
        dlg.setContentView(view);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        TextView txtTitle = (TextView) dlg.findViewById(R.id.dlg_title);
        ImageView imgCancel = (ImageView) dlg.findViewById(R.id.imgClose);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        txtTitle.setText("Add take away");

        dlg.show();
    }
}
