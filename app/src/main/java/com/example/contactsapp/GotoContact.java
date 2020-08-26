/*
    Shows the profile of a contact.
    User may wish to Edit,Delete or click on Options
    The Option button is used to show existing contacts living nearby the selected contact

 */

package com.example.contactsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class GotoContact extends AppCompatActivity {
    public String getname;
    public String getnumber;
    Context context;
    String TAG="SAVED";
    ImageView imv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goto_contact);
        SQLiteDatabase db=this.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);
        context=getApplicationContext();
        String name = getIntent().getStringExtra("getname");
        final String number = getIntent().getStringExtra("getnumber");
        getname=name;
        getnumber=number;

        TextView showname = findViewById(R.id.contactname);
        TextView shownickname=findViewById(R.id.contactnick);
        TextView showprimary = findViewById(R.id.primary);
        TextView showsecondary = findViewById(R.id.secondary);
        TextView showmail=findViewById(R.id.mail);
        TextView showaltmail=findViewById(R.id.othermail);
        TextView showaddr=findViewById(R.id.homeaddress);
        TextView showaltaddr=findViewById(R.id.officeaddress);
        TextView showcategory=findViewById(R.id.category);

        imv=(ImageView)findViewById(R.id.showImage);
        Drawable myDrawable = getResources().getDrawable(R.drawable.defaultimage);
        imv.setImageDrawable(myDrawable);

        Cursor cursor1=db.rawQuery("SELECT IMG FROM IMAGEDB WHERE MOBILE='"+getnumber+"' ",null);
        if(cursor1.moveToNext())
        {
            byte[] image = cursor1.getBlob(0);
            Bitmap bmp= BitmapFactory.decodeByteArray(image, 0 , image.length);
            imv.setImageBitmap(bmp);
        }


        showname.setText(name);
        showprimary.setText(number);
        showprimary.setCompoundDrawablePadding(20);
        showprimary.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_call_black_24dp,0);

        final Cursor cursor=db.rawQuery("SELECT ALTMOBILE,NICKNAME,MAIL,ALTMAIL,ADDRESS,ALTADDRESS,CATEGORY FROM CONTACTS WHERE NAME='"+name+"' and MOBILE='"+number+"'",null);

        try {
            if(cursor.moveToNext())
            {
                shownickname.setText("Nickname : "+cursor.getString(1));
                showmail.setText("Mail : "+cursor.getString(2));
                showaltmail.setText("Alternate Email : "+cursor.getString(3));
                showaddr.setText("Home Address : "+cursor.getString(4).trim());
                showaltaddr.setText("Office Address : "+cursor.getString(5));
                showcategory.setText("Category : "+cursor.getString(6));

                final String addr1=cursor.getString(4).trim();
                final String addr2=cursor.getColumnName(5).trim();
                if(addr1.length()>0)
                {

                    showaddr.setText("Home Address : "+cursor.getString(4).trim());
                    showaddr.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_call_black_24dp,0);

                    showaddr.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_location_on_black_24dp,0);
                    showaddr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String addatbeginning="geo:0,0?q=";
                            String showaddr=addatbeginning+cursor.getString(4).replace(' ','+');
                            Uri gmmIntentUri = Uri.parse(showaddr);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    });
                }
                else
                    showaddr.setText("Home Address : ");

                if(cursor.getString(5).trim().length()!=0)
                {


                    showaltaddr.setText("Office Address : "+cursor.getString(5));
                    showaltaddr.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.ic_location_on_black_24dp);
                    showaltaddr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String addatbeginning="geo:0,0?q=";
                            String showaddr=addatbeginning+cursor.getString(5).replace(' ','+');
                            Uri gmmIntentUri = Uri.parse(showaddr);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    });
                }
                else
                    showaltaddr.setText("Office Address : ");

                Log.i("Other ",cursor.getString(0));
                final String othernumber=cursor.getString(0).trim();
                if(othernumber.length()>0)
                {
                    showsecondary.setCompoundDrawablePadding(20);
                    showsecondary.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_call_black_24dp,0);

                    showsecondary.setText(othernumber);
                    showsecondary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(GotoContact.this,new String[]{Manifest.permission.CALL_PHONE},1);
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions

                                return;
                            }
                            Log.i("Calling ",othernumber);
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + othernumber));
                            startActivity(callIntent);
                        }
                    });
                }


            }
        }
        catch (Exception e)
        {
            Log.i("Debug ","Error");
        }


        showprimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(GotoContact.this,new String[]{Manifest.permission.CALL_PHONE},1);
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions

                    return;
                }
                String callnumber = number.trim();
                Log.i("Calling ",callnumber);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + callnumber));
                startActivity(callIntent);

            }
        });

    }
    public void editContact(View view)
    {
        SQLiteDatabase db=this.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);
        Cursor cursor=db.rawQuery("SELECT ID FROM CONTACTS WHERE NAME='"+getname+"' AND MOBILE='"+getnumber+"' ",null);
        String id;
        try {

            if (cursor.moveToNext()) {
                id = cursor.getString(0);
                Log.i("Id ", id);

                Intent intent = new Intent(this, Save.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this,"Unexpected Error",Toast.LENGTH_SHORT);
        }

    }
    public void deleteContact(View view)
    {

        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setMessage("Delete?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                util();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog ad=alert.create();
        ad.show();

    }
    public void options(View view)
    {
        SQLiteDatabase db=this.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);
        Cursor cursor;
        cursor=db.rawQuery("SELECT ID,ADDRESS FROM CONTACTS WHERE NAME='"+getname+"' AND MOBILE='"+getnumber+"' ",null);
        if(cursor.moveToNext())
        {
            Log.i("Show ",cursor.getString(0)+ " LENGTH = "+ String.valueOf(cursor.getString(0).length()));
            String id=cursor.getString(0);
            String addr=cursor.getString(1);
            if(addr.length()>0)
            {
                Intent intent=new Intent(this,Options.class);
                intent.putExtra("id",id);
                intent.putExtra("name",getname);
                intent.putExtra("mobile",getnumber);
                intent.putExtra("address",addr);
                startActivity(intent);
            }

            else
                Toast.makeText(this,"No Address Found",Toast.LENGTH_SHORT);

        }

    }
    public void home(View view)
    {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void util()
    {
        SQLiteDatabase db=context.openOrCreateDatabase("ContactsDB",MODE_PRIVATE,null);
        try
        {
            db.execSQL("DELETE FROM CONTACTS WHERE NAME='"+getname+"' AND MOBILE='"+getnumber+"' ");
            db.execSQL("DELETE FROM COORDS WHERE MOBILE='"+getnumber+"' ");
            db.execSQL("DELETE FROM IMAGEDB WHERE MOBILE='"+getnumber+"' ");
            Toast toast=Toast.makeText(context,"Deleted!",Toast.LENGTH_SHORT);
            toast.show();
        }
        catch (Exception e)
        {
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
        }
        Intent intent=new Intent(context,MainActivity.class);
        startActivity(intent);

    }

}