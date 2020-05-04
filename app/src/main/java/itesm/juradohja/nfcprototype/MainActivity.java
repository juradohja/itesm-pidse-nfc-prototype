package itesm.juradohja.nfcprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends NfcActivity {

    TextView nameView, birthDateView, bloodTypeView, weightView, heightView;
    TextView hospitalView, ailmentsView, allergiesView;
    TextView contactNameView, contactPhoneView, contactRelationshipView;

    Button btnWrite, btnClear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWrite = findViewById(R.id.writeButton);
        btnClear = findViewById(R.id.clearButton);

        nameView = findViewById(R.id.nameText);
        birthDateView = findViewById(R.id.birthDateText);
        bloodTypeView = findViewById(R.id.bloodTypeText);
        weightView = findViewById(R.id.weightText);
        heightView = findViewById(R.id.heightText);
        hospitalView = findViewById(R.id.hospitalText);
        ailmentsView = findViewById(R.id.conditionsText);
        allergiesView = findViewById(R.id.allergiesText);
        contactNameView = findViewById(R.id.contactNameText);
        contactPhoneView = findViewById(R.id.contactPhoneText);
        contactRelationshipView = findViewById(R.id.contactRelationshipText);

        updateTagProfile();
        buildTagViews();

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTagProfile();
                confirmTagWrite();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nameView.setText("");
                    birthDateView.setText("");
                    bloodTypeView.setText("");
                    weightView.setText("");
                    heightView.setText("");
                    hospitalView.setText("");
                    ailmentsView.setText("");
                    allergiesView.setText("");
                    contactNameView.setText("");
                    contactPhoneView.setText("");
                    contactRelationshipView.setText("");
                } catch (Exception e) {
                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        buildTagViews();
    }

    private void updateTagProfile() {
        tagProfile.name = nameView.getText().toString();
        tagProfile.birthDate = birthDateView.getText().toString();
        tagProfile.bloodType = bloodTypeView.getText().toString();
        tagProfile.weight = weightView.getText().toString();
        tagProfile.height = heightView.getText().toString();
        tagProfile.hospital = hospitalView.getText().toString();
        tagProfile.ailments = ailmentsView.getText().toString();
        tagProfile.allergies = allergiesView.getText().toString();
        tagProfile.contactName = contactNameView.getText().toString();
        tagProfile.contactPhone = contactPhoneView.getText().toString();
        tagProfile.contactRelationship = contactRelationshipView.getText().toString();
    }

    private void buildTagViews() {
        nameView.setText(tagProfile.name);
        birthDateView.setText(tagProfile.birthDate);
        bloodTypeView.setText(tagProfile.bloodType);
        weightView.setText(tagProfile.weight);
        heightView.setText(tagProfile.height);
        hospitalView.setText(tagProfile.hospital);
        ailmentsView.setText(tagProfile.ailments);
        allergiesView.setText(tagProfile.allergies);
        contactNameView.setText(tagProfile.contactName);
        contactPhoneView.setText(tagProfile.contactPhone);
        contactRelationshipView.setText(tagProfile.contactRelationship);
    }
}
