package com.example.clowntoy.tpdm_u5_practica2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Juego extends View {

    int NumeroJugador;
    boolean ambosSelectos;
    Context contexto;
    Thread respuesta;
    int status=0;
    Intent intent;
    int figura,figuraB;
    private SensorManager sensorManager;
    private SensorEventListener evento;
    private Sensor sensor;
    private int puntaje1,puntaje2;
    private boolean juego;
    CountDownTimer a;
    boolean movable;
    DatabaseReference data;
    Bitmap ro=BitmapFactory.decodeResource(getResources(),R.drawable.rock);
    Bitmap pa=BitmapFactory.decodeResource(getResources(),R.drawable.paper);
    Bitmap sc=BitmapFactory.decodeResource(getResources(),R.drawable.scissor);

    public Juego(Context context,Intent intent) {
        super(context);
        contexto=context;
        movable=true;
        this.intent=intent;
        data=FirebaseDatabase.getInstance().getReference();

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        evento=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(movable){
                    float x=event.values[0];
                    float y=event.values[1];
                    float z=event.values[2];
                    if(Math.abs(x)>Math.abs(z) && Math.abs(x)>Math.abs(y)){
                        if(NumeroJugador==1){
                            data.child("Jugador1").child("respuesta").setValue(1);
                            figura=1;
                            movable=false;
                            if(figuraB!=0){
                                ambosSelectos=true;
                                a.start();
                            }
                        }
                        if(NumeroJugador==2){
                            data.child("Jugador2").child("respuesta").setValue(1);
                            figura=1;
                            movable=false;
                            if(figuraB!=0){
                                ambosSelectos=true;
                                a.start();
                            }
                        }
                    }
                    if(Math.abs(y)>Math.abs(z) && Math.abs(y)>Math.abs(x)){
                        if(NumeroJugador==1){
                            data.child("Jugador1").child("respuesta").setValue(2);
                            figura=2;
                            movable=false;
                            if(figuraB!=0){
                                ambosSelectos=true;
                                a.start();
                            }
                        }
                        if(NumeroJugador==2){
                            data.child("Jugador2").child("respuesta").setValue(2);
                            figura=2;
                            movable=false;
                            if(figuraB!=0){
                                ambosSelectos=true;
                                a.start();
                            }
                        }
                    }
                    if(Math.abs(z)>Math.abs(x) && Math.abs(z)>Math.abs(y)){
                        if(NumeroJugador==1){
                            data.child("Jugador1").child("respuesta").setValue(3);
                            figura=3;
                            movable=false;
                            if(figuraB!=0){
                                ambosSelectos=true;
                                a.start();
                            }
                        }
                        if(NumeroJugador==2){
                            data.child("Jugador2").child("respuesta").setValue(3);
                            figura=3;
                            movable=false;
                            if(figuraB!=0){
                                ambosSelectos=true;
                                a.start();
                            }
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(evento,sensor,SensorManager.SENSOR_DELAY_NORMAL);




        juego=true;
        NumeroJugador=this.intent.getExtras().getInt("jugador");
        respuesta=new Thread(new Runnable() {
            @Override
            public void run() {
                do{
                    if(NumeroJugador==1){
                        data.child("Jugador2").child("respuesta").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                figuraB=Integer.parseInt(dataSnapshot.getValue().toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        if(figura!=0 && figuraB!=0){
                            ambosSelectos=true;
                        }
                    }
                    if(NumeroJugador==2){
                        data.child("Jugador1").child("respuesta").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                figuraB=Integer.parseInt(dataSnapshot.getValue().toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        if(figura!=0 && figuraB!=0){
                            ambosSelectos=true;
                        }
                    }
                    if(ambosSelectos){
                        a.start();
                    }
                    invalidate();
                }while(true);
            }
        });
        respuesta.start();
        a=new CountDownTimer(100000,100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                movable=true;
                if((figura==1 && figuraB==3)||(figura==2 && figuraB==1)||(figura==3 && figuraB==2)){
                    puntaje1=puntaje1+1;
                }
                if((figura==1 && figuraB==2)||(figura==2 && figuraB==3)||(figura==3 && figuraB==1)){
                    puntaje2=puntaje2+1;
                }
                figura=figuraB=0;
                if(NumeroJugador==1){
                    data.child("Jugador1").child("respuesta").setValue(0);
                }
                if(NumeroJugador==2){
                    data.child("Jugador2").child("respuesta").setValue(0);
                }

                if(puntaje1==3){
                    status=1;
                    juego=false;
                }
                if(puntaje2==3){
                    status=2;
                    juego=false;
                }

            }
        };



    }

    @Override
    protected void onDraw(Canvas c) {
        Paint p=new Paint();
        if(juego) {
            if (intent.getExtras().getInt("jugador") == 1) {
                data.child("Jugador1").child("puntaje").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        puntaje1 = Integer.parseInt(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                data.child("Jugarod2").child("puntaje").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        puntaje2 = Integer.parseInt(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            if (intent.getExtras().getInt("jugador") == 2) {
                data.child("Jugador2").child("puntaje").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        puntaje1 = Integer.parseInt(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                data.child("Jugarod1").child("puntaje").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        puntaje2 = Integer.parseInt(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            p.setColor(Color.BLACK);
            p.setTextSize(20);
            c.drawText(puntaje1+"",50,50,p);
            c.drawText(puntaje2+"",getWidth()-50,getHeight()-50,p);
            switch (figura) {
                case 0:
                    break;
                case 1:
                    c.drawBitmap(ro, 100, 500, p);
                    break;
                case 2:
                    c.drawBitmap(pa,100,500,p);
                    break;
                case 3:
                    c.drawBitmap(sc,100,500,p);
                    break;
                default:break;
            }
            switch (figuraB) {
                case 0:
                    break;
                case 1:
                    c.drawBitmap(ro, 100, 200, p);
                    break;
                case 2:
                    c.drawBitmap(pa,100,200,p);
                    break;
                case 3:
                    c.drawBitmap(sc,100,200,p);
                    break;
                default:break;
            }
        }else{
            switch(status){
                case 1:
                    p.setTextSize(50);
                    p.setColor(Color.GREEN);
                    c.drawText("VICTORIA!!!",100,100,p);
                    break;
                case 2:
                    p.setTextSize(40);
                    p.setColor(Color.RED);
                    c.drawText("Derrota",100,800,p);
                    break;
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!juego){
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                Intent intentC=new Intent(contexto,MainActivity.class);
                contexto.startActivity(intentC);
            }
        }
        return false;
    }
}
