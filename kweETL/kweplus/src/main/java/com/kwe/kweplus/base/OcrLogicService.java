package com.kwe.kweplus.base;

import com.kwe.kweplus.util.ocrUtil.OcrUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ly on 2019/8/26 16:56
 */
public class OcrLogicService {



    public Map<String,List<Map>> forMatOfController(Map<String,List<Map>> dataMap) {
        OcrUtil util = new OcrUtil();
        Map<String,List<Map>> Map = new HashMap<>();

        List<Map> XDList = dataMap.get("XDList");
        for (int i = 0; i < XDList.size(); i++) {
            String XDShipmentNo = XDList.get(i).get("XDShipmentNo")+"";
            List<Map> XDCommodityList = (List<java.util.Map>) XDList.get(i).get("XDCommodityList");
            if(XDCommodityList != null && XDCommodityList.size() >0 ){
                for (int j = 0; j < XDCommodityList.size(); j++) {

                    if( !util.checkNull(XDCommodityList.get(j).get("XDCommodity_ShipmentNo")) ){
                        XDCommodityList.get(j).put("XDCommodity_ShipmentNo",XDShipmentNo);
                    }

                    String XDCommodity_BatchNo = XDList.get(i).get("XDCommodity_BatchNo")+"";
                    String XDCommodity_BatchQty = XDList.get(i).get("XDCommodity_BatchQty")+"";
                    String XDCommodity_SupplierNumber = XDList.get(i).get("XDCommodity_SupplierNumber")+"";
                    String XDCommodity_SupplierTotalQty = XDList.get(i).get("XDCommodity_SupplierTotalQty")+"";
                    String XDCommodity_SupplierGoodQty = XDList.get(i).get("XDCommodity_SupplierGoodQty")+"";
                    String XDCommodity_SupplierNw = XDList.get(i).get("XDCommodity_SupplierNw")+"";
                    String XDCommodity_SupplierGw = XDList.get(i).get("XDCommodity_SupplierGw")+"";
                    String XDCommodity_SupplierAmount = XDList.get(i).get("XDCommodity_SupplierAmount")+"";
                    String XDCommodity_CtnNo = XDList.get(i).get("XDCommodity_CtnNo")+"";
                    String XDCommodity_CtnDim = XDList.get(i).get("XDCommodity_CtnDim")+"";
                    String XDCommodity_CtnDimUnit = XDList.get(i).get("XDCommodity_CtnDimUnit")+"";
                    String XDCommodity_CtnGdsQty = XDList.get(i).get("XDCommodity_CtnGdsQty")+"";
                    String XDCommodity_EngineNo = XDList.get(i).get("XDCommodity_EngineNo")+"";
                    String XDCommodity_LotNo = XDList.get(i).get("XDCommodity_LotNo")+"";

                    List<Map> XDCommodity2List = (List<java.util.Map>) XDCommodityList.get(j).get("XDCommodity2List");
                    if( XDCommodity2List != null && XDCommodity2List.size()>0 ){
                        for (int k = 0; k < XDCommodity2List.size(); k++) {
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_BatchNo")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_BatchNo",XDCommodity_BatchNo); }
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_BatchQty")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_BatchQty",XDCommodity_BatchQty); }
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_SupplierNumber")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_SupplierNumber",XDCommodity_SupplierNumber); }
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_SupplierTotalQty")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_SupplierTotalQty",XDCommodity_SupplierTotalQty); }
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_SupplierGoodQty")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_SupplierGoodQty",XDCommodity_SupplierGoodQty); }
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_SupplierNw")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_SupplierNw",XDCommodity_SupplierNw); }
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_SupplierGw")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_SupplierGw",XDCommodity_SupplierGw); }
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_SupplierAmount")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_SupplierAmount",XDCommodity_SupplierAmount); }
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_CtnNo")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_CtnNo",XDCommodity_CtnNo); }
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_CtnDim")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_CtnDim",XDCommodity_CtnDim); }
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_CtnDimUnit")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_CtnDimUnit",XDCommodity_CtnDimUnit); }
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_CtnGdsQty")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_CtnGdsQty",XDCommodity_CtnGdsQty); }
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_EngineNo")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_EngineNo",XDCommodity_EngineNo); }
                            if( !util.checkNull(XDCommodity2List.get(k).get("XDCommodity_Attach_LotNo")) ){ XDCommodity2List.get(k).put("XDCommodity_Attach_LotNo",XDCommodity_LotNo); }

                            String XDCommodity_Attach_ItemNo = XDCommodity2List.get(k).get("XDCommodity_Attach_ItemNo")+"";
                            String XDCommodity_Attach_PartNumber = XDCommodity2List.get(k).get("XDCommodity_Attach_PartNumber")+"";
                            String XDCommodity_Attach_GdsQty1 = XDCommodity2List.get(k).get("XDCommodity_Attach_GdsQty1")+"";

                            if( util.checkNull(XDCommodity_Attach_ItemNo) || util.checkNull(XDCommodity_Attach_PartNumber) || util.checkNull(XDCommodity_Attach_GdsQty1) ){
                                Map map = new HashMap();
                                map.putAll(XDCommodityList.get(j));
                                map.put("XDCommodity_ItemNo",XDCommodity_Attach_ItemNo);
                                map.put("XDCommodity_PartNumber",XDCommodity_Attach_PartNumber);
                                map.put("XDCommodity_GdsQty1",XDCommodity_Attach_GdsQty1);
                                XDCommodityList.add(map);
                                XDCommodityList.remove(j);
                                j--;
                            }

                        }
                    }
                }
            }
        }
        return Map;
    }
}
