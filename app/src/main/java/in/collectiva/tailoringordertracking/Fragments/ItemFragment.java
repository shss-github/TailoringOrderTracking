package in.collectiva.tailoringordertracking.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import in.collectiva.tailoringordertracking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends DialogFragment {

    private EditText ledt_ItemName;
    private EditText ledt_ItemAmount;
    private Button lbtnSave;
    private Button lbtnClose;

    public ItemFragment() {
        // Empty constructor required for DialogFragment
    }

    public static ItemFragment newInstance(String title) {
        ItemFragment frag = new ItemFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view and set events.
        ledt_ItemName = (EditText) view.findViewById(R.id.edt_ItemName);
        ledt_ItemAmount = (EditText) view.findViewById(R.id.edt_ItemAmount);
        lbtnSave = (Button) view.findViewById(R.id.btnItemSave);
        lbtnClose = (Button) view.findViewById(R.id.btnItemSave);
        lbtnSave.setOnClickListener(lbtnSaveListener);
        lbtnClose.setOnClickListener(lbtnCloseListener);

        // Set title
        getDialog().setTitle(R.string.dialog_title_items);
        // Show soft keyboard automatically and request focus to field
        ledt_ItemName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private View.OnClickListener lbtnSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener lbtnCloseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater dialogInflater = getActivity().getLayoutInflater();
        View openSourceLicensesView = dialogInflater.inflate(R.layout.fragment_item, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(openSourceLicensesView)
                .setTitle((getString(R.string.dialog_title_items)))
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ItemFragment.this.getDialog().cancel();
                    }
                });
                //.setNeutralButton(android.R.string.ok, null);



        return dialogBuilder.create();
    }*/

}
