package com.baimeng.loveflower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private LoveLayout mLoveLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoveLayout = (LoveLayout)findViewById(R.id.my_love);
    }

    public void addLove(View view){
        mLoveLayout.addLove();
    }
}
