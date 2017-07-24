package in.collectiva.tailoringordertracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_items);

        // Session Manager
        session = new SessionManagement(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // User Id
        String lUserId = user.get(SessionManagement.KEY_USERID);

        RadioButton rbtnGents = (RadioButton) findViewById(R.id.rbtnGents);
        RadioButton rbtnLadies = (RadioButton) findViewById(R.id.rbtnLadies);
        String strGender = "";

        if (rbtnGents.isChecked()) {
            strGender = "M";
        } else {
            strGender = "F";
        }

        LoadData(lUserId, strGender);
    }

    private void LoadData(String lUserId, String Gender) {
        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = lUserId;
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "Gender";
        objParam.ParameterValue = Gender;
        lstParameters.add(objParam);

        String lMethodName = "GetConfigItems";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        TextView ltxtImportItemNoRecords = (TextView) findViewById(R.id.txtImportItemNoRecords);
        ltxtImportItemNoRecords.setText("");

        if (jsonString.equals("0")) {
            ltxtImportItemNoRecords.setText("No records found!");
            ListView lstAll = (ListView) findViewById(R.id.lstConfigItems);
            lstAll.setVisibility(View.GONE);
        } else {
            ListView lstAll = (ListView) findViewById(R.id.lstConfigItems);
            lstAll.setVisibility(View.VISIBLE);
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONConfigItems.newInstance().GetJSONItemList(jsonString),
                    R.layout.import_items, new String[]{"ConfigItemId", "ConfigItemName"},
                    new int[]{R.id.txtConfigItemId, R.id.txtConfigItemName});

            lstAll.setAdapter(simpleAdapter);
        }
    }
}
