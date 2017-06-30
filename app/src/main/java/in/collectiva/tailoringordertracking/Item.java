package in.collectiva.tailoringordertracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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


        }
    };
}
