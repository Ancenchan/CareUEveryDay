package cn.ofpp.core;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class RandomAncientPoetry {

    // 当所有接口都挂掉时的保底数据
    private static final AncientPoetry[] DEFAULT = new AncientPoetry[] {
            new AncientPoetry("苏轼", "江城子", "十年生死两茫茫，不思量，自难忘。"),
            new AncientPoetry("李商隐", "夜雨寄北", "君问归期未有期，巴山夜雨涨秋池。"),
            new AncientPoetry("晏几道", "生查子", "天与短因缘，聚散常容易。")
    };

    public static AncientPoetry getNext() {
        // 尝试第一个最稳的接口：Hitokoto (全球 CDN 加速)
        try {
            // c=i 表示只获取“诗词”分类
            String res = HttpUtil.get("https://v1.hitokoto.cn/?c=i", 5000); 
            JSONObject json = JSONUtil.parseObj(res);
            
            String content = json.getStr("hitokoto");
            String origin = json.getStr("from");
            String author = json.getStr("from_who");
            
            // 简单清洗：如果作者为空，填个佚名
            author = (author == null || author.isEmpty()) ? "佚名" : author;

            System.out.println("成功通过 Hitokoto 获取诗词: " + content);
            return new AncientPoetry(author, origin, content);
            
        } catch (Exception e1) {
            System.err.println("Hitokoto 接口解析失败，尝试备用接口...");
            
            // 尝试第二个接口：如果你之前那个接口偶尔能通，可以在这儿加个重试逻辑
            try {
                String res = HttpUtil.get("https://v1.jinrishici.com/all.json", 5000);
                JSONObject json = JSONUtil.parseObj(res);
                return new AncientPoetry(
                    json.getStr("author"), 
                    json.getStr("origin"), 
                    json.getStr("content")
                );
            } catch (Exception e2) {
                System.err.println("备用接口也挂了，启用本地保底数据。");
                return DEFAULT[RandomUtil.randomInt(0, DEFAULT.length)];
            }
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

        public String getAuthor() { return author; }
        public String getOrigin() { return origin; }
        public String getContent() { return content; }
    }
}
