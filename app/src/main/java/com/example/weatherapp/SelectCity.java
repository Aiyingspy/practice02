package com.example.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.app.MyApplication;
import com.example.weatherapp.com.example.weatherapp.bean.City;

import java.util.ArrayList;
import java.util.List;

public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;
    private ListView mlistView;
    private TextView title_name;
    private List<City> mCityList;
    private List<City> mCityList_on;
   // private Myadapter myadapter;
    private int xiabiao;
    List<String> listOfStringName = new ArrayList<String>();
    List<String> listOfStringCode = new ArrayList<String>();
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.select_city);

        initView();
    }
 //   private String[]  data={"第一组","第二组","第三组","第四组"};

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                Intent i = new Intent();

                i.putExtra("cityCode", listOfStringCode.get(xiabiao));
                setResult(RESULT_OK, i);
                finish();
                break;
            default:
                break;
        }

    }
    private void initView(){
        mBackBtn=(ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        title_name= (TextView) findViewById(R.id.title_name);
        mlistView = (ListView)findViewById(R.id.list_view);
        MyApplication myApplication=(MyApplication) getApplication();
        mCityList= myApplication.getCityList();
        for(City city:mCityList)
        {
          //  mCityList_on.add(city);
            listOfStringName.add(city.getCity());
            listOfStringCode.add(city.getNumber());

        }
        //myadapter = new Myadapter(SelectCity.this,mCityList);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                SelectCity.this,android.R.layout.simple_list_item_1,listOfStringName);
        mlistView.setAdapter(adapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                xiabiao=i;
               // title_name
                title_name.setText("当前城市："+listOfStringName.get(xiabiao));
                Toast.makeText(SelectCity.this,
                        "你单击了:"+i,

                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
