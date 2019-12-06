package matei.mple.com.qrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public Button addB, scanB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addB=(Button)findViewById(R.id.addbutton);
        scanB=(Button)findViewById(R.id.scanbutton);
        scanB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this,ScanActivity.class);
                startActivity(i);
            }
        });
        addB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2= new Intent(MainActivity.this,AddActivity.class);
                startActivity(i2);
            }
        });

    }
}
