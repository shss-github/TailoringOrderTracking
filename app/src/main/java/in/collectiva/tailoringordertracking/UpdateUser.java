package in.collectiva.tailoringordertracking;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.GPSTracker;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class UpdateUser extends AppCompatActivity {

    private EditText lEditName;
    private EditText lEditShopName;
    private TextView lTxtLatitude;
    private TextView lTxtLongitude;
    private String jsonString;
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/";

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();
    // Session Manager Class
    SessionManagement session;

    // GPSTracker class
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        lEditName = (EditText) findViewById(R.id.editContactPerson);
        lEditShopName = (EditText) findViewById(R.id.editShopName);
        lTxtLatitude = (TextView) findViewById(R.id.textLatitude);
        lTxtLongitude = (TextView) findViewById(R.id.textLangitude);
        /*EditText lEditAddress1 = (EditText) findViewById(R.id.editAddress1);
        EditText lEditAddress2 = (EditText) findViewById(R.id.editAddress2);
        EditText lEditState = (EditText) findViewById(R.id.editState);
        EditText lEditPincode = (EditText) findViewById(R.id.editPincode);*/

        //Session Manager
        session = new SessionManagement(getApplicationContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //To Enable the Network Permission in the Application
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        LoadUserDetails();

        Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(lbtnUpdateDetail);

        Button btnChangeLocation = (Button) findViewById(R.id.btnChangeLocation);
        btnChangeLocation.setOnClickListener(lbtnChangeLocation);
    }

    private View.OnClickListener lbtnChangeLocation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // create class object
            gps = new GPSTracker(UpdateUser.this);

            // check if GPS enabled
            if (gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                // \n is for new line
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
        }
    };

    private View.OnClickListener lbtnUpdateDetail = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Get User Detail From Session
            HashMap<String, String> user = session.getUserDetails();
            //Get User_Id
            String UserID = user.get(SessionManagement.KEY_USERID);

            String lName = lEditName.getText().toString();
            String lShopName = lEditShopName.getText().toString();

            ArrayList<clsParameters> lstParameters = new ArrayList<>();
            clsParameters objParam = new clsParameters();
            objParam.ParameterName = "UserId";
            objParam.ParameterValue = UserID;
            lstParameters.add(objParam);

            objParam = new clsParameters();
            objParam.ParameterName = "ShopName";
            objParam.ParameterValue = lShopName;
            lstParameters.add(objParam);

            objParam = new clsParameters();
            objParam.ParameterName = "Name";
            objParam.ParameterValue = lName;
            lstParameters.add(objParam);

            String lMethodName = "UpdateUserById";
            String resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

            if (resultData.equals("true")) {
                Toast.makeText(UpdateUser.this, "Detail Updated Successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UpdateUser.this, "User Detail Not Updated!", Toast.LENGTH_SHORT).show();
            }
            LoadUserDetails();

            startActivity(new Intent(UpdateUser.this, HomeMenu.class));
        }
    };


    public void LoadUserDetails() {
        //Get User Detail From Session
        HashMap<String, String> user = session.getUserDetails();

        //Get User_Id
        String UserID = user.get(SessionManagement.KEY_USERID);

        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = UserID;
        lstParameters.add(objParam);

        String lMethodName = "GetUserById";
        String resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        if (resultData.equals("0")) {
            Toast.makeText(UpdateUser.this, "Detail Not Found!", Toast.LENGTH_SHORT).show();
        } else {
            String lName, lShopName, lMobile;
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
                lMobile = c.getString("MobileNo");
                lShopName = c.getString("ShopName");

                lEditName.setText(lName);
                lEditShopName.setText(lShopName);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
