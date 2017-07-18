package in.collectiva.tailoringordertracking.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.Item;
import in.collectiva.tailoringordertracking.JSONFiles.JSONItems;
import in.collectiva.tailoringordertracking.R;
import in.collectiva.tailoringordertracking.cConstant.clsItems;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditItem.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditItem extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String lItemId = "ItemId";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView ltxtEditItemId;
    private EditText ledtEditItemName;
    private EditText ledtEditItemAmount;
    private Button lbtnEditItemUpdate;
    private Button lbtnEditItemDelete;


    // Session Manager Class
    SessionManagement session;

    final CRUDProcess objCRUD = new CRUDProcess();
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    public EditItem() {
        // Required empty public constructor
    }

    public static EditItem newInstance(String ItemId) {
        EditItem fragment = new EditItem();
        Bundle args = new Bundle();
        args.putString(lItemId, ItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Session Manager
        session = new SessionManagement(getActivity().getApplicationContext());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field from view and set events.
        ltxtEditItemId = (TextView) view.findViewById(R.id.txtEditItemId);
        ltxtEditItemId.setText(getArguments().getString(lItemId));

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String lUserId = user.get(SessionManagement.KEY_USERID);

        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = lUserId;
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "ItemId";
        objParam.ParameterValue = ltxtEditItemId.getText().toString();
        lstParameters.add(objParam);

        String lMethodName = "GetItems";
        String jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        clsItems obj = JSONItems.newInstance().GetJSONItem(jsonString);

        ledtEditItemName = (EditText) view.findViewById(R.id.edtEditItemName);
        ledtEditItemAmount = (EditText) view.findViewById(R.id.edtEditItemAmount);

        ledtEditItemName.setText(obj.ItemName);
        ledtEditItemAmount.setText(obj.Amount.toString());

        lbtnEditItemUpdate = (Button) view.findViewById(R.id.btnEditItemUpdate);
        lbtnEditItemDelete = (Button) view.findViewById(R.id.btnEditItemDelete);
        lbtnEditItemUpdate.setOnClickListener(lbtnEditItemUpdateListener);
        lbtnEditItemDelete.setOnClickListener(lbtnEditItemDeleteListener);

        ImageView img = (ImageView) view.findViewById(R.id.imgEditItemClose);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditItem.this.getDialog().dismiss();
            }
        });

        // Set title
        getDialog().setTitle(R.string.dialog_title_items);
        // Show soft keyboard automatically and request focus to field
        ledtEditItemName.requestFocus();
        //getDialog().getWindow().setSoftInputMode(
        //WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private View.OnClickListener lbtnEditItemUpdateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

             boolean ProceedToSave = true;
            if (ledtEditItemName.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledtEditItemName.setError("Item Name is required!");
            } else if (ledtEditItemAmount.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledtEditItemAmount.setError("Amount is required!");
            }

            try {
                // get user data from session
                HashMap<String, String> user = session.getUserDetails();
                String lUserId = user.get(SessionManagement.KEY_USERID);

                //Here Creating List for the Parameters, which we need to pass to the method.
                ArrayList lstParameters = new ArrayList<>();
                clsParameters objParam = new clsParameters();
                objParam.ParameterName = "ItemId";
                objParam.ParameterValue = ltxtEditItemId.getText().toString();
                lstParameters.add(objParam);

                objParam = new clsParameters();
                objParam.ParameterName = "ItemName";
                objParam.ParameterValue = ledtEditItemName.getText().toString();
                lstParameters.add(objParam);

                objParam = new clsParameters();
                objParam.ParameterName = "Amount";
                objParam.ParameterValue = ledtEditItemAmount.getText().toString();
                lstParameters.add(objParam);

                objParam = new clsParameters();
                objParam.ParameterName = "UserId";
                objParam.ParameterValue = lUserId;
                lstParameters.add(objParam);

                String lMethodName = "InsertItem";
                String resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

                Toast.makeText(getActivity().getApplicationContext(), "Successfully Updated!", Toast.LENGTH_LONG).show();

                EditItem.this.getDialog().dismiss();

                //Refresh the Grid in the Parent
                Item activity = (Item) getActivity();
                activity.BindListView();

            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private View.OnClickListener lbtnEditItemDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Here Creating List for the Parameters, which we need to pass to the method.
            ArrayList lstParameters = new ArrayList<>();
            clsParameters objParam = new clsParameters();
            objParam.ParameterName = "ItemId";
            objParam.ParameterValue = ltxtEditItemId.getText().toString();
            lstParameters.add(objParam);

            String lMethodName = "DeleteItemById";
            String resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

            Toast.makeText(getActivity().getApplicationContext(), "Successfully Deleted!", Toast.LENGTH_LONG).show();

            EditItem.this.getDialog().dismiss();

            //Refresh the Grid in the Parent
            Item activity = (Item) getActivity();
            activity.BindListView();
        }
    };
}
