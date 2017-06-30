package in.collectiva.tailoringordertracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;

public class HomeMenu extends AppCompatActivity {
    // Session Manager Class
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        // Session Manager
        session = new SessionManagement(getApplicationContext());

        TextView ltxtWelcomeMsg = (TextView) findViewById(R.id.txtWelcomeMsg);

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String lName = user.get(SessionManagement.KEY_NAME);

        ltxtWelcomeMsg.setText("Welcome " + lName);

        Toast.makeText(this, user.get(SessionManagement.KEY_USERID), Toast.LENGTH_SHORT).show();
    }
}
