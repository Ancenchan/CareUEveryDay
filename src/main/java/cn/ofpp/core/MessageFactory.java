package cn.ofpp.core;

import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import java.util.ArrayList;
import java.util.List;

public class MessageFactory {

    public static WxMpTemplateMessage resolveMessage(Friend friend) {
        // 1. 获取天气数据
        String adcCode = GaodeUtil.getAdcCode(friend.getProvince(), friend.getCity());
        WeatherInfo weather = GaodeUtil.getForecastWeatherInfo(adcCode);
        
        // 2. 获取诗词数据
        RandomAncientPoetry.AncientPoetry poetry = RandomAncientPoetry.getNext();
        
        List<WxMpTemplateData> data = new ArrayList<>();
        // 基础信息
        data.add(new WxMpTemplateData("friendName", "亲爱的老妈", "#FF80AB"));
        data.add(new WxMpTemplateData("city", friend.getCity(), "#4FC3F7"));
        data.add(new WxMpTemplateData("weather", weather.getWeather(), "#66BB6A"));
        
        // 气温与建议
        data.add(new WxMpTemplateData("tempRange", weather.getNighttemp() + "℃ ~ " + weather.getDaytemp() + "℃", "#FFA726"));
        data.add(new WxMpTemplateData("tips", weather.getTips(), "#9575CD"));
        
        // 生日倒计时
        data.add(new WxMpTemplateData("nextBirthday", friend.getNextBirthdayDays(), "#EF5350"));

        // --- 重点：诗词字段（必须和后台模板 {{XXX.DATA}} 一一对应） ---
        data.add(new WxMpTemplateData("content", poetry.getContent(), "#7E57C2"));
        data.add(new WxMpTemplateData("author", poetry.getAuthor(), "#9E9E9E"));
        data.add(new WxMpTemplateData("origin", "《" + poetry.getOrigin() + "》", "#9E9E9E"));
        
        return WxMpTemplateMessage.builder()
                .toUser(friend.getUserId())
                .templateId(Bootstrap.TEMPLATE_ID)
                .data(data)
                .build();
    }
}
