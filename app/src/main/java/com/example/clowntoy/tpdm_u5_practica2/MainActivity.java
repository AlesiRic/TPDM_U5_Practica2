package com.example.clowntoy.tpdm_u5_practica2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS=0;
    Button arr,bus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arr=findViewById(R.id.armar);
        bus=findViewById(R.id.esperar);

        arr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Main3Activity.class);
                intent.putExtra("jugador",2);
                startActivity(intent);
            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_PHONE_NUMBERS)){
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_NUMBERS},MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
        }
    }
}
