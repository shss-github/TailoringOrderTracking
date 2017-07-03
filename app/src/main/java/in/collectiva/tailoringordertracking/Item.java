package in.collectiva.tailoringordertracking;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.JSONAdapter;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.Fragments.ItemFragment;
import in.collectiva.tailoringordertracking.cConstant.clsItems;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class Item extends AppCompatActivity {

    //json string
    /*private String jsonString = "{\"employee\":[{\"emp_name\":\"employee1\",\"emp_no\":\"101700\"},{\"emp_name\":\"employee2\",\"emp_no\":\"101701\"},{\"emp_name\":\"employee3\",\"emp_no\":\"101702\"},"+
            "{\"emp_name\":\"employee4\",\"emp_no\":\"101703\"},{\"emp_name\":\"employee5\",\"emp_no\":\"101704\"},{\"emp_name\":\"employee6\",\"emp_no\":\"101705\"},"+
            "{\"emp_name\":\"employee7\",\"emp_no\":\"101706\"},{\"emp_name\":\"employee8\",\"emp_no\":\"101707\"},{\"emp_name\":\"employee9\",\"emp_no\":\"101708\"},"+
            "{\"emp_name\":\"employee10\",\"emp_no\":\"101709\"},{\"emp_name\":\"employee11\",\"emp_no\":\"101710\"},{\"emp_name\":\"employee12\",\"emp_no\":\"101711\"},"+
            "{\"emp_name\":\"employee13\",\"emp_no\":\"101712\"},{\"emp_name\":\"employee14\",\"emp_no\":\"101713\"},{\"emp_name\":\"employee15\",\"emp_no\":\"101712\"}]}";*/

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
        setContentView(R.layout.activity_item);

        Button lbtnAddItem = (Button) findViewById(R.id.btnAddItem);
        lbtnAddItem.setOnClickListener(lbtnAddItemListener);

        //To Enable the Network Permission in the Application
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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

        ListView lstItem = (ListView) findViewById(R.id.lstItems);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, parse(jsonString), R.layout.row,
                new String[] {"ItemId", "ItemDetail", "Amount"},
                new int[] {R.id.txtRowItemId, R.id.ItemDetail, R.id.txtRowAmount});
        lstItem.setAdapter(simpleAdapter);

        lstItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                String selected = ((TextView) view.findViewById(R.id.txtRowItemId)).getText().toString();

                Toast toast = Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private View.OnClickListener lbtnAddItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAlertDialog();
        }
    };

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ItemFragment alertDialog = ItemFragment.newInstance("Add Item");
        alertDialog.show(fm, "fragment_item");
    }


    public List<HashMap<String,String>> parse(String jsonString){

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
        int countryCount = jItems.length();
        List<HashMap<String, String>> lItemList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> lItem = null;

        /** Taking each country, parses and adds to list object */
        for(int i=0; i<countryCount;i++){
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
            lItemMap.put("ItemDetail", lSerialNo + ". " + ItemName);
            lItemMap.put("Amount", Amount);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lItemMap;
    }


    /*List<Map<String,String>> ItemList = new ArrayList<Map<String,String>>();
    private void initList(){

        try{
            JSONObject jsonResponse = new JSONObject(jsonString);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("Items");

            for(int i = 0; i<jsonMainNode.length();i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String lItemName = jsonChildNode.optString("ItemName");
                String lItemId = jsonChildNode.optString("ItemId");
                String outPut = (i+1) + ". " + lItemName;
                ItemList.add(CreateItem("Items", outPut));
            }
        }
        catch(JSONException e){
            Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private HashMap<String, String>CreateItem(String name, String number){
        HashMap<String, String> ItemDetail = new HashMap<String, String>();
        ItemDetail.put(name, number);
        return ItemDetail;
    }*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/

    /*List<Map<String,String>> employeeList = new ArrayList<Map<String,String>>();
    private void initList(){

        try{
            JSONObject jsonResponse = new JSONObject(jsonString);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("employee");

            for(int i = 0; i<jsonMainNode.length();i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("emp_name");
                String number = jsonChildNode.optString("emp_no");
                String outPut = name + "-" +number;
                employeeList.add(createEmployee("employees", outPut));
            }
        }
        catch(JSONException e){
            Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private HashMap<String, String>createEmployee(String name,String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }*/
}
