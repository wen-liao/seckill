package com.ds.seckill.model;

public class Order {

    private int id;
    private int productId;
    private int consumerId;
    private long timestamp;
    private int paid;

    public Order(int id, int productId, int consumerId, long timestamp, int paid){
        this.id = id; this.consumerId = consumerId; this.timestamp = timestamp; this.paid = paid;
    }

    public Order(int productId, int consumerId, long timestamp, int paid){
        this.consumerId = consumerId; this.timestamp = timestamp; this.paid = paid;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }
}
