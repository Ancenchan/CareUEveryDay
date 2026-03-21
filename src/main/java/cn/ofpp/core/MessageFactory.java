package cn.ofpp.core;

import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import java.util.ArrayList;
import java.util.List;
import cn.ofpp.Bootstrap; // 必须导入这个，否则编译报错

public class MessageFactory {

    public static WxMpTemplateMessage resolveMessage(Friend friend) {
        // 1. 获取天气和诗词
        String adcCode = GaodeUtil.getAdcCode(friend.getProvince(), friend.getCity());
        WeatherInfo weather = GaodeUtil.getForecastWeatherInfo(adcCode);
        RandomAncientPoetry.AncientPoetry poetry = RandomAncientPoetry.getNext();
        
        // 2. 这里的读取方式改回你之前成功的样子 (通过 Bootstrap.TEMPLATE_ID)
        String templateId = cn.hutool.core.util.StrUtil.blankToDefault(friend.getTemplateId(), Bootstrap.TEMPLATE_ID);
        
        List<WxMpTemplateData> data = new ArrayList<>();
        data.add(new WxMpTemplateData("friendName", "亲爱的老妈", "#FF80AB"));
        data.add(new WxMpTemplateData("city", friend.getCity(), "#4FC3F7"));
        data.add(new WxMpTemplateData("weather", weather.getWeather(), "#66BB6A"));
        
        // 新加的字段
        data.add(new WxMpTemplateData("tempRange", weather.getNighttemp() + "℃ ~ " + weather.getDaytemp() + "℃", "#FFA726"));
        data.add(new WxMpTemplateData("tips", weather.getTips(), "#9575CD"));
        data.add(new WxMpTemplateData("nextBirthday", friend.getNextBirthdayDays(), "#EF5350"));

        // 诗词字段
        data.add(new WxMpTemplateData("content", poetry.getContent(), "#7E57C2"));
        data.add(new WxMpTemplateData("author", poetry.getAuthor(), "#9E9E9E"));
        data.add(new WxMpTemplateData("origin", "《" + poetry.getOrigin() + "》", "#9E9E9E"));
        
        return WxMpTemplateMessage.builder()
                .toUser(friend.getUserId())
                .templateId(templateId)
                .data(data)
                .build();
    }
}
