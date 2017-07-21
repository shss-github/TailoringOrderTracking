package in.collectiva.tailoringordertracking;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class Register extends AppCompatActivity {

    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();
    boolean isInternetConnected = false;

    // Session Manager Class
    SessionManagement session;

    private View.OnClickListener lbtnRegisterNowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            EditText ledtName = (EditText) findViewById(R.id.edtName);
            EditText ledtMobileNo = (EditText) findViewById(R.id.edtMobileNo);
            EditText ledtPassword = (EditText) findViewById(R.id.edtPassword);
            /*CheckBox lchkTerms = (CheckBox) findViewById(R.id.chkTerms);*/

            boolean ProceedToSave = true;
            if (ledtName.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledtName.setError("Name is required!");
            } else if (ledtMobileNo.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledtMobileNo.setError("Mobile No. is required!");
            } else if (ledtPassword.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledtPassword.setError("Password is required!");
            }

            /*else if(!lchkTerms.isChecked()){
                ProceedToSave = false;
                Toast.makeText(Register.this, "Accept the Terms and Conditions to Proceed!", Toast.LENGTH_LONG).show();
            }*/

            if (ProceedToSave) {
                String lMethodName, resultData;
                //Check for Duplication
                ArrayList<clsParameters> lstParameters = new ArrayList<>();
                clsParameters objParam = new clsParameters();
                objParam.ParameterName = "MobileNo";
                objParam.ParameterValue = ledtMobileNo.getText().toString();
                lstParameters.add(objParam);

                lMethodName = "CheckForDuplication";
                resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

                boolean lResult = Boolean.parseBoolean(resultData);
                if (lResult) {
                    Toast.makeText(Register.this, "Mobile No. already exists!", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        //Here Creating List for the Parameters, which we need to pass to the method.
                        lstParameters = new ArrayList<>();
                        objParam = new clsParameters();
                        objParam.ParameterName = "Name";
                        objParam.ParameterValue = ledtName.getText().toString();
                        lstParameters.add(objParam);

                        objParam = new clsParameters();
                        objParam.ParameterName = "Password";
                        objParam.ParameterValue = ledtPassword.getText().toString();
                        lstParameters.add(objParam);

                        objParam = new clsParameters();
                        objParam.ParameterName = "MobileNo";
                        objParam.ParameterValue = ledtMobileNo.getText().toString();
                        lstParameters.add(objParam);

                        lMethodName = "InsertUser";
                        resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

                        session.createLoginSession(Integer.parseInt(resultData), ledtName.getText().toString(), ledtMobileNo.getText().toString(), false);

                        startActivity(new Intent(Register.this, HomeMenu.class));
                        //Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Session Manager
        session = new SessionManagement(getApplicationContext());

        Button lbtnRegisterNow = (Button) findViewById(R.id.btnRegisterNow);
        lbtnRegisterNow.setOnClickListener(lbtnRegisterNowListener);
    }
}
