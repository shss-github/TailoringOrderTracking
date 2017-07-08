package in.collectiva.tailoringordertracking.CommonFunction;

/**
 * Created by dhakchina on 7/8/2017.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;

import in.collectiva.tailoringordertracking.cConstant.clsOrderDetail;

public class SessionOrderDetail {

    public static final String PREFS_NAME = "TOTSession";
    public static final String ORDER_DETAIL = "OrderDetail";

    SharedPreferences settings;
    Editor editor;
    Context _context;

    public SessionOrderDetail() {
        super();
    }

    // Constructor
    public SessionOrderDetail(Context context){
        this._context = context;
        settings = _context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        editor = settings.edit();
    }

    // This four methods are used for maintaining order items.
    public void saveOrderDetails(List<clsOrderDetail> orderDetails) {
        settings = _context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonOrderDetail = gson.toJson(orderDetails);

        editor.putString(ORDER_DETAIL, jsonOrderDetail);

        editor.commit();
    }

    public void addOrderDetail(clsOrderDetail orderDetail) {
        List<clsOrderDetail> orderDetails = getOrderDetail();
        if (orderDetails == null)
            orderDetails = new ArrayList<clsOrderDetail>();
        orderDetails.add(orderDetail);
        saveOrderDetails(orderDetails);
    }

    public void removeOrderDetail(clsOrderDetail orderDetail) {
        ArrayList<clsOrderDetail> orderDetails = getOrderDetail();
        if (orderDetails != null) {
            orderDetails.remove(orderDetail);
            saveOrderDetails(orderDetails);
        }
    }

    public void updateOrderDetail(clsOrderDetail orderDetail) {
        ArrayList<clsOrderDetail> orderDetails = getOrderDetail();

        clsOrderDetail exItem = getExistingItem(orderDetail.OrderId, orderDetail.ItemId);
        if (orderDetails != null) {
            // Remove existing record.
            if(exItem != null)
                orderDetails.remove(exItem);

            orderDetails.add(orderDetail);
            saveOrderDetails(orderDetails);
        }
    }

    public ArrayList<clsOrderDetail> getOrderDetail() {
        SharedPreferences settings;
        List<clsOrderDetail> orderDetails;

        settings = _context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(ORDER_DETAIL)) {
            String jsonOrderDetail = settings.getString(ORDER_DETAIL, null);
            Gson gson = new Gson();
            clsOrderDetail[] orderDetailItems = gson.fromJson(jsonOrderDetail,
                    clsOrderDetail[].class);

            orderDetails = Arrays.asList(orderDetailItems);
            orderDetails = new ArrayList<clsOrderDetail>(orderDetails);
        } else
            return null;

        return (ArrayList<clsOrderDetail>) orderDetails;
    }

    public clsOrderDetail getExistingItem(long lOrderId, long lItemId) {
        clsOrderDetail rtnObj=null;
        List<clsOrderDetail> orderDetails = getOrderDetail();
        if (orderDetails != null) {
            for (clsOrderDetail orderItem : orderDetails) {
                if (orderItem.OrderId == lOrderId && orderItem.ItemId == lItemId) {
                    rtnObj = orderItem;
                    break;
                }
            }
        }
        return rtnObj;
    }

    public boolean checkIncludedItem(long lOrderId, long lItemId) {
        boolean check = false;
        List<clsOrderDetail> orderDetails = getOrderDetail();
        if (orderDetails != null) {
            for (clsOrderDetail orderItem : orderDetails) {
                if (orderItem.OrderId == lOrderId && orderItem.ItemId == lItemId) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    public void clearOrderDetail()
    {
        // Remove all data from Shared Preferences
        editor.remove(ORDER_DETAIL);
        editor.commit();
    }
}