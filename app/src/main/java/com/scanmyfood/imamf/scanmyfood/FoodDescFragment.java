package com.scanmyfood.imamf.scanmyfood;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FoodDescFragment extends Fragment {

    private TextView tv_nutrition;
    private View v;

    public FoodDescFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_food_desc, container, false);
        // Inflate the layout for this fragment

        tv_nutrition = v.findViewById(R.id.tv_nutrition);
        tv_nutrition.setText(getResources().getText(R.string.lorem));
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
