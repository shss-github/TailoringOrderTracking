package in.collectiva.tailoringordertracking.Fragments;

<<<<<<< HEAD
import android.content.DialogInterface;
=======
import android.app.Activity;
import android.app.DatePickerDialog;
>>>>>>> origin/master
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
<<<<<<< HEAD
=======
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
>>>>>>> origin/master
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
import in.collectiva.tailoringordertracking.MyOrders;
import in.collectiva.tailoringordertracking.R;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class AllFragment extends Fragment {

<<<<<<< HEAD
    private String jsonString, lSelectedOrderId;
=======
    private TextView ltxtAllNoRecords;
    private ListView lstAll;
    private Spinner spinner;
    private EditText edAsOnDate;

    private String jsonString;
>>>>>>> origin/master
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

        // User Id
<<<<<<< HEAD
        String lUserId = user.get(SessionManagement.KEY_USERID);

        /*ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = lUserId;
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "OrderId";
        objParam.ParameterValue = "0";
        lstParameters.add(objParam);

        String lMethodName = "GetOrders";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);*/

        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = lUserId;
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "StatusId";
        objParam.ParameterValue = "1";
        lstParameters.add(objParam);

        String lMethodName = "GetOrdersByStatus";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        TextView ltxtAllNoRecords = (TextView) view.findViewById(R.id.txtAllNoRecords);
        ltxtAllNoRecords.setText("");

        if (jsonString.equals("0")) {
            ltxtAllNoRecords.setText("No records found!");
        } else {

            ListView lstAll = (ListView) view.findViewById(R.id.lstAllOrderList);
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), JSONOrder.newInstance().GetJSONOrderList(jsonString),
                    R.layout.orderrow, new String[]{"OrderId", "OrderDetail", "DeliveryDate", "Status"},
                    new int[]{R.id.txtOrderRowId, R.id.txtOrderRowDetail, R.id.txtOrderRowDeliveryDate,
                            R.id.txtOrderRowStatus});

            lstAll.setAdapter(simpleAdapter);

            lstAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // selected item
                    lSelectedOrderId = ((TextView) view.findViewById(R.id.txtOrderRowId)).getText().toString();
                    String OrderDetail = ((TextView) view.findViewById(R.id.txtOrderRowDeliveryDate)).getText().toString();

                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("Are you sure to Update the Status as 'In-Progress' for the Order " + OrderDetail + "?")
                            .setTitle("");

                    // Add the buttons
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button

                            ArrayList<clsParameters> lstParameters = new ArrayList<>();
                            clsParameters objParam = new clsParameters();
                            objParam.ParameterName = "OrderID";
                            objParam.ParameterValue = lSelectedOrderId;
                            lstParameters.add(objParam);

                            objParam = new clsParameters();
                            objParam.ParameterName = "StatusID";
                            objParam.ParameterValue = "2";
                            lstParameters.add(objParam);

                            String lMethodName = "UpdateOrderStatus";
                            jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

                            //Refresh the Grid in the Parent
                            MyOrders activity = (MyOrders) getActivity();
                            activity.BindTab(0);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();


                    dialog.show();
                }
            });
        }
=======
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

        Button btnGetResult = (Button) view.findViewById(R.id.btnGetOrder);
        btnGetResult.setOnClickListener(lbtnGetOrderDetail);

        txtDate = (EditText) view.findViewById(R.id.edtAsonDate);
        txtDate.setOnClickListener(lbtnDatePickerListener);
>>>>>>> origin/master
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
}

