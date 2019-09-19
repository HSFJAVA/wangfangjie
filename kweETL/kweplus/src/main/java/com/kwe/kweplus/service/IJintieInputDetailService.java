package com.kwe.kweplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kwe.kweplus.modal.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
public interface IJintieInputDetailService extends IService<JintieInputDetail> {

     boolean saveExcelDate(JintieYwinfo ywinfo, MultipartFile excel) throws IOException;

    List<JintieInputDetail> selectByYwNo(String ywNo);

    List<JintieCustomField> selectByStoreId(String storeid,String type,String modelId);

    ReturnMessage reverseExcel(JintieYwinfo info) throws IOException;
}
