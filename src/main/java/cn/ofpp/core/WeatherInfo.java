package cn.ofpp.core;

import lombok.Data;

/**
 * 对应高德 API 的天气信息模型
 * 使用 @Data 注解自动生成 Getter/Setter
 */
@Data
public class WeatherInfo {
    private String province;
    private String city;
    private String adcode;
    private String weather;
    private String temperature; // 实时温度（base模式用）
    private String winddirection;
    private String windpower;
    private String humidity;
    private String reporttime;

    // --- 重点新增字段 ---
    private String daytemp;      // 白天最高温
    private String nighttemp;    // 晚上最低温
    private String tips;         // 穿衣建议（我们在 GaodeUtil 里生成的）
}
