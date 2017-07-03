package in.collectiva.tailoringordertracking.CommonFunction;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.collectiva.tailoringordertracking.R;

/**
 * Created by dhivya on 01/07/2017.
 */

public class JSONAdapter extends BaseAdapter implements ListAdapter {
    private final Activity activity;
    private final JSONArray jsonArray;

    public JSONAdapter(Activity activity, JSONArray jsonArray) {
        assert activity != null;
        assert jsonArray != null;

        this.jsonArray = jsonArray;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        if (null == jsonArray)
            return 0;
        else
            return jsonArray.length();
    }

    @Override
    public JSONObject getItem(int position) {
        if (null == jsonArray) return null;
        else
            return jsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        JSONObject jsonObject = getItem(position);

        return jsonObject.optLong("UserId");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.row, null);

        TextView text = (TextView) convertView.findViewById(R.id.ItemDetail);

        try {
            JSONObject json_data = getItem(position);
            if (null != json_data) {
                String jj = json_data.getString("Name");
                text.setText(jj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
