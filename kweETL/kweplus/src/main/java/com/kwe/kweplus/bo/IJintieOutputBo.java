package com.kwe.kweplus.bo;

import com.kwe.kweplus.modal.JintieYwinfo;
import com.kwe.kweplus.modal.ReturnMessage;
import com.kwe.kweplus.modal.jintie_list;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface IJintieOutputBo {
    Map getDetailByNo(String wlywno, String type) throws Exception;

    String insert(jintie_list data) throws IllegalAccessException;

    String update(jintie_list data) throws IllegalAccessException;

    void remove(String ywno, JintieYwinfo ywInfo) throws IllegalAccessException;

    void audit(String wlywno, JintieYwinfo ywinfo) throws IllegalAccessException;

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = IllegalAccessException.class)
    void updateYw(JintieYwinfo ywinfo) throws IllegalAccessException;

    void test();

    void saveOutputUpdate(JintieYwinfo ywinfo);

    ReturnMessage saveAs(JintieYwinfo ywinfo);
}
