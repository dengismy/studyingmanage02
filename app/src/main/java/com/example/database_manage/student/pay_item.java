package com.example.database_manage.student;

public class pay_item {
    String pay_name;//项目名称
    int pay_number;//金额
    Boolean ischeck;


    public pay_item(String name,int number) {
        this.pay_name=name;
        this.pay_number = number;
        this.ischeck = false;

    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public int getPay_number() {
        return pay_number;
    }

    public void setPay_number(int pay_number) {
        this.pay_number = pay_number;
    }

    public Boolean getIscheck() {
        return ischeck;
    }

    public void setIscheck(Boolean ischeck) {
        this.ischeck = ischeck;
    }
}
