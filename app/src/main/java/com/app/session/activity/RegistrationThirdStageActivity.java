package com.app.session.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.app.session.R;
import com.app.session.adapter.CustomSpinnerGenderAdapters;
import com.app.session.adapter.SpinnerCAdapter;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RegistrationThirdStageActivity extends BaseActivity {

    Spinner spinnerGender;
    TextInputEditText edt_fullname;
    CheckBox checkbox;
    String gender="M";
    String isConsultants = "0",isUser="0";
   ArrayList<String> arrayList=new ArrayList<>();
   CustomTextView txtSelectGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_third_stage);
        initView();
    }

    private void initView() {

        edt_fullname = (TextInputEditText) findViewById(R.id.edt_fullname);
        checkbox=(CheckBox)findViewById(R.id.checkbox);

        ((CustomTextView) findViewById(R.id.txtBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtSelectGender=(CustomTextView) findViewById(R.id.txtSelectGender);
        txtSelectGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSpinner();
            }
        });
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        arrayList.add("Male");
        arrayList.add("Female");
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);

        SpinnerCAdapter spinnerCustomeAdapter = new SpinnerCAdapter(this, R.layout.spinner_item, arrayList);
        spinnerGender.setAdapter(spinnerCustomeAdapter);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                {
//                    Toast.makeText(context, categoryArrayList.get(i).getCategory_name(), Toast.LENGTH_SHORT).show();
                  gender=arrayList.get(i);
                    Drawable img = context.getResources().getDrawable(R.mipmap.ic_male);
//                    img.setBounds(0, 0, 60, 60);
                    txtSelectGender.setCompoundDrawables(img, null, null, null);
                    txtSelectGender.setText(gender);
//
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l)
//            {
//                showToast(parent.getItemAtPosition(pos).toString());
//                gender=parent.getItemAtPosition(pos).toString();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


        ((CustomTextView) findViewById(R.id.txtNext)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edt_fullname.getText().toString().isEmpty()&&edt_fullname.getText().length()>3)
                {
                    DataPrefrence.setPref(context, Constant.FULLNAME, edt_fullname.getText().toString());
                    DataPrefrence.setPref(context,Constant.GENDER,gender);

//                    if(checkbox.isChecked())
//                    {
//                        isConsultants="1";
//                        isUser="0";
//
//
//                    }
//                    else {
//                        isConsultants="0";
//                        isUser="1";
//
//                    }


                    isConsultants="1";
                    isUser="0";
                    DataPrefrence.setPref(context, Constant.IS_CONSULTANT, isConsultants);
                    DataPrefrence.setPref(context, Constant.IS_USER, isUser);
                    Intent intent = new Intent(context, RegistrationFirstActivity.class);
                    startActivity(intent);

//


                } else {
                    if(edt_fullname.getText().length()<4)
                    {
                        edt_fullname.setError(context.getResources().getString(R.string.error_name_invalid));
                    }
                    else {
                        edt_fullname.setError(context.getResources().getString(R.string.hint_fullname));
                    }
                }
            }
        });

    }



    RecyclerView recyclerViewSpinner;
    public void dialogSpinner() {

        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.myspinner_layout);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            recyclerViewSpinner=(RecyclerView)dd.findViewById(R.id.recyclerView);
            recyclerViewSpinner.setLayoutManager(new LinearLayoutManager(context));

            CustomSpinnerGenderAdapters customSpinnerCountryAdapters=new CustomSpinnerGenderAdapters(context, arrayList, new ApiCallback()
            {
                @Override
                public void result(String x)
                {
                    int position = Integer.parseInt(x);
                    Drawable imgdrop = context.getResources().getDrawable(R.mipmap.dropdownarrow);

                    if(position==0) {
                        gender="M";
                        Drawable img = context.getResources().getDrawable(R.mipmap.ic_male);
                        txtSelectGender.setCompoundDrawablesWithIntrinsicBounds(img, null, imgdrop, null);
                    }
                    else
                    {
                        gender="F";
                        Drawable  imgf = context.getResources().getDrawable(R.mipmap.ic_female);
                        txtSelectGender.setCompoundDrawablesWithIntrinsicBounds(imgf, null, imgdrop, null);
                    }
//                    img.setBounds(0, 0, 60, 60);


                    txtSelectGender.setText(arrayList.get(position));
                    dd.dismiss();
                }
            }) ;
            recyclerViewSpinner.setAdapter(customSpinnerCountryAdapters);
            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }
    }




}
