package com.kwe.kweplus.util;

/**
 * Created by wanli on 2019/8/19 15:22
 */
public enum DIMFlagObj {
    未标记(0),
    已标记(1);

    private final Integer value;

    DIMFlagObj(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
