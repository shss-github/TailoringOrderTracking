package in.collectiva.tailoringordertracking.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.Item;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrder;
import in.collectiva.tailoringordertracking.MyOrders;
import in.collectiva.tailoringordertracking.R;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class MakeListFragment extends Fragment {

    private String jsonString, lSelectedOrderId;
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();

    // Session Manager Class
    SessionManagement session;


    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Session Manager
        session = new SessionManagement(getActivity().getApplicationContext());

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.fragment_make_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // User Id
        String lUserId = user.get(SessionManagement.KEY_USERID);

        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = lUserId;
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "StatusId";
        objParam.ParameterValue = "2";
        lstParameters.add(objParam);

        String lMethodName = "GetOrdersByStatus";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        TextView ltxtMakeNoRecords = (TextView) view.findViewById(R.id.txtMakeNoRecords);
        ltxtMakeNoRecords.setText("");

        if (jsonString.equals("0")) {
            ltxtMakeNoRecords.setText("No records found!");
        } else {

            ListView lstAll = (ListView) view.findViewById(R.id.lstMakeOrderList);
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), JSONOrder.newInstance().GetJSONOrderList(jsonString),
                    R.layout.orderrow, new String[]{"OrderId", "OrderDetail", "DeliveryDate", "Status"},
                    new int[]{R.id.txtOrderRowId, R.id.txtOrderRowDetail, R.id.txtOrderRowDeliveryDate,
                            R.id.txtOrderRowStatus});

            lstAll.setAdapter(simpleAdapter);

            lstAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // selected item
                    lSelectedOrderId = ((TextView) view.findViewById(R.id.txtOrderRowId)).getText().toString();
                    String OrderDetail = ((TextView) view.findViewById(R.id.txtOrderRowDeliveryDate)).getText().toString();

                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("Are you sure to Update the Status as 'Ready to Deliver' for the Order " + OrderDetail + "?")
                            .setTitle("");

                    // Add the buttons
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button

                            ArrayList<clsParameters> lstParameters = new ArrayList<>();
                            clsParameters objParam = new clsParameters();
                            objParam.ParameterName = "OrderID";
                            objParam.ParameterValue = lSelectedOrderId;
                            lstParameters.add(objParam);

                            objParam = new clsParameters();
                            objParam.ParameterName = "StatusID";
                            objParam.ParameterValue = "3";
                            lstParameters.add(objParam);

                            String lMethodName = "UpdateOrderStatus";
                            jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

                            //Refresh the Grid in the Parent
                            MyOrders activity = (MyOrders) getActivity();
                            activity.BindTab(1);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();


                    dialog.show();
                }
            });
        }
    }
}