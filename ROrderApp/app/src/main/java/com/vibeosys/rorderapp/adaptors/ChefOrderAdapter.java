package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.ChefMenuDetailsDTO;
import com.vibeosys.rorderapp.data.ChefOrderCompleted;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;
import com.vibeosys.rorderapp.data.ServerSync;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.database.DbRepository;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.ServerSyncManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shrinivas on 12-02-2016.
 */
public class ChefOrderAdapter extends BaseExpandableListAdapter  {
    private Context context;
    private ArrayList<ChefOrderDetailsDTO> chefOrderDetailsDTOs;
    private GroupHolder groupHolder;
    private ChildHolder childHolder;
    private DbRepository mDbRepository;
    private  ServerSyncManager mserverSyncManager;
    private OnDoneClickListener onDoneClickListener;
    ArrayList<ChefMenuDetailsDTO> child;
    ArrayList<ChefMenuDetailsDTO> child1;
    HashMap<Integer ,ArrayList<ChefMenuDetailsDTO>> expHashMap ;


   public ChefOrderAdapter(Context context,ArrayList<ChefOrderDetailsDTO>chefOrderDetailsDTOs,DbRepository dbRepository,ServerSyncManager mserverSyncManager)
    {
        this.context = context;
        this.chefOrderDetailsDTOs =chefOrderDetailsDTOs;
        child = new ArrayList<>();
        this.mDbRepository =dbRepository;
        this.mserverSyncManager =mserverSyncManager;
        expHashMap = new HashMap<>();

    }

    @Override
    public int getGroupCount() {
       // return 0;
        return chefOrderDetailsDTOs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
    //     expHashMap.get(groupPosition).size();
//        Toast.makeText(context,"size is"+expHashMap.get(groupPosition).size(),Toast.LENGTH_LONG).show();



        if(expHashMap.size()>0) {
            if (expHashMap.containsKey(groupPosition))
            {
                child1 = expHashMap.get(groupPosition);
                {
                    return child1.size();
                }

            }
            else {
                return 0;
            }

        }
        if(expHashMap.size() == 0)
        {
            return 0;
        }

       return 0;
      // return 2;
    }

    @Override
    public Object getGroup(int groupPosition) {
       // return null;
        return  this.chefOrderDetailsDTOs.get(groupPosition);

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
       // return null;
        return chefOrderDetailsDTOs.get(groupPosition).getmMenuChild().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
       // return 0;
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        //return 0;
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ChefOrderDetailsDTO chefOrderDetailsDTO = chefOrderDetailsDTOs.get(groupPosition);
        if(convertView ==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chef_main_table_order,null);
            groupHolder = new GroupHolder();
            groupHolder.groupTextView = (TextView)convertView.findViewById(R.id.orderFromTableNo);
            groupHolder.getGroupTableNo = (TextView)convertView.findViewById(R.id.orderTakenBy);
            groupHolder.orderDoneBtn = (Button)convertView.findViewById(R.id.OrderDoneChef);
            convertView.setTag(groupHolder);
        }
        else
        {

            groupHolder = (GroupHolder)convertView.getTag();
        }

        groupHolder.getGroupTableNo.setText("Table No :"+chefOrderDetailsDTO.getmTableNo());
        groupHolder.groupTextView.setText(chefOrderDetailsDTO.getmUserName());
        groupHolder.orderDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDoneClickListener !=null)
                {
                    onDoneClickListener.onDonClick(chefOrderDetailsDTO.getmNewOrderId());
                }
//                String OrderId = chefOrderDetailsDTO.getmNewOrderId();
//                sendToServer(OrderId);

            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChefMenuDetailsDTO chefMenuDetailsDTO = child.get(childPosition);
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chef_main_row_order,null);
            childHolder = new ChildHolder();
            childHolder.childTextView = (TextView)convertView.findViewById(R.id.chefMenuName);
            childHolder.childQty = (TextView)convertView.findViewById(R.id.chefQty);
            convertView.setTag(childHolder);
        }
        else
        {
            childHolder = (ChildHolder)convertView.getTag();
        }
        childHolder.childTextView.setText(""+chefMenuDetailsDTO.getmChefMenuTitle());
        childHolder.childQty.setText(""+chefMenuDetailsDTO.getmChefQty());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        // String str =""+groupPosition;
       ChefOrderDetailsDTO chefOrderDetailsDTO = chefOrderDetailsDTOs.get(groupPosition);
       String selectedOrderId= String.valueOf(chefOrderDetailsDTO.getmNewOrderId());
        child.clear();
        child=  mDbRepository.getChefMenu(selectedOrderId);
        expHashMap.put(groupPosition,child);
        //Toast.makeText(context,""+expHashMap.size(),Toast.LENGTH_LONG);
       // expHashMap.put(groupPosition,child);
     //   Toast.makeText(context,"database order id "+selectedOrderId,Toast.LENGTH_LONG).show();
//        ChefOrderDetailsDTO mChefOrderDetailsDTO = (ChefOrderDetailsDTO) chefOrderDetailsDTOs.get(groupPosition);
//        mChefOrderDetailsDTO.setmMenuChild(mDbRepository.getChefMenu(mChefOrderDetailsDTO.getmNewOrderId()));
        notifyDataSetChanged();


    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
      //  child.clear();
        expHashMap.remove(groupPosition);
        notifyDataSetChanged();
    }



    public final class GroupHolder
    {
        TextView groupTextView;
        TextView getGroupTableNo;
        Button orderDoneBtn;
    }
    public final class ChildHolder
    {
        TextView childTextView;
        TextView childQty;
    }


    public interface OnDoneClickListener
    {
        public void onDonClick(String ChefOrderId);
    }
    public void setOnDoneClickListener(OnDoneClickListener listener)
    {
       this.onDoneClickListener=listener;
    }
}
