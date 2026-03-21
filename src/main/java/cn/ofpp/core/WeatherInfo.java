package cn.ofpp.core;

/**
 * 纯净版 WeatherInfo，不依赖 Lombok
 */
public class WeatherInfo {
    private String province;
    private String city;
    private String weather;
    private String daytemp;      // 最高温
    private String nighttemp;    // 最低温
    private String tips;         // 穿衣建议

    // 手动生成 Getter 和 Setter
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }

    public String getDaytemp() { return daytemp; }
    public void setDaytemp(String daytemp) { this.daytemp = daytemp; }

    public String getNighttemp() { return nighttemp; }
    public void setNighttemp(String nighttemp) { this.nighttemp = nighttemp; }

    public String getTips() { return tips; }
    public void setTips(String tips) { this.tips = tips; }
}
