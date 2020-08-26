package com.example.contactsapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressService extends IntentService {
    Context context;
    String TAG="ADDRESS";
    String mobile,name,address;
    public AddressService() {
        super("AddressService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
            context=getApplicationContext();
            SQLiteDatabase db=context.openOrCreateDatabase("ContactsDB", MODE_PRIVATE, null);
            Log.i(TAG,"Service Ok");
            name=intent.getStringExtra("name");
            mobile=intent.getStringExtra("mobile");
            address=intent.getStringExtra("address");
            String latitude="",longitude="";




                try{
                    Geocoder geo=new Geocoder(context,Locale.getDefault());
                    List<Address> current=geo.getFromLocationName(address,1);
                    if(current.size()>0)
                    {
                        Double y=current.get(0).getLatitude();
                        Double ry=Math.round(y*100000D)/100000D;
                        latitude=String.valueOf(ry);
                        Double z=current.get(0).getLongitude();
                        Double rz=Math.round(z*100000D)/100000D;
                        longitude=String.valueOf(rz);

                        db.execSQL("INSERT INTO COORDS VALUES ('"+mobile+"','"+name+"','"+latitude+"','"+longitude+"')");
                        Log.i(TAG,latitude+" "+longitude);



                    }
                }
                catch (Exception e)
                {
                    Log.e(TAG,"GEOCODE ERROR",e);
                }




    }
}
