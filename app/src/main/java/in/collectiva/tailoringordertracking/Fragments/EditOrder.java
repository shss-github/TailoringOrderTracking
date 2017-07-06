package in.collectiva.tailoringordertracking.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.collectiva.tailoringordertracking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditOrder extends Fragment {


    public EditOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_order, container, false);
    }

}
