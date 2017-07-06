package in.collectiva.tailoringordertracking.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import in.collectiva.tailoringordertracking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddOrder extends DialogFragment {

    private EditText ledtAddOrderName;
    private EditText ledtAddOrderMobileNo;
    private EditText ledtAddOrderDeliveryDate;
    private Button lbtnAddOrderSave;
    private Button lbtnAddOrderClose;

    public AddOrder() {
        // Required empty public constructor
    }

    public static AddOrder newInstance(String title) {
        AddOrder frag = new AddOrder();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field from view and set events.
        ledtAddOrderName = (EditText) view.findViewById(R.id.edtAddOrderName);
        ledtAddOrderMobileNo = (EditText) view.findViewById(R.id.edtAddOrderMobileNo);
        ledtAddOrderDeliveryDate = (EditText) view.findViewById(R.id.edtAddOrderDeliveryDate);
        lbtnAddOrderSave = (Button) view.findViewById(R.id.btnAddOrderSave);
        lbtnAddOrderClose = (Button) view.findViewById(R.id.btnAddOrderClose);

        lbtnAddOrderSave.setOnClickListener(lbtnAddOrderSaveListener);
        lbtnAddOrderClose.setOnClickListener(lbtnAddOrderCloseListener);

        // Set title
        getDialog().setTitle("Add Order");
        // Show soft keyboard automatically and request focus to field
        ledtAddOrderName.requestFocus();
        //getDialog().getWindow().setSoftInputMode(
        //WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private View.OnClickListener lbtnAddOrderSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener lbtnAddOrderCloseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AddOrder.this.getDialog().cancel();
        }
    };

}
