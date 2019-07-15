package org.rdk.CarSell;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class InformationsStage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String spinnerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations_stage);

        // Making that our app have only portrait mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final TextView comunicat      = findViewById(R.id.comunicat);
        Spinner carsBrands            = findViewById(R.id.carsBrands);
        final EditText carModelText   = findViewById(R.id.carModelText);
        final EditText carYearText    = findViewById(R.id.carYearText);
        final EditText describeCarTex = findViewById(R.id.describeCarText);
        final CheckBox isDamagedBox   = findViewById(R.id.isDamagedBox);
        final Button thirdScreenBtn   = findViewById(R.id.thirdScreenBtn);

        String userName = null;
        String phoneNumber = null;

        // Message to the User
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userName = extras.getString("userName");
            phoneNumber = extras.getString("phoneNumber");
            comunicat.setText("Witaj "+ userName+"! \nUzupełnij proszę wszystkie informacje");
        }

        // Creating our spinner
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.carsBrands, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        carsBrands.setAdapter(adapter);
        carsBrands.setOnItemSelectedListener(this);


        final String finalUserName = userName;
        final String finalPhoneNumber = phoneNumber;
        thirdScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String carModel = carModelText.getText().toString();
                String carYear  = carYearText.getText().toString();
                String describeCar = describeCarTex.getText().toString();
                Boolean isDamaged = isDamagedBox.isChecked();

                if(carModel.isEmpty() || carYear.isEmpty() || describeCar.isEmpty()){
                    comunicat.setTextColor(Color.RED);
                    comunicat.setText("Proszę Uzupełnić wszystkie pola");
                    return;
                }

                if(carYear.length()<4){
                    comunicat.setTextColor(Color.RED);
                    comunicat.setText("Zła data");
                    return;
                }

                Intent thirdScreenIntent=new Intent(getApplicationContext(), PhotoStage.class);
                thirdScreenIntent.putExtra("userName", finalUserName);
                thirdScreenIntent.putExtra("phoneNumber", finalPhoneNumber);
                thirdScreenIntent.putExtra("carMark", spinnerText);
                thirdScreenIntent.putExtra("carModel", carModel);
                thirdScreenIntent.putExtra("carYear", carYear);
                thirdScreenIntent.putExtra("describeCar", describeCar);
                thirdScreenIntent.putExtra("isDamaged", isDamaged);
                startActivity(thirdScreenIntent);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerText=parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),spinnerText,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
