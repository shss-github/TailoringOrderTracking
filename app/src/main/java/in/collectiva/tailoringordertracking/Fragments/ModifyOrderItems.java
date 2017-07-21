package in.collectiva.tailoringordertracking.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ModifyOrderItems.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ModifyOrderItems#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyOrderItems extends DialogFragment {
    private static final String lItemId = "ItemId";

    // Session Manager Class
    SessionManagement session;

    public ModifyOrderItems() {
        // Required empty public constructor
    }

    public static ModifyOrderItems newInstance(String ItemId) {
        ModifyOrderItems fragment = new ModifyOrderItems();
        Bundle args = new Bundle();
        args.putString(lItemId, ItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Session Manager
        session = new SessionManagement(getActivity().getApplicationContext());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify_order_items, container, false);
    }
}
