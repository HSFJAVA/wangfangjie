package com.kwe.kweplus.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

public class HttpServiceUtil {

	private static Logger _log = LoggerFactory.getLogger(HttpServiceUtil.class);

	/**
	 * 向指定URL发送GET方法的请求
	 * @param url  发送请求的URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) throws ConnectException, SocketTimeoutException, IOException, Exception{
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + URLEncoder.encode(param, "GBK");

			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置 HttpURLConnection的断开时间
			conn.setConnectTimeout(50000);
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			// 建立实际的连接
			conn.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line;
			StringBuffer buf = new StringBuffer();
			while ((line = in.readLine()) != null) {
				buf.append(line);
			}
			result = buf.toString();
		} catch (ConnectException e) {
			System.out.println("发送GET请求拒绝连接！" + e);
			throw e;
		} catch (SocketTimeoutException e) {
			System.out.println("发送GET请求超时！" + e);
			throw e;
		}  catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			throw e;
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e2) {
				throw e2;

			}
		}
		return result;
	}
	/**
	 * 向指定URL发送GET方法的请求
	 * @param url  发送请求的URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGetUrl(String url, String param) throws ConnectException, SocketTimeoutException, IOException, Exception{
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			System.out.print("urlNameString："+urlNameString);
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置 HttpURLConnection的断开时间
			conn.setConnectTimeout(50000);
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			// 建立实际的连接
			conn.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line;
			StringBuffer buf = new StringBuffer();
			while ((line = in.readLine()) != null) {
				buf.append(line);
			}
			result = buf.toString();
		} catch (ConnectException e) {
			System.out.println("发送GET请求拒绝连接！" + e);
			throw e;
		} catch (SocketTimeoutException e) {
			System.out.println("发送GET请求超时！" + e);
			throw e;
		}  catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			throw e;
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e2) {
				throw e2;

			}
		}
		return result;
	}
	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url  发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @param param 请求参数，jsonChar true  参数为 json
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param,Boolean jsonChar) throws ConnectException, SocketTimeoutException, IOException, Exception{
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置 HttpURLConnection的断开时间
			conn.setConnectTimeout(5000);

			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "UTF-8");

			if(jsonChar){
				conn.setRequestProperty("Content-Type", "application/json");
			}
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line;
			StringBuffer buf = new StringBuffer();
			while ((line = in.readLine()) != null) {
				buf.append(line);
			}
			result = buf.toString();
		} catch (ConnectException e) {
			System.out.println("发送请求拒绝连接！" + e);
			throw e;
		} catch (SocketTimeoutException e) {
			System.out.println("发送请求超时！" + e);
			throw e;
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			throw e;
		} finally {
			// 使用finally块来关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e2) {
				throw e2;
			}
		}
		return result;
	}


	public static void main(String[] args) throws ConnectException, SocketTimeoutException, IOException, Exception {
		String url = "http://export.wanlish.com/external/getArchivedData";
		String param = "clientCode=SHLM&customsNumber=224620170000015508";
		String reault = sendPost(url, param, true);
		_log.info(reault);
	}
}