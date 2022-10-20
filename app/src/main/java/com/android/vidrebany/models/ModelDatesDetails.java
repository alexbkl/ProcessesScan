package com.android.vidrebany.models;

public class ModelDatesDetails {
    String code, process, started, ended, user;

    public ModelDatesDetails() {

    }

    public ModelDatesDetails(String code, String process, String started, String ended, String user) {
        this.code = code;
        this.started = started;
        this.ended = ended;
        this.user = user;
        this.process = process;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStarted() {
        return started;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getEnded() {
        return ended;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

