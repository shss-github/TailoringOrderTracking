package in.collectiva.tailoringordertracking;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
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
import in.collectiva.tailoringordertracking.Fragments.EditItem;
import in.collectiva.tailoringordertracking.Fragments.ItemFragment;
import in.collectiva.tailoringordertracking.JSONFiles.JSONItems;
import in.collectiva.tailoringordertracking.cConstant.clsItems;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class Item extends AppCompatActivity {

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

        // Session Manager
        session = new SessionManagement(getApplicationContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button lbtnAddItem = (Button) findViewById(R.id.btnAddItem);
        lbtnAddItem.setOnClickListener(lbtnAddItemListener);

        //To Enable the Network Permission in the Application
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        BindListView();
    }

    public void BindListView()
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

        ListView lstItem = (ListView) findViewById(R.id.lstItems);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONItems.newInstance().GetJSONItemList(jsonString), R.layout.row,
                new String[] {"ItemId", "ItemDetail", "Amount"},
                new int[] {R.id.txtRowItemId, R.id.ItemDetail, R.id.txtRowAmount});
        lstItem.setAdapter(simpleAdapter);

        lstItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                String selected = ((TextView) view.findViewById(R.id.txtRowItemId)).getText().toString();
                //showAlertDialog();
                showEditAlertDialog(selected);

            }
        });
    }

    private View.OnClickListener lbtnAddItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAlertDialog();
        }
    };

    private void showEditAlertDialog(String SelectedItemID) {
        FragmentManager fm = getSupportFragmentManager();
        EditItem alertDialog = EditItem.newInstance(SelectedItemID);
        alertDialog.show(fm, "fragment_edit_item");
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ItemFragment alertDialog = ItemFragment.newInstance("Add Item");
        alertDialog.show(fm, "fragment_item");
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
