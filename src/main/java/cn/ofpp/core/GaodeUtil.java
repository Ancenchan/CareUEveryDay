import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.core.util.StrUtil;
import static cn.hutool.json.JSONUtil.parseObj;

public class GaodeUtil {
    // 优先从环境变量读取，读取不到再用你申请的新 Key
    private static final String key = StrUtil.blankToDefault(System.getenv("AMAP_KEY"), "7b914ed7b3c410872c9a689f51e50b13");

    private static final String GEO_API = "https://restapi.amap.com/v3/geocode/geo?key=%s&address=%s&city=%s";
    // 关键：为了拿预报，extensions 必须为 all
    private static final String WEATHER_API = "https://restapi.amap.com/v3/weather/weather_info?key=%s&city=%d&extensions=all";

    public static Integer getAdcCode(String province, String city) {
        HttpResponse response = HttpUtil.createGet(String.format(GEO_API, key, province, city)).execute();
        return parseObj(parseObj(response.body()).getJSONArray("geocodes").get(0)).getInt("adcode");
    }

    /**
     * 获取预报天气（含最高、最低温）
     */
    public static WeatherInfo getForecastWeatherInfo(Integer adcCode) {
        HttpResponse response = HttpUtil.createGet(String.format(WEATHER_API, key, adcCode)).execute();
        JSONObject body = parseObj(response.body());
        
        // 高德 all 模式返回的是 forecasts 数组
        JSONObject forecast = (JSONObject) body.getJSONArray("forecasts").get(0);
        JSONArray casts = forecast.getJSONArray("casts");
        JSONObject today = (JSONObject) casts.get(0); // 今天的预报

        WeatherInfo info = new WeatherInfo();
        info.setProvince(forecast.getStr("province"));
        info.setCity(forecast.getStr("city"));
        info.setWeather(today.getStr("dayweather"));
        // 关键数据：最高温和最低温
        info.setDaytemp(today.getStr("daytemp"));
        info.setNighttemp(today.getStr("nighttemp"));
        
        // 计算穿衣建议
        info.setTips(generateTips(today.getInt("daytemp")));
        
        return info;
    }

    private static String generateTips(int highTemp) {
        if (highTemp < 10) return "天气很冷，老妈记得穿羽绒服呀！❄️";
        if (highTemp < 18) return "有点凉，穿件厚外套或者呢大衣吧。🧥";
        if (highTemp < 25) return "气温很舒服，穿件长袖或薄外套就行。🌸";
        return "太阳挺大，记得防晒，穿轻薄的衣服哦。☀️";
    }
}
