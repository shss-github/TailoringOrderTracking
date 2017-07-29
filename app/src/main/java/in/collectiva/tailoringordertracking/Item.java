package in.collectiva.tailoringordertracking;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

        /*Button lbtnAddItem = (Button) findViewById(R.id.btnAddItem);
        lbtnAddItem.setOnClickListener(lbtnAddItemListener);

        Button lbtnImportItems = (Button) findViewById(R.id.btnImportItems);
        lbtnImportItems.setOnClickListener(lbtnImportItemsListener);*/

        LinearLayout linearItemAdd = (LinearLayout) findViewById(R.id.linearItemAdd);
        LinearLayout linearItemImport = (LinearLayout) findViewById(R.id.linearItemImport);

        linearItemAdd.setOnClickListener(lbtnAddItemListener);
        linearItemImport.setOnClickListener(lbtnImportItemsListener);

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

        TextView ltxtItemNoRecords = (TextView) findViewById(R.id.txtItemNoRecords);
        ltxtItemNoRecords.setText("");

        if (jsonString.equals("0")) {
            ltxtItemNoRecords.setText("No records found!");
        } else {
            ListView lstItem = (ListView) findViewById(R.id.lstItems);

            SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONItems.newInstance().GetJSONItemList(jsonString), R.layout.row,
                    new String[]{"ItemId", "ItemDetail", "AmountDetail"},
                    new int[]{R.id.txtRowItemId, R.id.ItemDetail, R.id.txtRowAmount});
            /*{

                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    if (position % 2 == 0) {
                        //v.setBackgroundColor(Color.parseColor("#8e99d7"));
                        v.setBackgroundColor(Color.parseColor("#ffffff"));
                    } else {
                        //v.setBackgroundColor(Color.parseColor("#eceef8"));
                        v.setBackgroundColor(Color.parseColor("#809fff"));
                    }

                    return v;
                }
            };*/
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
    }

    private View.OnClickListener lbtnAddItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAlertDialog();
        }
    };

    private View.OnClickListener lbtnImportItemsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Item.this, ImportItems.class));
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
