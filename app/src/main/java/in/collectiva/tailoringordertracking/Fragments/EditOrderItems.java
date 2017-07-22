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
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.AddOrderItems;
import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.Item;
import in.collectiva.tailoringordertracking.JSONFiles.JSONItems;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrderDetail;
import in.collectiva.tailoringordertracking.R;
import in.collectiva.tailoringordertracking.cConstant.clsOrderDetail;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class EditOrderItems extends DialogFragment {
    private Spinner lsprModifyOrderItemName;
    private TextView ltxtModifyOrderItemId, ltxtModifyOrderItemOrderId;

    private EditText ledtModifyOrderItemRate;
    private EditText ledtModifyOrderItemQty;
    private EditText ledtModifyOrderItemAmount;

    private Button lbtnModifyOrderItemSave;
    private Button lbtnModifyOrderItemDelete;

    // Session Manager Class
    SessionManagement session;
    double lRate = 0.00;

    private static final String lSelectedOrderDetailID = "SelectedOrderDetailID";

    private String jsonString;
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();

    public EditOrderItems() {
        // Required empty public constructor
    }

    public static EditOrderItems newInstance(String SelectedOrderDetailID) {
        EditOrderItems fragment = new EditOrderItems();
        Bundle args = new Bundle();
        args.putString(lSelectedOrderDetailID, SelectedOrderDetailID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Session Manager
        session = new SessionManagement(getActivity().getApplicationContext());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_order_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ltxtModifyOrderItemId = (TextView) view.findViewById(R.id.txtModifyOrderItemId);
        ltxtModifyOrderItemOrderId = (TextView) view.findViewById(R.id.txtModifyOrderItemOrderId);

        ledtModifyOrderItemRate = (EditText) view.findViewById(R.id.edtModifyOrderItemRate);
        ledtModifyOrderItemQty = (EditText) view.findViewById(R.id.edtModifyOrderItemQty);
        ledtModifyOrderItemAmount = (EditText) view.findViewById(R.id.edtModifyOrderItemAmount);

        lbtnModifyOrderItemSave = (Button) view.findViewById(R.id.btnModifyOrderItemSave);
        lbtnModifyOrderItemDelete = (Button) view.findViewById(R.id.btnModifyOrderItemDelete);
        lbtnModifyOrderItemSave.setOnClickListener(lbtnModifyOrderItemSaveListener);
        lbtnModifyOrderItemDelete.setOnClickListener(lbtnModifyOrderItemDeleteListener);

        lsprModifyOrderItemName = (Spinner) view.findViewById(R.id.sprModifyOrderItemName);
        BindSpinner(lsprModifyOrderItemName);

        ltxtModifyOrderItemId.setText(getArguments().getString(lSelectedOrderDetailID));

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String lUserId = user.get(SessionManagement.KEY_USERID);

        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "OrderDetailId";
        objParam.ParameterValue = ltxtModifyOrderItemId.getText().toString();
        lstParameters.add(objParam);

        String lMethodName = "GetOrderDetailById";
        String jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        clsOrderDetail obj = JSONOrderDetail.newInstance().GetJSONOrderDetail(jsonString);

        ltxtModifyOrderItemOrderId.setText(String.valueOf(obj.OrderId));
        selectSpinnerValue(lsprModifyOrderItemName, String.valueOf(obj.ItemId));
        ledtModifyOrderItemQty.setText(String.valueOf(obj.Qty));
        ledtModifyOrderItemRate.setText(String.valueOf(obj.Rate));
        ledtModifyOrderItemAmount.setText(String.valueOf(obj.Amount));

        ImageView img = (ImageView) view.findViewById(R.id.imgEditOrderClose);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditOrderItems.this.getDialog().dismiss();
            }
        });

        lsprModifyOrderItemName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        ledtModifyOrderItemQty.addTextChangedListener(new TextWatcher() {
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
        lsprModifyOrderItemName.requestFocus();
    }

    private void selectSpinnerValue(Spinner spinner, String myVal)
    {
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            HashMap<String, String> litem = (HashMap<String, String>)spinner.getItemAtPosition(i);
            if( String.valueOf(litem.get("ItemId")).equals(myVal)){
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void calculateAmount() {
        Integer lQty = 0;
        double lAmount = 0.00;

        if(ledtModifyOrderItemQty.getText().toString().trim().equals("") != true && lRate > 0.00)
        {
            lQty = Integer.parseInt(ledtModifyOrderItemQty.getText().toString());
            lAmount = (lQty * lRate);
        }

        ledtModifyOrderItemRate.setText(String.valueOf(lRate));
        ledtModifyOrderItemAmount.setText(String.valueOf(lAmount));
    }

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

    private View.OnClickListener lbtnModifyOrderItemSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            boolean ProceedToSave = true;
            if (ledtModifyOrderItemQty.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledtModifyOrderItemQty.setError("Quantity is required!");
            }
            try {
                if(ProceedToSave)
                {
                    // Calculate Order amount
                    HashMap<String, String> litem = (HashMap<String, String>)lsprModifyOrderItemName.getSelectedItem();

                    ArrayList lstParameters = new ArrayList<>();
                    clsParameters objParam = new clsParameters();
                    objParam.ParameterName = "OrderId";
                    objParam.ParameterValue = ltxtModifyOrderItemOrderId.getText().toString();
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "OrderDetailId";
                    objParam.ParameterValue = ltxtModifyOrderItemId.getText().toString();
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "ItemId";
                    objParam.ParameterValue = litem.get("ItemId");
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "Qty";
                    objParam.ParameterValue = ledtModifyOrderItemQty.getText().toString();
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "Rate";
                    objParam.ParameterValue = ledtModifyOrderItemRate.getText().toString();
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "Amount";
                    objParam.ParameterValue = ledtModifyOrderItemAmount.getText().toString();
                    lstParameters.add(objParam);

                    String lMethodName = "SaveOrderDetail";
                    String resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

                    Toast.makeText(getActivity().getApplicationContext(), "Successfully Saved!", Toast.LENGTH_LONG).show();
                    EditOrderItems.this.getDialog().dismiss();

                    //Refresh the Grid in the Parent
                    AddOrderItems activity = (AddOrderItems) getActivity();
                    activity.BindData(ltxtModifyOrderItemOrderId.getText().toString());
                }else {
                    //NewOrderItems.this.getDialog().cancel();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private View.OnClickListener lbtnModifyOrderItemDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList lstParameters = new ArrayList<>();
            clsParameters objParam = new clsParameters();
            objParam.ParameterName = "OrderId";
            objParam.ParameterValue = ltxtModifyOrderItemOrderId.getText().toString();
            lstParameters.add(objParam);

            objParam = new clsParameters();
            objParam.ParameterName = "OrderDetailId";
            objParam.ParameterValue = ltxtModifyOrderItemId.getText().toString();
            lstParameters.add(objParam);

            String lMethodName = "DeleteOrderDetailById";
            String resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

            Toast.makeText(getActivity().getApplicationContext(), "Successfully Deleted!", Toast.LENGTH_LONG).show();

            EditOrderItems.this.getDialog().dismiss();

            //Refresh the Grid in the Parent
            AddOrderItems activity = (AddOrderItems) getActivity();
            activity.BindData(ltxtModifyOrderItemOrderId.getText().toString());
        }
    };
}
