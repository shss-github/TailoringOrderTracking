package in.collectiva.tailoringordertracking.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.AddOrderItems;
import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.Item;
import in.collectiva.tailoringordertracking.JSONFiles.JSONItems;
import in.collectiva.tailoringordertracking.Order;
import in.collectiva.tailoringordertracking.R;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class NewOrderItems extends DialogFragment {

    private Spinner lsprNewOrderItemName;
    private TextView ltxtNewOrderId;

    private EditText ledtNewOrderItemRate;
    private EditText ledtNewOrderItemQty;
    private EditText ledtNewOrderItemAmount;

    private Button lbtnNewOrderItemSave;
    private Button lbtnNewOrderItemClose;

    // Session Manager Class
    SessionManagement session;
    double lRate = 0.00;

    private static final String lOrderId = "OrderId";

    private String jsonString;
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();

    public NewOrderItems() {
        // Required empty public constructor
    }

    public static NewOrderItems newInstance(String OrderId) {
        NewOrderItems fragment = new NewOrderItems();
        Bundle args = new Bundle();
        args.putString(lOrderId, OrderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Session Manager
        session = new SessionManagement(getActivity().getApplicationContext());

         // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_order_items, container, false);
    }

    private void calculateAmount() {
        Integer lQty = 0;
        double lAmount = 0.00;

        if(ledtNewOrderItemQty.getText().toString().trim().equals("") != true && lRate > 0.00)
        {
            lQty = Integer.parseInt(ledtNewOrderItemQty.getText().toString());
            lAmount = (lQty * lRate);
        }

        ledtNewOrderItemRate.setText(String.valueOf(lRate));
        ledtNewOrderItemAmount.setText(String.valueOf(lAmount));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ltxtNewOrderId = (TextView) view.findViewById(R.id.txtNewOrderId);

        ledtNewOrderItemRate = (EditText) view.findViewById(R.id.edtNewOrderItemRate);
        ledtNewOrderItemQty = (EditText) view.findViewById(R.id.edtNewOrderItemQty);
        ledtNewOrderItemAmount = (EditText) view.findViewById(R.id.edtNewOrderItemAmount);

        lbtnNewOrderItemSave = (Button) view.findViewById(R.id.btnNewOrderItemSave);
        lbtnNewOrderItemClose = (Button) view.findViewById(R.id.btnNewOrderItemClose);
        lbtnNewOrderItemSave.setOnClickListener(lbtnNewOrderItemSaveListener);
        lbtnNewOrderItemClose.setOnClickListener(lbtnNewOrderItemCloseListener);

        ltxtNewOrderId.setText(getArguments().getString(lOrderId));

        lsprNewOrderItemName = (Spinner) view.findViewById(R.id.sprNewOrderItemName);
        BindSpinner(lsprNewOrderItemName);

        lsprNewOrderItemName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Calculate Order amount
                HashMap<String, String> litem = (HashMap<String, String>)parent.getItemAtPosition(position);
                if(litem != null)
                {
                    lRate = Double.parseDouble(litem.get("Amount"));
                }
                else {
                    lRate = 0.00;
                }
                calculateAmount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ledtNewOrderItemQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateAmount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getDialog().setTitle("Add Order Item");
        lsprNewOrderItemName.requestFocus();
    }

    private View.OnClickListener lbtnNewOrderItemSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            boolean ProceedToSave = true;
            if (ledtNewOrderItemQty.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledtNewOrderItemQty.setError("Quantity is required!");
            } else {
                // Calculate Order amount
                HashMap<String, String> litem1 = (HashMap<String, String>)lsprNewOrderItemName.getSelectedItem();

                ArrayList lstParameters1 = new ArrayList<>();
                clsParameters objParam1 = new clsParameters();
                objParam1.ParameterName = "OrderId";
                objParam1.ParameterValue = ltxtNewOrderId.getText().toString();
                lstParameters1.add(objParam1);

                objParam1 = new clsParameters();
                objParam1.ParameterName = "OrderDetailId";
                objParam1.ParameterValue = "0";
                lstParameters1.add(objParam1);

                objParam1 = new clsParameters();
                objParam1.ParameterName = "ItemId";
                objParam1.ParameterValue = litem1.get("ItemId");
                lstParameters1.add(objParam1);

                String lMethodName1 = "CheckDuplicateItemInOrderDetail";
                String resultData1 = objCRUD.GetScalar(NAMESPACE, lMethodName1, REQURL, SOAP_ACTION + lMethodName1, lstParameters1);
                ProceedToSave = Boolean.parseBoolean(resultData1);

                if(ProceedToSave){
                    ProceedToSave = false;
                    Toast.makeText(getActivity(), "Item Name already exists.", Toast.LENGTH_SHORT).show();
                } else {
                    ProceedToSave = true;
                }
            }

            try {
                if(ProceedToSave)
                {
                    // Calculate Order amount
                    HashMap<String, String> litem = (HashMap<String, String>)lsprNewOrderItemName.getSelectedItem();

                    ArrayList lstParameters = new ArrayList<>();
                    clsParameters objParam = new clsParameters();
                    objParam.ParameterName = "OrderId";
                    objParam.ParameterValue = ltxtNewOrderId.getText().toString();
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "OrderDetailId";
                    objParam.ParameterValue = "0";
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "ItemId";
                    objParam.ParameterValue = litem.get("ItemId");
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "Qty";
                    objParam.ParameterValue = ledtNewOrderItemQty.getText().toString();
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "Rate";
                    objParam.ParameterValue = ledtNewOrderItemRate.getText().toString();
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "Amount";
                    objParam.ParameterValue = ledtNewOrderItemAmount.getText().toString();
                    lstParameters.add(objParam);

                    String lMethodName = "SaveOrderDetail";
                    String resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

                    Toast.makeText(getActivity().getApplicationContext(), "Successfully Saved!", Toast.LENGTH_LONG).show();
                    NewOrderItems.this.getDialog().dismiss();

                    //Refresh the Grid in the Parent
                    AddOrderItems activity = (AddOrderItems) getActivity();
                    activity.BindData(ltxtNewOrderId.getText().toString());
                }else {
                    //NewOrderItems.this.getDialog().cancel();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private View.OnClickListener lbtnNewOrderItemCloseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NewOrderItems.this.getDialog().cancel();
        }
    };

    public void BindSpinner(Spinner lspinner)
    {
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String lUserId = user.get(SessionManagement.KEY_USERID);

        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = lUserId;
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "ItemId";
        objParam.ParameterValue = "0";
        lstParameters.add(objParam);

        String lMethodName = "GetItems";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),
                JSONItems.newInstance().GetJSONItemList(jsonString), R.layout.item_spinner_row,
                new String[] {"ItemId", "ItemName", "Amount"},
                new int[] {R.id.txtSpinnerItemId, R.id.txtSpinnerItemName, R.id.txtSpinnerItemAmount});

        // Drop down layout style - list view with radio button
        //simpleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        lspinner.setAdapter(simpleAdapter);
        //lspinner.setOnItemSelectedListener(Order.this);
    }
}
