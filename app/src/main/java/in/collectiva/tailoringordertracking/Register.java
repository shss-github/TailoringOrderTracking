package in.collectiva.tailoringordertracking;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class Register extends AppCompatActivity {

    private static final String METHOD_NAME = "InsertUser"; //"RegisterUser";
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/InsertUser"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();
    boolean isInternetConnected = false;

    private View.OnClickListener lbtnRegisterNowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            EditText ledtName = (EditText) findViewById(R.id.edtName);
            EditText ledtMobileNo = (EditText) findViewById(R.id.edtMobileNo);
            EditText ledtPassword = (EditText) findViewById(R.id.edtPassword);
            CheckBox lchkTerms = (CheckBox) findViewById(R.id.chkTerms);

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
            } else if(!lchkTerms.isChecked()){
                ProceedToSave = false;
                Toast.makeText(Register.this, "Accept the Terms and Conditions to Proceed!", Toast.LENGTH_LONG).show();
            }

            if (ProceedToSave) {
                try {
                    //Here Creating List for the Parameters, which we need to pass to the method.
                    ArrayList<clsParameters> lstParameters = new ArrayList<>();
                    clsParameters objParam = new clsParameters();
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
                    /*objParam = new clsParameters();
                    objParam.ParameterName = "EMail";
                    objParam.ParameterValue = "support@shss.co.in";
                    lstParameters.add(objParam);*/

                    String resultData = objCRUD.GetScalar(NAMESPACE, METHOD_NAME, REQURL, SOAP_ACTION, lstParameters);

                    /*lblStatus.setText(resultData);
                    float textSize = getResources().getDimension(R.dimen.detail_text_size);
                    lblStatus.setTextSize(textSize);

                    if (resultData.equals("Register Successfully.")) {
                        int successcolor = getResources().getColor(R.color.success_color);
                        lblStatus.setTextColor(successcolor);
                    } else {
                        int textColor = getResources().getColor(R.color.error_color);
                        lblStatus.setTextColor(textColor);
                    }*/

                    Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    /*Toast.makeText(getApplicationContext(), e.getMessage() + "\n", Toast.LENGTH_LONG).show();
                    Log.i("Exception", "Exception: " + e.getLocalizedMessage().toString());
                    int txtErrColor = getResources().getColor(R.color.error_color);
                    lblStatus.setText(e.getMessage());
                    lblStatus.setTextColor(txtErrColor);*/

                    Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_LONG).show();
                }
            }

        }

        /*@Override
        public void onClick(View v) {

            EditText txtName = (EditText) findViewById(R.id.txtName);
            EditText txtMobile = (EditText) findViewById(R.id.txtMobile);
            EditText txtEMail = (EditText) findViewById(R.id.txtEmail);
            TextView lblStatus = (TextView) findViewById(R.id.lblStatus);

            boolean ProceedToSave = true;

            if (txtName.getText().toString().trim().equals("") &&
                    txtMobile.getText().toString().trim().equals("") &&
                    txtEMail.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                txtName.setError("Name is required!");
                txtMobile.setError("Mobile No. is required!");
                txtEMail.setError("E-Mail is required!");
            } else if (txtName.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                txtName.setError("Name is required!");
            } else if (txtMobile.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                txtMobile.setError("Mobile No. is required!");
            } else if (txtEMail.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                txtEMail.setError("E-Mail is required!");
            }

            if (ProceedToSave) {
                try {
                    //Here Creating List for the Parameters, which we need to pass to the method.
                    ArrayList<clsParameters> lstParameters = new ArrayList<>();
                    clsParameters objParam = new clsParameters();
                    objParam.ParameterName = "Name";
                    objParam.ParameterValue = txtName.getText().toString();
                    lstParameters.add(objParam);
                    objParam = new clsParameters();
                    objParam.ParameterName = "Mobile";
                    objParam.ParameterValue = txtMobile.getText().toString();
                    lstParameters.add(objParam);
                    objParam = new clsParameters();
                    objParam.ParameterName = "EMail";
                    objParam.ParameterValue = txtEMail.getText().toString();
                    lstParameters.add(objParam);

                    String resultData = objCRUD.GetScalar(NAMESPACE, METHOD_NAME, REQURL, SOAP_ACTION, lstParameters);

                    lblStatus.setText(resultData);
                    float textSize = getResources().getDimension(R.dimen.detail_text_size);
                    lblStatus.setTextSize(textSize);

                    if (resultData.equals("Register Successfully.")) {
                        int successcolor = getResources().getColor(R.color.success_color);
                        lblStatus.setTextColor(successcolor);
                    } else {
                        int textColor = getResources().getColor(R.color.error_color);
                        lblStatus.setTextColor(textColor);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage() + "\n", Toast.LENGTH_LONG).show();
                    Log.i("Exception", "Exception: " + e.getLocalizedMessage().toString());
                    int txtErrColor = getResources().getColor(R.color.error_color);
                    lblStatus.setText(e.getMessage());
                    lblStatus.setTextColor(txtErrColor);
                }
            }
        }*/
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //To Enable the Network Permission in the Application
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Button lbtnRegisterNow = (Button) findViewById(R.id.btnRegisterNow);
        lbtnRegisterNow.setOnClickListener(lbtnRegisterNowListener);
    }
}
