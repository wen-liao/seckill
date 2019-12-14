package com.ds.seckill.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable {

    private Integer id;
    private Integer sellerId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer count;

    public Product(Integer sellerId, String name, String description, Integer count, BigDecimal price){
        this.sellerId = sellerId; this.name = name; this.description = description; this.count = count; this.price = price;
    }

    public Product(Integer id, Integer sellerId, String name, String description, Integer count, BigDecimal price){
        this.id = id; this.sellerId = sellerId; this.name = name; this.description = description; this.count = count; this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public String toString(){
        return new StringBuilder("{id:").append(id)
                .append(", sellerId:").append(sellerId)
                .append(", name:").append(name)
                .append(", description:").append(description)
                .append(", price:").append(price)
                .append(", count:").append(count)
                .append("}").toString();
    }
}
