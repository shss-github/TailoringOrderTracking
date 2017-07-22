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
import in.collectiva.tailoringordertracking.Fragments.EditOrderItems;
import in.collectiva.tailoringordertracking.Fragments.ItemFragment;
import in.collectiva.tailoringordertracking.Fragments.ModifyOrderItems;
import in.collectiva.tailoringordertracking.Fragments.NewOrderItems;
import in.collectiva.tailoringordertracking.JSONFiles.JSONItems;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrder;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrderDetail;
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

    private TextView ltxtAddOrderItemsOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Session Manager
        session = new SessionManagement(getApplicationContext());
        sessionOrder = new SessionOrderDetail(getApplicationContext());
        sessionOrder.clearOrderDetail();

        ltxtAddOrderItemsOrderId = ((TextView) findViewById(R.id.txtAddOrderItemsOrderId));
        String lOrderId = getIntent().getStringExtra("CURRENT_ORDER_ID");
        ltxtAddOrderItemsOrderId.setText(lOrderId);

        Button lbtnAddOrderItems = (Button) findViewById(R.id.btnAddOrderItems);
        lbtnAddOrderItems.setOnClickListener(lbtnAddOrderItemsListener);

        BindData(lOrderId);
    }

    public void BindData(String lOrderId)
    {
        BindOrderDetails(lOrderId);
        BindListItem(lOrderId);
    }

    public void BindOrderDetails(String lOrderId)
    {
        TextView ltxtAddOrderItemsDesc1 = ((TextView) findViewById(R.id.txtAddOrderItemsDesc1));
        TextView ltxtAddOrderItemsDesc2 = ((TextView) findViewById(R.id.txtAddOrderItemsDesc2));
        TextView ltxtAddOrderItemsDesc3 = ((TextView) findViewById(R.id.txtAddOrderItemsDesc3));
        TextView ltxtAddOrderItemsDesc4 = ((TextView) findViewById(R.id.txtAddOrderItemsDesc4));

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
            ltxtAddOrderItemsDesc4.setText("Total : " +itm.TotalQty + " qty Rs. " + itm.TotalAmount);

            itm = null;
        }
    }

    public void BindListItem(String lOrderId)
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
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONOrderDetail.newInstance().GetJSONOrderDetailList(jsonString),
                    R.layout.order_detail_row, new String[] {"OrderDetailId", "OrderId",
                    "ItemName", "OrderDetailDesc1", "OrderDetailDesc2"},
                    new int[] {R.id.txtOrderItemDetailId, R.id.txtOrderItemDetailOrderId, R.id.txtItemName,
                            R.id.txtOrderDetailDesc1, R.id.txtOrderDetailDesc2});

            ListView lstAddOrderItems = (ListView) findViewById(R.id.lstAddOrderItems);
            lstAddOrderItems.setAdapter(simpleAdapter);

            lstAddOrderItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // selected item
                    String selected = ((TextView) view.findViewById(R.id.txtOrderItemDetailId)).getText().toString();
                    //showAlertDialog();
                    showEditAlertDialog(selected);

                }
            });
        }
    }

    private View.OnClickListener lbtnAddOrderItemsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String selected = ltxtAddOrderItemsOrderId.getText().toString(); //((TextView) v.findViewById(R.id.txtAddOrderItemsOrderId)).getText().toString();
            showAlertDialog(selected);
        }
    };

    private void showEditAlertDialog(String SelectedOrderDetailID) {
        FragmentManager fm = getSupportFragmentManager();
        EditOrderItems alertDialog = EditOrderItems.newInstance(SelectedOrderDetailID);
        alertDialog.show(fm, "fragment_edit_order_items");
    }

    private void showAlertDialog(String SelectedOrderId) {
        FragmentManager fm = getSupportFragmentManager();
        NewOrderItems alertDialog = NewOrderItems.newInstance(SelectedOrderId);
        alertDialog.show(fm, "fragment_new_order_items");
    }
}
