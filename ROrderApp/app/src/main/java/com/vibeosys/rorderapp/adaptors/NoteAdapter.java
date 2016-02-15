package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.NoteDTO;
import com.vibeosys.rorderapp.data.TableCategoryDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 15-02-2016.
 */
public class NoteAdapter extends BaseAdapter {

    private static final String TAG = NoteAdapter.class.getSimpleName();
    private Context mContext;
    ArrayList<NoteDTO> noteList;

    public NoteAdapter(Context mContext, ArrayList<NoteDTO> noteList) {
        this.mContext = mContext;
        this.noteList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder = null;

        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.row_select_note, null);
            viewHolder = new ViewHolder();
            viewHolder.txtTitle = (TextView) row.findViewById(R.id.txtNote);
            viewHolder.checkMark = (ImageView) row.findViewById(R.id.imgCheck);
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();
        NoteDTO note = noteList.get(position);
        Log.d(TAG, note.toString());
        viewHolder.txtTitle.setText(note.getNoteTitle());

        if (note.isSelected()) {
            viewHolder.checkMark.setVisibility(View.VISIBLE);
        } else {
            viewHolder.checkMark.setVisibility(View.INVISIBLE);
        }
        //viewHolder.imgTablePhoto.loadImageFromFile("file:" + myImageDBs.get(position).getmImagePath());
        return row;
    }

    private class ViewHolder {
        TextView txtTitle;
        ImageView checkMark;
    }

    public void setItemChecked(int noteId) {
        for (int i = 0; i < noteList.size(); i++) {
            NoteDTO note = noteList.get(i);
            if (note.getNoteId() == noteId) {
                note.setSelected(!note.isSelected());
            } else {
                note.setSelected(false);
            }

        }
        notifyDataSetChanged();
    }
}
