package in.collectiva.tailoringordertracking;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class update_user extends AppCompatActivity {


    private String jsonString;
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/";

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();

    // Session Manager Class
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        EditText lEditName = (EditText) findViewById(R.id.editName);
        EditText lEditShopName = (EditText) findViewById(R.id.editShopName);
        EditText lEditAddress1 = (EditText) findViewById(R.id.editAddress1);
        EditText lEditAddress2 = (EditText) findViewById(R.id.editAddress2);
        EditText lEditState = (EditText) findViewById(R.id.editState);
        EditText lEditPincode = (EditText) findViewById(R.id.editPincode);

        //Session Manager
        session = new SessionManagement(getApplicationContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //To Enable the Network Permission in the Application
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        LoadUserDetails();
    }

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

        String lMethodName = "GetUserByID";
        String resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        if (resultData.equals("0")) {
            Toast.makeText(update_user.this, "Detail Not Found!", Toast.LENGTH_SHORT).show();
        } else {
            String lName, lShopName, lAddress1, lAddress2, lState, lPincode;
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
                lShopName = c.getString("MobileNo");
                lAddress1 = c.getString("Address1");
                lAddress2 = c.getString("Address2");
                lState = c.getString("State");
                lPincode = c.getString("Pincode");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            startActivity(new Intent(update_user.this, HomeMenu.class));
        }
    }

}
