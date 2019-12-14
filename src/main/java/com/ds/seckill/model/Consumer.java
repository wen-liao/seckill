package com.ds.seckill.model;

import com.ds.seckill.util.DigestUtil;

import java.io.Serializable;

public class Consumer implements Serializable {

    private Integer id;
    private String name;
    private String passwordDigest;
    private Integer bankAccount;

    public Consumer(Integer id, String name, String passwordDigest, Integer bankAccount){
        this.id = id; this.name = name; this.passwordDigest = passwordDigest; this.bankAccount = bankAccount;
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

    public Integer getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(Integer bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getPasswordDigest() {
        return passwordDigest;
    }

    public void setPasswordDigest(String passwordDigest) {
        this.passwordDigest = passwordDigest;
    }

    @Override
    public String toString(){
        return new StringBuilder("{id:").append(id)
                .append(", name:").append(name)
                .append(", passwordDigest:").append(passwordDigest)
                .append(", bankAccount:").append(bankAccount)
                .append("}").toString();
    }
}
