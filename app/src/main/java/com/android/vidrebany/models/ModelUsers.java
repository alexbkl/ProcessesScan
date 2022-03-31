package com.android.vidrebany.models;

public class ModelUsers {
    String name, code, process, number;


    public ModelUsers() {
    }

    public ModelUsers(String name, String code, String process, String number) {
        this.name = name;
        this.code = code;
        this.process = process;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
