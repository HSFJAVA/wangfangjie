package com.kwe.kweplus.util;

import com.kwe.kweplus.modal.JintieYwinfo;
import com.kwe.kweplus.util.ocrUtil.OcrUtil;
import com.kwe.kweplus.util.text.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;

public class ExcelUtil {

    /*json头模板*/
    private static final int HEADER_VALUE_TYPE_O = 1;

    /*文件过滤,只有表格才可以处理*/
    private boolean fileNameFileter(File file) {
        boolean endsWith = false;
        if (file != null) {
            String fileName = file.getName();
            endsWith = fileName.endsWith(".xls") || fileName.endsWith(".xlsx");
        }
        return endsWith;
    }

    /*获取表的行*/
    private Row getHeaderRow(Sheet sheet, int index) {
        Row headerRow = null;
        if (sheet != null) {
            headerRow = sheet.getRow(index);
        }
        return headerRow;
    }

    /*获取表头的value*/
    private String getHeaderCellValue(Row headerRow, int cellIndex, int type) {
        Cell cell = headerRow.getCell(cellIndex);
        String headerValue = null;
        if (cell != null) {
            if (HEADER_VALUE_TYPE_O == type) {
                headerValue = cell.getRichStringCellValue().getString();
            }
        }
        return headerValue;
    }

    /*获取单元格的值*/
    private Object getCellValue(Row row, int cellIndex, FormulaEvaluator formulaEvaluator) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            switch (cell.getCellType()) {
                //String
                case Cell.CELL_TYPE_STRING:
                    return cell.getRichStringCellValue().getString().trim();

                //Number
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getNumericCellValue();
                    } else {
                            DecimalFormat df = new DecimalFormat("0");
                            return df.format(cell.getNumericCellValue());
                    }

                    //boolean
                case Cell.CELL_TYPE_BOOLEAN:
                    System.out.println("Boolean");
                    return cell.getBooleanCellValue();

