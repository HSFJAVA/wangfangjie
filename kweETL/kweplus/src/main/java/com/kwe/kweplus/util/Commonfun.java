package com.kwe.kweplus.util;


import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.apache.tools.tar.TarOutputStream;*/

public class Commonfun {
    public static final int error = -1;
    public static final int Workday = 0;
    public static final int Weekend = 1;
    public static final int Holiday = 2;

    public boolean IsFileLocked(File file) throws IOException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception ex) {
            return true;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return false;
    }


    /**
     * 解压缩zipFile
     * @param FileToUpZip 要解压的zip文件对象
     * @param ZipedFolder 要解压到某个指定的目录下
     * @throws IOException
     */
    public static String UnZip(String FileToUpZip,String ZipedFolder) throws IOException {
        ZipFile zipFile = null;
        String result = "success";
        try {
            File file1 = new File(FileToUpZip);
            if(!file1.exists()){
                result = "fail";
            }
            File file = new File(ZipedFolder);
            if(!file.exists()){
                file.mkdir();
            }
            Charset CP866 = Charset.forName("CP866");
            zipFile =  new ZipFile(file1, CP866);
            createDirectory(ZipedFolder,null);

            Enumeration<?> enums = zipFile.entries();
            while(enums.hasMoreElements()){

                ZipEntry entry = (ZipEntry) enums.nextElement();
                System.out.println("解压." +  entry.getName());

                if(entry.isDirectory()){
                    createDirectory(ZipedFolder,entry.getName());
                }else{
                    File tmpFile = new File(ZipedFolder + "/" + entry.getName());
                    createDirectory(tmpFile.getParent() + "/",null);

                    InputStream in = null;
                    OutputStream out = null;
                    try{
                        in = zipFile.getInputStream(entry);
                        out = new FileOutputStream(tmpFile);
                        int length = 0;

                        byte[] b = new byte[2048];
                        while((length = in.read(b)) != -1){
                            out.write(b, 0, length);
                        }

                    }catch(IOException ex){
                        throw ex;
                    }finally{
                        if(in!=null)
                            in.close();
                        if(out!=null)
                            out.close();
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally{
            try{
                if(zipFile != null){
                    zipFile.close();
                }
            }catch(IOException ex){
                throw new IOException("关闭zipFile出现异常",ex);
            }
        }
        return result;
    }

    /**
     * 构建目录
     * @param outputDir
     * @param subDir
     */
    public static void createDirectory(String outputDir,String subDir){
        File file = new File(outputDir);
        if(!(subDir == null || subDir.trim().equals(""))){//子目录不为空
            file = new File(outputDir + "/" + subDir);
        }
        if(!file.exists()){
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            file.mkdirs();
        }
    }
    public int toint(String strData)
    {
        if (strData != null)
        {
            strData = strData.replace(",", "").trim();
            int dData = 0;
            boolean re = strData.matches("\\d+");
            if(re){
                dData = Integer.parseInt(strData);
            }
            return dData;
        }
        else
        {
            return 0;
        }
    }
    public double todecimal(String strData)
    {
        if (strData != null && strData.matches("\\d+"))
        {
            strData = strData.replace(",", "").trim();
            double dData = 0.0;
            if (strData.toUpperCase().contains("E"))
            {
                dData =  Long.parseLong(strData.toString(), 16);
            }
            else
            {
                dData = Double.parseDouble(strData.toString());
            }
            return dData;
        }
        else
        {
            return 0;
        }
    }
    //递归获取文件列表
    public  List<String> func(File file){
        File[] fs = file.listFiles();
        List<String> fl = new ArrayList<>();
        for(File f:fs){
            if(f.isDirectory()){
              return  func(f);
            }else
            if(f.isFile())
                fl.add(f.getPath());
        }
        return fl;
    }
    public String[] rmnull(String[] re) {
        List<String> ok = new ArrayList<>();
        for (int ko = 0; ko < re.length; ko++) {
            if (!re[ko].trim().isEmpty()) {
                ok.add(re[ko]);
            }
        }
        String [] aa = new String[ok.size()];
        return ok.toArray(aa);
    }
}
