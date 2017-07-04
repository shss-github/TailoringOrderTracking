package in.collectiva.tailoringordertracking;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

public class ReadyList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
