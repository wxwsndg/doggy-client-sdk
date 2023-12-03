package com.doggy.doggyclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.doggy.doggyclientsdk.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.doggy.doggyclientsdk.utils.SignUtils.getSign;


/**
 * 调用第三方接口的客户端
 */

public class DoggyApiclient {

    private String accessKey;
    private String secretKey;

    public DoggyApiclient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    //使用GET方法从服务器获取名称信息
    public String getNameByGet(String name){
         //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
         HashMap<String, Object> paramMap = new HashMap<>();
         //将‘name’参数添加到映射中
         paramMap.put("name", name);
         //使用Httputil工具发起GET请求，并获取服务器返回的结果
        String result= HttpUtil.get("http://localhost:8123/api/name/", paramMap);
        // 打印服务器返回的结果
        System.out.println(result);
        //返回服务器返回的结果
        return result;
    }

    //使用POST方法从服务器获取名称信息
    public String getNameByPost(String name){
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        //使用Httputil工具发起POST请求，并获取服务器返回的结果
        String result= HttpUtil.get("http://localhost:8123/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    /**
     * 获取请求头的哈希映射
     * @param body 请求头内容
     * @return 包含请求头参数的哈希映射
     */
    private Map<String,String> getHeadMap(String body){
        //创建一个新的 HashMap 对象
        Map<String,String> hashMap = new HashMap<>();
        //将“accessKey"和其对应的值放入 map 中
        hashMap.put("accessKey",accessKey);
        //注意，不能直接发送密钥
        //hashMap.put("secretKey",secretKey);
        //生成随机数（生成一个包含4个随机数字的字符串）
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        //请求体内容
        hashMap.put("body",body);
        //返回构造的请求头 map
        //当前时间戳
        //System.currentTimeMillis()返回当前时间的的毫秒数。通过除以1000，可以将毫秒数转换为秒数，以得到当前时间戳的秒级表示
        //String.valueOf()方法用于将数值转换为字符串。在这里，将计算得到的时间戳(以秒为单位) 转换为字符串
        hashMap.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        //生成签名
        hashMap.put("sign",getSign(body,secretKey));
        return hashMap;
    }

    //使用POST方法向服务器发送User对象，并获取服务器返回的结果
    public String getUserNameByPost( User user){
        //将User对象转换为JSON字符串
         String json = JSONUtil.toJsonStr(user);
        //使用HttpRequest工具发起POST请求，并获取服务器的响应
         HttpResponse httpResponse = HttpRequest.post("http://localhost:8123/api/name/user")
                         .addHeaders(getHeadMap(json)) //添加前面构造的请求头
                         .body(json)  //将JSON字符串设置为请求体
                         .execute();//执行请求
        //打印服务器返回的状态码
         System.out.println(httpResponse.getStatus());
         //获取服务器返回的结果
         String result = httpResponse.body();
         //打印服务器返回的结果
         System.out.println(result);
         //返回服务器返回的结果
         return result;
        }
}
