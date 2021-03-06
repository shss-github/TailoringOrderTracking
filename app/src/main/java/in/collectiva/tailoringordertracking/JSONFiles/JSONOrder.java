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

import in.collectiva.tailoringordertracking.cConstant.clsItems;
import in.collectiva.tailoringordertracking.cConstant.clsOrder;

/**
 * Created by dhivya on 03/07/2017.
 */

public class JSONOrder {

    public static String strJSONArrayName = "Orders";

    public static JSONOrder newInstance() {
        return new JSONOrder();
    }

    public List<HashMap<String, String>> GetJSONOrderList(String jsonString) {

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
        return getOrders(jItems);
    }

    private List<HashMap<String, String>> getOrders(JSONArray jOrder) {
        int OrderCnt = jOrder.length();
        List<HashMap<String, String>> lOrderList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> lOrder = null;

        /** Taking each country, parses and adds to list object */
        for (int i = 0; i < OrderCnt; i++) {
            try {
                /** Call getCountry with country JSON object to parse the country */

                lOrder = getOrder((JSONObject) jOrder.get(i), String.valueOf(i + 1));
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
    private HashMap<String, String> getOrder(JSONObject lOrder, String lSerialNo) {

        HashMap<String, String> lOrderMap = new HashMap<String, String>();
        String OrderId = "";
        String OrderNo = "";
        String DeliveryDate = "";
        String MobileNo = "";
        String Name = "", Status = "";
        double TotalAmount =0.0;
        int TotalQty = 0;

        try {
            OrderId = lOrder.getString("OrderId");
            OrderNo = lOrder.getString("OrderNo");
            DeliveryDate = lOrder.getString("DeliveryDate");
            MobileNo = lOrder.getString("MobileNo");
            Name = lOrder.getString("Name");
            Status = lOrder.getString("StatusName");

            lOrderMap.put("OrderId", OrderId);
            /*lOrderMap.put("OrderDt", ConvertStringToDate(lOrder.getString("OrderDate")));
            lOrderMap.put("DeliveryDt", ConvertStringToDate(lOrder.getString("DeliveryDate")));
            lOrderMap.put("ShopName", lOrder.getString("ShopName"));*/

            lOrderMap.put("OrderDetail", Name + " (" + MobileNo + ")");
            lOrderMap.put("DeliveryDate", "Order #" + OrderNo + " dt. " + ConvertStringToDate(DeliveryDate));
            lOrderMap.put("MobileNo", MobileNo);
            lOrderMap.put("Status", "Status - " + Status);
            lOrderMap.put("TotalAmount", String.valueOf(TotalAmount));
            lOrderMap.put("TotalQty", String.valueOf(TotalQty));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lOrderMap;
    }

    public clsOrder GetJSONOrder(String jsonString) {
        JSONArray jItems = null;
        clsOrder obj = new clsOrder();

        try {
            JSONObject jObject = new JSONObject(jsonString);
            /** Retrieves all the elements in the 'countries' array */
            jItems = jObject.getJSONArray(strJSONArrayName);
            JSONObject lItm = (JSONObject) jItems.get(0);

            String lDeliveryDate = lItm.getString("DeliveryDate");

            obj.OrderId = lItm.getInt("OrderId");
            obj.Name = lItm.getString("Name");
            obj.OrderNo = lItm.getString("OrderNo");
            obj.MobileNo = lItm.getString("MobileNo");
            obj.TotalQty = lItm.getInt("TotalQty");
            obj.TotalAmount = lItm.getDouble("TotalAmount");


            obj.OrderDt = ConvertStringToDate(lItm.getString("OrderDate"));
            obj.DeliveryDt = ConvertStringToDate(lItm.getString("DeliveryDate"));
            obj.ShopName = lItm.getString("ShopName");

            obj.OrderDetail = lItm.getString("Name") + " (" + lItm.getString("MobileNo") + ")";
            obj.DeliveryDate = "Order #" + lItm.getString("OrderNo") + " dt. " + ConvertStringToDate(lItm.getString("DeliveryDate"));
            obj.Status = "Status - " + lItm.getString("StatusName");
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
