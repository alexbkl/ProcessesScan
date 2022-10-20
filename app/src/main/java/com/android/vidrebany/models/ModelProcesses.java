package com.android.vidrebany.models;

public class ModelProcesses {
    String process, started, ended, user, code;

    public ModelProcesses() {
    }

    public ModelProcesses(String process, String started, String ended, String user, String code) {
        this.process = process;
        this.started = started;
        this.ended = ended;
        this.user = user;
        this.code = code;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getEnded() {
        return ended;
    }

    public void setEnded(String ended) {
        this.ended = ended;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}