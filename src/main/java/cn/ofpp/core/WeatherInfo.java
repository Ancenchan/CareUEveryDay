package cn.ofpp.core;

public class WeatherInfo {
    private String province;
    private String city;
    private String weather;
    private String daytemp;      
    private String nighttemp;    
    private String tips;         

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
