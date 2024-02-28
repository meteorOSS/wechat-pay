package com.meteor.wechatpay.model;

import lombok.Data;

@Data
public class Order {
    private String notes;
    private Double amount;
    private long time;
}
