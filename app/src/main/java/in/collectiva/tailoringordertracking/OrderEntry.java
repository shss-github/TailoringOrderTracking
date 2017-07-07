package in.collectiva.tailoringordertracking;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.Fragments.AddOrder;
import in.collectiva.tailoringordertracking.Fragments.ItemFragment;
import in.collectiva.tailoringordertracking.JSONFiles.JSONItems;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrder;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class OrderEntry extends AppCompatActivity {

    private String jsonString;
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
        setContentView(R.layout.activity_order_entry);

        // Session Manager
        session = new SessionManagement(getApplicationContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button lbtnOrderDetail = (Button) findViewById(R.id.btnOrderDetail);
        lbtnOrderDetail.setOnClickListener(lbtnOrderDetailListener);

        BindListView();
    }

    public void BindListView()
    {
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String lUserId = user.get(SessionManagement.KEY_USERID);

        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = lUserId;
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "OrderId";
        objParam.ParameterValue = "0";
        lstParameters.add(objParam);

        String lMethodName = "GetOrders";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        ListView lstOrderDetail = (ListView) findViewById(R.id.lstOrderDetail);

        TextView ltxtOrderEntryNoRecords = (TextView) findViewById(R.id.txtOrderEntryNoRecords);
        ltxtOrderEntryNoRecords.setText("");

        if (jsonString.equals("0")) {
            ltxtOrderEntryNoRecords.setText("No records found!");
        } else {

            /*SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONOrder.newInstance().GetJSONOrderList(jsonString),
                    R.layout.orderentryrow, new String[]{"OrderId", "OrderDetail", "DeliveryDate", "Status"},
                    new int[]{R.id.txtOrderRowId, R.id.txtOrderEntryRowDesc1, R.id.txtOrderEntryRowDesc2,
                            R.id.txtOrderEntryRowDesc3});
            lstOrderDetail.setAdapter(simpleAdapter);*/

            SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONOrder.newInstance().GetJSONOrderList(jsonString),
                    R.layout.orderentryrow, new String[]{"OrderId", "OrderDetail", "DeliveryDate", "Status"},
                    new int[]{R.id.txtOrderRowId, R.id.txtOrderEntryRowDesc1, R.id.txtOrderEntryRowDesc2,
                            R.id.txtOrderEntryRowDesc3}) {
                @Override
                public View getView (int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView ltxtOrderEntryRowId = ((TextView) view.findViewById(R.id.txtOrderEntryRowId));

                    ImageView limgOrderEntryRowAddOrderDetail = ((ImageView) view.findViewById(R.id.imgOrderEntryRowAddOrderDetail));
                    limgOrderEntryRowAddOrderDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //showAddOrderAlertDialog();
                            startActivity(new Intent(OrderEntry.this, OrderItems.class));
                        }
                    });

                    return view;
                }

            };
            lstOrderDetail.setAdapter(simpleAdapter);


            /*for(int i = 0; i < lstOrderDetail.getCount(); i++) {
                Item item = (Item) lstOrderDetail.getAdapter().getItem(i);
                TextView ltxtOrderEntryRowId = ((TextView) item.findViewById(R.id.txtOrderEntryRowId));
                ImageView limgOrderEntryRowAddOrderDetail = ((ImageView) item.findViewById(R.id.imgOrderEntryRowAddOrderDetail));
                showAddOrderAlertDialog();
            }*/
        }
    }

    private View.OnClickListener lbtnOrderDetailListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        showAddOrderAlertDialog();
        }
    };

    private void showAddOrderAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddOrder alertDialog = AddOrder.newInstance("Add Order");
        alertDialog.show(fm, "fragment_add_order");
    }
}
