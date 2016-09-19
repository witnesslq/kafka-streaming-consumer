package com.fitttime.kafka.model;

import com.alibaba.fastjson.JSON;
import com.caishi.kafka.model.*;

/**
 * Created by root on 15-10-27.
 */
public class Util {

    public static String convertToJson(String data,int flag){
        String formatLog = "";
        Object o=null;
        if(flag == 1){
            o = JSON.parseObject(data, MallOrder.class);
        }else if (flag == 2){
            o = JSON.parseObject(data, MallPay.class);
        }else if (flag == 3){
            o = JSON.parseObject(data, MallBrowse.class);
        }
        else if (flag == 4){
            o = JSON.parseObject(data, UserBrowse.class);
        }
        if(o != null)
            formatLog = JSON.toJSONString(o);
        return formatLog;
    }

    public static void main(String[] args){
        String data = Util.convertToJson("{\"topic\":\"topic-mall-browse\",\"userId\":\"\",\"uuid\":\"rjft_uuid14691595233119716632\",\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/601.2.7 (KHTML, like Gecko) Version/9.0.1 Safari/601.2.7\",\"channel2\":\"\",\"url\":\"http://malltest.rjfittime.com/\",\"referer\":\"\",\"pageType\":\"index\",\"timestamp\":1469179254754}",3);
        System.out.println(data);
        String data2 = Util.convertToJson("{\"outerOrigin\":null,\"couponName\":null,\"orderId\":1007373,\"city\":\"嘉兴市\",\"couponTemplateId\":null,\"userId\":\"10567693\",\"orderItems\":[{\"unitPrice\":79.00,\"itemOrderAmount\":79.00,\"buyCount\":1,\"supplierId\":7,\"orderItemId\":11356,\"goodsId\":410,\"skuDesc\":\"口味:草莓口味\",\"title\":\"腹愁者 蛋白棒 健身代餐能量棒减脂低卡零食 6种口味可选\",\"skuId\":1211,\"itemPayAmount\":79.00}],\"orderTime\":\"2016-07-24 18:42:54\",\"orderAmount\":79.00,\"payAmount\":79.00,\"province\":\"浙江省\",\"district\":\"桐乡市\",\"userIp\":\"220.185.71.89\",\"topic\":\"topic-mall-order\",\"innerOrigin\":\"APP_DETAIL\"}",2);
        System.out.println(data2);
        String data3 = Util.convertToJson("{\"topic\":\"topic-mall-browse\",\"userId\":\"\",\"uuid\":\"rjft_uuid14691595233119716632\",\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/601.2.7 (KHTML, like Gecko) Version/9.0.1 Safari/601.2.7\",\"channel2\":\"\",\"url\":\"http://malltest.rjfittime.com/\",\"referer\":\"\",\"pageType\":\"index\",\"timestamp\":1469179254754}",3);
        System.out.println(data);


    }
}
