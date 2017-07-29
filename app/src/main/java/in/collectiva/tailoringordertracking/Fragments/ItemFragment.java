package in.collectiva.tailoringordertracking.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.HomeMenu;
import in.collectiva.tailoringordertracking.Item;
import in.collectiva.tailoringordertracking.R;
import in.collectiva.tailoringordertracking.Register;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends DialogFragment {

    private EditText ledt_ItemName;
    private EditText ledt_ItemAmount;
    private Button lbtnSave;
    private Button lbtnClose;

    // Session Manager Class
    SessionManagement session;

    final CRUDProcess objCRUD = new CRUDProcess();
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    public ItemFragment() {
        // Empty constructor required for DialogFragment
    }

    public static ItemFragment newInstance(String title) {
        ItemFragment frag = new ItemFragment();
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
        return inflater.inflate(R.layout.fragment_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view and set events.
        ledt_ItemName = (EditText) view.findViewById(R.id.txtItem);
        ledt_ItemAmount = (EditText) view.findViewById(R.id.edt_ItemAmount);
        lbtnSave = (Button) view.findViewById(R.id.btnItemSave);
        lbtnClose = (Button) view.findViewById(R.id.btnItemClose);
        lbtnSave.setOnClickListener(lbtnSaveListener);
        lbtnClose.setOnClickListener(lbtnCloseListener);

        // Set title
        getDialog().setTitle(R.string.dialog_title_items);
        // Show soft keyboard automatically and request focus to field
        ledt_ItemName.requestFocus();
        //getDialog().getWindow().setSoftInputMode(
          //WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private View.OnClickListener lbtnSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            boolean ProceedToSave = true;
            if (ledt_ItemName.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledt_ItemName.setError("Item Name is required!");
            } else if (ledt_ItemAmount.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledt_ItemAmount.setError("Amount is required!");
            }

            try {
                if(ProceedToSave) {
                    // get user data from session
                    HashMap<String, String> user = session.getUserDetails();
                    String lUserId = user.get(SessionManagement.KEY_USERID);

                    //Here Creating List for the Parameters, which we need to pass to the method.
                    ArrayList lstParameters = new ArrayList<>();
                    clsParameters objParam = new clsParameters();
                    objParam.ParameterName = "ItemId";
                    objParam.ParameterValue = "0";
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "ItemName";
                    objParam.ParameterValue = ledt_ItemName.getText().toString();
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "Amount";
                    objParam.ParameterValue = ledt_ItemAmount.getText().toString();
                    lstParameters.add(objParam);

                    objParam = new clsParameters();
                    objParam.ParameterName = "UserId";
                    objParam.ParameterValue = lUserId;
                    lstParameters.add(objParam);

                    String lMethodName = "InsertItem";
                    String resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

                    Toast.makeText(getActivity().getApplicationContext(), "Successfully Saved!", Toast.LENGTH_LONG).show();

                    ItemFragment.this.getDialog().dismiss();

                    //Refresh the Grid in the Parent
                    Item activity = (Item) getActivity();
                    activity.BindListView();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private View.OnClickListener lbtnCloseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ItemFragment.this.getDialog().cancel();
        }
    };

    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater dialogInflater = getActivity().getLayoutInflater();
        View openSourceLicensesView = dialogInflater.inflate(R.layout.fragment_item, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(openSourceLicensesView)
                .setTitle("Add Item");
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ItemFragment.this.getDialog().cancel();
                    }
                });
                //.setNeutralButton(android.R.string.ok, null);

        return dialogBuilder.create();
    }*/

}
