package r.alpha.aquarium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("TestIoT");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Switch switch1 = (Switch) findViewById(R.id.switchFeed);
        final Switch switch2 = (Switch) findViewById(R.id.switchLight);
        final Switch switch3 = (Switch) findViewById(R.id.switchCover);

        switch1.setOnCheckedChangeListener(this);
        switch2.setOnCheckedChangeListener(this);
        switch3.setOnCheckedChangeListener(this);

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String V1 = dataSnapshot.child("feed").getValue().toString();
                String V2 = dataSnapshot.child("light").getValue().toString();
                String V3 = dataSnapshot.child("cover").getValue().toString();

                if ( V1.compareTo("on")==0 )
                {
                    switch1.setChecked(true);
                }
                else
                {
                    switch1.setChecked(false);
                }
                if ( V2.compareTo("on")==0 )
                {
                    switch2.setChecked(true);
                }
                else
                {
                    switch2.setChecked(false);
                }
                if ( V3.compareTo("on")==0 )
                {
                    switch3.setChecked(true);
                }
                else
                {
                    switch3.setChecked(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // data analysis button -> web link
        LinearLayout dashboard = (LinearLayout) findViewById(R.id.dashboard);
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webLink = new Intent(Intent.ACTION_VIEW);
                // link to analysis website
                webLink.setData(Uri.parse("http://beebotte.com/dash/5fc97370-bfe3-11e9-abc8-f95a79a564a6"));
                startActivity(webLink);
            }
        });
        // live camera button -> web link
        LinearLayout camera = (LinearLayout) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webLink = new Intent(Intent.ACTION_VIEW);
                // link to analysis website
                webLink.setData(Uri.parse("http://192.168.43.229:8000"));
                startActivity(webLink);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.switchFeed:
                    myRef.child("feed").setValue("on");
                    Toast.makeText(MainActivity.this, "State : Start Feeding", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.switchLight:
                    myRef.child("light").setValue("on");
                    Toast.makeText(MainActivity.this, "State : Turn On Light", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.switchCover:
                    myRef.child("cover").setValue("on");
                    Toast.makeText(MainActivity.this, "State : Open Cover", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else
        {
            switch (buttonView.getId()) {
                case R.id.switchFeed:
                    myRef.child("feed").setValue("off");
                    Toast.makeText(MainActivity.this, "State : Stop Feeding", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.switchLight:
                    myRef.child("light").setValue("off");
                    Toast.makeText(MainActivity.this, "State : Turn Off Light", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.switchCover:
                    myRef.child("cover").setValue("off");
                    Toast.makeText(MainActivity.this, "State : Close Cover", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
