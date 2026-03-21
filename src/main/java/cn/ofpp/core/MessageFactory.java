package cn.ofpp.core;

import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import java.util.ArrayList;
import java.util.List;

public class MessageFactory {

    public static WxMpTemplateMessage resolveMessage(Friend friend) {
        // 调用 GaodeUtil 获取数据
        String adcCode = GaodeUtil.getAdcCode(friend.getProvince(), friend.getCity());
        WeatherInfo weather = GaodeUtil.getForecastWeatherInfo(adcCode);
        
        List<WxMpTemplateData> data = new ArrayList<>();
        data.add(new WxMpTemplateData("friendName", "亲爱的老妈", "#FF80AB"));
        data.add(new WxMpTemplateData("city", friend.getCity(), "#4FC3F7"));
        data.add(new WxMpTemplateData("weather", weather.getWeather(), "#66BB6A"));
        
        // 气温区间
        data.add(new WxMpTemplateData("tempRange", weather.getNighttemp() + "℃ ~ " + weather.getDaytemp() + "℃", "#FFA726"));
        // 穿衣建议
        data.add(new WxMpTemplateData("tips", weather.getTips(), "#9575CD"));
        
        // 生日倒计时（假设你的 Friend 类有这个方法）
        data.add(new WxMpTemplateData("nextBirthday", friend.getNextBirthdayDays(), "#EF5350"));
        
        return WxMpTemplateMessage.builder()
                .toUser(friend.getUserId())
                .templateId(cn.ofpp.Bootstrap.TEMPLATE_ID)
                .data(data)
                .build();
    }
}
