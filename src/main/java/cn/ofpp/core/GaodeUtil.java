package cn.ofpp.core;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class GaodeUtil {
    // 优先读取 Github Secret 中的 AMAP_KEY
    private static final String key = cn.hutool.core.util.StrUtil.blankToDefault(System.getenv("AMAP_KEY"), "7b914ed7b3c410872c9a689f51e50b13");

    public static String getAdcCode(String province, String city) {
        String url = "https://restapi.amap.com/v3/geocode/geo?key=" + key + "&address=" + province + city;
        String result = HttpUtil.get(url);
        JSONObject obj = JSONUtil.parseObj(result);
        return obj.getJSONArray("geocodes").getJSONObject(0).getStr("adcode");
    }

    public static WeatherInfo getForecastWeatherInfo(String adcCode) {
        String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=" + key + "&city=" + adcCode + "&extensions=all";
        String result = HttpUtil.get(url);
        JSONObject obj = JSONUtil.parseObj(result);
        
        // 获取预报数组中的当天数据
        JSONObject forecast = obj.getJSONArray("forecasts").getJSONObject(0);
        JSONObject today = forecast.getJSONArray("casts").getJSONObject(0);

        WeatherInfo info = new WeatherInfo();
        info.setWeather(today.getStr("dayweather"));
        info.setDaytemp(today.getStr("daytemp"));
        info.setNighttemp(today.getStr("nighttemp"));
        
        // 穿衣建议：根据最高温判断
        int high = Integer.parseInt(today.getStr("daytemp"));
        if (high < 12) info.setTips("天冷啦，老妈多穿点厚羽绒服呀❄️");
        else if (high < 22) info.setTips("凉飕飕的，穿件挡风的外套吧🧥");
        else info.setTips("阳光不错，穿件薄长袖就行，注意防晒🌸");
        
        return info;
    }
}
