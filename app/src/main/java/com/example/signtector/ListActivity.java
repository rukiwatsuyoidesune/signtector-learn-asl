package com.example.signtector;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = findViewById(R.id.listView);

        List<Item> items = new ArrayList<>();
        items.add(new Item("0 Zero", R.drawable.asl_zero));
        items.add(new Item("1 One", R.drawable.asl_one));
        items.add(new Item("2 Two", R.drawable.asl_two));
        items.add(new Item("3 Three", R.drawable.asl_three));
        items.add(new Item("4 Four", R.drawable.asl_four));
        items.add(new Item("5 Five", R.drawable.asl_five));
        items.add(new Item("6 Six", R.drawable.asl_six));
        items.add(new Item("7 Seven", R.drawable.asl_seven));
        items.add(new Item("8 Eight", R.drawable.asl_eight));
        items.add(new Item("9 Nine", R.drawable.asl_nine));
        items.add(new Item("Aa", R.drawable.asl_a));
        items.add(new Item("Bb", R.drawable.asl_b));
        items.add(new Item("Cc", R.drawable.asl_c));
        items.add(new Item("Dd", R.drawable.asl_d));
        items.add(new Item("Ee", R.drawable.asl_e));
        items.add(new Item("Ff", R.drawable.asl_f));
        items.add(new Item("Gg", R.drawable.asl_g));
        items.add(new Item("Hh", R.drawable.asl_h));
        items.add(new Item("Ii", R.drawable.asl_i));
        items.add(new Item("Jj", R.drawable.asl_j));
        items.add(new Item("Kk", R.drawable.asl_k));
        items.add(new Item("Ll", R.drawable.asl_l));
        items.add(new Item("Mm", R.drawable.asl_m));
        items.add(new Item("Nn", R.drawable.asl_n));
        items.add(new Item("Oo", R.drawable.asl_o));
        items.add(new Item("Pp", R.drawable.asl_p));
        items.add(new Item("Qq", R.drawable.asl_q));
        items.add(new Item("Rr", R.drawable.asl_r));
        items.add(new Item("Ss", R.drawable.asl_s));
        items.add(new Item("Tt", R.drawable.asl_t));
        items.add(new Item("Uu", R.drawable.asl_u));
        items.add(new Item("Vv", R.drawable.asl_v));
        items.add(new Item("Ww", R.drawable.asl_w));
        items.add(new Item("Xx", R.drawable.asl_x));
        items.add(new Item("Yy", R.drawable.asl_y));
        items.add(new Item("Zz", R.drawable.asl_z));

        CustomAdapter adapter = new CustomAdapter(this, items);
        listView.setAdapter(adapter);
    }
}

