package in.collectiva.tailoringordertracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.JSONFiles.JSONConfigItems;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrder;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class ImportItems extends AppCompatActivity {

    private String jsonString, lSelectedOrderId;
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();
    // Session Manager Class
    SessionManagement session;
    private RadioButton rbtnGents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_items);

        // Session Manager
        session = new SessionManagement(getApplicationContext());

        rbtnGents = (RadioButton) findViewById(R.id.rbtnGents);
        rbtnGents.setChecked(true);

        RadioButton rbtnLadies = (RadioButton) findViewById(R.id.rbtnLadies);
        Button lbtnImportItems = (Button) findViewById(R.id.btnImportItems);

        rbtnGents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData();
            }
        });

        rbtnLadies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData();
            }
        });

        lbtnImportItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListView lstConfigItems = (ListView) findViewById(R.id.lstConfigItems);

                // get user data from session
                HashMap<String, String> user = session.getUserDetails();
                String lUserId = user.get(SessionManagement.KEY_USERID);

                boolean IsRateExists = true;

                for (int i = 0; i < lstConfigItems.getCount(); i++) {
                    View vwItem = lstConfigItems.getChildAt(i);

                    CheckBox chkItem = (CheckBox) vwItem.findViewById(R.id.chkSelectImportItems);
                    EditText ledtImportItemsRate = (EditText) vwItem.findViewById(R.id.edtImportItemsRate);

                    if (chkItem.isChecked() && ledtImportItemsRate.getText().toString().equals("")) {
                        IsRateExists = false;
                        ledtImportItemsRate.setError("Rate is required");
                    }else
                        ledtImportItemsRate.setError(null);
                }

                if(IsRateExists) {
                    for (int i = 0; i < lstConfigItems.getCount(); i++) {
                        View vwItem = lstConfigItems.getChildAt(i);

                        CheckBox chkItem = (CheckBox) vwItem.findViewById(R.id.chkSelectImportItems);
                        TextView ltxtConfigItemId = (TextView) vwItem.findViewById(R.id.txtConfigItemId);
                        EditText ledtImportItemsRate = (EditText) vwItem.findViewById(R.id.edtImportItemsRate);

                        //Insert Into Item Table
                        if (chkItem.isChecked()) {
                            //Here Creating List for the Parameters, which we need to pass to the method.
                            ArrayList lstParameters = new ArrayList<>();
                            clsParameters objParam = new clsParameters();
                            objParam.ParameterName = "ConfigItemId";
                            objParam.ParameterValue = ltxtConfigItemId.getText().toString();
                            lstParameters.add(objParam);

                            objParam = new clsParameters();
                            objParam.ParameterName = "Rate";
                            objParam.ParameterValue = ledtImportItemsRate.getText().toString();
                            lstParameters.add(objParam);

                            objParam = new clsParameters();
                            objParam.ParameterName = "UserId";
                            objParam.ParameterValue = lUserId;
                            lstParameters.add(objParam);

                            String lMethodName = "SaveConfigItems";
                            String resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

                        }
                    }
                    Toast.makeText(ImportItems.this, "Imported Successfully!", Toast.LENGTH_SHORT).show();
                    LoadData();
                }
            }
        });

        LoadData();
    }

    private void LoadData() {

        String strGender = "";
        if (rbtnGents.isChecked()) {
            strGender = "M";
        } else {
            strGender = "F";
        }

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String lUserId = user.get(SessionManagement.KEY_USERID);

        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = lUserId;
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "Gender";
        objParam.ParameterValue = strGender;
        lstParameters.add(objParam);

        String lMethodName = "GetConfigItems";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        TextView ltxtImportItemNoRecords = (TextView) findViewById(R.id.txtImportItemNoRecords);
        ltxtImportItemNoRecords.setText("");

        ListView lstAll = (ListView) findViewById(R.id.lstConfigItems);

        if (jsonString.equals("0")) {
            ltxtImportItemNoRecords.setText("No records found!");
            lstAll.setVisibility(View.GONE);
        } else {
            lstAll.setVisibility(View.VISIBLE);
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONConfigItems.newInstance().GetJSONItemList(jsonString),
                    R.layout.import_items, new String[]{"ConfigItemId", "ConfigItemName"},
                    new int[]{R.id.txtConfigItemId, R.id.chkSelectImportItems});

            /*
             SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONConfigItems.newInstance().GetJSONItemList(jsonString),
                    R.layout.import_items, new String[]{"ConfigItemId", "ConfigItemName"},
                    new int[]{R.id.txtConfigItemId, R.id.txtConfigItemName});
            */

            lstAll.setAdapter(simpleAdapter);
        }
    }
}
