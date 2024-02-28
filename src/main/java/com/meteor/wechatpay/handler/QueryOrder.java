package com.meteor.wechatpay.handler;

import com.alibaba.fastjson2.JSON;
import com.meteor.wechatpay.PluginMain;
import com.meteor.wechatpay.model.Order;
import com.meteor.wechatpay.model.Response;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询订单
 */
public class QueryOrder implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        Map<String, String> map = parseQuery(query);
        Response response = new Response();
        if(!map.containsKey("orderID")){
            response.setCode(-200);
            response.setMessage("wrong request params");
        }else {
            String orderID = map.get("orderID");
            Order order = PluginMain.orderStorage.queryOrder(orderID);
            if(order == null){
                response.setCode(-1200);
                response.setMessage("order does not exist");
            }else {
                response.setCode(200);
                response.setMessage("success");
                response.setData(order);
            }
        }

        httpExchange.getResponseHeaders().set("Content-Type", "application/json");

        String body = JSON.toJSONString(response);

        httpExchange.sendResponseHeaders(200, body.getBytes(StandardCharsets.UTF_8).length);

        // 发送响应体
        OutputStream os = httpExchange.getResponseBody();
        os.write(body.getBytes(StandardCharsets.UTF_8));
        os.close();
    }


    private static Map<String, String> parseQuery(String query) {
        Map<String, String> queryPairs = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                try {
                    queryPairs.put(
                            URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                            URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
                    );
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return queryPairs;
    }


}
