package com.meteor.wechatpay;

import com.meteor.wechatbc.entitiy.message.Message;
import com.meteor.wechatbc.event.EventHandler;
import com.meteor.wechatbc.impl.event.Listener;
import com.meteor.wechatbc.impl.event.sub.ReceiveMessageEvent;
import com.meteor.wechatbc.impl.model.message.PayMessage;
import com.meteor.wechatbc.impl.plugin.BasePlugin;
import com.meteor.wechatpay.handler.QueryOrder;
import com.meteor.wechatpay.model.Order;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class PluginMain extends BasePlugin implements Listener {


    public static OrderStorage orderStorage;

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        orderStorage = new OrderStorage(this);

        saveDefaultConfig();

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(getConfig().getInt("port")), 0);
            server.createContext("/query",new QueryOrder());
            server.setExecutor(Executors.newCachedThreadPool()); // 在另一个线程运行http服务器
            server.start();
            getLogger().info("服务已挂载于 127.0.0.1:%s",getConfig().getInt("port"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getWeChatClient().getEventManager().registerPluginListener(this,this);

        getLogger().info("收款码监听插件已载入");
    }

    @EventHandler
    public void receiveMessage(ReceiveMessageEvent receiveMessageEvent){
        Message message = receiveMessageEvent.getMessage();
        if(message instanceof PayMessage){
            PayMessage payMessage = (PayMessage) message;
            Order order = new Order();
            order.setNotes(payMessage.getNotes());
            order.setAmount(payMessage.getAmount());
            orderStorage.insert(order);
            getLogger().info(String.format("insert order,note is %s",order.getNotes()));
        }
    }

}
