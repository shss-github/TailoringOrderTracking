package in.collectiva.tailoringordertracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrder;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class DeliveredList extends AppCompatActivity {

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
        setContentView(R.layout.activity_delivered_list);

        // Session Manager
        session = new SessionManagement(getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // User Id
        String lUserId = user.get(SessionManagement.KEY_USERID);

        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = lUserId;
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "StatusId";
        objParam.ParameterValue = "4";
        lstParameters.add(objParam);

        String lMethodName = "GetOrdersByStatus";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        TextView ltxtMakeNoRecords = (TextView) findViewById(R.id.txtDeliveredNoRecords);
        ltxtMakeNoRecords.setText("");

        if (jsonString.equals("0")) {
            ltxtMakeNoRecords.setText("No records found!");
        } else {

            ListView lstAll = (ListView) findViewById(R.id.lstDeliveredList);
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONOrder.newInstance().GetJSONOrderList(jsonString),
                    R.layout.orderrow, new String[]{"OrderId", "OrderDetail", "DeliveryDate", "Status"},
                    new int[]{R.id.txtOrderRowId, R.id.txtOrderRowDetail, R.id.txtOrderRowDeliveryDate,
                            R.id.txtOrderRowStatus});

            lstAll.setAdapter(simpleAdapter);
        }
    }
}
