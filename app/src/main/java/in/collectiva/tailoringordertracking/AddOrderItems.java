package in.collectiva.tailoringordertracking;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.CommonFunction.SessionOrderDetail;
import in.collectiva.tailoringordertracking.Fragments.EditItem;
import in.collectiva.tailoringordertracking.Fragments.ItemFragment;
import in.collectiva.tailoringordertracking.Fragments.ModifyOrderItems;
import in.collectiva.tailoringordertracking.Fragments.NewOrderItems;
import in.collectiva.tailoringordertracking.JSONFiles.JSONItems;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrder;
import in.collectiva.tailoringordertracking.cConstant.clsOrder;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class AddOrderItems extends AppCompatActivity {

    private String jsonString;
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();

    // Session Manager Class
    SessionManagement session;
    SessionOrderDetail sessionOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Session Manager
        session = new SessionManagement(getApplicationContext());
        sessionOrder = new SessionOrderDetail(getApplicationContext());
        sessionOrder.clearOrderDetail();

        TextView ltxtAddOrderItemsOrderId = ((TextView) findViewById(R.id.txtAddOrderItemsOrderId));
        String lOrderId = getIntent().getStringExtra("CURRENT_ORDER_ID");
        ltxtAddOrderItemsOrderId.setText(lOrderId);

        Button lbtnAddOrderItems = (Button) findViewById(R.id.btnAddOrderItems);
        lbtnAddOrderItems.setOnClickListener(lbtnAddOrderItemsListener);

        BindOrderDetails(lOrderId);
        BindListItem(lOrderId);
    }

    private void BindOrderDetails(String lOrderId)
    {
        TextView ltxtAddOrderItemsDesc1 = ((TextView) findViewById(R.id.txtAddOrderItemsDesc1));
        TextView ltxtAddOrderItemsDesc2 = ((TextView) findViewById(R.id.txtAddOrderItemsDesc2));
        TextView ltxtAddOrderItemsDesc3 = ((TextView) findViewById(R.id.txtAddOrderItemsDesc3));

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
        objParam.ParameterValue = lOrderId;
        lstParameters.add(objParam);

        String lMethodName = "GetOrders";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        if (jsonString.equals("0")) {}
        else
        {
            clsOrder itm = JSONOrder.newInstance().GetJSONOrder(jsonString);

            ltxtAddOrderItemsDesc1.setText(itm.OrderDetail);
            ltxtAddOrderItemsDesc2.setText(itm.DeliveryDate);
            ltxtAddOrderItemsDesc3.setText(itm.Status);

            itm = null;
        }
    }

    private void BindListItem(String lOrderId)
    {
        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "OrderId";
        objParam.ParameterValue = lOrderId;
        lstParameters.add(objParam);

        String lMethodName = "GetOrderDetailByOrderId";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        TextView ltxtAddOrderItemsNoRecords = (TextView) findViewById(R.id.txtAddOrderItemsNoRecords);
        ltxtAddOrderItemsNoRecords.setText("");

        if (jsonString.equals("0")) {
            ltxtAddOrderItemsNoRecords.setText("No records found!");
        }
        else {
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONItems.newInstance().GetJSONItemList(jsonString),
                    R.layout.order_item_row, new String[] {"ItemId", "ItemName", "Amount"},
                    new int[] {R.id.txtOrderItemId, R.id.txtOrderItemName, R.id.txtOrderItemRate});

            ListView lstAddOrderItems = (ListView) findViewById(R.id.lstAddOrderItems);
            lstAddOrderItems.setAdapter(simpleAdapter);

            lstAddOrderItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // selected item
                    String selected = ((TextView) view.findViewById(R.id.txtOrderItemId)).getText().toString();
                    //showAlertDialog();
                    showEditAlertDialog(selected);

                }
            });
        }
    }

    private View.OnClickListener lbtnAddOrderItemsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        showAlertDialog();
        }
    };

    private void showEditAlertDialog(String SelectedItemID) {
        FragmentManager fm = getSupportFragmentManager();
        ModifyOrderItems alertDialog = ModifyOrderItems.newInstance(SelectedItemID);
        alertDialog.show(fm, "fragment_modify_order_items");
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        NewOrderItems alertDialog = NewOrderItems.newInstance("Add Order Item");
        alertDialog.show(fm, "fragment_new_order_items");
    }
}
