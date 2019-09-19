package com.kwe.kweplus.modal;

public class JintieModeParm {

    public String type;
    public String name;
    public String rows;
    public String page;
    public String user;
    public String custom;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    @Override
    public String toString() {
        return "JintieModeParm{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", rows='" + rows + '\'' +
                ", page='" + page + '\'' +
                ", user='" + user + '\'' +
                ", custom='" + custom + '\'' +
                '}';
    }
}
