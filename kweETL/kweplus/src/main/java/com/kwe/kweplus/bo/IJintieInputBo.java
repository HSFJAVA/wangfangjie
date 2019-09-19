package com.kwe.kweplus.bo;

import com.kwe.kweplus.modal.JintieYwinfo;
import com.kwe.kweplus.modal.ReturnMessage;
import com.kwe.kweplus.modal.jintie_list;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface IJintieInputBo {
    Map getDetailByNo(String wlywno, String type) throws Exception;

    String insert(@RequestBody jintie_list data) throws IllegalAccessException;

    String update(jintie_list data) throws IllegalAccessException;

    void remove(String ywno, JintieYwinfo ywInfo) throws IllegalAccessException;

    void audit(String wlywno, JintieYwinfo ywinfo) throws IllegalAccessException;

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = IllegalAccessException.class)
    void updateYw(JintieYwinfo ywinfo) throws IllegalAccessException;

    void saveInputUpdate(JintieYwinfo ywinfo);

    ReturnMessage savaAs(JintieYwinfo ywinfo);
}
