package com.kwe.kweplus.bo;

import java.time.LocalDateTime;

public interface ITaskBo {
    public void refreshtatus(String wlYwno,Integer status, LocalDateTime takeTime) throws IllegalAccessException;
}
