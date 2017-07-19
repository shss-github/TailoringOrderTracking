package in.collectiva.tailoringordertracking.CommonFunction;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.collectiva.tailoringordertracking.R;
import in.collectiva.tailoringordertracking.cConstant.clsOrderDetail;

/**
 * Created by dhivya on 01/07/2017.
 */

public class JSONAdapter extends BaseAdapter implements ListAdapter {
    private final Activity activity;
    private final JSONArray jsonArray;

    public JSONAdapter(Activity activity, JSONArray jsonArray) {
        assert activity != null;
        assert jsonArray != null;

        this.jsonArray = jsonArray;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        if (null == jsonArray)
            return 0;
        else
            return jsonArray.length();
    }

    @Override
    public JSONObject getItem(int position) {
        if (null == jsonArray) return null;
        else
            return jsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        JSONObject jsonObject = getItem(position);

        return jsonObject.optLong("UserId");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.row, null);

        TextView text = (TextView) convertView.findViewById(R.id.ItemDetail);

        try {
            JSONObject json_data = getItem(position);
            if (null != json_data) {
                String jj = json_data.getString("Name");
                text.setText(jj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;*/

        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.row, null);

        ImageButton lPlus = (ImageButton) convertView.findViewById(R.id.imgPlus);
        ImageButton lMinus = (ImageButton) convertView.findViewById(R.id.imgMinus);

        //final TextView ltxtOrderId = (TextView) convertView.findViewById(R.id.txtCurrentOrderId);
        final TextView ltxtOrderDetailId = (TextView) convertView.findViewById(R.id.txtOrderDetailId);
        final TextView ltxtOrderItemId = (TextView) convertView.findViewById(R.id.txtOrderItemId);
        final TextView ltxtOrderItemQty = (TextView) convertView.findViewById(R.id.txtOrderItemQty);
        final TextView ltxtOrderItemAmount = (TextView) convertView.findViewById(R.id.txtOrderItemAmount);
        final TextView ltxtOrderItemName = (TextView) convertView.findViewById(R.id.txtOrderItemName);
        final TextView ltxtOrderItemRate = (TextView) convertView.findViewById(R.id.txtOrderItemRate);

        final long lOrderId = 0; //Long.parseLong(ltxtOrderId.getText().toString());
        final long lItemId = Long.parseLong(ltxtOrderItemId.getText().toString());
        final double lItemRate = Double.parseDouble(ltxtOrderItemRate.getText().toString());

        lPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(ltxtOrderItemQty.getText().toString());
                double ItemAmount = 0.00;
                qty = qty+1;
                ItemAmount = qty * lItemRate;
                ltxtOrderItemQty.setText(String.valueOf(qty));
                ltxtOrderItemAmount.setText("Rs. " + String.valueOf(ItemAmount));


                /*clsOrderDetail ordItem = sessionOrder.getExistingItem(lOrderId, lItemId);
                if(ordItem != null) {
                    ordItem.Qty = qty;
                    ordItem.Amount = ItemAmount;
                    sessionOrder.updateOrderDetail(ordItem);
                }
                else {
                    ordItem = new clsOrderDetail(0, lOrderId, lItemId, qty, lItemRate, ItemAmount);
                    sessionOrder.addOrderDetail(ordItem);
                }*/
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

                /*clsOrderDetail ordItem = sessionOrder.getExistingItem(lOrderId, lItemId);
                if(ordItem != null) {
                    if (qty > 0) {
                        ordItem.Qty = qty;
                        ordItem.Amount = ItemAmount;
                        sessionOrder.updateOrderDetail(ordItem);
                    } else {
                        sessionOrder.removeOrderDetail(ordItem);
                    }
                }*/
            }
        });

        return convertView;
    }
}
