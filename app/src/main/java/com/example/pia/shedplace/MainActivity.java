package com.example.pia.shedplace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.view.Menu;
import java.io.*;
import android.database.Cursor;
import android.widget.Toast;
import android.content.Context;
import java.util.ArrayList;
import java.util.Iterator;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

public class MainActivity extends Activity {

    //instance variables
    public static int currentPage = 0;
    public static String tittles[] = {"Shed 0", "Shed 1", "Shed 2", "Shed 3", "Shed 4"};
    public static ArrayList<ShedLogs> entryLogs = new ArrayList<ShedLogs>();
    public static String username;
    public String emailString;
    public String[] to = {"piakarine@gmail.com"};
    public String[] cc = {};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBAdapter db = new DBAdapter(this);
        try {
            String destPath = "/data/data/" + getPackageName() + "/databases";
            File f = new File(destPath);
            if (!f.exists()) {   // create dir and then copy db
                f.mkdirs();
                f.createNewFile();
                //---copy the db from the assets folder into
                // the databases folder---
                CopyDB(getBaseContext().getAssets().open("shedlogs.db"),
                        new FileOutputStream(destPath + "/shedlogs.db"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        db.open();
        Cursor c = db.getAllLogs();
        if (c.moveToFirst()){
            do{
                ShedLogs sl = new ShedLogs(Integer.valueOf(c.getString(1)),Float.parseFloat(c.getString(2)),
                        Float.parseFloat(c.getString(3)),Float.parseFloat(c.getString(4)),c.getString(5),c.getString(6), c.getString(7),
                        c.getString(7));
                entryLogs.add(sl);
            } while(c.moveToNext());
        }
        db.close();
        HomeFragment frag = new HomeFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.shedPlace,frag);
        ft.commit();
    }


    public void onClick(View view){
        currentPage = Integer.valueOf((String)view.getTag());
        showCurrentPage();
    }

    public void previous(View view){
        currentPage--;
        if(currentPage <= 0)
            currentPage = 5;
        showCurrentPage();
    }

    public void next(View view){
        currentPage++;
        if(currentPage > 5)
            currentPage = 1;
        showCurrentPage();
    }

    public void home(View view){
        currentPage = 0;
        showCurrentPage();
    }

    public void showCurrentPage(){

        if (currentPage == 0){
            HomeFragment frag = new HomeFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.shedPlace,frag);
            ft.commit();
        }
        else{
            ShedFragment fr = new ShedFragment();
            Bundle args = new Bundle();
            args.putInt("Shed", currentPage);
            fr.setArguments(args);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.shedPlace,fr);
            ft.commit();
        }
    }

    // checks if editTexts have contents

    public static boolean isEmpty(EditText editText){
        return editText.getText().toString().trim().length() == 0;
    }

    public void addReturn(View view){
        ShedFragment fr = new ShedFragment();
        Bundle args = new Bundle();
        args.putInt("Shed", currentPage);
        fr.setArguments(args);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.shedPlace,fr);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.profile:
                ProfileFragment frag = new ProfileFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.shedPlace,frag);
                ft.commit();
                return true;
            case R.id.menusave:

                DBAdapter db = new DBAdapter(this);
                db.open();
                db.removeAll();

                Iterator iterator = MainActivity.entryLogs.iterator();
                while (iterator.hasNext()){
                    ShedLogs shedItem = (ShedLogs) iterator.next();
                    db.insertShedLog((""+shedItem.getShedNbr()),""+shedItem.getTemperature(),
                            ""+shedItem.getHumidity(), ""+shedItem.getAmmonia(),
                            ""+shedItem.getTreatment(),""+shedItem.getDate() ,""+shedItem.getLatitude(),""+shedItem.getLongitude());
                }
                db.close();
                MainActivity.entryLogs.clear();

                Toast.makeText(this,"Log saved to the Database", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.send:
                //final Context ctx = MainActivity.this;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Send all entries").setTitle("Are you sure? This will delete all entries.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new sendingEmail().execute();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
                default:
                    return super.onOptionsItemSelected(item);


        }
    }

    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Database not saved")
                .setMessage("Save entry to database first?")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        DBAdapter db2 = new DBAdapter(MainActivity.this);
                        db2.open();
                        db2.removeAll();

                        Iterator iterator = MainActivity.entryLogs.iterator();
                        while (iterator.hasNext()){
                            ShedLogs shedItem = (ShedLogs) iterator.next();
                            db2.insertShedLog((""+shedItem.getShedNbr()),""+shedItem.getTemperature(),
                                    ""+shedItem.getHumidity(), ""+shedItem.getAmmonia(),
                                    ""+shedItem.getTreatment(),""+shedItem.getDate(),""+shedItem.getLatitude(), ""+shedItem.getLongitude());
                        }
                        db2.close();
                        finish();

                    }
                }).create().show();
    }

   private void sendEmail(String[] emailAddresses, String[] carbonCopies,
                           String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        String[] to = emailAddresses;
        String[] cc = carbonCopies;
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email"));
    }


    private class sendingEmail extends AsyncTask<Void, Void, Boolean>{

        protected Boolean doInBackground(Void...params){
            emailString = ""+username+"\n";
            Iterator iterator = MainActivity.entryLogs.iterator();

            while (iterator.hasNext()){

                ShedLogs item = (ShedLogs) iterator.next();
                if (item.getShedNbr() == (MainActivity.currentPage-1)) {
                    emailString = emailString + " Shed "+item.getShedNbr() + " " + item.getTemperature() + " " + item.getHumidity() + " " + item.getAmmonia() + " " + item.getTreatment() + " " + item.getDate()
                            +" " +item.getLatitude()+"  "+item.getLongitude()+"\n";
                }
            }

            // clears database and ArrayList
            if(emailString!= null){
                DBAdapter db1 = new DBAdapter(MainActivity.this);
                db1.open();
                db1.removeAll();
                db1.close();
                MainActivity.entryLogs.clear();
                sendEmail(to, cc, "Shed Logs", emailString);
            }
            return true;

        }


    }


}