                //公式
                case Cell.CELL_TYPE_FORMULA:
                    System.out.println("FORMULA");
                    return formulaEvaluator.evaluate(cell).getNumberValue();
                default:
                    return null;
            }
        }

        return null;
    }

    /*获取单元格的值*/
    private Object getCellValueTwo(Row row, int cellIndex, FormulaEvaluator formulaEvaluator) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            switch (cell.getCellType()) {
                //String
                case Cell.CELL_TYPE_STRING:
                    return cell.getRichStringCellValue().getString().trim();

                //Number
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getNumericCellValue();
                    } else {
                        DecimalFormat df = new DecimalFormat("0.00000");
                        return df.format(cell.getNumericCellValue());
                    }

                    //boolean
                case Cell.CELL_TYPE_BOOLEAN:
                    System.out.println("Boolean");
                    return cell.getBooleanCellValue();

                //公式
                case Cell.CELL_TYPE_FORMULA:
                    System.out.println("FORMULA");
                    return formulaEvaluator.evaluate(cell).getNumberValue();
                default:
                    return null;
            }
        }
        return null;
    }


    /*读取excel*/
    public synchronized List<Map<String, Object>>  readExcel(File file, int headerIndex, int headType) {
        List<Map<String, Object>> lists = new ArrayList();
        OcrUtil util = new OcrUtil();
        if (!fileNameFileter(file)) {
            return null;
        } else {
            try {
                WorkbookFactory factory = new WorkbookFactory();
                Workbook workbook = factory.create(file);
                Sheet sheet = workbook.getSheetAt(0);
                Row headerRow = getHeaderRow(sheet, headerIndex);
                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                for (int r = headerIndex + 1; r < sheet.getLastRowNum() + 1; r++) {
                    Row dataRow = sheet.getRow(r);
                    Map<String, Object> map = new HashMap<String, Object>();
                    for (int h = 0; h < dataRow.getLastCellNum(); h++) {
                        String key = getHeaderCellValue(headerRow, h, headType);
                        Object value ;
                        if( "单价".equals(key) || "总价".equals(key) || "毛重".equals(key) || "净重".equals(key) || "体积".equals(key)
                                               || "分金额".equals(key)  || "分毛重".equals(key) || "分净重".equals(key)
                                ){
                             value = getCellValueTwo(dataRow, h, formulaEvaluator);
                        }else{
                            value = getCellValue(dataRow, h, formulaEvaluator);
                        }
                        if (!"".equals(key) && !"null".equals(key) && key != null) {
                            map.put(key, trim(value+""));
                        }
                    }
                    // 3. 使用Iterator遍历 如果此行都为空，则不进行添加
                    Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
                    boolean isNotNull = false;
                    while (it.hasNext()) {
                        Map.Entry<String, Object> entry = it.next();
                        if( util.checkNull(entry.getValue()) ){
                            isNotNull = true;
                            break;
                        }
                    }
                    if(isNotNull){
                        lists.add(map);
                    }
                }
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lists;
    }



    /*出仓数据转换*/
//    public List dataConversionOfOut(List<Map<String,Object>> list){
//       if(list == null ){
//           return null;
//       }else {
//           List<Map<String,Object>> list1 = new ArrayList<Map<String, Object>>();
//           int a= 0;
//           for (int i = 0; i <list.size() ; i++) {
//               Map<String,Object> map = new HashMap<String, Object>();
//               if(
//                       ("null".equals(list.get(i).get("数量")) || list.get(i).get("数量") == null || "".equals(list.get(i).get("数量"))) &&
//                               ("null".equals(list.get(i).get("总价")) || list.get(i).get("总价") == null || "".equals(list.get(i).get("总价")))
//                       ){
//                   map.put("COPYLINENO",list1.get(i-1).get("COPYLINENO"));
//                   map.put("发票号",list1.get(i-1).get("发票号"));
//                   map.put("料号",list1.get(i-1).get("料号"));
//                   map.put("数量",list1.get(i-1).get("数量"));
//                   map.put("单价",list1.get(i-1).get("单价"));
//                   map.put("总价",list1.get(i-1).get("总价"));
//                   map.put("单位",list1.get(i-1).get("单位"));
//                   map.put("币制",list1.get(i-1).get("币制"));
//                   map.put("原产国",list1.get(i-1).get("原产国"));
//                   map.put("行号",list1.get(i-1).get("行号"));
//                   map.put("进库业务编号",list1.get(i-1).get("进库业务编号"));
//                   map.put("进库运单号",list1.get(i-1).get("进库运单号"));
//                   map.put("入库发票号",list1.get(i-1).get("入库发票号"));
//                   map.put("CPN",list1.get(i-1).get("CPN"));
//                   map.put("SO号",list1.get(i-1).get("SO号"));
//                   map.put("SN号",list1.get(i-1).get("SN号"));
//                   map.put("Delivery No",list1.get(i-1).get("Delivery No"));
//                   map.put("最终用户",list1.get(i-1).get("最终用户"));
//                   map.put("batch号",list.get(i).get("batch号"));
//                   map.put("箱号",list.get(i).get("箱号"));
//                   map.put("发动机号",list.get(i).get("发动机号"));
//                   map.put("batch数量",list.get(i).get("batch数量"));
//                   map.put("Lot No",list.get(i).get("Lot No"));
//                   //6.16 新增
//                   map.put("分金额",list.get(i).get("分金额"));
//               }else{
//                   a=a+1;
//                   map.put("COPYLINENO",a+"");
//                   map.put("发票号",list.get(i).get("发票号"));
//                   map.put("料号",list.get(i).get("料号"));
//                   map.put("数量",list.get(i).get("数量"));
//                   map.put("单价",list.get(i).get("单价"));
//                   map.put("总价",list.get(i).get("总价"));
//                   map.put("单位",list.get(i).get("单位"));
//                   map.put("币制",list.get(i).get("币制"));
//                   map.put("原产国",list.get(i).get("原产国"));
//                   map.put("行号",list.get(i).get("行号"));
//                   map.put("进库业务编号",list.get(i).get("进库业务编号"));
//                   map.put("进库运单号",list.get(i).get("进库运单号"));
//                   map.put("入库发票号",list.get(i).get("入库发票号"));
//                   map.put("CPN",list.get(i).get("CPN"));
//                   map.put("SO号",list.get(i).get("SO号"));
//                   map.put("SN号",list.get(i).get("SN号"));
//                   map.put("Delivery No",list.get(i).get("Delivery No"));
//                   map.put("最终用户",list.get(i).get("最终用户"));
//                   map.put("batch号",list.get(i).get("batch号"));
//                   map.put("箱号",list.get(i).get("箱号"));
//                   map.put("发动机号",list.get(i).get("发动机号"));
//                   map.put("batch数量",list.get(i).get("batch数量"));
//                   map.put("Lot No",list.get(i).get("Lot No"));
//                   //6.16 新增
//                   map.put("分金额",list.get(i).get("分金额"));
//               }
//               list1.add(map);
//           }
//           return list1;
//       }
//    }
//
//    /*进仓数据转换*/
//    public List dataConversionOfIn(List<Map<String,Object>> list){
//       if(list == null){
//           return null;
//       }else {
//           List<Map<String,Object>> list1 = new ArrayList<Map<String, Object>>();
//           int a= 0;
//           for (int i = 0; i <list.size() ; i++) {
//               Map<String,Object> map = new HashMap<String, Object>();
//               if(i == 0){
//                   map.put("COPYLINENO",a+"");
//                   map.put("发票号",list.get(i).get("发票号"));
//                   map.put("料号",list.get(i).get("料号"));
//                   map.put("数量",list.get(i).get("数量"));
//                   map.put("单位",list.get(i).get("单位"));
//                   map.put("单价",list.get(i).get("单价"));
//                   map.put("总价",list.get(i).get("总价"));
//                   map.put("币制",list.get(i).get("币制"));
//                   map.put("原产国",list.get(i).get("原产国"));
//                   map.put("毛重",list.get(i).get("毛重"));
//                   map.put("净重",list.get(i).get("净重"));
//                   map.put("体积",list.get(i).get("体积"));
//                   map.put("行号",list.get(i).get("行号"));
//                   map.put("订单号",list.get(i).get("订单号"));
//                   map.put("PO号",list.get(i).get("PO号"));
//                   map.put("SO号",list.get(i).get("SO号"));
//                   map.put("SN号",list.get(i).get("SN号"));
//                   map.put("Shipment No",list.get(i).get("Shipment No"));
//                   map.put("Lot No",list.get(i).get("Lot No"));
//                   map.put("Delivery No",list.get(i).get("Delivery No"));
//                   map.put("CPN",list.get(i).get("CPN"));
//                   map.put("最终用户料号",list.get(i).get("最终用户料号"));
//                   map.put("最终用户料号2",list.get(i).get("最终用户料号2"));
//                   map.put("最终用户料号3",list.get(i).get("最终用户料号3"));
//                   map.put("最终用户",list.get(i).get("最终用户"));
//                   map.put("最小包装数量",list.get(i).get("最小包装数量"));
//                   map.put("箱数",list.get(i).get("箱数"));
//                   map.put("唛头/件数/米数",list.get(i).get("唛头/件数/米数"));
//                   map.put("部门",list.get(i).get("部门"));
//                   map.put("备注栏",list.get(i).get("备注栏"));
//                   map.put("batch号",list.get(i).get("batch号"));
//                   map.put("箱号",list.get(i).get("箱号"));
//                   map.put("发动机号",list.get(i).get("发动机号"));
//                   map.put("batch数量",list.get(i).get("batch数量"));
//                   map.put("良品数量",list.get(i).get("良品数量"));
//                   map.put("税号",list.get(i).get("税号"));
//                   map.put("品名",list.get(i).get("品名"));
//
//                   //6.16新增
//                   map.put("分毛重",list.get(i).get("分毛重"));
//                   map.put("分净重",list.get(i).get("分净重"));
//                   map.put("分体积",list.get(i).get("分体积"));
//                   map.put("分金额",list.get(i).get("分金额"));
//               }else {
//                   if(
//                           ("null".equals(list.get(i).get("数量")) || list.get(i).get("数量") == null || "".equals(list.get(i).get("数量"))) &&
//                                   ("null".equals(list.get(i).get("总价")) || list.get(i).get("总价") == null || "".equals(list.get(i).get("总价")))
//                           ){
//                       map.put("COPYLINENO",list1.get(i-1).get("COPYLINENO"));
//                       map.put("发票号",list1.get(i-1).get("发票号"));
//                       map.put("料号",list1.get(i-1).get("料号"));
//                       map.put("数量",list1.get(i-1).get("数量"));
//                       map.put("单位",list1.get(i-1).get("单位"));
//                       map.put("单价",list1.get(i-1).get("单价"));
//                       map.put("总价",list1.get(i-1).get("总价"));
//                       map.put("币制",list1.get(i-1).get("币制"));
//                       map.put("原产国",list1.get(i-1).get("原产国"));
//                       map.put("毛重",list1.get(i-1).get("毛重"));
//                       map.put("净重",list1.get(i-1).get("净重"));
//                       map.put("体积",list1.get(i-1).get("体积"));
//                       map.put("行号",list1.get(i-1).get("行号"));
//                       map.put("订单号",list1.get(i-1).get("订单号"));
//                       map.put("PO号",list1.get(i-1).get("PO号"));
//                       map.put("SO号",list1.get(i-1).get("SO号"));
//                       map.put("SN号",list1.get(i-1).get("SN号"));
//                       map.put("Shipment No",list1.get(i-1).get("Shipment No"));
//                       map.put("Lot No",list1.get(i-1).get("Lot No"));
//                       map.put("Delivery No",list1.get(i-1).get("Delivery No"));
//                       map.put("CPN",list1.get(i-1).get("CPN"));
//                       map.put("最终用户料号",list1.get(i-1).get("最终用户料号"));
//                       map.put("最终用户料号2",list1.get(i-1).get("最终用户料号2"));
//                       map.put("最终用户料号3",list1.get(i-1).get("最终用户料号3"));
//                       map.put("最终用户",list1.get(i-1).get("最终用户"));
//                       map.put("最小包装数量",list1.get(i-1).get("最小包装数量"));
//                       map.put("箱数",list1.get(i-1).get("箱数"));
//                       map.put("唛头/件数/米数",list1.get(i-1).get("唛头/件数/米数"));
//                       map.put("部门",list1.get(i-1).get("部门"));
//                       map.put("备注栏",list1.get(i-1).get("备注栏"));
//                       map.put("税号",list1.get(i-1).get("税号"));
//                       map.put("品名",list1.get(i-1).get("品名"));
//                       map.put("batch号",list.get(i).get("batch号"));
//                       map.put("箱号",list.get(i).get("箱号"));
//                       map.put("发动机号",list.get(i).get("发动机号"));
//                       map.put("batch数量",list.get(i).get("batch数量"));
//                       map.put("良品数量",list.get(i).get("良品数量"));
//                       //6.16新增
//                       map.put("分毛重",list.get(i).get("分毛重"));
//                       map.put("分净重",list.get(i).get("分净重"));
//                       map.put("分体积",list.get(i).get("分体积"));
//                       map.put("分金额",list.get(i).get("分金额"));
//                   }else{
//                       a=a+1;
//                       map.put("COPYLINENO",a+"");
//                       map.put("发票号",list.get(i).get("发票号"));
//                       map.put("料号",list.get(i).get("料号"));
//                       map.put("数量",list.get(i).get("数量"));
//                       map.put("单位",list.get(i).get("单位"));
//                       map.put("单价",list.get(i).get("单价"));
//                       map.put("总价",list.get(i).get("总价"));
//                       map.put("币制",list.get(i).get("币制"));
//                       map.put("原产国",list.get(i).get("原产国"));
//                       map.put("毛重",list.get(i).get("毛重"));
//                       map.put("净重",list.get(i).get("净重"));
//                       map.put("体积",list.get(i).get("体积"));
//                       map.put("行号",list.get(i).get("行号"));
//                       map.put("订单号",list.get(i).get("订单号"));
//                       map.put("PO号",list.get(i).get("PO号"));
//                       map.put("SO号",list.get(i).get("SO号"));
//                       map.put("SN号",list.get(i).get("SN号"));
//                       map.put("Shipment No",list.get(i).get("Shipment No"));
//                       map.put("Lot No",list.get(i).get("Lot No"));
//                       map.put("Delivery No",list.get(i).get("Delivery No"));
//                       map.put("CPN",list.get(i).get("CPN"));
//                       map.put("最终用户料号",list.get(i).get("最终用户料号"));
//                       map.put("最终用户料号2",list.get(i).get("最终用户料号2"));
//                       map.put("最终用户料号3",list.get(i).get("最终用户料号3"));
//                       map.put("最终用户",list.get(i).get("最终用户"));
//                       map.put("最小包装数量",list.get(i).get("最小包装数量"));
//                       map.put("箱数",list.get(i).get("箱数"));
//                       map.put("唛头/件数/米数",list.get(i).get("唛头/件数/米数"));
//                       map.put("部门",list.get(i).get("部门"));
//                       map.put("备注栏",list.get(i).get("备注栏"));
//                       map.put("batch号",list.get(i).get("batch号"));
//                       map.put("箱号",list.get(i).get("箱号"));
//                       map.put("发动机号",list.get(i).get("发动机号"));
//                       map.put("batch数量",list.get(i).get("batch数量"));
//                       map.put("良品数量",list.get(i).get("良品数量"));
//                       map.put("税号",list.get(i).get("税号"));
//                       map.put("品名",list.get(i).get("品名"));
//
//                       //6.16新增
//                       map.put("分毛重",list.get(i).get("分毛重"));
//                       map.put("分净重",list.get(i).get("分净重"));
//                       map.put("分体积",list.get(i).get("分体积"));
//                       map.put("分金额",list.get(i).get("分金额"));
//                   }
//               }
//               list1.add(map);
//           }
//           return list1;
//       }
//    }


    public List dataConversion(List<Map<String,Object>> list){
        OcrUtil util = new OcrUtil();
        if(list == null || list.size() == 0 ){
            return null;
        } else {
                int a = 1;
                for (int i = 0; i < list.size(); i++) {
                    Map<String,Object> map = list.get(i);
                    map.put("COPYLINENO","");
                    //通过判断是否有数量总价，如果有就是分行，如果没有就是一行
                    if( util.checkNull(map.get("数量")) || util.checkNull(map.get("总价")) ){
                        map.put("COPYLINENO",a+"");
                        a++;
                    }else {
                        if(i != 0){
                            for (String str:list.get(i-1).keySet()) {
                                if(!util.checkNull(map.get(str))){
                                    map.put(str,list.get(i-1).get(str));
                                }
                            }
                        }

                    }
                }
                return list;
        }
    }


    /*测试入口*/
    public static void main(String[] args) {
//        File file = new File("D:\\AAA.xlsx");
//        ExcelUtil test1 = new ExcelUtil();
//        List<Map<String,Object>> list= test1.readExcel(file, 1, 1);
//        ExcelUtil util = new ExcelUtil();
//        list = util.dataConversionOfOut(list);
//        for (Map<String,Object> map:list) {
//            System.out.println(map);
//        }
//        System.out.println(list.size());
        //System.out.println("  2  2  3  ".trim());
//        System.out.println(OUTPUTJSON.contains("qtyordered"));
        String a = "URU1J221MHD       ";
        System.out.println(a.trim()+"123");
    }



    /**
     * 去除字符串中所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     * @param s
     * @return
     */
    public static String removeAllBlank(String s){
        String result = "";
        if(null!=s && !"".equals(s)){
            result = s.replaceAll("[　*| *| *|//s*]*", "");
        }
        return result;
    }

    /**
     * 去除字符串中头部和尾部所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     * @param s
     * @return
     */
    public static String trim(String s){
        String result = "";
        if(null!=s && !"".equals(s)){
            result = s.replaceAll("^[　*| *| *|//s*]*", "").replaceAll("[　*| *| *|//s*]*$", "");
        }
        return result;
    }

    /**
     *
     * @param list   对象数组
     * @param list1  Excel第一行解释名称
     * @param ywNo
     * @throws IOException
     */
    public static void exportToExcel(List<Map<String,Object>> list,List<String> list1,String ywNo) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("识别数据表");
        //创建行
        HSSFRow row = sheet.createRow(0);
        for (int j = 0; j < list1.size(); j++) {
            //创建单元格
            HSSFCell cell = row.createCell(j);
            //定义单元格为字符串类型，这个字符串类型也可在创建单元格里面设置。
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            //在单元格中输入一些内容　　
            cell.setCellValue(list1.get(j));
        }
        if (MyUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                Map<String,Object> map = list.get(i);
                row = sheet.createRow(i+1);
                for (int j = 0; j < list1.size(); j++) {
                    //创建单元格
                    HSSFCell cell = row.createCell(j);
                    //定义单元格为字符串类型，这个字符串类型也可在创建单元格里面设置。
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    //在单元格中输入一些内容　　
                    cell.setCellValue(map.get(list1.get(j))+"");
                }
            }
        }

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream("E:\\kweServer\\ocrfile\\"+ ywNo + ".xls");
            workbook.write(fOut);
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            fOut.close();
        }


    }

    //业务详情数据导出。
    public static void exportToExcel2(List<Map<String,Object>> list,List<String> list1,List<String> list2,String ywNo,List<String> YY) throws Exception {
        OcrUtil util = new OcrUtil();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("数据列表");
        //创建行
        HSSFRow row = sheet.createRow(1);
        //需要一个新的List<Map<String,Object>>来存储新格式数据。
        List<Map<String,Object>> list3=new ArrayList<>();
        for (int j = 0; j < list1.size(); j++) {
            //创建单元格
            HSSFCell cell = row.createCell(j);
            //定义单元格为字符串类型，这个字符串类型也可在创建单元格里面设置。
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            //在单元格中输入一些内容　　
            cell.setCellValue(list1.get(j));
        }

        if (MyUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                //如果COPYLINENO一样，就把重复字段去重，对多字段留下。
                //需要得参数信息：对多字段。
                //筛选第二条数据。
                Map<String,Object> mapyy=new HashMap<>();
                if(i >0 ){//总条数大于1
                    if(list.get(i).get("copylineno").equals(list.get(i-1).get("copylineno"))){//行号相等
                        //遍历旧格式的表体内容数据。
                        for (String yy:YY) {
                            String key=yy;
                            Object value=list.get(i).get(yy);
                            mapyy.put(key,value);
                        }
                        list3.add(mapyy);
                    }else{//行号不相等
                        list3.add(list.get(i));
                    }
                }else
                {
                    list3.add(list.get(i));
                }
                Map<String,Object> map = list3.get(i);
                row = sheet.createRow(i+2);
                for (int j = 0; j < list1.size(); j++) {
                    //创建单元格
                    HSSFCell cell = row.createCell(j);
                    //定义单元格为字符串类型，这个字符串类型也可在创建单元格里面设置。
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    //在单元格中输入一些内容　.list1是中文得，list2是原来得。　
                    if(util.checkNull(map.get(list2.get(j)))){
                        cell.setCellValue(map.get(list2.get(j))+"");
                    }
                }
            }
        }
        FileOutputStream fOut = null;
        fOut = new FileOutputStream("E:\\kweServer\\reverseExcelFile\\"+ ywNo + ".xls");
        workbook.write(fOut);
        fOut.flush();
        fOut.close();
    }

    /**
     *  业务列表导出。
     * @param list   对象数组
     * @param list1  Excel第一行解释名称
     * @throws IOException
     */
    public static void exportToExcel3(List<Map<String,Object>> list, List<String> list1, List<String> list2) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("识别数据表");
        //创建行
        HSSFRow row = sheet.createRow(0);
        for (int j = 0; j < list1.size(); j++) {
            //创建单元格
            HSSFCell cell = row.createCell(j);
            //定义单元格为字符串类型，这个字符串类型也可在创建单元格里面设置。
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            //在单元格中输入一些内容　　
            cell.setCellValue(list1.get(j));
        }
        if (MyUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                Map<String,Object> map = list.get(i);
                row = sheet.createRow(i+1);
                for (int j = 0; j < list2.size(); j++) {
                    //创建单元格
                    HSSFCell cell = row.createCell(j);
                    //定义单元格为字符串类型，这个字符串类型也可在创建单元格里面设置。
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    //在单元格中输入一些内容　　
                    cell.setCellValue(map.get(list2.get(j))+"");
                }
            }
        }

        FileOutputStream fOut = null;
        try {
            String time=new SimpleDateFormat("yyyyMMdd").format(new Date());
            fOut = new FileOutputStream("E:\\kweServer\\reverseExcelFile\\"+ time + ".xls");
            workbook.write(fOut);
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            fOut.close();
        }


    }

}
