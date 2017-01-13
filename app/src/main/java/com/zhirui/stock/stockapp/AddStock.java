package com.zhirui.stock.stockapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class AddStock extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View bv = this.findViewById(R.id.title);
        bv.setVisibility(View.GONE);
        setContentView(R.layout.activity_add_stock);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        //p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.8
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.7
        getWindow().setAttributes(p);
        Button addBtn = (Button)findViewById(R.id.confirm_add);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);

                finish();
            }
        });
        EditText buyAvgPriceET = (EditText)findViewById(R.id.buy_avg_price);
        EditText buyNumET = (EditText)findViewById(R.id.buy_num);
        EditText waveAvgPriceET = (EditText)findViewById(R.id.wave_avg_price);
        buyAvgPriceET.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        buyNumET.setInputType(InputType.TYPE_CLASS_NUMBER);
        waveAvgPriceET.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);

    }
}
