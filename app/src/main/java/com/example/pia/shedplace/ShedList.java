package com.example.pia.shedplace;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShedList extends Fragment {


    public ShedList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shed_list, container, false);
        Button backButton = (Button) view.findViewById(R.id.btnback);
        String btnText = "BACK TO SHED " + (MainActivity.currentPage-1);
        backButton.setText(btnText);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        ListView lv = (ListView) getActivity().findViewById(R.id.list);
        ArrayList<String> listArray = new ArrayList<String>();
        Iterator iterator = MainActivity.entryLogs.iterator();
        while (iterator.hasNext()){
            ShedLogs item = (ShedLogs) iterator.next();
            if (item.getShedNbr() == (MainActivity.currentPage-1)) {
                listArray.add(item.getShedNbr() + " " + item.getTemperature() + " " + item.getHumidity() + " " + item.getAmmonia() + " " + item.getTreatment() + " " + item.getDate()
                +" " +item.getLatitude()+"  "+item.getLongitude());
            }
        }
        ArrayAdapter<String> adp = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listArray);
        lv.setAdapter(adp);


    }

}
