package in.collectiva.tailoringordertracking.CommonFunction;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import in.collectiva.tailoringordertracking.cConstant.*;

/**
 * Created by Gopinath.R on 6/22/2017.
 */
public class CRUDProcess {

    //Using this method we can get Single Value. For Ex: String, Integer, Long, Double and JSONString (List, DateTable converted into JSONString)...etc
    public String GetScalar(String NameSpace, String MethodName, String RequestURL, String SoapAction, ArrayList<clsParameters> lstParameters) {
        String resultData = "";
        try {
            //Create object for SoapObject for
            SoapObject request = new SoapObject(NameSpace, MethodName);

            if (lstParameters.size() > 0) {
                for (clsParameters ObjParam : lstParameters) {
                    request.addProperty(ObjParam.ParameterName, ObjParam.ParameterValue);
                }
            }
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(RequestURL);
            androidHttpTransport.call(SoapAction, envelope);
            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            resultData = result.toString();
        } catch (Exception e) {
            Log.i("Exception", "Exception Is " + e.getMessage());
            e.printStackTrace();
        }
        return resultData;
    }
}
