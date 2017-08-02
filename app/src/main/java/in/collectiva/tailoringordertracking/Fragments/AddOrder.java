package in.collectiva.tailoringordertracking.Fragments;


import android.Manifest;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.GeneralMethods;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.HomeMenu;
import in.collectiva.tailoringordertracking.Item;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrder;
import in.collectiva.tailoringordertracking.OrderEntry;
import in.collectiva.tailoringordertracking.R;
import in.collectiva.tailoringordertracking.cConstant.clsOrder;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddOrder extends DialogFragment {

    private EditText ledtAddOrderName;
    private EditText ledtAddOrderMobileNo;
    private EditText ledtAddOrderDeliveryDate;
    private Button lbtnAddOrderSave;
    private Button lbtnAddOrderClose;

    // Session Manager Class
    SessionManagement session;

    final CRUDProcess objCRUD = new CRUDProcess();
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    EditText txtDate;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public AddOrder() {
        // Required empty public constructor
    }

    public static AddOrder newInstance(String title) {
        AddOrder frag = new AddOrder();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Session Manager
        session = new SessionManagement(getActivity().getApplicationContext());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field from view and set events.
        ledtAddOrderName = (EditText) view.findViewById(R.id.edtAddOrderName);
        ledtAddOrderMobileNo = (EditText) view.findViewById(R.id.edtAddOrderMobileNo);
        ledtAddOrderDeliveryDate = (EditText) view.findViewById(R.id.edtAddOrderDeliveryDate);
        lbtnAddOrderSave = (Button) view.findViewById(R.id.btnAddOrderSave);
        lbtnAddOrderClose = (Button) view.findViewById(R.id.btnAddOrderClose);

        lbtnAddOrderSave.setOnClickListener(lbtnAddOrderSaveListener);
        lbtnAddOrderClose.setOnClickListener(lbtnAddOrderCloseListener);

        txtDate = (EditText) view.findViewById(R.id.edtAddOrderDeliveryDate);
        txtDate.setOnClickListener(lbtnDatePickerListener);

        // Set title
        getDialog().setTitle("Add Order");
        // Show soft keyboard automatically and request focus to field
        ledtAddOrderName.requestFocus();
        //getDialog().getWindow().setSoftInputMode(
        //WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

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
                            //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            txtDate.setText(dayOfMonth + "-" + new DateFormatSymbols().getMonths()[monthOfYear] + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    };


    private View.OnClickListener lbtnAddOrderSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            boolean ProceedToSave = true;
            if (ledtAddOrderName.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledtAddOrderName.setError("Name is required!");
            } else if (ledtAddOrderMobileNo.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledtAddOrderMobileNo.setError("Mobile No. is required!");
            } else if (ledtAddOrderDeliveryDate.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledtAddOrderDeliveryDate.setError("Delivery Date is required!");
            }

            try {
                if(ProceedToSave == true) {
                    // get user data from session
                    HashMap<String, String> user = session.getUserDetails();
                    String lShopName = user.get(SessionManagement.KEY_SHOPNAME);
                    String lUserId = user.get(SessionManagement.KEY_USERID);

                    //Here Creating List for the Parameters, which we need to pass to the method.
                    ArrayList lstParameters = new ArrayList<>();
                    clsParameters objParam = new clsParameters();
                    objParam.ParameterName = "OrderId";
                    objParam.ParameterValue = "0";
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "Name";
                    objParam.ParameterValue = ledtAddOrderName.getText().toString();
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "MobileNo";
                    objParam.ParameterValue = ledtAddOrderMobileNo.getText().toString();
                    lstParameters.add(objParam);

                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                    objParam = new clsParameters();
                    objParam.ParameterName = "DeliveryDate";
                    objParam.ParameterValue = ledtAddOrderDeliveryDate.getText().toString(); //"2017-07-12";
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "UserId";
                    objParam.ParameterValue = lUserId;
                    lstParameters.add(objParam);

                    String lMethodName = "SaveOrders";
                    String resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

                    clsOrder obj = JSONOrder.newInstance().GetJSONOrder(resultData);

                    SendOrderSMS(obj);

                    Toast.makeText(getActivity().getApplicationContext(), "Successfully Saved!", Toast.LENGTH_LONG).show();

                    AddOrder.this.getDialog().dismiss();

                    //Refresh the Grid in the Parent
                    OrderEntry activity = (OrderEntry) getActivity();
                    activity.BindListView();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private void SendOrderSMS(clsOrder lObj) //(String OrderId)
    {
        String SMSMessage = getResources().getString(R.string.SMSOrderEntry);;
        SMSMessage = SMSMessage.replace("{OrderNo}", lObj.OrderNo);
        SMSMessage = SMSMessage.replace("{OrderDate}", lObj.OrderDt);
        SMSMessage = SMSMessage.replace("{DeliveryDate}", lObj.DeliveryDt);
        SMSMessage = SMSMessage.replace("{TailorShopName}", lObj.ShopName);

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},1);
        GeneralMethods.fnSendSMS(lObj.MobileNo, SMSMessage);

        /*HashMap<String, String> user = session.getUserDetails();
        String lUserId = user.get(SessionManagement.KEY_USERID);

        ArrayList lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = lUserId;
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "OrderId";
        objParam.ParameterValue = OrderId;
        lstParameters.add(objParam);

        String lMethodName = "GetOrders";
        String jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        Toast.makeText(getActivity(), jsonString, Toast.LENGTH_LONG).show();
        if (jsonString.equals("0")) {
            Toast.makeText(getActivity(), "Empty Data", Toast.LENGTH_LONG).show();
        }
        else {
            clsOrder obj = JSONOrder.newInstance().GetJSONOrder(jsonString);

            String SMSMessage = "Your Order No. {OrderNo} dt. {OrderDate} Delivery Date : {DeliveryDate} {TailorShopName}";
            SMSMessage = SMSMessage.replace("{OrderNo}", obj.OrderNo);
            SMSMessage = SMSMessage.replace("{OrderDate}", obj.OrderDt);
            SMSMessage = SMSMessage.replace("{DeliveryDate}", obj.DeliveryDt);
            SMSMessage = SMSMessage.replace("{TailorShopName}", obj.ShopName);

            Toast.makeText(getActivity(), SMSMessage, Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},1);
            GeneralMethods.fnSendSMS("8608778742", SMSMessage);
            Toast.makeText(getActivity(), "Msg Sent Successfully", Toast.LENGTH_LONG).show();
        }*/
    }

    private View.OnClickListener lbtnAddOrderCloseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AddOrder.this.getDialog().cancel();
        }
    };

}
