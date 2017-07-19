package in.collectiva.tailoringordertracking;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.CommonFunction.SessionOrderDetail;
import in.collectiva.tailoringordertracking.JSONFiles.JSONItems;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrder;
import in.collectiva.tailoringordertracking.cConstant.clsOrder;
import in.collectiva.tailoringordertracking.cConstant.clsOrderDetail;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class OrderItems extends AppCompatActivity {

    private String jsonString;
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();

    // Session Manager Class
    SessionManagement session;
    SessionOrderDetail sessionOrder;

    /*private View.OnClickListener lbtnOrderItemsProceedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<clsOrderDetail> lstOrderItems = sessionOrder.getOrderDetail();
            for (clsOrderDetail itm:lstOrderItems) {
                Toast.makeText(getApplicationContext(),
                        "Order Id : " + itm.OrderId + ", Item Id :" + itm.ItemId + ", Amount :" + itm.Amount,
                        Toast.LENGTH_LONG);
            }
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Session Manager
        session = new SessionManagement(getApplicationContext());
        sessionOrder = new SessionOrderDetail(getApplicationContext());
        sessionOrder.clearOrderDetail();

        TextView ltxtCurrentOrderId = ((TextView) findViewById(R.id.txtCurrentOrderId));
        String lOrderId = getIntent().getStringExtra("CURRENT_ORDER_ID");
        ltxtCurrentOrderId.setText(lOrderId);

        Button lbtnOrderItemsProceed = (Button) findViewById(R.id.btnOrderItemsProceed);
        lbtnOrderItemsProceed.setOnClickListener(lbtnOrderItemsProceedListener);

        BindOrderDetails(lOrderId);
        BindListItem();
    }

    private View.OnClickListener lbtnOrderItemsProceedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<clsOrderDetail> lst = sessionOrder.getOrderDetail();
            Toast.makeText(getApplicationContext(), "Count - " + lst.size(), Toast.LENGTH_SHORT).show();

            for (clsOrderDetail itm:lst) {
                ArrayList lstParameters = new ArrayList<>();
                clsParameters objParam = new clsParameters();
                objParam.ParameterName = "OrderId";
                objParam.ParameterValue = new Long(itm.OrderId).toString();
                lstParameters.add(objParam);

                objParam = new clsParameters();
                objParam.ParameterName = "OrderDetailId";
                objParam.ParameterValue = new Long(itm.OrderDetailId).toString();
                lstParameters.add(objParam);

                objParam = new clsParameters();
                objParam.ParameterName = "ItemId";
                objParam.ParameterValue = new Long(itm.ItemId).toString();
                lstParameters.add(objParam);

                objParam = new clsParameters();
                objParam.ParameterName = "Qty";
                objParam.ParameterValue = new Long(itm.Qty).toString();
                lstParameters.add(objParam);

                objParam = new clsParameters();
                objParam.ParameterName = "Rate";
                objParam.ParameterValue = new Double(itm.Rate).toString();
                lstParameters.add(objParam);

                objParam = new clsParameters();
                objParam.ParameterName = "Amount";
                objParam.ParameterValue = new Double(itm.Amount).toString();;
                lstParameters.add(objParam);

                String lMethodName = "SaveOrderDetail";
                String resultData = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);
            }

            sessionOrder.clearOrderDetail();

            //Redirect to Order Entry Page
            startActivity(new Intent(OrderItems.this, Order.class));
        }
    };

    private void BindOrderDetails(String lOrderId)
    {
        TextView ltxtOrderItemsDesc1 = ((TextView) findViewById(R.id.txtOrderItemsDesc1));
        TextView ltxtOrderItemsDesc2 = ((TextView) findViewById(R.id.txtOrderItemsDesc2));
        TextView ltxtOrderItemsDesc3 = ((TextView) findViewById(R.id.txtOrderItemsDesc3));

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

        if (jsonString.equals("0")) {

        }
        else
        {
            clsOrder itm = JSONOrder.newInstance().GetJSONOrder(jsonString);

            ltxtOrderItemsDesc1.setText(itm.OrderDetail);
            ltxtOrderItemsDesc2.setText(itm.DeliveryDate);
            ltxtOrderItemsDesc3.setText(itm.Status);

            itm = null;
        }
    }

    private void BindListItem()
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
        objParam.ParameterName = "ItemId";
        objParam.ParameterValue = "0";
        lstParameters.add(objParam);

        String lMethodName = "GetItems";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        TextView ltxtOrderItemsNoRecords = (TextView) findViewById(R.id.txtOrderItemsNoRecords);
        ltxtOrderItemsNoRecords.setText("");

        Button lbtnOrderItemsProceed = (Button) findViewById(R.id.btnOrderItemsProceed);
        lbtnOrderItemsProceed.setVisibility(View.VISIBLE);

        if (jsonString.equals("0")) {
            ltxtOrderItemsNoRecords.setText("No records found!");
            lbtnOrderItemsProceed.setVisibility(View.GONE);
        }
        else {
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONItems.newInstance().GetJSONItemList(jsonString),
                    R.layout.order_item_row, new String[] {"ItemId", "ItemName", "Amount"},
                    new int[] {R.id.txtOrderItemId, R.id.txtOrderItemName, R.id.txtOrderItemRate});

            ListView lstOrderItems = (ListView) findViewById(R.id.lstOrderItems);
            lstOrderItems.setAdapter(simpleAdapter);

            lstOrderItems.setOnItemClickListener(lstOrderItemsItemClickListener);
        }
    }

    private AdapterView.OnItemClickListener lstOrderItemsItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final TextView ltxtOrderItemRate = (TextView) view.findViewById(R.id.txtOrderItemRate);

            LinearLayout ltItemContainer = (LinearLayout) view.findViewById(R.id.lltItemContainer);
            LinearLayout ltItemOption = (LinearLayout) view.findViewById(R.id.lltItemOptions);
            ImageButton lPlus = (ImageButton) view.findViewById(R.id.imgPlus);
            ImageButton lMinus = (ImageButton) view.findViewById(R.id.imgMinus);
            final TextView ltxtOrderId = (TextView) findViewById(R.id.txtCurrentOrderId);
            final TextView ltxtOrderDetailId = (TextView) view.findViewById(R.id.txtOrderDetailId);
            final TextView ltxtOrderItemId = (TextView) view.findViewById(R.id.txtOrderItemId);
            final TextView ltxtOrderItemQty = (TextView) view.findViewById(R.id.txtOrderItemQty);
            final TextView ltxtOrderItemAmount = (TextView) view.findViewById(R.id.txtOrderItemAmount);
            final TextView ltxtOrderItemName = (TextView) view.findViewById(R.id.txtOrderItemName);

            ltxtOrderDetailId.setText("0");
            ltxtOrderItemQty.setText("0");
            ltxtOrderItemAmount.setText("Rs. 0.0");

            final long lOrderId = Long.parseLong(ltxtOrderId.getText().toString());
            final long lItemId = Long.parseLong(ltxtOrderItemId.getText().toString());
            final double lItemRate = Double.parseDouble(ltxtOrderItemRate.getText().toString());

            clsOrderDetail exItem = sessionOrder.getExistingItem(lOrderId, lItemId);
            if(exItem != null)
            {
                ltxtOrderDetailId.setText(String.valueOf(exItem.OrderDetailId));
                ltxtOrderItemQty.setText(String.valueOf(exItem.Qty));
                ltxtOrderItemAmount.setText("Rs. " + String.valueOf(exItem.Amount));
                //ltItemContainer.setBackgroundColor(Color.parseColor("#eee.eee"));
            }
            //ltxtOrderItemName.setText(ltxtOrderItemName.getText().toString() + "-Selected");


            if (ltItemOption.getVisibility() == View.VISIBLE) {
                ltItemOption.setVisibility(View.GONE);
            }
            else {
                ltItemOption.setVisibility(View.VISIBLE);
            }

            lPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(ltxtOrderItemQty.getText().toString());
                    double ItemAmount = 0.00;
                    qty = qty+1;
                    ItemAmount = qty * lItemRate;
                    ltxtOrderItemQty.setText(String.valueOf(qty));
                    ltxtOrderItemAmount.setText("Rs. " + String.valueOf(ItemAmount));

                    clsOrderDetail ordItem = sessionOrder.getExistingItem(lOrderId, lItemId);
                    if(ordItem != null) {
                        ordItem.Qty = qty;
                        ordItem.Amount = ItemAmount;
                        sessionOrder.updateOrderDetail(ordItem);
                    }
                    else {
                        ordItem = new clsOrderDetail(0, lOrderId, lItemId, qty, lItemRate, ItemAmount);
                        sessionOrder.addOrderDetail(ordItem);
                    }
                }
            });

            lMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(ltxtOrderItemQty.getText().toString());
                    double ItemAmount = 0.00;
                    if(qty > 0) {
                        qty = qty - 1;
                    }
                    else {
                        qty = 0;
                    }
                    ItemAmount = qty * lItemRate;
                    ltxtOrderItemQty.setText(String.valueOf(qty));
                    ltxtOrderItemAmount.setText("Rs. " + String.valueOf(ItemAmount));

                    clsOrderDetail ordItem = sessionOrder.getExistingItem(lOrderId, lItemId);
                    if(ordItem != null) {
                        if (qty > 0) {
                            ordItem.Qty = qty;
                            ordItem.Amount = ItemAmount;
                            sessionOrder.updateOrderDetail(ordItem);
                        } else {
                            sessionOrder.removeOrderDetail(ordItem);
                        }
                    }
                }
            });
        }
    };


}


