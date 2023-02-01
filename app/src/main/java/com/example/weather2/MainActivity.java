package com.example.weather2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "[" + MainActivity.class.getSimpleName() + "]";
    Context context = MainActivity.this;

    TextView tvName;
    TextView tvCountry;
    TextView tvTemp;
    TextView tvMain;
    TextView tvDescription;
    TextView tvWind;
    TextView tvCloud;
    TextView tvHumidity;
    ImageView ivWeather;

    APIService apiInterface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvCountry = (TextView) findViewById(R.id.tv_country);
        ivWeather = (ImageView) findViewById(R.id.iv_weather);
        tvTemp = (TextView) findViewById(R.id.tv_temp);
        tvMain = (TextView) findViewById(R.id.tv_main);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvWind = (TextView) findViewById(R.id.tv_wind);
        tvCloud = (TextView) findViewById(R.id.tv_cloud);
        tvHumidity = (TextView) findViewById(R.id.tv_humidity);

        requestNetwork();
    }

    //retrofit을 통해 통신을 요청
    private void requestNetwork(){

        //retrofit 객체와 인터페이스 연결
        apiInterface = APIClient.getClient(getString(R.string.weather_url)).create(APIService.class);

        //통신 요청
        Call<WeatherInfoModel> call = apiInterface.doGetJsonData("weather", "seoul", getString(R.string.weather_app_id));

        call.enqueue(new Callback<WeatherInfoModel>() {
            @Override
            public void onResponse(Call<WeatherInfoModel> call, Response<WeatherInfoModel> response) {
                WeatherInfoModel resource = response.body();
                if (response.isSuccessful()){
                    setWeatherData(resource);// UI 업데이트
                } else {
                    showFailPop();
                }
            }

            @Override
            public void onFailure(Call<WeatherInfoModel> call, Throwable t) {
                call.cancel();
                showFailPop();
            }
        });
    }

    //통신 하여 받아온 날씨 데이터를 통해 UI 업데이트 메소드
    private void setWeatherData(WeatherInfoModel model){
        tvName.setText(model.getName());
        tvCountry.setText(model.getSys().getCountry());

        //Glide 라이브러리를 이용하여 ImageView에 url로 이미지 지정
        Glide.with(context).load(getString(R.string.weather_url) + "img/w/" + model.getWeather().get(0).getIcon() + ".png")
                .placeholder(R.drawable.icon_image)
                .error(R.drawable.icon_image)
                .into(ivWeather);

        //소수점 2번째 자리까지 반올림
        tvTemp.setText(doubleToStrFormat(2, model.getMain().getTemp() - 273.15) + " 'C");
        tvMain.setText(model.getWeather().get(0).getMain());
        tvDescription.setText(model.getWeather().get(0).getDescription());
        tvWind.setText(doubleToStrFormat(2, model.getWind().getSpeed()) + " m/s");
        tvCloud.setText(doubleToStrFormat(2, model.getClouds().getAll()) + " %");
        tvHumidity.setText(doubleToStrFormat(2, model.getMain().getHumidity()) + " %");
    }

    //통신 실패시 AlertDialog 표시하는 메소드
    private void showFailPop(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setTitle("통신 실패");
        builder.setMessage("API를 불러 오는 것을 실패하였습니다.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // 소수점 n번째 자리까지 반올림
    private String doubleToStrFormat(int n, double value) {
        return String.format("%." + n + "f", value);
    }
}