package in.collectiva.tailoringordertracking.JSONFiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.collectiva.tailoringordertracking.cConstant.clsItems;

/**
 * Created by Gopi on 24/07/2017.
 */

public class JSONConfigItems {

    public static JSONConfigItems newInstance() {
        return new JSONConfigItems();
    }

    public List<HashMap<String,String>> GetJSONItemList(String jsonString){

        JSONArray jItems = null;
        try {
            JSONObject jObject = new JSONObject(jsonString);
            /** Retrieves all the elements in the 'countries' array */
            jItems = jObject.getJSONArray("ConfigItems");
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
        String ConfigItemId = "";
        String ConfigItemName = "";
        String Gender = "";

        try {
            ConfigItemId = lItem.getString("ConfigItemId");
            ConfigItemName = lItem.getString("ConfigItemName");
            Gender = lItem.getString("Gender");

            lItemMap.put("ConfigItemId", ConfigItemId);
            lItemMap.put("ConfigItemName", ConfigItemName);
            lItemMap.put("ItemDetail", lSerialNo + ". " + ConfigItemName);
            lItemMap.put("Gender", Gender);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lItemMap;
    }

    /*public clsConfigItems GetJSONConfigItem(String jsonString)
    {
        JSONArray jItems = null;
        clsConfigItems obj = new clsConfigItems();

        try {
            JSONObject jObject = new JSONObject(jsonString);
            /** Retrieves all the elements in the 'countries' array */
            /*jItems = jObject.getJSONArray("ConfigItems");

            JSONObject lItm = (JSONObject)jItems.get(0);
            obj.ConfigItemId = lItm.getInt("ConfigItemId");
            obj.ConfigItemName = lItm.getString("ConfigItemName");
            obj.Gender = lItm.getDouble("Gender");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }*/
}
