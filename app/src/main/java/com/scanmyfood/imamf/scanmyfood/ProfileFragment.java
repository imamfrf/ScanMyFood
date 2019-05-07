package com.scanmyfood.imamf.scanmyfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scanmyfood.imamf.scanmyfood.Model.User;

import java.util.ArrayList;
import java.util.HashMap;

import static com.scanmyfood.imamf.scanmyfood.util.Constant.CHILD.CHILD_USERS;


public class ProfileFragment extends Fragment {

    private Toolbar mToolbar;

    private TextView mTextViewNamaLengkap;
    private TextView mTextViewEmail;
    private TextView mTextViewUsia;
    private TextView tv_avg_cal;

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth auth;
    private String mUserId;
    private HashMap<String, String> weekly;
    private ArrayList<String> arr = new ArrayList<>();
    private ArrayList<Double> arr1 = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        mToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weekly = new HashMap<>();

        mTextViewNamaLengkap = view.findViewById(R.id.textViewNamaLengkap);
        mTextViewEmail = view.findViewById(R.id.textViewEmail);
        mTextViewUsia = view.findViewById(R.id.textViewUsia);
        tv_avg_cal = view.findViewById(R.id.tv_avg_cal);

        auth = FirebaseAuth.getInstance();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CHILD_USERS);
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseReference.child(mUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String nama = user.getNama();
                String email = user.getEmail();
                int usia = user.getUsia();
                mTextViewNamaLengkap.setText(nama);
                mTextViewEmail.setText(email);
                mTextViewUsia.setText(usia + " tahun");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseReference.child(mUserId).child("daily").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double sum = 0;
                int count = 1;
                for (DataSnapshot sn : dataSnapshot.getChildren()) {
                    sum += Double.valueOf(String.valueOf(sn.getValue()));
                    count++;
                }
                double avg = sum / count;
                tv_avg_cal.setText(String.format("%.2f", avg) + " kal");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseReference.child(mUserId).child("daily").addValueEventListener(new ValueEventListener() {
            String date = "";

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> date = new ArrayList<>();
                ArrayList<Double> cal = new ArrayList<>();

                for (DataSnapshot sn : dataSnapshot.getChildren()) {
                    date.add(sn.getKey());
                    cal.add(Double.valueOf(sn.getValue().toString()));
                }
                fetch(date, cal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        BarChart chart = view.findViewById(R.id.barChart);
        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDescription("Diagram Kalori");
        chart.animateXY(2000, 2000);
        chart.invalidate();


        // Log.d("fetch", fetch().toString());
    }

    public ArrayList<String> fetch(ArrayList<String> date, ArrayList<Double> cal) {
        // arr = new ArrayList<>();
//        arr1 = new ArrayList<>();

        for (int i = 0; i < date.size(); i++) {
            arr.add(date.get(i));
            arr1.add(cal.get(i));
        }
        Log.d("fetchh", arr.get(0).toString());
        Log.d("fettt", String.valueOf(arr.size()));
        return arr;
    }


    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Kalori");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
//        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
//        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
//        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Sen");
        xAxis.add("Sel");
        xAxis.add("Rab");
        xAxis.add("Kam");
        xAxis.add("Jum");
        xAxis.add("Sab");
        return xAxis;
    }
//        db.getReference("users").child(auth.getUid()).child("daily").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int i = 0;
//                for (DataSnapshot sn : dataSnapshot.getChildren()){
//                    Log.d("aduh", sn.getKey());
//                    xAxis.add(sn.getKey());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        xAxis.add("Sen");
//        xAxis.add("Sel");
//        xAxis.add("Rab");
//        xAxis.add("Kam");
//        xAxis.add("Jum");
//        xAxis.add("Sab");
//        xAxis.add("Min");
    // return xAxis;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                auth.signOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
                Toast.makeText(getContext(), "Logout", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}