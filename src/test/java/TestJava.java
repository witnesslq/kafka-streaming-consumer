import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caishi.kafka.model.CommentEvent;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by root on 15-10-29.
 */
public class TestJava {
    public static void main(String[] args) throws Exception{
        String data ="{\"auditLevel\":\"MONITOR\",\"auditStatus\":\"UNDONE\",\"commentLevel\":\"GENERAL\",\"commentStatus\":\"ONLINE\",\"content\":\"图兔兔图兔兔\",\"createTime\":1445858401970,\"deviceId\":\"357784059026714\",\"deviceType\":\"01\",\"forbiddenWordList\":[1,2,3],\"id\":\"0ba08b4bc91d4f1bbc3ed7f19bbfef0f\",\"messageId\":\"28209bf84c32b241\",\"messageType\":\"NEWS\",\"nickName\":\"溜达用户7403\",\"osType\":\"01\",\"portrait\":\"http://meta.9liuda.com/image/comment/user/icon/center_default_icon04.png\",\"sensitiveWordList\":[],\"userId\":\"0f8dbb917347550b0451e844b9e866a0\"}";

//        CommentEvent ce = com.alibaba.fastjson.JSON.parseObject(data,CommentEvent.class);
//
//        System.out.println(ce.toString());


        String d = "{%20%20%20%22userId%22:%20%22userid%22,%20%20%20%22appVersion%22:%20%221.0.2%22,%20%20%20%22appType%22:%20%2201:ios\\/02:android%22,%20%20%20%22dataId%22:%20%22dataId%22,%20%20%20%22dataType%22:%20%2201:%E6%96%87%E7%AB%A0\\/02:%E8%AF%BE%E7%A8%8B\\/03:%E7%A4%BE%E5%8C%BA%22,%20%20%20%22duration%22:%20%22%E5%8D%95%E4%BD%8D%E6%AF%AB%E7%A7%92%22,%20%20%20%22geo%22:%20{%20%20%20%20%20%22lat%22:%20%22N%E7%BA%AC%E5%BA%A6%22,%20%20%20%20%20%22lon%22:%20%22E%E7%BB%8F%E5%BA%A6%22%20%20%20},%20%20%20%22tags%22:%20[%20%20%20%20%20{%20%20%20%20%20%20%20%22name%22:%20%22tag1%22,%20%20%20%20%20%20%20%22weight%22:%200.2%20%20%20%20%20},%20%20%20%20%20{%20%20%20%20%20%20%20%22name%22:%20%22tag2%22,%20%20%20%20%20%20%20%22weight%22:%200.4%20%20%20%20%20}%20%20%20],%20%20%20%22categoryIds%22:%20[%20%20%20%20%20{%20%20%20%20%20%20%20%22id%22:%201,%20%20%20%20%20%20%20%22weight%22:%200.4%20%20%20%20%20},%20%20%20%20%20{%20%20%20%20%20%20%20%22id%22:%202,%20%20%20%20%20%20%20%22weight%22:%200.4%20%20%20%20%20}%20%20%20],%20%20%20%22data%22:%20{},%20%20%20%22createTime%22:%201462508234000%20}";
        String x = URLDecoder.decode(d,"utf-8");
        System.out.println(x);
        JSONObject object = JSON.parseObject(x);
        System.out.println(object.get("createTime")+URLEncoder.encode(" 1111 ","utf-8"));

    }
}
