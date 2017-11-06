package baidumapsdk.demo.asmewill;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.blankj.utilcode.util.AppUtils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/6/13.
 */

public class Utils {
    /**
     * 去导航
     *
     * @param context
     * @param mLocationLat  开始lat
     * @param mLocationLon  开始 lon
     * @param mLocationAddr 开始 地址
     * @param endLat        结束地址
     * @param endLon        结束lon
     * @param endAddress    结束地址
     */
    public static void mapWalkingDaoHang(Context context, String mLocationLat, String mLocationLon, String mLocationAddr
            , String endLat, String endLon, String endAddress, String city) {
        Intent intent = new Intent();


        if (AppUtils.isInstallApp("com.baidu.BaiduMap")) {
            try {
                intent = Intent.parseUri("intent://map/direction?" +
                        "origin=latlng:" + mLocationLat + "," + mLocationLon +
                        "|name:" + mLocationAddr +
                        "&destination=latlng:" + endLat + "," + endLon +
                        "|name:" + endAddress +
                        "&mode=walking" +
                        "&src=Name|AppName" +
                        "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end", 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            context.startActivity(intent);
        } else if (AppUtils.isInstallApp("com.autonavi.minimap")) {
            double[] start = bdToGaoDe(Double.valueOf(mLocationLat), Double.valueOf(mLocationLon));
            double[] end = bdToGaoDe(Double.valueOf(endLat), Double.valueOf(endLon));
            intent.setData(Uri
                    .parse("androidamap://route?" +
                            "sourceApplication=softname" +
                            "&slat=" + start[1] +
                            "&slon=" + start[0] +
                            "&sname=" + mLocationAddr +
                            "&dlat=" + end[1] +
                            "&dlon=" + end[0] +
                            "&dname=" + endAddress +
                            "&dev=0" +
                            "&m=0" +
                            "&t=4"));
            context.startActivity(intent);
        } else {
            openWebMap(context, Double.valueOf(mLocationLat), Double.valueOf(mLocationLon), mLocationAddr, Double.valueOf(endLat), Double.valueOf(endLon), endAddress, city);
        }
    }


    public static String getWebBaiduMapUri(double originLat, double originLon, String originName, double desLat, double desLon, String destination, String region, String appName) {
        String uri = "http://api.map.baidu.com/direction?origin=latlng:%1$s,%2$s|name:%3$s" +
                "&destination=latlng:%4$s,%5$s|name:%6$s&mode=walking&region=%7$s&output=html" +
                "&src=%8$s";
        return String.format(uri, originLat, originLon, originName, desLat, desLon, destination, region, appName);
    }

    /**
     * 打开浏览器进行百度地图导航
     */
    public static void openWebMap(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname, String city) {
        Uri mapUri = Uri.parse(getWebBaiduMapUri(slat, slon, sname,
                dlat, dlon,
                dname, city, AppUtils.getAppName()));
        Intent loction = new Intent(Intent.ACTION_VIEW, mapUri);
        context.startActivity(loction);
    }


    /**
     * 百度to高德
     *
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }








    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    public static Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", AppUtils.getAppPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", AppUtils.getAppPackageName());
        }
        return localIntent;
    }
    public static Observable<List<CityChildBean>> getCity(final Context context){
        return  Observable.create(new Observable.OnSubscribe<List<CityChildBean>>() {
            @Override
            public void call(Subscriber<? super List<CityChildBean>> subscriber) {
                Gson mGson = new Gson();
                CityBean mCityBean = mGson.fromJson(getFromAssets(context), CityBean.class);
                List<CityChildBean> mList = new ArrayList<>();
                for (int i = 0; i < mCityBean.getCitys().size(); i++) {
                    CityBean.CitysBean mDictBeanX = mCityBean.getCitys().get(i);
                    mList.add(new CityChildBean("", mDictBeanX.getTitle()));
                    for (int j = 0; j < mDictBeanX.getMList().size(); j++) {
                        mList.add(new CityChildBean(mDictBeanX.getTitle(), mDictBeanX.getMList().get(j).getCityname()));
                    }
                }
                subscriber.onNext(mList);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }
    /**
     * 获取城市内容
     */
    public static String getFromAssets(Context context) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open("CityData.txt"));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * 旋转动画
     * 是否选中
     */
    public static void rotation(View v, boolean isChoose) {
        final RotateAnimation animation = new RotateAnimation(isChoose ? 180f : 0f, isChoose ? 360 : 180f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);//设置动画持续时间
        animation.setFillAfter(true);
        v.startAnimation(animation);
    }











}
