package cn.ofpp.core;

import cn.hutool.core.util.StrUtil;
import cn.ofpp.Bootstrap;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import java.util.List;

import static cn.ofpp.core.GaodeUtil.getAdcCode;

/**
 * 消息工厂：负责组装发送给妈妈的消息内容
 */
public class MessageFactory {

    public static WxMpTemplateMessage resolveMessage(Friend friend) {
        return WxMpTemplateMessage.builder()
                .url("https://mp.weixin.qq.com") // 点击详情跳转，可以不填
                .toUser(friend.getUserId())
                .templateId(StrUtil.emptyToDefault(friend.getTemplateId(), Bootstrap.TEMPLATE_ID))
                .data(buildData(friend))
                .build();
    }

    private static List<WxMpTemplateData> buildData(Friend friend) {
        // 1. 调用预报接口获取最高/最低温和建议
        WeatherInfo weather = GaodeUtil.getForecastWeatherInfo(getAdcCode(friend.getProvince(), friend.getCity()));
        
        // 获取一句温馨的古诗词或情话（保留原项目功能）
        RandomAncientPoetry.AncientPoetry ancientPoetry = RandomAncientPoetry.getNext();

        return List.of(
                // 基础信息
                TemplateDataBuilder.builder().name("friendName").value("亲爱的老妈").color("#FF80AB").build(),
                TemplateDataBuilder.builder().name("city").value(friend.getCity()).color("#4FC3F7").build(),
                TemplateDataBuilder.builder().name("weather").value(weather.getWeather()).color("#66BB6A").build(),
                
                // --- 重点修改：气温区间和穿衣建议 ---
                // 模板变量名记得在微信后台对应改好：{{tempRange.DATA}} 和 {{tips.DATA}}
                TemplateDataBuilder.builder().name("tempRange").value(weather.getNighttemp() + "℃ ~ " + weather.getDaytemp() + "℃").color("#FFA726").build(),
                TemplateDataBuilder.builder().name("tips").value(weather.getTips()).color("#9575CD").build(),
                
                // 生日倒计时
                TemplateDataBuilder.builder().name("nextBirthday").value(friend.getNextBirthdayDays()).color("#EF5350").build(),
                
                // 诗词寄语
                TemplateDataBuilder.builder().name("content").value(ancientPoetry.getContent()).color("#7E57C2").build()
        );
    }

    static class TemplateDataBuilder {
        private String name;
        private String value;
        private String color;

        public static TemplateDataBuilder builder() {
            return new TemplateDataBuilder();
        }
        public TemplateDataBuilder name(String name) {
            this.name = name;
            return this;
        }
        public TemplateDataBuilder value(String value) {
            this.value = value;
            return this;
        }
        public TemplateDataBuilder color(String color) {
            this.color = color;
            return this;
        }
        public WxMpTemplateData build() {
            if (StrUtil.hasEmpty(name, value)) {
                throw new IllegalArgumentException("参数不正确");
            }
            WxMpTemplateData data = new WxMpTemplateData();
            data.setName(name);
            data.setValue(value);
            data.setColor(color);
            return data;
        }
    }
}
