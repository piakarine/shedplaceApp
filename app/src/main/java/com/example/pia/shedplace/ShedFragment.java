package com.example.pia.shedplace;


import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.Calendar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShedFragment extends Fragment {

    private String selectedTreat;
    private int shedID;
    private TrackGPS tracking;
    double longitude;
    double latitude;

    public ShedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shed, container, false);
        Bundle args = getArguments();
        shedID = args.getInt("Shed", 0);
        TextView tittleText = (TextView) view.findViewById(R.id.tittle);
        tittleText.setText(MainActivity.tittles[shedID-1]);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        final String[] treatments;
        super.onViewCreated(view, savedInstanceState);
        Button btnSave = (Button) getActivity().findViewById(R.id.saveButton);
        Button btnShow = (Button) getActivity().findViewById(R.id.showButton);
        final EditText temperature = (EditText) getActivity().findViewById(R.id.temp);
        final EditText  humidity = (EditText) getActivity().findViewById(R.id.hum);
        final EditText amm = (EditText) getActivity().findViewById(R.id.amm);
        final Spinner treatment = (Spinner) getActivity().findViewById(R.id.trea);
        treatments = getResources().getStringArray(R.array.treatmentArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, treatments);
        treatment.setAdapter(adapter);
        treatment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int index = adapterView.getSelectedItemPosition();
                selectedTreat = treatments[index];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // check for EditView content

                tracking = new TrackGPS(getContext());

                if(tracking.canGetLocation()){
                    if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION
                                }, 1);
                    }

                    longitude = tracking.getLongitude();
                    latitude = tracking.getLatitude();
                }
                else
                {
                    tracking.showSettingsAlert();
                }



                if(!MainActivity.isEmpty(temperature) && !MainActivity.isEmpty(humidity) && !MainActivity.isEmpty(amm) &&
                        !selectedTreat.equals("")){

                    float temp = Float.parseFloat(temperature.getText().toString());
                    float hum = Float.parseFloat(humidity.getText().toString());
                    float ammonia = Float.parseFloat(amm.getText().toString());
                    ShedLogs dl = new ShedLogs(shedID-1,  temp, hum, ammonia, selectedTreat,currentDate(), Double.toString(latitude), Double.toString(longitude));
                    MainActivity.entryLogs.add(dl);
                    temperature.setText("");
                    humidity.setText("");
                    amm.setText("");
                    treatment.setSelected(false);
                    Toast.makeText(getActivity(),"Log saved!", Toast.LENGTH_SHORT).show();
                }
                else if (MainActivity.isEmpty(temperature)&& MainActivity.isEmpty(humidity) && MainActivity.isEmpty(amm) &&
                        selectedTreat.equals("")){
                    Toast.makeText(getActivity(),"Fields cannot be empty, please enter values", Toast.LENGTH_SHORT).show();

                } else
                    {
                        if (MainActivity.isEmpty(temperature)){
                            Toast.makeText(getActivity(),"Temperature cannot be empty, please enter a value", Toast.LENGTH_SHORT).show();
                        } if (MainActivity.isEmpty(humidity)){
                        Toast.makeText(getActivity(),"Humidity cannot be empty, please enter a value", Toast.LENGTH_SHORT).show();
                        } if (MainActivity.isEmpty(amm)){
                        Toast.makeText(getActivity(),"Ammonia cannot be empty, please enter a value", Toast.LENGTH_SHORT).show();
                        } if (selectedTreat.equals("")){
                        Toast.makeText(getActivity(),"Please select a treatment to be applied", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        });



        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShedList fr = new ShedList();
                Bundle args = new Bundle();
                args.putInt("Shed", MainActivity.currentPage);
                fr.setArguments(args);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.shedPlace,fr);
                ft.commit();

            }
        });
    }

    private String currentDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH) + 1;
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        String currentTime = day + "/" + month + "/" + year + " " + hour + ":" + minute + ":" + second;
        return currentTime;
    }
    /*@Override
    public void onDestroy(){
        super.onDestroy();
        tracking.stopUsingGPS();
    }*/


}
