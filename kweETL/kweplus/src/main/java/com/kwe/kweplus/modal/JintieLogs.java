package com.kwe.kweplus.modal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class JintieLogs {

    @TableId(value = "log_id",type = IdType.AUTO)
    private Integer logId;

    private String username;

    private String description;

    private Date startTime;

    private Date spendTime;

    private String url;

    private String method;

    private String parameter;

    private String ip;

    private String result;

    private String remarks1;

    private String remarks2;

    private String remarks3;

    private String remarks4;

    private String remarks5;

    private String remarks6;

    private String remarks7;

    private String remarks8;

    private String remarks9;

    private String remarks10;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(Date spendTime) {
        this.spendTime = spendTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRemarks1() {
        return remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getRemarks3() {
        return remarks3;
    }

    public void setRemarks3(String remarks3) {
        this.remarks3 = remarks3;
    }

    public String getRemarks4() {
        return remarks4;
    }

    public void setRemarks4(String remarks4) {
        this.remarks4 = remarks4;
    }

    public String getRemarks5() {
        return remarks5;
    }

    public void setRemarks5(String remarks5) {
        this.remarks5 = remarks5;
    }

    public String getRemarks6() {
        return remarks6;
    }

    public void setRemarks6(String remarks6) {
        this.remarks6 = remarks6;
    }

    public String getRemarks7() {
        return remarks7;
    }

    public void setRemarks7(String remarks7) {
        this.remarks7 = remarks7;
    }

    public String getRemarks8() {
        return remarks8;
    }

    public void setRemarks8(String remarks8) {
        this.remarks8 = remarks8;
    }

    public String getRemarks9() {
        return remarks9;
    }

    public void setRemarks9(String remarks9) {
        this.remarks9 = remarks9;
    }

    public String getRemarks10() {
        return remarks10;
    }

    public void setRemarks10(String remarks10) {
        this.remarks10 = remarks10;
    }

    public JintieLogs() {
    }

}