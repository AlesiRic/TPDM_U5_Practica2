package com.example.clowntoy.tpdm_u5_practica2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main2Activity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;
    EditText tel;
    Button busc;
    DatabaseReference data;
    String[] telefonos;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tel = findViewById(R.id.tel);
        busc = findViewById(R.id.botonBuscar);
        data = FirebaseDatabase.getInstance().getReference();
        i = 0;



        data.child("telefono").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    telefonos=new String[(int)dataSnapshot.getChildrenCount()];
                    for(DataSnapshot snap:dataSnapshot.getChildren()){
                        telefonos[i]=snap.getValue().toString();
                        i++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        busc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this,Main3Activity.class);
                intent.putExtra("jugador",1);
                for (int j = 0; j < telefonos.length; j++) {
                    if (tel.getText().toString().equals(telefonos[j])) {
                        data.child("Jugador1").child("telefono").setValue(getPhoneNumber());
                        data.child("Jugador1").child("activo").setValue("Si");
                        data.child("Jugador1").child("respuesta").setValue(0);
                    }
                }
                startActivity(intent);
            }
        });

    }

    private String getPhoneNumber(){
        String phone="";
        try{

            TelephonyManager mTelephonyManager;
            mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            phone=mTelephonyManager.getLine1Number();

        }catch(SecurityException e){
            Toast.makeText(Main2Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return phone;
    }



}
