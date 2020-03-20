
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        address="";
        id=getIntent().getStringExtra("id");
        Toast.makeText(this,"Enable Internet If You Haven't",Toast.LENGTH_LONG).show();
        SQLiteDatabase db=this.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);
        cursor=db.rawQuery("SELECT ADDRESS FROM CONTACTS WHERE ID='"+id+"' ",null);
        if(cursor.moveToNext())
        {
            if(cursor.getString(0).trim().length()>0)
            {
                address=cursor.getString(0);
                address_exists=true;
            }
        }
        if(address_exists)
            Toast.makeText(this,"Address Valid",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"Address Invalid",Toast.LENGTH_SHORT).show();
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
            Log.i("Geocode","Inside shownearby");
            latitude="";longitude="";
            Geocoder geo=new Geocoder(this, Locale.getDefault());
            try{
                List<Address> current=geo.getFromLocationName(address,1);
                if(current.size()>0)
                {
                    Double y=current.get(0).getLatitude();
                    Double ry=Math.round(y*100000D)/100000D;
                    latitude=String.valueOf(ry);
                    Double z=current.get(0).getLongitude();
                    Double rz=Math.round(z*100000D)/100000D;
                    longitude=String.valueOf(rz);
                    Log.i("Geocode","First Geo Ok");
                }
            }
            catch (IOException e)
            {
                Log.e("Geocode","Error",e);
            }

            if(latitude.trim().length()>0 && longitude.trim().length()>0)
            {
                Double dlat=Double.valueOf(latitude.trim());
                Double dlong=Double.valueOf(longitude.trim());
                Log.i("Geocode",dlat+"  "+dlong);

                ArrayList<String>addresses=new ArrayList<>();
                ArrayList<String>names=new ArrayList<>();
                ArrayList<String>numbers=new ArrayList<>();

                cursor=db.rawQuery("SELECT ID,NAME,MOBILE,ADDRESS FROM CONTACTS WHERE LENGTH(ADDRESS)>0 ",null);

                while(cursor.moveToNext())
                {

                    if(cursor.getString(3).trim().length()>0 && !id.trim().equals(cursor.getString(0).trim()))
                    {
                        names.add(cursor.getString(1));
                        numbers.add(cursor.getString(2));
                        addresses.add(cursor.getString(3));
                    }
                }
                ArrayList<String>finalnames=new ArrayList<>();
                ArrayList<String>finalnumbers=new ArrayList<>();
                if(addresses.size()>0)
                {
                    for(int i=0;i<addresses.size();i++)
                    {
                        try{
                            List<Address> addressList=geo.getFromLocationName(addresses.get(i),1);
                            if(addressList.size()>0)
                            {
                                Double rd1=addressList.get(0).getLatitude();
                                Double rd2=addressList.get(0).getLongitude();
                                Double d1=Math.round(rd1*100000D)/100000D;
                                Double d2=Math.round(rd2*100000D)/100000D;
                                Log.i("Geocode",names.get(i)+" "+String.valueOf(d1)+" "+String.valueOf(d2));



                                if(d1<d+dlat && d1>dlat-d && d2<dlong+d && d2>dlong-d)
                                {
                                    finalnames.add(names.get(i));
                                    finalnumbers.add(numbers.get(i));
                                }

                            }
                        }
                        catch (Exception e)
                        {
                            Log.i("Geocode","SECOND Geo Error");
                        }
                    }
                }

                Log.i("Geocode","All Geo Ok");
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
            Toast.makeText(this,"Address Error/Invalid",Toast.LENGTH_SHORT).show();


    }
}
