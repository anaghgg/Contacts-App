
/* Shows which existing contacts live nearby a selected Contact

   User can choose to merge the selected contact with anyone living nearby

   User has to ensure there's a proper internet connection so the app can reverse map
   the address using Geocoder
 */


package com.example.contactsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Options extends AppCompatActivity {
    String name,mobile,address,id,latitude,longitude;
    Cursor cursor;
    boolean address_exists=false;
    boolean merge;
    String TAG="ADDRESS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        id=getIntent().getStringExtra("id");
        name=getIntent().getStringExtra("name");
        mobile=getIntent().getStringExtra("mobile");
        address=getIntent().getStringExtra("address");

        Toast.makeText(this,"Please Enable Internet If You Haven't",Toast.LENGTH_SHORT).show();

        SQLiteDatabase db=this.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);
        cursor=db.rawQuery("SELECT LAT,LON FROM COORDS WHERE MOBILE='"+mobile+"' ",null);
        if(!cursor.moveToNext())
        {
            try{
                Geocoder geo=new Geocoder(this,Locale.getDefault());
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
                    address_exists=true;

                }
            }
            catch (Exception e)
            {
                Log.e(TAG,"GEOCODE ERROR",e);
            }
        }
        else {
            address_exists=true;
            latitude=cursor.getString(0);
            longitude=cursor.getString(1);
        }

    }

    public void shownearby(View view)
    {
        merge=false;
        Double d=0.005;
        util(d);

    }

    public void mergenearby(View view)
    {
        merge=true;
        Double d=0.005;
        util(d);
    }
    public void util(Double d)
    {

        SQLiteDatabase db=this.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);
        if(address_exists)
        {


            if(latitude.trim().length()>0 && longitude.trim().length()>0)
            {
                Double dlat=Double.valueOf(latitude.trim());
                Double dlong=Double.valueOf(longitude.trim());
                Log.i(TAG,dlat+"  "+dlong);

                ArrayList<String>finalnames=new ArrayList<>();
                ArrayList<String>finalnumbers=new ArrayList<>();

                cursor=db.rawQuery("SELECT * FROM COORDS WHERE MOBILE!='"+mobile+"' ",null);
                while(cursor.moveToNext())
                {
                    String _mobile=cursor.getString(0);
                    String _name=cursor.getString(1);
                    String _lat=cursor.getString(2);
                    String _long=cursor.getString(3);

                    Double lat=Double.valueOf(_lat);
                    Double lon=Double.valueOf(_long);

                    if(lat<d+dlat && lat>dlat-d && lon<d+dlong && lon>dlong-d)
                    {
                        finalnames.add(_name);
                        finalnumbers.add(_mobile);
                    }
                }



                Log.i(TAG,"All Geo Ok");
                if(finalnames.size()>0 &&!merge)
                {
                    String addressshow="Yes";
                    Intent intent=new Intent(this,Show.class);
                    intent.putExtra("addressconflict",addressshow);
                    intent.putExtra("namelist",finalnames);
                    intent.putExtra("numberlist",finalnumbers);
                    startActivity(intent);
                }
                else if(finalnames.size()>0 && merge)
                {
                    Log.i("Geocode","Merging Geo Ops Started");
                    try {
                        ArrayList<String>info=new ArrayList<>();
                        cursor=db.rawQuery("SELECT * FROM CONTACTS WHERE ID='"+id+"' ",null);
                        if(cursor.moveToNext())
                        {
                            for(int i=0;i<10;i++)
                                info.add(cursor.getString(i));
                            String mergemode="Yes";
                            Intent intent=new Intent(this,Show.class);
                            intent.putExtra("mergemode",mergemode);
                            intent.putExtra("info",info);
                            intent.putExtra("namelist",finalnames);
                            intent.putExtra("numberlist",finalnumbers);
                            startActivity(intent);
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("Geo","Couldn't Go to Show",e);
                    }

                }
                else
                    Toast.makeText(this,"No Contact Nearby",Toast.LENGTH_SHORT).show();

            }
            else
                Toast.makeText(this,"Address Error",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,"Address Error/Invalid or No Internet",Toast.LENGTH_SHORT).show();


    }
}