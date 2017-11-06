package baidumapsdk.demo.asmewill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import baidumapsdk.demo.BMapApiDemoMain;
import baidumapsdk.demo.DemoApplication;
import baidumapsdk.demo.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/10/26 0026.
 */

public class BaiduDemoMain extends Activity implements View.OnClickListener{
    @BindView(R.id.btn_my_demo)
    Button btn_my_demo;
    @BindView(R.id.btn_api_demo)
    Button btn_api_demo;
    @BindView(R.id.btn_route_plan)
    Button btn_route_plan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_main);
        ButterKnife.bind(this);
        btn_my_demo.setOnClickListener(this);
        btn_api_demo.setOnClickListener(this);
        btn_route_plan.setOnClickListener(this);
        initCityList();
    }

    /***
     * 初始化城市列表
     */
    private void initCityList() {
        Utils.getCity(getApplicationContext()).subscribe(new Action1<List<CityChildBean>>() {
            @Override
            public void call(List<CityChildBean> cityChildBeen) {
                if(cityChildBeen!=null){
                    DemoApplication.mCity=cityChildBeen;
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_my_demo:
                startActivity(new Intent(this, MySelectAddress.class));
                break;
            case R.id.btn_api_demo:
                startActivity(new Intent(this, BMapApiDemoMain.class));
                break;
            case R.id.btn_route_plan:
                startActivity(new Intent(this, MyRoutePlanActivity.class));
                break;
        }

    }
}
