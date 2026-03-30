package cn.ofpp.core;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class RandomAncientPoetry {

    /**
     * 备用保底诗词（当接口全都挂掉时使用）
     */
    private static final AncientPoetry[] DEFAULT = new AncientPoetry[] {
            new AncientPoetry("晏几道", "生查子", "天与短因缘，聚散常容易。"),
            new AncientPoetry("李商隐", "夜雨寄北", "君问归期未有期，巴山夜雨涨秋池。"),
            new AncientPoetry("苏轼", "江城子", "十年生死两茫茫，不思量，自难忘。")
    };

    public static AncientPoetry getNext() {
        try {
            // 换成这个 API，响应速度极快且内容完整
            String res = HttpUtil.get("https://api.vvhan.com/api/ian/poetry", 10000);
            System.out.println("API 原始返回数据: " + res); // 在 Actions 日志里能看到这个

            JSONObject json = JSONUtil.parseObj(res);
            if (json.getBool("success", false)) {
                JSONObject data = json.getJSONObject("data");
                return new AncientPoetry(
                    data.getStr("author"),
                    data.getStr("origin"),
                    data.getStr("content")
                );
            }
            throw new RuntimeException("API 返回状态异常");
        } catch (Exception e) {
            System.err.println("诗词接口请求失败，使用保底数据: " + e.getMessage());
            return DEFAULT[RandomUtil.randomInt(0, DEFAULT.length)];
        }
    }

    public static class AncientPoetry {
        private String author;
        private String origin;
        private String content;

        public AncientPoetry() {}

        public AncientPoetry(String author, String origin, String content) {
            this.author = author;
            this.origin = origin;
            this.content = content;
        }

        // Getters and Setters
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getOrigin() { return origin; }
        public void setOrigin(String origin) { this.origin = origin; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
