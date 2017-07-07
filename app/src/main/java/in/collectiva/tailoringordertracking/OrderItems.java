package in.collectiva.tailoringordertracking;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.JSONFiles.JSONItems;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BindListItem();
    }

    private void BindListItem()
    {
        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = "14";
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "ItemId";
        objParam.ParameterValue = "0";
        lstParameters.add(objParam);

        String lMethodName = "GetItems";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONItems.newInstance().GetJSONItemList(jsonString), R.layout.order_item_row,
                new String[] {"ItemId", "ItemName", "Amount"},
                new int[] {R.id.txtOrderItemId, R.id.txtOrderItemName, R.id.txtOrderItemRate});

        ListView lstOrderItems = (ListView) findViewById(R.id.lstOrderItems);
        lstOrderItems.setAdapter(simpleAdapter);

        lstOrderItems.setOnItemClickListener(lstOrderItemsItemClickListener);
        /*lstOrderItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final TextView ltxtOrderItemRate = (TextView) view.findViewById(R.id.txtOrderItemRate);

                LinearLayout ltItemOption = (LinearLayout) view.findViewById(R.id.lltItemOptions);
                ImageButton lPlus = (ImageButton) view.findViewById(R.id.imgPlus);
                ImageButton lMinus = (ImageButton) view.findViewById(R.id.imgMinus);
                final TextView ltxtOrderItemAmount = (TextView) view.findViewById(R.id.txtOrderItemAmount);
                final TextView ltxtOrderItemQty = (TextView) view.findViewById(R.id.txtOrderItemQty);

                ltxtOrderItemAmount.setText("0.00");
                ltxtOrderItemQty.setText("0");

                final double lItemRate = Double.parseDouble(ltxtOrderItemRate.getText().toString());


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
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    private AdapterView.OnItemClickListener lstOrderItemsItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final TextView ltxtOrderItemRate = (TextView) view.findViewById(R.id.txtOrderItemRate);

            LinearLayout ltItemOption = (LinearLayout) view.findViewById(R.id.lltItemOptions);
            ImageButton lPlus = (ImageButton) view.findViewById(R.id.imgPlus);
            ImageButton lMinus = (ImageButton) view.findViewById(R.id.imgMinus);
            final TextView ltxtOrderItemAmount = (TextView) view.findViewById(R.id.txtOrderItemAmount);
            final TextView ltxtOrderItemQty = (TextView) view.findViewById(R.id.txtOrderItemQty);

            ltxtOrderItemAmount.setText("Rs. 0.0");
            ltxtOrderItemQty.setText("0");

            final double lItemRate = Double.parseDouble(ltxtOrderItemRate.getText().toString());


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
                }
            });

        }
    };
}
