package in.collectiva.tailoringordertracking.Fragments;


import android.content.DialogInterface;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.JSONFiles.JSONItems;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrder;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrderItemSummary;
import in.collectiva.tailoringordertracking.MyOrders;
import in.collectiva.tailoringordertracking.R;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class AllFragment extends Fragment {


    private String jsonString, lSelectedOrderId;

    private TextView ltxtAllNoRecords;
    private ListView lstAll;
    private ListView lstAllSummary;
    private Spinner spinner;
    private EditText edAsOnDate;

    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();

    // Session Manager Class
    SessionManagement session;
    String lUserId = "";

    EditText txtDate;
    private int mYear, mMonth, mDay, mHour, mMinute;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Session Manager
        session = new SessionManagement(getActivity().getApplicationContext());

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.fragment_all, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        lUserId = user.get(SessionManagement.KEY_USERID);

        edAsOnDate = (EditText) view.findViewById(R.id.edtAsonDate);
        edAsOnDate.setOnClickListener(lbtnDatePickerListener);
        edAsOnDate.setVisibility(View.GONE);

        spinner = (Spinner) view.findViewById(R.id.cboOption);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()) {
                    case "All":
                        edAsOnDate.setVisibility(View.GONE);
                        edAsOnDate.setText("");
                        break;
                    case "Today":
                        edAsOnDate.setVisibility(View.GONE);
                        edAsOnDate.setText("");
                        break;
                    case "Next 7 days":
                        edAsOnDate.setVisibility(View.GONE);
                        edAsOnDate.setText("");
                        break;
                    case "As on":
                        edAsOnDate.setVisibility(View.VISIBLE);
                        edAsOnDate.setText("");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.orders_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        ltxtAllNoRecords = (TextView) view.findViewById(R.id.txtAllNoRecords);
        lstAll = (ListView) view.findViewById(R.id.lstAllOrderList);
        lstAllSummary = (ListView) view.findViewById(R.id.lstAllOrderListSummary);

        // Load OrderList and OrderItemSummaryList
        LoadListOrders();
        LoadListOrderDetailItemSummary();

        Button btnGetResult = (Button) view.findViewById(R.id.btnGetOrder);
        btnGetResult.setOnClickListener(lbtnGetOrderDetail);

        txtDate = (EditText) view.findViewById(R.id.edtAsonDate);
        txtDate.setOnClickListener(lbtnDatePickerListener);
    }

    private View.OnClickListener lbtnGetOrderDetail = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean ProceedToGet = true;
            if (spinner.getSelectedItem().toString().trim().equals("As on")) {
                if (edAsOnDate.getText().toString().trim().equals("")) {
                    ProceedToGet = false;
                    edAsOnDate.setError("Date is required!");
                }
            }

            if (ProceedToGet) {
                try {
                    LoadListOrders();
                    LoadListOrderDetailItemSummary();
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private View.OnClickListener lbtnDatePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    };

    private  void  LoadListOrders()
    {
        try {
            ArrayList<clsParameters> lstParameters = new ArrayList<>();
            clsParameters objParam = new clsParameters();
            objParam.ParameterName = "UserId";
            objParam.ParameterValue = lUserId;
            lstParameters.add(objParam);

            objParam = new clsParameters();
            objParam.ParameterName = "FilterBy";
            objParam.ParameterValue = spinner.getSelectedItem().toString();
            lstParameters.add(objParam);

            objParam = new clsParameters();
            objParam.ParameterName = "FilterDate";
            objParam.ParameterValue = edAsOnDate.getText().toString();
            lstParameters.add(objParam);

            String lMethodName = "GetOrdersByParticular";
            jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

            ltxtAllNoRecords.setText("");

            if (jsonString.equals("0")) {
                ltxtAllNoRecords.setText("No records found!");
            } else {
                // ltxtAllNoRecords.setText("records found!");
                SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), JSONOrder.newInstance().GetJSONOrderList(jsonString),
                        R.layout.orderrow, new String[]{"OrderId", "OrderDetail", "DeliveryDate", "Status"},
                        new int[]{R.id.txtOrderRowId, R.id.txtOrderRowDetail, R.id.txtOrderRowDeliveryDate,
                                R.id.txtOrderRowStatus});

                lstAll.setAdapter(simpleAdapter);
            }
        } catch(Exception e) {
            throw e;
        }
    }

    private void LoadListOrderDetailItemSummary()
    {
        try {
            ArrayList<clsParameters> lstParametersItemSummary = new ArrayList<>();
            clsParameters objParam = new clsParameters();
            objParam.ParameterName = "UserId";
            objParam.ParameterValue = lUserId;
            lstParametersItemSummary.add(objParam);

            objParam = new clsParameters();
            objParam.ParameterName = "FilterBy";
            objParam.ParameterValue = spinner.getSelectedItem().toString();
            lstParametersItemSummary.add(objParam);

            objParam = new clsParameters();
            objParam.ParameterName = "FilterDate";
            objParam.ParameterValue = edAsOnDate.getText().toString();
            lstParametersItemSummary.add(objParam);

            String lMethodName = "GetOrderDetailByParticulars";
            jsonString = "";
            jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParametersItemSummary);

            ltxtAllNoRecords.setText("");

            if (jsonString.equals("0")) {
                ltxtAllNoRecords.setText("No records found!");
            } else {
                Log.d("jsonString",jsonString);
                SimpleAdapter simpleAdapterItemSummary = new SimpleAdapter(getActivity(), JSONOrderItemSummary.newInstance().GetJSONOrderItemSummaryList(jsonString),
                        R.layout.order_item_summary_row, new String[]{"OrderItemName", "OrderNoOfQty"},
                        new int[]{R.id.txtOrderItemSummaryRowItemName, R.id.txtOrderItemSummaryRowNoOfQty});

                lstAllSummary.setAdapter(simpleAdapterItemSummary);
            }
        } catch(Exception e) {
            throw e;
        }
    }
}

