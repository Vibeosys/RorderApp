package com.vibeosys.quickserve.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.vibeosys.quickserve.data.BillDetailsDTO;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.data.DeliveryDTO;
import com.vibeosys.quickserve.data.OrderDetailsDTO;
import com.vibeosys.quickserve.data.PrinterDetailsDTO;
import com.vibeosys.quickserve.data.RestaurantDTO;
import com.vibeosys.quickserve.data.TableCommonInfoDTO;
import com.vibeosys.quickserve.data.TakeAwayDTO;
import com.vibeosys.quickserve.printutils.PrintBody;
import com.vibeosys.quickserve.printutils.PrintDataDTO;
import com.vibeosys.quickserve.printutils.PrintHeader;
import com.vibeosys.quickserve.printutils.PrintPaper;
import com.vibeosys.quickserve.printutils.PrinterFactory;
import com.vibeosys.quickserve.printutils.exceptions.OpenPrinterException;
import com.vibeosys.quickserve.printutils.exceptions.PrintException;
import com.vibeosys.quickserve.util.AppConstants;
import com.vibeosys.quickserve.util.ROrderDateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by shrinivas on 08-02-2016.
 */
public class BillDetailsActivity extends BaseActivity {


    private final static String screenName = "Bill Details";
    private BillDetailsDTO mBillDetailsDTOs;
    private TableCommonInfoDTO tableCommonInfoDTO;
    private int mTableId, mTableNo, mTakeAwayNo, mDeliveryNo;
    private String custId;
    private TextView mTxtTableNo;
    private TextView mTxtServedBy;
    private TextView mTxtBillDate;
    private TextView mTxtNetAmount;
    private TextView mTxtTotalTaxes;
    //private TextView mTxtServicesCharges;
    private TextView mTxtDiscountAmount;
    private TextView mTxtTotalPayableAmnount;
    private TextView mTxtDiscountTitle;
    private TextView mTxtTableNoTitle, mTxtServedByTitle, mTxtAddress, mTxtDeliveryAmt;
    private double mDiscount = 0.00;
    private double mDiscPer = 0.0;
    private double mDeliveryCharges = 0;
    private LinearLayout layoutLocation;
    private ImageView mImgTable;
    private TableRow mDeliveryChargeRow;
    private TextView mBillStatus;
    private ProgressBar mProgressBar;
    private LinearLayout mScrollBill;

