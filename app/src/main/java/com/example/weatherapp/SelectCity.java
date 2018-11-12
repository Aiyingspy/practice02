package com.example.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.app.MyApplication;
import com.example.weatherapp.com.example.weatherapp.bean.City;

import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;
    private ListView mlistView;
    private TextView title_name;
    private List<City> mCityList;
    private EditText mEditText;
    private Button searchBtn;



    private List<City> mCityList_on;
    // private Myadapter myadapter;
    private int xiabiao;
    List<String> listOfStringName = new ArrayList<String>();
    List<String> listOfStringCode = new ArrayList<String>();
    List<String> listOfStringRefrsh = new ArrayList<String>();

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.select_city);
        mEditText = (EditText) findViewById(R.id.search_edit);
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        title_name = (TextView) findViewById(R.id.title_name);
        mlistView = (ListView) findViewById(R.id.list_view);
        MyApplication myApplication = (MyApplication) getApplication();
        mCityList = myApplication.getCityList();

        searchBtn = (Button) findViewById(R.id.button);
        searchBtn.setOnClickListener(this);

        for (City city : mCityList) {
            listOfStringName.add(city.getCity());
            listOfStringCode.add(city.getNumber());

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                SelectCity.this, android.R.layout.simple_list_item_1, listOfStringName);
        adapter.notifyDataSetChanged();
        mlistView.setAdapter(adapter);

//        mListViewAdapter=new ListViewAdapter(this, listOfStringName);
//        //过滤数据
//        //过滤出姓名里面包含"张"的数据
//       mListViewAdapter.getFilter().filter(inputText);
//        mlistView.setAdapter(mListViewAdapter);


        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                xiabiao = i;
                // title_name
                title_name.setText("当前城市：" + listOfStringName.get(xiabiao));
                Toast.makeText(SelectCity.this,
                        "你单击了:" + i,

                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                Intent i = new Intent();

                i.putExtra("cityCode", listOfStringCode.get(xiabiao));
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.button:
                String inputText = mEditText.getText().toString();
                listOfStringName.clear();
                listOfStringCode.clear();
                for (int j = 0; j < mCityList.size(); j++) {

                    String number = mCityList.get(j).getNumber();

                    String cityName = mCityList.get(j).getCity();
                    String Pin = mCityList.get(j).getAllFristPY();

                    if (number.equals(inputText) || cityName.equals(inputText) || Pin.equals(inputText)) {

                        listOfStringName.add(cityName);
                        listOfStringCode.add(number);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            SelectCity.this, android.R.layout.simple_list_item_1, listOfStringName);
                    adapter.notifyDataSetChanged();
                    mlistView.setAdapter(adapter);
                }

            default:
                break;
        }

    }
}



