package com.kwe.kweplus.service;

import com.kwe.kweplus.modal.JintieModeParm;
import com.kwe.kweplus.modal.JintieModel;
import com.kwe.kweplus.modal.JintieModelList;
import com.kwe.kweplus.modal.ReturnMessage;

import java.util.List;
import java.util.Map;

public interface JintieModelListService {
    ReturnMessage createModel(JintieModelList model);

    Map getModelList(JintieModeParm parm);

    Map getTemplateByModelId(String modelId);

    ReturnMessage updateTemplate(JintieModel template);

    ReturnMessage deleteModel(List<String> modelId);
}
