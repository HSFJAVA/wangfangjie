package com.kwe.kweplus.modal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.Map;

public class MyPage extends Page {
    private Map<Object,Object> condition = new HashMap<>();

    @Override
    public Map<Object, Object> condition() {
        return condition;
    }

    public void setCondition(Map<Object, Object> condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "MyPage{" +
                "condition=" + condition +
                '}';
    }
}
