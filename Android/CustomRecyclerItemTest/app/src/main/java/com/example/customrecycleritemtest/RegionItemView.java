package com.example.customrecycleritemtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


public class RegionItemView extends ConstraintLayout {
    private TextView textView;

    public RegionItemView(@NonNull Context context) {
        super(context);
        init(context);

        textView = findViewById(R.id.tv_region_name);
    }

    public RegionItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

        textView = findViewById(R.id.tv_region_name);
    }

    public RegionItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

        textView = findViewById(R.id.tv_region_name);
    }

    public RegionItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);

        textView = findViewById(R.id.tv_region_name);
    }

    public void setRegionName(String name){
        textView.setText(name);
    }

    private void init(Context context){
        //inflate(getContext(), R.layout.item_region, this);


        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.item_region, this, true);
    }

}
