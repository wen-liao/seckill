package com.ds.seckill.model;

public class Consumer {

    private Integer id;
    private String name;
    private String passwordDigest;
    private Integer account;

    public Consumer(Integer id, String name, String passwordDigest, Integer account){
        this.id = id; this.name = name; this.passwordDigest = passwordDigest; this.account = account;
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

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public String getPasswordDigest() {
        return passwordDigest;
    }

    public void setPasswordDigest(String passwordDigest) {
        this.passwordDigest = passwordDigest;
    }

}
