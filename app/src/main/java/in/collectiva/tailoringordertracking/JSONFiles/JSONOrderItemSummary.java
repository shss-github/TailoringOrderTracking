package in.collectiva.tailoringordertracking.JSONFiles;

import android.util.Log;

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

import in.collectiva.tailoringordertracking.cConstant.clsOrderItemSummary;

/**
 * Created by dhivya on 03/07/2017.
 */

public class JSONOrderItemSummary {

    public static String strJSONArrayName = "OrderDetail";

    public static JSONOrderItemSummary newInstance() {
        return new JSONOrderItemSummary();
    }

    public List<HashMap<String, String>> GetJSONOrderItemSummaryList(String jsonString) {

        JSONArray jOrderItemSummary = null;
        try {
            JSONObject jObject = new JSONObject(jsonString);
            /** Retrieves all the elements in the 'countries' array */
            jOrderItemSummary = jObject.getJSONArray(strJSONArrayName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getCountries with the array of json object
         * where each json object represent a country
         */
        return getOrderItemSummaries(jOrderItemSummary);
    }

    private List<HashMap<String, String>> getOrderItemSummaries(JSONArray jOrderItemSummary) {
        int OrderItemSummaryCnt = jOrderItemSummary.length();
        List<HashMap<String, String>> lOrderItemSummaryList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> lOrderItemSummary = null;

        /*lOrderItemSummary = getOrderItemSummary(String.valueOf(""), String.valueOf(""));
        lOrderItemSummaryList.add(lOrderItemSummary);*/

        /** Taking each country, parses and adds to list object */
        for (int i = 0; i < OrderItemSummaryCnt; i++) {
            try {
                /** Call getCountry with country JSON object to parse the country */

                lOrderItemSummary = getOrderItemSummary((JSONObject) jOrderItemSummary.get(i), String.valueOf(i + 1));
                lOrderItemSummaryList.add(lOrderItemSummary);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lOrderItemSummaryList;
    }

    /**
     * Parsing the Country JSON object
     */
    private HashMap<String, String> getOrderItemSummary(JSONObject lOrderItemSummary, String lSerialNo) {

        HashMap<String, String> lOrderItemSummaryMap = new HashMap<String, String>();
        String OrderItemName = "";
        String OrderNoOfQty = "";

        try {
            OrderItemName = lOrderItemSummary.getString("ItemName");
            OrderNoOfQty = lOrderItemSummary.getString("NoOfQty");

            //lOrderItemSummaryMap.put("OrderItemName", lSerialNo + ". " + OrderItemName);
            lOrderItemSummaryMap.put("OrderItemName", OrderItemName);
            lOrderItemSummaryMap.put("OrderNoOfQty", OrderNoOfQty);
            Log.d("ITemName", OrderItemName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lOrderItemSummaryMap;
    }

    public clsOrderItemSummary GetJSONOrderItemSummary(String jsonString) {
        JSONArray jOrderItemSummary = null;
        clsOrderItemSummary obj = new clsOrderItemSummary();

        try {
            JSONObject jObject = new JSONObject(jsonString);
            /** Retrieves all the elements in the 'countries' array */
            jOrderItemSummary = jObject.getJSONArray(strJSONArrayName);
            JSONObject lOIS = (JSONObject) jOrderItemSummary.get(0);

            obj.ItemName = lOIS.getString("ItemName");
            obj.NoOfQty = lOIS.getInt("NoOfQty");
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
        return lFormat.format(date);
    }
}
