package in.collectiva.tailoringordertracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    private View.OnClickListener lbtnRegisterNowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText ledtName = (EditText) findViewById(R.id.edtName);
            EditText ledtMobileNo = (EditText) findViewById(R.id.edtMobileNo);
            EditText ledtPassword = (EditText) findViewById(R.id.edtPassword);

            CheckBox lchkTerms = (CheckBox) findViewById(R.id.chkTerms);
            if(lchkTerms.isChecked()) {
                Toast.makeText(Register.this, "I Accept", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(Register.this, "I Decline", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button lbtnRegisterNow = (Button) findViewById(R.id.btnRegisterNow);
        lbtnRegisterNow.setOnClickListener(lbtnRegisterNowListener);
    }
}
