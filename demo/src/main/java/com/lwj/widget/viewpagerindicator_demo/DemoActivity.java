package com.lwj.widget.viewpagerindicator_demo;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        findViewById(R.id.btn_carouse).setOnClickListener(this);
        findViewById(R.id.btn_not_carouse).setOnClickListener(this);
        findViewById(R.id.btn_banner_view).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_carouse:
                startActivity(new Intent(this,CarouseActivity.class));
                break;
            case R.id.btn_not_carouse:
                startActivity(new Intent(this,NotCarouselActivity.class));
                break;
            case R.id.btn_banner_view:
                startActivity(new Intent(this,BannerViewActivity.class));
                break;
        }
    }
}
