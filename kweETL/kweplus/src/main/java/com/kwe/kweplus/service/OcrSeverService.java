package com.kwe.kweplus.service;

import com.kwe.kweplus.modal.JintieYwinfo;

import java.util.List;
import java.util.Map;

public interface OcrSeverService {
    void formatData(Map<String, List<Map>> dataMap, JintieYwinfo ywinfo);
}
