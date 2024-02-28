package com.meteor.wechatpay.model;

import lombok.Data;

@Data
public class Response {
    private int code;
    private String message;
    private Order data;
}
