package in.collectiva.tailoringordertracking.JSONFiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.collectiva.tailoringordertracking.cConstant.clsOrder;
import in.collectiva.tailoringordertracking.cConstant.clsOrderDetail;

/**
 * Created by dhivya on 21/07/2017.
 */

public class JSONOrderDetail {
    public static String strJSONArrayName = "OrderDetail";

    public static JSONOrderDetail newInstance() {
        return new JSONOrderDetail();
    }

    public List<HashMap<String, String>> GetJSONOrderDetailList(String jsonString) {

        JSONArray jItems = null;
        try {
            JSONObject jObject = new JSONObject(jsonString);
            /** Retrieves all the elements in the 'countries' array */
            jItems = jObject.getJSONArray(strJSONArrayName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getCountries with the array of json object
         * where each json object represent a country
         */
        return getOrderDetails(jItems);
    }

    private List<HashMap<String, String>> getOrderDetails(JSONArray jOrder) {
        int OrderCnt = jOrder.length();
        List<HashMap<String, String>> lOrderList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> lOrder = null;

        /** Taking each country, parses and adds to list object */
        for (int i = 0; i < OrderCnt; i++) {
            try {
                /** Call getCountry with country JSON object to parse the country */

                lOrder = getOrderDetail((JSONObject) jOrder.get(i), String.valueOf(i + 1));
                lOrderList.add(lOrder);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lOrderList;
    }

    /**
     * Parsing the Country JSON object
     */
    private HashMap<String, String> getOrderDetail(JSONObject lOrder, String lSerialNo) {

        HashMap<String, String> lOrderMap = new HashMap<String, String>();
        String OrderId = "", OrderDetailId = "", ItemId = "", ItemName = "", Qty = "", Rate = "", Amount = "";

        try {
            OrderId = lOrder.getString("OrderId");
            OrderDetailId = lOrder.getString("OrderDetailId");
            ItemId = lOrder.getString("ItemId");
            ItemName = lOrder.getString("ItemName");
            Qty = lOrder.getString("Qty");
            Rate = lOrder.getString("Rate");
            Amount = lOrder.getString("Amount");

            lOrderMap.put("OrderId", OrderId);
            lOrderMap.put("OrderDetailId", OrderDetailId);
            lOrderMap.put("ItemId", ItemId);
            lOrderMap.put("ItemName", ItemName);
            lOrderMap.put("Qty", Qty);
            lOrderMap.put("Rate", Rate);
            lOrderMap.put("Amount", Amount);

            lOrderMap.put("OrderDetailDesc1", Qty  + " Qty X Rs. " + Rate);
            lOrderMap.put("OrderDetailDesc2", "Rs. " + Amount);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lOrderMap;
    }

    public clsOrderDetail GetJSONOrderDetail(String jsonString) {
        JSONArray jItems = null;
        clsOrderDetail obj = new clsOrderDetail();

        try {
            JSONObject jObject = new JSONObject(jsonString);
            /** Retrieves all the elements in the 'countries' array */
            jItems = jObject.getJSONArray(strJSONArrayName);
            JSONObject lItm = (JSONObject) jItems.get(0);

            String OrderId = "", OrderDetailId = "", ItemId = "", ItemName = "", Qty = "", Rate = "", Amount = "";

            obj.OrderId = lItm.getInt("OrderId");
            obj.OrderDetailId = lItm.getInt("OrderDetailId");
            obj.ItemId = lItm.getInt("ItemId");
            obj.ItemName = lItm.getString("ItemName");
            obj.Qty = lItm.getInt("Qty");
            obj.Rate = lItm.getDouble("Rate");
            obj.Amount = lItm.getDouble("Amount");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    private String ConvertStringToDate(String dtStart) {
        //String dtStart = "2010-10-15T09:27:37Z";
        SimpleDateFormat lFormat = new SimpleDateFormat("dd/mm/yyyy", Locale.US);
        Date date = null;
        try {
            date = lFormat.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //lFormat = new SimpleDateFormat("dd/MM/yyyy");
        return  lFormat.format(date);
    }
}
