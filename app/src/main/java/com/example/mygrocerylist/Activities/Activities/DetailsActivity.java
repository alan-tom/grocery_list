package com.example.mygrocerylist.Activities.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mygrocerylist.R;

public class DetailsActivity extends AppCompatActivity {

    private TextView name;
    private TextView quantity;
    private Bundle extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        extra = getIntent().getExtras();

        name = (TextView)findViewById(R.id.textView2);
        quantity = (TextView) findViewById(R.id.textView3);

        name.setText(extra.getString("Name"));
        quantity.setText(extra.getString("Quantity"));
    }
}
