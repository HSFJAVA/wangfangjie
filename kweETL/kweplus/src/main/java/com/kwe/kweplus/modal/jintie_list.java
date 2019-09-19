package com.kwe.kweplus.modal;

import java.util.List;

public class jintie_list {
    public List<JintieInputHead> getInputHeadList() {
        return inputHeadList;
    }

    public void setInputHeadList(List<JintieInputHead> inputHeadList) {
        this.inputHeadList = inputHeadList;
    }

    public List<JintieInputDetail> getInputDetailList() {
        return inputDetailList;
    }

    public void setInputDetailList(List<JintieInputDetail> inputDetailList) {
        this.inputDetailList = inputDetailList;
    }

    public List<JintieOutputHead> getOutputHeadList() {
        return outputHeadList;
    }

    public void setOutputHeadList(List<JintieOutputHead> outputHeadList) {
        this.outputHeadList = outputHeadList;
    }

    public List<JintieOutputDetail> getOutputDetailList() {
        return outputDetailList;
    }

    public void setOutputDetailList(List<JintieOutputDetail> outputDetailList) {
        this.outputDetailList = outputDetailList;
    }

    public List<JintieOutputSerial> getOutputSerialList() {
        return outputSerialList;
    }

    public void setOutputSerialList(List<JintieOutputSerial> outputSerialList) {
        this.outputSerialList = outputSerialList;
    }

    public String getOp_user() {
        return op_user;
    }

    public void setOp_user(String op_user) {
        this.op_user = op_user;
    }

    private String op_user;
    private List<JintieInputHead> inputHeadList;
    private List<JintieInputDetail> inputDetailList;
    private List<JintieOutputHead> outputHeadList;
    private List<JintieOutputDetail> outputDetailList;
    private List<JintieOutputSerial> outputSerialList;

    @Override
    public String toString() {
        return "jintie_list{" +
                "op_user='" + op_user + '\'' +
                ", inputHeadList=" + inputHeadList +
                ", inputDetailList=" + inputDetailList +
                ", outputHeadList=" + outputHeadList +
                ", outputDetailList=" + outputDetailList +
                ", outputSerialList=" + outputSerialList +
                '}';
    }
}
