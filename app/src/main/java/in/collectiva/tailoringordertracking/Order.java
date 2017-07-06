package in.collectiva.tailoringordertracking;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import in.collectiva.tailoringordertracking.CommonFunction.CRUDProcess;
import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;
import in.collectiva.tailoringordertracking.JSONFiles.JSONItems;
import in.collectiva.tailoringordertracking.cConstant.clsParameters;

public class Order extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String jsonString;
    private static final String NAMESPACE = "http://ws.collectiva.in/";
    private static final String REQURL = "http://ws.collectiva.in/AndroidTailoringService.svc"; //"http://ws.collectiva.in/AndroidTestService.svc";
    final String SOAP_ACTION = "http://ws.collectiva.in/IAndroidTailoringService/"; //http://ws.collectiva.in/IAndroidTestService/RegisterUser";

    //Creating Object For the CRUDProcess common class.
    final CRUDProcess objCRUD = new CRUDProcess();

    // Session Manager Class
    SessionManagement session;
    double lRate = 0.00;

    TextView ltvRate;
    TextView ltvAmount;
    EditText ltxtQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BindSpinner();

        ltvRate = (TextView) findViewById(R.id.tvRate);
        ltvAmount = (TextView) findViewById(R.id.tvAmount);
        ltxtQty = (EditText) findViewById(R.id.txtQty);

        //EditText edtQty = (EditText) findViewById(R.id.txtQty);
        ltxtQty.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                calculateAmount();
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        //DatePicker dtDate = (DatePicker)findViewById(R.id.dtOrderDate);
        //dtDate.setCalendarViewShown(true);
        //dtDate.setSpinnersShown(false);

        final EditText ltxtDate = (EditText) findViewById(R.id.txtDate);
        ImageButton limgDate = (ImageButton) findViewById(R.id.imgDate);

        // perform click event on edit text
        limgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                DatePickerDialog datePicker = new DatePickerDialog(Order.this,
                    android.R.style.Theme_Holo_Light,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text
                            String lday = String.format("%02d", dayOfMonth);
                            String lmonth = String.format("%02d", (monthOfYear + 1));
                            ltxtDate.setText(lday + "/" + lmonth + "/" + year);
                        }
                    }, mYear, mMonth, mDay);

                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.show();
            }
        });
    }

    private void calculateAmount() {
        Integer lQty = 0;
        double lAmount = 0.00;

        if(ltxtQty.getText().toString().trim().equals("") != true && lRate > 0.00)
        {
            lQty = Integer.parseInt(ltxtQty.getText().toString());
            lAmount = (lQty * lRate);
        }

        ltvRate.setText("Rate : " + lRate);
        ltvAmount.setText("Amount : " + lAmount);
    }

    public void BindSpinner()
    {
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.sprItem);

        /*List<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        */

        ArrayList<clsParameters> lstParameters = new ArrayList<>();
        clsParameters objParam = new clsParameters();
        objParam.ParameterName = "UserId";
        objParam.ParameterValue = "14";
        lstParameters.add(objParam);

        objParam = new clsParameters();
        objParam.ParameterName = "ItemId";
        objParam.ParameterValue = "0";
        lstParameters.add(objParam);

        String lMethodName = "GetItems";
        jsonString = objCRUD.GetScalar(NAMESPACE, lMethodName, REQURL, SOAP_ACTION + lMethodName, lstParameters);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, JSONItems.newInstance().GetJSONItemList(jsonString), R.layout.item_spinner_row,
                new String[] {"ItemId", "ItemName", "Amount"},
                new int[] {R.id.txtSpinnerItemId, R.id.txtSpinnerItemName, R.id.txtSpinnerItemAmount});

        // Drop down layout style - list view with radio button
        //simpleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(simpleAdapter);
        spinner.setOnItemSelectedListener(Order.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // Calculate Order amount
        HashMap<String, String> litem = (HashMap<String, String>)parent.getItemAtPosition(position);
        if(litem != null)
        {
            lRate = Double.parseDouble(litem.get("Amount"));
        }
        else {
            lRate = 0.00;
        }
        calculateAmount();


        // Showing selected spinner item
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + lRate , Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}


