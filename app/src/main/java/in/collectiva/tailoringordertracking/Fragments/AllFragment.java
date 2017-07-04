package in.collectiva.tailoringordertracking.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.JSONFiles.JSONItems;
import in.collectiva.tailoringordertracking.JSONFiles.JSONOrder;
import in.collectiva.tailoringordertracking.R;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class AllFragment extends Fragment {

    private String jsonString;
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
        return inflater.inflate(R.layout.fragment_all, container, false);
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
        objParam.ParameterName = "OrderId";
        objParam.ParameterValue = "0";
        lstParameters.add(objParam);

        String lMethodName = "GetOrders";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        ListView lstAll = (ListView) view.findViewById(R.id.lstAllOrderList);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), JSONOrder.newInstance().GetJSONOrderList(jsonString),
                R.layout.orderrow, new String[] {"OrderId", "OrderDetail", "DeliveryDate", "Status"},
                new int[] {R.id.txtOrderRowId, R.id.txtOrderRowDetail, R.id.txtOrderRowDeliveryDate,
                         R.id.txtOrderRowStatus});

        lstAll.setAdapter(simpleAdapter);
    }

}

