package org.rdk.CarSell;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainStage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        // Making that our app have only portrait mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageButton fb    = findViewById(R.id.FacebookBtn);
        final Button scndScreen = findViewById(R.id.SecondScreenBtn);

        // Validation of data and switching to next menu
        scndScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText phone        = findViewById(R.id.phoneNumber);
                EditText user         = findViewById(R.id.userName);
                TextView errorMessage = findViewById(R.id.errorMessage);
                errorMessage.setTextColor(Color.RED);

                // We are getting phone number without whitespace
                String phoneNumber = phone.getText().toString().trim();
                String userName    = user.getText().toString();

                // We are checking if phone number is correct or if field for name isn't empty
                if(!Patterns.PHONE.matcher(phoneNumber).matches() || (Integer.parseInt(phoneNumber)<111111111)){
                    errorMessage.setText("Niepoprawny numer telefonu");
                    return;
                }else if(userName.isEmpty()){
                    errorMessage.setText("Musisz podać imię");
                    return;
                }

                errorMessage.setText("");

                Intent scndScreenIntent = new Intent(getApplicationContext(), InformationsStage.class);
                scndScreenIntent.putExtra("userName", userName);
                scndScreenIntent.putExtra("phoneNumber", phoneNumber);
                startActivity(scndScreenIntent);

            }
        });

        // Jumping to facebook side
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/skup.aut.797681366"));

                if(facebookIntent.resolveActivity(getPackageManager())!= null){
                    startActivity(facebookIntent);
                }
            }
        });

    }
}
