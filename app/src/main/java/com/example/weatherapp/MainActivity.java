package com.example.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.example.weatherapp.app.MyApplication;
import com.example.weatherapp.util.pageAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.com.example.weatherapp.bean.TodayWeather;
import com.example.weatherapp.util.NetUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener,ViewPager.OnPageChangeListener {
    private static final int UPDATE_TODAY_WEATHER = 1;
    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv,
            pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;
    private TextView date1, temperature1, fengxiang1,climate1,
            date2, temperature2, fengxiang2,climate2,
            date3, temperature3, fengxiang3,climate3,
            date4, temperature4, fengxiang4,climate4;

    private ImageView weatherImg, pmImg;
    private ImageView weatherImg1,weatherImg2,weatherImg3,weatherImg4;
    private ImageView page1Img,page2Img;
    private ImageView mCitySelect;
    private ImageView mUpdateBtn;
    private ListView mlistView;
    private ViewPager viewPager;
    private List<View> views;
    private pageAdapter pageadapter;
      // mlistView=(ListView)findViewById(R.id.list_view);
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        initView();
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }

        mCitySelect=(ImageView)findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);

    }
    void initView(){
        //updatedCityCode = "101010100";
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        viewPager = (ViewPager)findViewById(R.id.pages);
        View page1 = LayoutInflater.from(this).inflate(R.layout.page1,null);
        View page2 = LayoutInflater.from(this).inflate(R.layout.page2,null);
         views = new ArrayList<>();
         //findViewById，注意要用R.id.XX
        date1 = (TextView) page1.findViewById(R.id.date1);
        temperature1 = (TextView)page1.findViewById(R.id.temperature1);
        fengxiang1 = (TextView)page1.findViewById(R.id.fengxiang1);
        climate1 = (TextView)page1.findViewById(R.id.climate1);
        weatherImg1 = (ImageView)page1.findViewById(R.id.weather_img1);
        date2 = (TextView) page1.findViewById(R.id.date2);
        temperature2 = (TextView)page1.findViewById(R.id.temperature2);
        fengxiang2 = (TextView)page1.findViewById(R.id.fengxiang2);
        climate2 = (TextView)page1.findViewById(R.id.climate2);
        weatherImg2 = (ImageView)page1.findViewById(R.id.weather_img2);
        date3 = (TextView) page2.findViewById(R.id.date3);
        temperature3 = (TextView)page2.findViewById(R.id.temperature3);
        fengxiang3 = (TextView)page2.findViewById(R.id.fengxiang3);
        climate3 = (TextView)page2.findViewById(R.id.climate3);
        weatherImg3 = (ImageView)page2.findViewById(R.id.weather_img3);
        date4 = (TextView) page2.findViewById(R.id.date4);
        temperature4 = (TextView)page2.findViewById(R.id.temperature4);
        fengxiang4 = (TextView)page2.findViewById(R.id.fengxiang4);
        climate4 = (TextView)page2.findViewById(R.id.climate4);
        weatherImg4 = (ImageView)page2.findViewById(R.id.weather_img4);
        //设置N/A

        date1.setText("N/A");
        temperature1.setText("N/A");

        fengxiang1.setText("N/A");
        climate1.setText("N/A");
        date2.setText("N/A");
        temperature2.setText("N/A");
        fengxiang2.setText("N/A");
        climate2.setText("N/A");
        date3.setText("N/A");
        temperature3.setText("N/A");
        fengxiang3.setText("N/A");
        climate3.setText("N/A");
        date4.setText("N/A");
        temperature4.setText("N/A");
        fengxiang4.setText("N/A");
        climate4.setText("N/A");
        //设置适配器
        views.add(page1);
        views.add(page2);
        pageadapter = new pageAdapter(this,views);
        viewPager.setAdapter(pageadapter);
        page1Img = findViewById(R.id.page1Img);
        page2Img = findViewById(R.id.page2Img);
        viewPager.setOnPageChangeListener(this);
        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
    }

    private TodayWeather parseXML(String xmldata) {

        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
// 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
// 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp"
                        )) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                            else if (xmlPullParser.getName().equals("date") &&
                                    dateCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow1_date(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high")
                                    && highCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow1_high(xmlPullParser.getText().substring(2).trim()
                                );
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low")
                                    && lowCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow1_low(xmlPullParser.getText().substring(2).trim());

                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type")
                                    && typeCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow1_type(xmlPullParser.getText());
                                typeCount++;
                            }else if(xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 1) {
                              eventType = xmlPullParser.next();
                              todayWeather.setTomorrow1_fengxiang(xmlPullParser.getText());
                              fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("date")&& dateCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow2_date(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high")&& highCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow2_high(xmlPullParser.getText().substring(2).trim()
                                );
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low")&& lowCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow2_low(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow2_type(xmlPullParser.getText());
                                typeCount++;
                            }else if(xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow2_fengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow3_date(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high")&& highCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow3_high(xmlPullParser.getText().substring(2).trim()
                                );
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow3_low(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow3_type(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow3_fengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow4_date(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow4_high(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow4_low(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow4_type(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow4_fengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            }
                        }
                        break;
// 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
// 进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }
    public void onClick(View view) {
        if(view.getId()==R.id.title_city_manager){
            Intent i=new Intent(this,SelectCity.class);
            startActivityForResult(i,1);
            //startActivity(i);
        }
        if (view.getId() == R.id.title_update_btn) {
            Log.d("myWeather", "点击更新");
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_ city_code", "101110101");
            Log.d("myWeather", cityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(cityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
            initView();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode= data.getStringExtra("cityCode");
            Log.d("myWeather", "选择的城市代码为"+newCityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(newCityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }
    void updateTodayWeather(TodayWeather todayWeather){

        //int pmIntdata = Integer.parseInt(todayWeather.getPm25());
        String name=todayWeather.getCity();
        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
       // MyApplication.cityname_global = todayWeather.getCity();
        timeTv.setText(todayWeather.getUpdatetime()+ "发布");
        humidityTv.setText("湿度："+todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:"+todayWeather.getFengli());
        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();

        date1.setText(todayWeather.getTomorrow1_date());
        temperature1.setText(todayWeather.getTomorrow1_high()+"~"+todayWeather.getTomorrow1_low());
        fengxiang1.setText(todayWeather.getTomorrow1_fengxiang());climate1.setText(todayWeather.getTomorrow1_type());
        date2.setText(todayWeather.getTomorrow2_date());
        temperature2.setText(todayWeather.getTomorrow2_high()+"~"+todayWeather.getTomorrow2_low());
        fengxiang2.setText(todayWeather.getTomorrow2_fengxiang());
        climate2.setText(todayWeather.getTomorrow2_type());
        date3.setText(todayWeather.getTomorrow3_date());
        temperature3.setText(todayWeather.getTomorrow3_high()+"~"+todayWeather.getTomorrow3_low());
        fengxiang3.setText(todayWeather.getTomorrow3_fengxiang());
        climate3.setText(todayWeather.getTomorrow3_type());
        date4.setText(todayWeather.getTomorrow4_date());
        temperature4.setText(todayWeather.getTomorrow4_high()+"~"+todayWeather.getTomorrow4_low());
        fengxiang4.setText(todayWeather.getTomorrow4_fengxiang());
        climate4.setText(todayWeather.getTomorrow4_type());
        pageadapter.notifyDataSetChanged();
        if(todayWeather.getTomorrow1_type().equals("晴"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_qing);
        if (todayWeather.getTomorrow1_type().equals("暴雪"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if (todayWeather.getTomorrow1_type().equals("暴雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        if (todayWeather.getTomorrow1_type().equals("大暴"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if (todayWeather.getTomorrow1_type().equals("大雪"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if (todayWeather.getTomorrow1_type().equals("大雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if (todayWeather.getTomorrow1_type().equals("多云"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if (todayWeather.getTomorrow1_type().equals("雷阵雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if (todayWeather.getTomorrow1_type().equals("雷阵雨冰雹"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if (todayWeather.getTomorrow1_type().equals("沙尘暴"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        if (todayWeather.getTomorrow1_type().equals("特大暴雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if(todayWeather.getTomorrow1_type().equals("雾"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_wu);
        if (todayWeather.getTomorrow1_type().equals("小雪"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if (todayWeather.getTomorrow1_type().equals("小雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if(todayWeather.getTomorrow1_type().equals("阴"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_yin);
        if (todayWeather.getTomorrow1_type().equals("雨夹雪"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if (todayWeather.getTomorrow1_type().equals("阵雪"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if (todayWeather.getTomorrow1_type().equals("阵雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if (todayWeather.getTomorrow1_type().equals("中雪"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if (todayWeather.getTomorrow1_type().equals("中雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        if(todayWeather.getTomorrow2_type().equals("晴"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_qing);
        if (todayWeather.getTomorrow2_type().equals("暴雪"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if (todayWeather.getTomorrow2_type().equals("暴雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        if (todayWeather.getTomorrow2_type().equals("大暴"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if (todayWeather.getTomorrow2_type().equals("大雪"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if (todayWeather.getTomorrow2_type().equals("大雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if (todayWeather.getTomorrow2_type().equals("多云"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if (todayWeather.getTomorrow2_type().equals("雷阵雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if (todayWeather.getTomorrow2_type().equals("雷阵雨冰雹"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if (todayWeather.getTomorrow2_type().equals("沙尘暴"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        if (todayWeather.getTomorrow2_type().equals("特大暴雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if(todayWeather.getTomorrow2_type().equals("雾"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_wu);
        if (todayWeather.getTomorrow2_type().equals("小雪"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if (todayWeather.getTomorrow2_type().equals("小雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if(todayWeather.getTomorrow2_type().equals("阴"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_yin);
        if (todayWeather.getTomorrow2_type().equals("雨夹雪"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if (todayWeather.getTomorrow2_type().equals("阵雪"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if (todayWeather.getTomorrow2_type().equals("阵雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if (todayWeather.getTomorrow2_type().equals("中雪"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if (todayWeather.getTomorrow2_type().equals("中雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_zhongyu);

        if(todayWeather.getTomorrow3_type().equals("晴"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_qing);
        if (todayWeather.getTomorrow3_type().equals("暴雪"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if (todayWeather.getTomorrow3_type().equals("暴雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        if (todayWeather.getTomorrow3_type().equals("大暴"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if (todayWeather.getTomorrow3_type().equals("大雪"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if (todayWeather.getTomorrow3_type().equals("大雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if (todayWeather.getTomorrow3_type().equals("多云"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if (todayWeather.getTomorrow3_type().equals("雷阵雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if (todayWeather.getTomorrow3_type().equals("雷阵雨冰雹"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if (todayWeather.getTomorrow3_type().equals("沙尘暴"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        if (todayWeather.getTomorrow3_type().equals("特大暴雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if(todayWeather.getTomorrow3_type().equals("雾"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_wu);
        if (todayWeather.getTomorrow3_type().equals("小雪"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if (todayWeather.getTomorrow3_type().equals("小雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if(todayWeather.getTomorrow3_type().equals("阴"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_yin);
        if (todayWeather.getTomorrow3_type().equals("雨夹雪"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if (todayWeather.getTomorrow3_type().equals("阵雪"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if (todayWeather.getTomorrow3_type().equals("阵雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if (todayWeather.getTomorrow3_type().equals("中雪"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if (todayWeather.getTomorrow3_type().equals("中雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        if(todayWeather.getTomorrow4_type().equals("晴"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_qing);
        if (todayWeather.getTomorrow4_type().equals("暴雪"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if (todayWeather.getTomorrow4_type().equals("暴雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        if (todayWeather.getTomorrow4_type().equals("大暴"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if (todayWeather.getTomorrow4_type().equals("大雪"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if (todayWeather.getTomorrow4_type().equals("大雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if (todayWeather.getTomorrow4_type().equals("多云"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if (todayWeather.getTomorrow4_type().equals("雷阵雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if (todayWeather.getTomorrow4_type().equals("雷阵雨冰雹"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if (todayWeather.getTomorrow4_type().equals("沙尘暴"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        if (todayWeather.getTomorrow4_type().equals("特大暴雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if(todayWeather.getTomorrow4_type().equals("雾"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_wu);
        if (todayWeather.getTomorrow4_type().equals("小雪"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if (todayWeather.getTomorrow4_type().equals("小雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if(todayWeather.getTomorrow4_type().equals("阴"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_yin);
        if (todayWeather.getTomorrow4_type().equals("雨夹雪"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if (todayWeather.getTomorrow4_type().equals("阵雪"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if (todayWeather.getTomorrow4_type().equals("阵雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if (todayWeather.getTomorrow4_type().equals("中雪"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if (todayWeather.getTomorrow4_type().equals("中雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_zhongyu);

        //if(pmIntdata<=50)pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
   //     else if(pmIntdata<=100)pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
      //  else if(pmIntdata<=150)pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
      //  else if(pmIntdata<=200)pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
      //  else if(pmIntdata<=300)pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
     //   else
         //   pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);

        if(todayWeather.getType().equals("晴"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
        if (todayWeather.getType().equals("暴雪"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if (todayWeather.getType().equals("暴雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        if (todayWeather.getType().equals("大暴雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if (todayWeather.getType().equals("大雪"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if (todayWeather.getType().equals("大雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if (todayWeather.getType().equals("多云"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if (todayWeather.getType().equals("雷阵雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if (todayWeather.getType().equals("雷阵雨冰雹"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if (todayWeather.getType().equals("沙尘暴"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);

        if (todayWeather.getType().equals("特大暴雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if(todayWeather.getType().equals("雾"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
        if (todayWeather.getType().equals("小雪"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if (todayWeather.getType().equals("小雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if(todayWeather.getType().equals("阴"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
        if (todayWeather.getType().equals("雨夹雪"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if (todayWeather.getType().equals("阵雪"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if (todayWeather.getType().equals("阵雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if (todayWeather.getType().equals("中雪"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if (todayWeather.getType().equals("中雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
                Toast.makeText(MainActivity.this, "更新成功!",
                        Toast.LENGTH_SHORT).show();
    }

    /**
     * @param cityCode
     */
    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
TodayWeather todayWeather=null;


                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);
                  // TodayWeather todayWeather = null;
                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null) {
                        Log.d("myWeather", todayWeather.toString());
                        Message msg =new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
                        mHandler.sendMessage(msg);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if(i==0){
            page1Img.setImageResource(R.drawable.point_enable);
            page2Img.setImageResource(R.drawable.point_disable);
        }else {
            //如果是第二页，设置第一个小圆点为灰，第二个为红色
            page1Img.setImageResource(R.drawable.point_disable);
            page2Img.setImageResource(R.drawable.point_enable);
            }


    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}

