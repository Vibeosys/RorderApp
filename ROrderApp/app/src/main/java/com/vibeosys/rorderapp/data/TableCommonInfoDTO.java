package com.vibeosys.rorderapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anand on 09-02-2016.
 */
public class TableCommonInfoDTO implements Parcelable {
    public static final Creator<TableCommonInfoDTO> CREATOR = new Creator<TableCommonInfoDTO>() {
        @Override
        public TableCommonInfoDTO createFromParcel(Parcel in) {
            return new TableCommonInfoDTO(in);
        }

        @Override
        public TableCommonInfoDTO[] newArray(int size) {
            return new TableCommonInfoDTO[size];
        }
    };
    private int mTableId;
    private int mTableNo;
    private String mCustId;
    private int mTakeAwayNo;

    public TableCommonInfoDTO() {

    }

    public TableCommonInfoDTO(int tableId, String custId, int tableNo, int takeAwayNo) {
        mTableId = tableId;
        mCustId = custId;
        mTableNo = tableNo;
        mTakeAwayNo = takeAwayNo;
    }

    protected TableCommonInfoDTO(Parcel in) {
        mCustId = in.readString();
        mTableId = in.readInt();
        mTableNo = in.readInt();
        mTakeAwayNo = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCustId);
        dest.writeInt(mTableId);
        dest.writeInt(mTableNo);
        dest.writeInt(mTakeAwayNo);
    }

    public int getTableId() {
        return mTableId;
    }

    public void setTableId(int tableId) {
        this.mTableId = tableId;
    }

    public int getTableNo() {
        return mTableNo;
    }

    public void setTableNo(int tableNo) {
        this.mTableNo = tableNo;
    }

    public String getCustId() {
        return mCustId;
    }

    public void setCustId(String custId) {
        this.mCustId = custId;
    }

    public int getTakeAwayNo() {
        return mTakeAwayNo;
    }

    public void setTakeAwayNo(int mTakeAwayNo) {
        this.mTakeAwayNo = mTakeAwayNo;
    }
}
