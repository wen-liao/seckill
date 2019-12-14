package com.ds.seckill.model;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

    private int id;
    private int productId;
    private int consumerId;
    private Date timestamp;
    private int paid;

    public Order(int id, int productId, int consumerId, Date timestamp, int paid){
        this.id = id; this.productId=productId; this.consumerId = consumerId; this.timestamp = timestamp; this.paid = paid;
    }

    public Order(int productId, int consumerId, Date timestamp, int paid){
        this.productId = productId; this.consumerId = consumerId; this.timestamp = timestamp; this.paid = paid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(int consumerId) {
        this.consumerId = consumerId;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString(){
        return new StringBuilder("{id:").append(id)
                .append(", productId:").append(productId)
                .append(", consumerId:").append(consumerId)
                .append(", timestamp:").append(timestamp.toString())
                .append(", paid:").append(paid).toString();
    }
}
