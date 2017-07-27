package in.collectiva.tailoringordertracking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.GeneralMethods;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class Login extends AppCompatActivity {

    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    // Session Manager Class
    SessionManagement session;

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();

    private View.OnClickListener lbtnLoginNowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText ledtMobileNo = (EditText) findViewById(R.id.edt_Login_MobileNo);
            EditText ledtPassword = (EditText) findViewById(R.id.edt_Login_Password);
            CheckBox lchkKeepMeAsLoggedIn = (CheckBox) findViewById(R.id.chkKeepMeAsLoggedIn);

            //Check for Duplication
            ArrayList<clsParameters> lstParameters = new ArrayList<>();
            clsParameters objParam = new clsParameters();
            objParam.ParameterName = "MobileNo";
            objParam.ParameterValue = ledtMobileNo.getText().toString();
            lstParameters.add(objParam);

            objParam = new clsParameters();
            objParam.ParameterName = "Password";
            objParam.ParameterValue = ledtPassword.getText().toString();
            lstParameters.add(objParam);

            boolean ProceedToSave = true;
            if (ledtMobileNo.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledtMobileNo.setError("Mobile No. is required!");
            } else if (ledtPassword.getText().toString().trim().equals("")) {
                ProceedToSave = false;
                ledtPassword.setError("Password is required!");
            }

            if (ProceedToSave) {
                String lMethodName = "GetUser";
                String resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

                if (resultData.equals("0")) {

                    Toast.makeText(Login.this, "Invalid Mobile Number/Password!", Toast.LENGTH_SHORT).show();

                } else {

                    String lName, lMobileNo;
                    int lUserId;
                    try {

                        //Convert the JSon String to JSonObject.
                        JSONObject object = new JSONObject(resultData);

                        //Get the array result from the JSonObject.
                        JSONArray topArray = object.optJSONArray("Users");

                        //Looping through All elements
                        JSONObject c = topArray.getJSONObject(0);

                        lUserId = c.getInt("UserId");
                        lName = c.getString("Name");
                        lMobileNo = c.getString("MobileNo");

                        session.createLoginSession(lUserId, lName, lMobileNo, false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    startActivity(new Intent(Login.this, HomeMenu.class));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //To Enable the Network Permission in the Application
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ImageView imgLoginHeader = (ImageView) findViewById(R.id.imgLoginHeader);
        GeneralMethods.ResizeImage(imgLoginHeader, R.drawable.login2, getWindowManager().getDefaultDisplay(), getResources());

        Button lbtnLoginNow = (Button) findViewById(R.id.btnLoginNow);
        lbtnLoginNow.setOnClickListener(lbtnLoginNowListener);

        TextView ltxtSignup = (TextView) findViewById(R.id.txtSignup);
        ltxtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        // Session Manager
        session = new SessionManagement(getApplicationContext());

        if(session != null) {
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            // User Id
            Boolean lKeepMeLoggedIn = Boolean.getBoolean(user.get(SessionManagement.KEY_KEEP_ME_LOGGED_IN));
            if (lKeepMeLoggedIn) {
                startActivity(new Intent(Login.this, HomeMenu.class));
            }
        }
    }

    /*public void ResizeImage(int lDrawableId)
    {
        ImageView imgLoginHeader = (ImageView) findViewById(R.id.imgLoginHeader);

        Display display = getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), lDrawableId, options);
        int width = options.outWidth;
        if (width > displayWidth) {
            int widthRatio = Math.round((float) width / (float) displayWidth);
            options.inSampleSize = widthRatio;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), lDrawableId, options);
        imgLoginHeader.setImageBitmap(bitmap);
    }*/
}
