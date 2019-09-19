package com.kwe.kweplus.modal;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public class AddFileYwParameter {

    String customKey;

    String customerNo;

    String type;

    String remark;

    String createUser;

    MultipartFile serviceFile;

    String templateID;

    String ocrTemplateID;

    String ocrUrl;

    HttpServletRequest request;

    public String getCustomKey() {
        return customKey;
    }

    public void setCustomKey(String customKey) {
        this.customKey = customKey;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public MultipartFile getServiceFile() {
        return serviceFile;
    }

    public void setServiceFile(MultipartFile serviceFile) {
        this.serviceFile = serviceFile;
    }

    public String getTemplateID() {
        return templateID;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public String getOcrTemplateID() {
        return ocrTemplateID;
    }

    public void setOcrTemplateID(String ocrTemplateID) {
        this.ocrTemplateID = ocrTemplateID;
    }

    public String getOcrUrl() {
        return ocrUrl;
    }

    public void setOcrUrl(String ocrUrl) {
        this.ocrUrl = ocrUrl;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