    @Override
    protected String getScreenName() {
        return screenName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_amount_payment);
        setTitle(getResources().getString(R.string.bill_details));
       /* BillDbDTO db = new BillDbDTO(1, Date.valueOf("02-02-2016"), Time.valueOf("10:10:11"),1200.00,102.00,10.00,Date.valueOf("02-02-2016"),Date.valueOf("02-02-2016"),2);

       List<BillDbDTO> bill = new ArrayList<>();
        bill.add(db);

        mDbRepository.insertBills(bill);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Tool bar intitilization
        tableCommonInfoDTO = getIntent().getParcelableExtra("tableCustInfo");
        mTableId = tableCommonInfoDTO.getTableId();
        mTableNo = tableCommonInfoDTO.getTableNo();
        custId = tableCommonInfoDTO.getCustId();
        mTakeAwayNo = tableCommonInfoDTO.getTakeAwayNo();
        mDeliveryNo = tableCommonInfoDTO.getDeliveryNo();
        mDiscPer = tableCommonInfoDTO.getDiscount();
        mDeliveryCharges = tableCommonInfoDTO.getDeliveryCharges();

        mTxtTableNo = (TextView) findViewById(R.id.TableNumber);
        //TextView orderNo = (TextView) findViewById(R.id.OrderNumber);
        mTxtServedBy = (TextView) findViewById(R.id.ServedByName);
        mTxtBillDate = (TextView) findViewById(R.id.DateDisplay);
        mTxtNetAmount = (TextView) findViewById(R.id.PaymentAmt);
        mTxtTotalTaxes = (TextView) findViewById(R.id.TaxAmt);
        mTxtTableNoTitle = (TextView) findViewById(R.id.TableNoTitle);
        mTxtServedByTitle = (TextView) findViewById(R.id.ServedByTitle);
        mTxtAddress = (TextView) findViewById(R.id.txtAddress);
        layoutLocation = (LinearLayout) findViewById(R.id.layoutLocation);
        //mTxtServicesCharges = (TextView) findViewById(R.id.ServicesChrgAmt);
        mTxtDiscountAmount = (TextView) findViewById(R.id.DiscountAmt);
        mTxtTotalPayableAmnount = (TextView) findViewById(R.id.TotalAmt);
        mTxtDiscountTitle = (TextView) findViewById(R.id.DiscountTitle);
        mImgTable = (ImageView) findViewById(R.id.imgTable);
        mTxtDeliveryAmt = (TextView) findViewById(R.id.deliveryAmt);
        mDeliveryChargeRow = (TableRow) findViewById(R.id.rowDeliveryChr);
        mBillStatus = (TextView) findViewById(R.id.billStatus);
        mProgressBar = (ProgressBar) findViewById(R.id.select_reto_progress);
        mScrollBill = (LinearLayout) findViewById(R.id.scrollBill);
        Button payment_bill_details = (Button) findViewById(R.id.BillDetailsPayment);
        Button btnBillSummary = (Button) findViewById(R.id.btnBillSummary);
        Button billPrinting = (Button) findViewById(R.id.BillPrinting);
        LinearLayout mLayoutAddDiscount = (LinearLayout) findViewById(R.id.layout_discount_per);

        mBillDetailsDTOs = mDbRepository.getBillDetailsRecords(custId);

        if (mBillDetailsDTOs.getBillPayed() == 1) {
            payment_bill_details.setEnabled(false);
            payment_bill_details.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.light_grey));
            btnBillSummary.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_custom));
            mBillStatus.setText("Bill Paid");
            mLayoutAddDiscount.setVisibility(View.INVISIBLE);
        }
        displayData(mDiscPer);

        payment_bill_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEventToGoogle("Action", "Payment Option selection");
                Intent i = new Intent(getApplicationContext(), BillPaymentOptionActivity.class);
                i.putExtra("tableCustInfo", tableCommonInfoDTO);
                i.putExtra("BillNo", mBillDetailsDTOs.getBillNo());
                i.putExtra("DiscountAmt", mDiscount);
                startActivity(i);
                finish();

            }
        });

        btnBillSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEventToGoogle("Action", "View Bill Summary");
                Intent i = new Intent(getApplicationContext(), BillSummeryActivity.class);
                i.putExtra("tableCustInfo", tableCommonInfoDTO);
                startActivityForResult(i, 1);
            }
        });

        mLayoutAddDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiscountDialog();
            }
        });
        billPrinting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionId = mDbRepository.getPermissionId(AppConstants.PERMISSION_PRINT_BILL);
                if (getPermissionStatus(permissionId)) {
                    AsyncPrint print = new AsyncPrint();
                    print.execute();
                } else {
                    customAlterDialog(getResources().getString(R.string.dialog_access_denied), getResources().getString(R.string.access_denied_place_order));
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mScrollBill.setVisibility(show ? View.GONE : View.VISIBLE);
            mScrollBill.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mScrollBill.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mScrollBill.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showDiscountDialog() {
        final Dialog dialog = new Dialog(BillDetailsActivity.this);
        //double percentage = 0;
        dialog.setContentView(R.layout.dialog_add_discount);
        final EditText txtPer = (EditText) dialog.findViewById(R.id.txtDiscountPer);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        TextView txtDiscount = (TextView) dialog.findViewById(R.id.txtDiscount);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txtDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double percentage = 0;
                String strPercentage = txtPer.getText().toString();
                try {
                    percentage = Double.parseDouble(strPercentage);
                    if (percentage > 99) {
                        txtPer.setError("Enter correct percentage");
                    } else {
                        dialog.dismiss();
                        displayData(percentage);
                    }

                } catch (NumberFormatException e) {
                    addError(screenName, "txtDiscountOnClickListener", e.getMessage());
                    Log.d(TAG, "## error in enter discount percentage");
                }
            }
        });
        dialog.show();
    }

    private void displayData(double percenatge) {

        double netAmount = mBillDetailsDTOs.getNetAmount();
        double totalPaybleAmount = mBillDetailsDTOs.getTotalPayableAmt();
        TakeAwayDTO takeAwayDTO = mDbRepository.getTakeAway(mTakeAwayNo);
        DeliveryDTO deliveryDTO = mDbRepository.getDelivery(mDeliveryNo);

        ROrderDateUtils dateUtils = new ROrderDateUtils();
        if (percenatge != 0) {
            mDiscount = Math.round((netAmount * percenatge) / 100);
            totalPaybleAmount = Math.round(totalPaybleAmount - mDiscount) + mDeliveryCharges;
        }

        if (mTableId != 0) {
            mTxtTableNoTitle.setText("Customer table number was");
            mTxtTableNo.setText(" # " + mBillDetailsDTOs.getTableNo());
            layoutLocation.setVisibility(View.GONE);
            mTxtServedByTitle.setText("Customer was served by");
            mImgTable.setImageResource(R.drawable.ic_table);
            mDeliveryChargeRow.setVisibility(View.GONE);
        } else if (mTakeAwayNo != 0) {
            mTxtTableNoTitle.setText("Order for " + takeAwayDTO.getmCustName() + " was ");
            mTxtTableNo.setText(" # " + mTakeAwayNo);
            layoutLocation.setVisibility(View.VISIBLE);
            mTxtAddress.setText(takeAwayDTO.getmCustAddress());
            mTxtServedByTitle.setText("Order was delivered by");
            mImgTable.setImageResource(R.drawable.ic_person);
            mDeliveryChargeRow.setVisibility(View.VISIBLE);
            mTxtDeliveryAmt.setText(String.format("%.2f", mDeliveryCharges));
        } else {
            mTxtTableNoTitle.setText("Order for " + deliveryDTO.getmCustName() + " was ");
            mTxtTableNo.setText(" # " + mDeliveryNo);
            layoutLocation.setVisibility(View.VISIBLE);
            mTxtAddress.setText(deliveryDTO.getmCustAddress());
            mTxtServedByTitle.setText("Order was delivered by");
            mImgTable.setImageResource(R.drawable.ic_person);
            mDeliveryChargeRow.setVisibility(View.VISIBLE);
            mTxtDeliveryAmt.setText(String.format("%.2f", mDeliveryCharges));
        }

        //orderNo.setText("");


        mTxtServedBy.setText(mBillDetailsDTOs.getServedByName());
        Log.d("##", "##" + mBillDetailsDTOs.getBillDate());
        java.util.Date date = new ROrderDateUtils().getFormattedDate(mBillDetailsDTOs.getBillDate());
        java.util.Date time = new ROrderDateUtils().getFormattedDate(mBillDetailsDTOs.getBillTime());
        long lngTime = time.getTime();

        long billTime = lngTime + dateUtils.getTimeOffsetAsPerLocal(5, 30);
        mTxtBillDate.setText(dateUtils.getLocalDateInReadableFormat(date) + " at " +
                dateUtils.getLocalTimeInReadableFormat(new Date(billTime)));
        mTxtDiscountTitle.setText("Discount (" + percenatge + "%)");
        mTxtNetAmount.setText(String.format("%.2f", netAmount));
        mTxtTotalTaxes.setText(String.format("%.2f", mBillDetailsDTOs.getTotalTax()));
        //mTxtServicesCharges.setText(String.format("%.2f", mDiscount));
        mTxtDiscountAmount.setText(String.format("%.2f", mDiscount));
        mTxtTotalPayableAmnount.setText(String.format("%.2f", totalPaybleAmount));

        mBillDetailsDTOs.setDiscount(mDiscount);
        mBillDetailsDTOs.setNetAmount(netAmount);
        mBillDetailsDTOs.setTotalPayableAmt(totalPaybleAmount);
        mBillDetailsDTOs.setDeliveryChr(mDeliveryCharges);
    }

    public String printDineIn(HashMap<String, OrderDetailsDTO> billdetails, PrinterDetailsDTO printerDetail, RestaurantDTO restaurantDTO) {
        PrintBody printBody = new PrintBody();
        printBody.setMenus(billdetails);
        ROrderDateUtils dateUtils = new ROrderDateUtils();
        PrintHeader header = new PrintHeader("By : " + mSessionManager.getUserName(), "Table #:"
                + mTableNo, "Bill Date : " + dateUtils.getLocalDateInReadableFormat(new java.util.Date()) + " " + dateUtils.getLocalTimeInReadableFormat());
        String Full_Address = restaurantDTO.getmAddress().concat(",").concat(restaurantDTO.getmArea()).concat(",").concat(restaurantDTO.getmCity());
        header.setBmpIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.ic_launcher));

        if (Full_Address.length() >= 37) {
            String subStringFirst = Full_Address.substring(0, 37);
            String subStringSecond = Full_Address.substring(38);
            header.setAddress(subStringFirst + "\n" + "     " + subStringSecond);

        } else {
            header.setAddress(Full_Address);
        }

        //header.setAddress(restaurantDTO.getmArea());
        header.setNumber("Bill No.: " + mBillDetailsDTOs.getBillNo());
        header.setBillType("Dine-In");
        header.setRestaurantName(mSessionManager.getUserRestaurantName());
        header.setPhoneNumber(restaurantDTO.getmPhoneNumber());
        //  String footer = "Powered by QuickServe";
        String footer = restaurantDTO.getmFooter();
        PrintDataDTO printData = new PrintDataDTO();
        printData.setConfigBarcode(mDbRepository.getConfigValue(AppConstants.CONFIG_BARCODE_PRINT));
        printData.setHeader(header);
        printData.setFooter(footer);
        printData.setBody(printBody);
        printData.setType(PrintDataDTO.BILL_DINE_IN);
        printData.setBillDetailsDTO(mBillDetailsDTOs);
        PrinterFactory printerFactory = new PrinterFactory();
        PrintPaper printPaper = printerFactory.getPrinter(printerDetail);
        try {
            printPaper.setPrinter(getApplicationContext(), printerDetail);
        } catch (OpenPrinterException e) {
            //  customAlterDialog("Printer Error", e.getMessage());
            addError(screenName, "SetPrinter PrintDineIn", e.getMessage());
            return e.getMessage();
        }
        printPaper.openPrinter();
        try {
            printPaper.printText(printData);
        } catch (PrintException e) {
            //  customAlterDialog("Printer Error", e.getMessage());
            addError(screenName, "PrintText PrintDineIn", e.getMessage());
            return e.getMessage();
        }
        return "Success";
    }

    public String printTakeAway(HashMap<String, OrderDetailsDTO> billdetails, PrinterDetailsDTO printerDetail, RestaurantDTO restaurantDTO) {
        TakeAwayDTO takeAwayDTO = mDbRepository.getTakeAway(mTakeAwayNo);
        PrintBody printBody = new PrintBody();
        printBody.setMenus(billdetails);
        ROrderDateUtils dateUtils = new ROrderDateUtils();
        PrintHeader header = new PrintHeader("", "Take Away No.: #"
                + mTakeAwayNo, "Bill Date : " + dateUtils.getLocalDateInReadableFormat(new java.util.Date()) + " " + dateUtils.getLocalTimeInReadableFormat());
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.ic_launcher);
        String Full_Address = restaurantDTO.getmAddress().concat(",").concat(restaurantDTO.getmArea()).concat(",").concat(restaurantDTO.getmCity());
        header.setBmpIcon(icon);
        if (Full_Address.length() >= 37) {
            String subStringFirst = Full_Address.substring(0, 37);
            String subStringSecond = Full_Address.substring(38);
            header.setAddress(subStringFirst + "\n" + "     " + subStringSecond);

        } else {
            header.setAddress(Full_Address);
        }

        // header.setAddress(restaurantDTO.getmArea());
        header.setNumber("Bill No.: " + mBillDetailsDTOs.getBillNo());
        header.setBillType("Take Away");
        header.setCustName("Name: " + takeAwayDTO.getmCustName());
        header.setCustAddress("Address:" + takeAwayDTO.getmCustAddress() + "\n");
        header.setPhNo("Phone.:" + takeAwayDTO.getCustPhone());
        header.setRestaurantName(mSessionManager.getUserRestaurantName());
        header.setPhoneNumber(restaurantDTO.getmPhoneNumber());
        //  String footer = "Powered by QuickServe";
        String footer = restaurantDTO.getmFooter();
        PrintDataDTO printData = new PrintDataDTO();
        printData.setConfigBarcode(mDbRepository.getConfigValue(AppConstants.CONFIG_BARCODE_PRINT));
        printData.setHeader(header);
        printData.setFooter(footer);
        printData.setBody(printBody);
        printData.setType(PrintDataDTO.BILL_TAKE_AWAY);
        printData.setBillDetailsDTO(mBillDetailsDTOs);
        PrinterFactory printerFactory = new PrinterFactory();
        PrintPaper printPaper = printerFactory.getPrinter(printerDetail);
        try {
            printPaper.setPrinter(getApplicationContext(), printerDetail);
        } catch (OpenPrinterException e) {
            addError(screenName, "SetPrinter PrintTakeAway", e.getMessage());
            // customAlterDialog("Printer Error", e.getMessage());
            return e.getMessage();
        }
        printPaper.openPrinter();
        try {
            printPaper.printText(printData);
        } catch (PrintException e) {
            //  customAlterDialog("Printer Error", e.getMessage());
            addError(screenName, "PrintText PrintTakeAway", e.getMessage());
            return e.getMessage();
        }
        return "Success";
    }

    public String printDelivery(HashMap<String, OrderDetailsDTO> billdetails, PrinterDetailsDTO printerDetail, RestaurantDTO restaurantDTO) {
        DeliveryDTO deliveryDTO = mDbRepository.getDelivery(mDeliveryNo);
        PrintBody printBody = new PrintBody();
        printBody.setMenus(billdetails);
        ROrderDateUtils dateUtils = new ROrderDateUtils();
        PrintHeader header = new PrintHeader("", "Take Away No.: #"
                + mDeliveryNo, "Bill Date : " + dateUtils.getLocalDateInReadableFormat(new java.util.Date()) + " " + dateUtils.getLocalTimeInReadableFormat());
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.ic_launcher);
        String Full_Address = restaurantDTO.getmAddress().concat(",").concat(restaurantDTO.getmArea()).concat(",").concat(restaurantDTO.getmCity());
        header.setBmpIcon(icon);
        if (Full_Address.length() >= 37) {
            String subStringFirst = Full_Address.substring(0, 37);
            String subStringSecond = Full_Address.substring(38);
            header.setAddress(subStringFirst + "\n" + "     " + subStringSecond);

        } else {
            header.setAddress(Full_Address);
        }

        // header.setAddress(restaurantDTO.getmArea());
        header.setNumber("Bill No.: " + mBillDetailsDTOs.getBillNo());
        header.setBillType("Delivery");
        header.setCustName("Name: " + deliveryDTO.getmCustName());
        header.setCustAddress("Address:" + deliveryDTO.getmCustAddress() + "\n");
        header.setPhNo("Phone.:" + deliveryDTO.getCustPhone());
        header.setRestaurantName(mSessionManager.getUserRestaurantName());
        header.setPhoneNumber(restaurantDTO.getmPhoneNumber());
        //  String footer = "Powered by QuickServe";
        String footer = restaurantDTO.getmFooter();
        PrintDataDTO printData = new PrintDataDTO();
        printData.setConfigBarcode(mDbRepository.getConfigValue(AppConstants.CONFIG_BARCODE_PRINT));
        printData.setHeader(header);
        printData.setFooter(footer);
        printData.setBody(printBody);
        printData.setType(PrintDataDTO.BILL_TAKE_AWAY);
        printData.setBillDetailsDTO(mBillDetailsDTOs);
        PrinterFactory printerFactory = new PrinterFactory();
        PrintPaper printPaper = printerFactory.getPrinter(printerDetail);
        try {
            printPaper.setPrinter(getApplicationContext(), printerDetail);
        } catch (OpenPrinterException e) {
            addError(screenName, "SetPrinter PrintDelivery", e.getMessage());
            // customAlterDialog("Printer Error", e.getMessage());
            return e.getMessage();
        }
        printPaper.openPrinter();
        try {
            printPaper.printText(printData);
        } catch (PrintException e) {
            //  customAlterDialog("Printer Error", e.getMessage());
            addError(screenName, "PrintText PrintDelivery", e.getMessage());
            return e.getMessage();
        }
        return "Success";
    }

    private class AsyncPrint extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "Fail";
            ArrayList<PrinterDetailsDTO> detailsArray = new ArrayList<>();
            detailsArray = mDbRepository.getPrinterDetails(2);
            mBillDetailsDTOs.getBillNo();
            ArrayList<String> getOrderId = new ArrayList<>();
            getOrderId = mDbRepository.getOderIdForPrinting("3", custId);
            RestaurantDTO restaurantDTO = mDbRepository.getRestaurantDetails(mSessionManager.getUserRestaurantName());
            HashMap<String, OrderDetailsDTO> billdetails = mDbRepository.getMenuDetailsForOrderPrint(getOrderId);
            for (PrinterDetailsDTO printerDetail : detailsArray) {

                if (mTableId != 0) {
                    result = printDineIn(billdetails, printerDetail, restaurantDTO);
                } else if (mTakeAwayNo != 0) {
                    result = printTakeAway(billdetails, printerDetail, restaurantDTO);
                } else if (mDeliveryNo != 0) {
                    result = printDelivery(billdetails, printerDetail, restaurantDTO);
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            showProgress(false);
            try {
                if (!str.equals("Success"))
                    customAlterDialog("Printer Error", str);
            } catch (NullPointerException e) {
                addError(screenName, "AsyncPrint", e.getMessage());
            }

        }
    }
}
