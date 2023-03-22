package edu.ucsd.cse110.team19.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HeightActivity extends AppCompatActivity {
    public static final String SHARED_PREF_FILE = "userprefs";
    public static final String SHARED_PREF_HEIGHT = "userHeight";
    public static final String HEIGHT_FORMAT = "^\\d\'(\\d+)\"?";
    private static final float DEFAULT_USER_HEIGHT = 5.6f; // In ft
    private static final int FT_TO_INCHES = 12;

    private EditText heightBox;
    private SharedPreferences pref;
    private float currHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        pref = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        heightBox =  findViewById(R.id.height_text_edit);
        currHeight = pref.getFloat(SHARED_PREF_HEIGHT, DEFAULT_USER_HEIGHT);
        String stringHeight = Conversion.heightFloatToString(currHeight);
        heightBox.setText(stringHeight);

        Button saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        });
    }

    public void updateInfo() {
        Toast errMsg;
        String rawHeight = heightBox.getText().toString().trim();

        if (rawHeight.isEmpty() || !rawHeight.matches(HEIGHT_FORMAT)) {
            errMsg = Toast.makeText(this, "Wrong height format (ft'in\"): " + rawHeight.isEmpty() + " " + rawHeight.matches(HEIGHT_FORMAT), Toast.LENGTH_LONG);
            errMsg.show();
            return;
        }

        float height = 0f;
        try {
            rawHeight= rawHeight.replace("\"", "");
            String[] ftInches = rawHeight.split("\'");
            int feet = Integer.parseInt(ftInches[0]);
            float inches = Float.parseFloat(ftInches[1]);
            // Get float value for height from x'y" format
            height = (float)feet + (inches / FT_TO_INCHES);
            heightBox.setContentDescription(String.valueOf(height));
        } catch (Exception e) {
            System.out.println(e);
        }

        pref.edit().putFloat(SHARED_PREF_HEIGHT, height).apply();

        finish();
    }
}
