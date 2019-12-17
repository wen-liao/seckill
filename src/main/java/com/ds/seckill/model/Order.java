package com.ds.seckill.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Order implements Serializable {

    private Integer id;
    private Integer productId;
    private Integer consumerId;
    private Timestamp timestamp;
    private Integer paid;

    public Order(Integer id, Integer productId, Integer consumerId, Timestamp timestamp, Integer paid){
        this.id = id; this.productId=productId; this.consumerId = consumerId; this.timestamp = timestamp; this.paid = paid;
    }

    public Order(Integer productId, Integer consumerId, Timestamp timestamp, Integer paid){
        this.productId = productId; this.consumerId = consumerId; this.timestamp = timestamp; this.paid = paid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Integer consumerId) {
        this.consumerId = consumerId;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString(){
        return new StringBuilder("{id:").append(id)
                .append(", productId:").append(productId)
                .append(", consumerId:").append(consumerId)
                .append(", timestamp:").append(timestamp.toString())
                .append(", paid:").append(paid)
                .append("}").toString();
    }
}
