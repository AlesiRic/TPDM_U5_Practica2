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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main3Activity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;
    DatabaseReference data;
    String otroActivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        data=FirebaseDatabase.getInstance().getReference();

        getPermiso();
        otroActivo="No";
        if(getIntent().getExtras().getInt("jugador")==2){
            data.child("Jugador2").child("telefono").setValue(getPhoneNumber());
            data.child("Jugador2").child("activo").setValue("Si");
            data.child("Jugador2").child("respuesta").setValue(0);
            boolean otroInactivo=true;
            do{

                data.child("Jugador1").child("activo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        otroActivo=dataSnapshot.getValue().toString();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if(otroActivo.equals("Si")){
                    otroInactivo=false;
                }
            }while(otroInactivo);
            Intent intent=new Intent(Main3Activity.this,Main4Activity.class);
            intent.putExtra("jugador",2);


        }
        if(getIntent().getExtras().getInt("jugador")==1){
            boolean otroInactivo=true;
            do{


                data.child("Jugador2").child("activo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        otroActivo=dataSnapshot.getValue().toString();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if(otroActivo.equals("Si")){
                    otroInactivo=false;
                }
            }while(otroInactivo);
            Intent intent=new Intent(Main3Activity.this,Main4Activity.class);
            intent.putExtra("jugador",1);

        }



    }

    private void getPermiso() {

    }


    private String getPhoneNumber(){
        String phone="";
        try{

            TelephonyManager mTelephonyManager;
            mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            phone=mTelephonyManager.getLine1Number();

        }catch(SecurityException e){
            Toast.makeText(Main3Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return phone;
    }


}
