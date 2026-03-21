package cn.ofpp.core;

import cn.hutool.core.util.StrUtil;
import cn.ofpp.Bootstrap;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import java.util.List;

public class MessageFactory {

    public static WxMpTemplateMessage resolveMessage(Friend friend) {
        return WxMpTemplateMessage.builder()
                .toUser(friend.getUserId())
                .templateId(StrUtil.emptyToDefault(friend.getTemplateId(), Bootstrap.TEMPLATE_ID))
                .data(buildData(friend))
                .build();
    }

    private static List<WxMpTemplateData> buildData(Friend friend) {
        // 使用类名.方法名调用，防止导入失败
        String adcCode = GaodeUtil.getAdcCode(friend.getProvince(), friend.getCity());
        WeatherInfo weather = GaodeUtil.getForecastWeatherInfo(adcCode);
        RandomAncientPoetry.AncientPoetry ancientPoetry = RandomAncientPoetry.getNext();

        return List.of(
                TemplateDataBuilder.builder().name("friendName").value("亲爱的老妈").color("#FF80AB").build(),
                TemplateDataBuilder.builder().name("city").value(friend.getCity()).color("#4FC3F7").build(),
                TemplateDataBuilder.builder().name("weather").value(weather.getWeather()).color("#66BB6A").build(),
                TemplateDataBuilder.builder().name("tempRange").value(weather.getNighttemp() + "℃ ~ " + weather.getDaytemp() + "℃").color("#FFA726").build(),
                TemplateDataBuilder.builder().name("tips").value(weather.getTips()).color("#9575CD").build(),
                TemplateDataBuilder.builder().name("nextBirthday").value(friend.getNextBirthdayDays()).color("#EF5350").build(),
                TemplateDataBuilder.builder().name("author").value(ancientPoetry.getAuthor()).color("#F53F3F").build(),
                TemplateDataBuilder.builder().name("origin").value(ancientPoetry.getOrigin()).color("#F53F3F").build(),
                TemplateDataBuilder.builder().name("content").value(ancientPoetry.getContent()).color("#F53F3F").build()
        );
    }

    // TemplateDataBuilder 保持之前给你的代码不变...
}
