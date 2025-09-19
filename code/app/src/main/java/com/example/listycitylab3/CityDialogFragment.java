package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CityDialogFragment extends DialogFragment {

    private static final String ARG_MODE = "mode";
    private static final String ARG_CITY = "city";
    private static final String ARG_POSITION = "position";

    private static final int MODE_ADD = 0;
    private static final int MODE_EDIT = 1;

    interface AddCityDialogListener {
        void addCity(City city);
        void updateCity(int position, String name, String province);
    }
    // factory to build ADD fragment
    public static CityDialogFragment newInstanceForAdd(){
        CityDialogFragment cdf = new CityDialogFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_MODE, MODE_ADD);
        cdf.setArguments(b);
        return cdf;
    }

    public static CityDialogFragment newInstanceForEdit(int position, City city){
        CityDialogFragment cdf = new CityDialogFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_MODE, MODE_EDIT);
        b.putInt(ARG_POSITION, position);
        b.putSerializable(ARG_CITY, city);
        cdf.setArguments(b);
        return cdf;
    }





    private AddCityDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener){
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + "must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);

        EditText editCityText = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceText = view.findViewById(R.id.edit_text_province_text);
        int mode;
        int position;
        City initial;
        Bundle args = getArguments();
        if (args != null){
            mode = args.getInt(ARG_MODE, MODE_ADD);
        }else{
            mode = MODE_ADD;
        }
        if (args != null){
            position = args.getInt(ARG_POSITION, -1);
        } else{
            position = -1;
        }
        if (args != null){
            initial = (City) args.getSerializable(ARG_CITY);
        } else{
            initial = null;
        }


        String title = "Add City";
        String positive = "Add";

        if (mode == MODE_EDIT && initial != null){
            editCityText.setText(initial.getName());
            editProvinceText.setText(initial.getProvince());
            title = "Edit City";
            positive = "Edit";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(positive, (dialog, which) -> {
                    String cityName = editCityText.getText().toString();
                    String cityProvince = editProvinceText.getText().toString();
                    if (mode == MODE_EDIT){
                        listener.updateCity(position, cityName, cityProvince);

                    } else {
                        listener.addCity(new City(cityName, cityProvince));
                    }
                })
                .create();





    }
}
