package in.collectiva.tailoringordertracking.JSONFiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.collectiva.tailoringordertracking.cConstant.clsItems;

/**
 * Created by dhivya on 03/07/2017.
 */

public class JSONItems {

    public static JSONItems newInstance() {
        return new JSONItems();
    }

    public List<HashMap<String,String>> GetJSONItemList(String jsonString){

        JSONArray jItems = null;
        try {
            JSONObject jObject = new JSONObject(jsonString);
            /** Retrieves all the elements in the 'countries' array */
            jItems = jObject.getJSONArray("Items");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getCountries with the array of json object
         * where each json object represent a country
         */
        return getItems(jItems);
    }

    private List<HashMap<String, String>> getItems(JSONArray jItems){
        int ItemCnt = jItems.length();
        List<HashMap<String, String>> lItemList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> lItem = null;

        /** Taking each country, parses and adds to list object */
        for(int i = 0; i < ItemCnt; i++){
            try {
                /** Call getCountry with country JSON object to parse the country */

                lItem = getItem((JSONObject)jItems.get(i), String.valueOf(i + 1));
                lItemList.add(lItem);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lItemList;
    }

    /** Parsing the Country JSON object */
    private HashMap<String, String> getItem(JSONObject lItem, String lSerialNo){

        HashMap<String, String> lItemMap = new HashMap<String, String>();
        String ItemId = "";
        String ItemName = "";
        String Amount = "";

        try {
            ItemId = lItem.getString("ItemId");
            ItemName = lItem.getString("ItemName");
            Amount = lItem.getString("Amount");

            lItemMap.put("ItemId", ItemId);
            lItemMap.put("ItemName", ItemName);
            lItemMap.put("ItemDetail", lSerialNo + ". " + ItemName);
            lItemMap.put("Amount", Amount);
            lItemMap.put("AmountDetail", "Rs. " + Amount);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lItemMap;
    }

    public clsItems GetJSONItem(String jsonString)
    {
        JSONArray jItems = null;
        clsItems obj = new clsItems();

        try {
            JSONObject jObject = new JSONObject(jsonString);
            /** Retrieves all the elements in the 'countries' array */
            jItems = jObject.getJSONArray("Items");

            JSONObject lItm = (JSONObject)jItems.get(0);
            obj.ItemId = lItm.getInt("ItemId");
            obj.ItemName = lItm.getString("ItemName");
            obj.Amount = lItm.getDouble("Amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
