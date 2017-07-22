package in.collectiva.tailoringordertracking;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrder;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class DeliveredList extends AppCompatActivity {

    private String jsonString, lSelectedOrderId;
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();
    // Session Manager Class
    SessionManagement session;

    Button btnGetDetail;
    EditText edtFromDate;
    EditText edtToDate;
    EditText txtFDate;
    EditText txtTDate;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_list);

        // Session Manager
        session = new SessionManagement(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // User Id
        String lUserId = user.get(SessionManagement.KEY_USERID);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String CurDate = sdf.format(new Date());

        edtFromDate = (EditText) findViewById(R.id.edtFromDate);
        edtFromDate.setOnClickListener(lbtnFDatePickerListener);
        edtFromDate.setText(CurDate);

        edtToDate = (EditText) findViewById(R.id.edtToDate);
        edtToDate.setOnClickListener(lbtnTDatePickerListener);
        edtToDate.setText(CurDate);

        txtFDate = (EditText) findViewById(R.id.edtFromDate);
        txtFDate.setOnClickListener(lbtnFDatePickerListener);

        txtTDate = (EditText) findViewById(R.id.edtToDate);
        txtTDate.setOnClickListener(lbtnTDatePickerListener);

        btnGetDetail = (Button) findViewById(R.id.btnGetDetail);
        btnGetDetail.setOnClickListener(lbtnGetDetails);

        LoadData(lUserId, CurDate, CurDate);
    }

    private View.OnClickListener lbtnGetDetails = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean ProceedToGet = true;
            if (edtFromDate.getText().toString().trim().equals("")) {
                ProceedToGet = false;
                edtFromDate.setError("Date is required!");
            }

            if (edtToDate.getText().toString().trim().equals("")) {
                ProceedToGet = false;
                edtToDate.setError("Date is required!");
            }

            if (ProceedToGet) {
                // Session Manager
                session = new SessionManagement(getApplicationContext());

                // get user data from session
                HashMap<String, String> user = session.getUserDetails();

                // User Id
                String lUserId = user.get(SessionManagement.KEY_USERID);

                String FDate = edtFromDate.getText().toString();
                String TDate = edtToDate.getText().toString();

                LoadData(lUserId, FDate, TDate);
            }
        }
    };

    private View.OnClickListener lbtnFDatePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(DeliveredList.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            txtFDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    };

    private View.OnClickListener lbtnTDatePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(DeliveredList.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            txtTDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    };

    private void LoadData(String lUserId, String FDate, String TDate) {

        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = lUserId;
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "FDate";
        objParam.ParameterValue = FDate;
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "TDate";
        objParam.ParameterValue = TDate;
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "StatusId";
        objParam.ParameterValue = "4";
        lstParameters.add(objParam);

        String lMethodName = "GetOrdersByStatusList";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        TextView ltxtMakeNoRecords = (TextView) findViewById(R.id.txtDeliveredNoRecords);
        ltxtMakeNoRecords.setText("");

        if (jsonString.equals("0")) {
            ltxtMakeNoRecords.setText("No records found!");
        } else {
            ListView lstAll = (ListView) findViewById(R.id.lstDeliveredList);
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONOrder.newInstance().GetJSONOrderList(jsonString),
                    R.layout.orderrow, new String[]{"OrderId", "OrderDetail", "DeliveryDate", "Status"},
                    new int[]{R.id.txtOrderRowId, R.id.txtOrderRowDetail, R.id.txtOrderRowDeliveryDate,
                            R.id.txtOrderRowStatus});

            lstAll.setAdapter(simpleAdapter);
        }
    }
}
