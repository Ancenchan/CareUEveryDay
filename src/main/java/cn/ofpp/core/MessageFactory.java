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

    // --- 这个内部类一定要保留在文件里 ---
    static class TemplateDataBuilder {
        private String name;
        private String value;
        private String color;

        public static TemplateDataBuilder builder() { return new TemplateDataBuilder(); }
        public TemplateDataBuilder name(String name) { this.name = name; return this; }
        public TemplateDataBuilder value(String value) { this.value = value; return this; }
        public TemplateDataBuilder color(String color) { this.color = color; return this; }
        public WxMpTemplateData build() {
            WxMpTemplateData data = new WxMpTemplateData();
            data.setName(name);
            data.setValue(value);
            data.setColor(color);
            return data;
        }
    }
}
