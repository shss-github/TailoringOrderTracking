package in.collectiva.tailoringordertracking;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import in.collectiva.tailoringordertracking.Fragments.ItemFragment;

public class Item extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Button lbtnAddItem = (Button) findViewById(R.id.btnAddItem);
        lbtnAddItem.setOnClickListener(lbtnAddItemListener);
    }

    private View.OnClickListener lbtnAddItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAlertDialog();
        }
    };

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ItemFragment alertDialog = ItemFragment.newInstance("Add Item");
        alertDialog.show(fm, "fragment_item");
    }
}
