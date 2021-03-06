package com.kwe.kweplus.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.interfaces.PBEKey;
import javax.imageio.ImageIO;

/***
 * 全局工具类
 * 
 * @author 康豪杰
 * 
 */
public final class MyUtils {

	/**
	 * List<Map>根据KEY转List<String>
	 * @param maps
	 * @param key
	 * @return
	 */
	public static List<String> listMapToListString(List<Map> maps,String key){
		List<String> list = new ArrayList<>();
		maps.forEach(e->{
			list.add(e.get(key).toString());
		});
		return list;
	}


	public static List removeDuplicate(List list) {
		if(isEmpty(list))
			return list;
		HashSet h = new HashSet(list);
		list.clear();
		list.addAll(h);
		return list;
	}

	/**
	 * 得到文件的后缀名<br/>
	 * System.out.println(getFileSubString("fudong.jpg"));>>>.jpg
	 * 
	 * @param fileName
	 * @return
	 */
	public final static String getFileSubString(String fileName) {
		if (fileName != null && fileName.indexOf(".") != -1) {
			return fileName.substring(fileName.indexOf("."));
		}
		return "";
	}

	/**
	 * 通过不定参数返回参数类型数组
	 * 
	 * @param objects
	 * @return
	 */
	public final static <T extends Object> T[] getObjs(T... ts) {
		return ts;
	}

	/**
	 * 去掉任何空白字符
	 * 
	 * @param content
	 * @return
	 */
	public static String getTrimStr(String content) {
		if (content == null) {
			return "";
		}
		return content.replaceAll("[\\s\\p{Zs}]+", "");
	}

	/**
	 * 删除集合当中的null值
	 * 
	 * @param list
	 */
	public static void removeNull(Collection<?> list) {
		if (isNotEmpty(list)) {
			Iterator<?> iter = list.iterator();
			while (iter.hasNext()) {
				if (iter.next() == null) {
					iter.remove();
				}
			}
		}
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		if ("null".equals(obj)) {
			return true;
		}

		if (obj == null) {
			return true;
		}

		if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length() == 0;
		}
		if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		}
		if (obj instanceof Map) {
			return ((Map) obj).isEmpty();
		}

		return false;
	}

	/**
	 * 判断对象是否不为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * 判断对象是否为数组
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isArray(Object obj) {
		return (obj != null && obj.getClass().isArray());
	}

	/**
	 * 判断数组是否为空
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	/**
	 * 判断数字是否大于零
	 * 
	 */
	public static boolean isGtZero(Number ls) {
		return ls != null && ls.doubleValue() > 0;
	}

	/**
	 * 随机count位数字字符串<br/>
	 * 可以用来生成代金券号码和密码
	 * 
	 * @param count
	 *            随机多少位
	 */
	public static String getRandomString(int count) {
		if (count > 0) {
			StringBuilder buf = new StringBuilder();
			SecureRandom rd = new SecureRandom();
			for (int i = 0; i < count; i++) {
				buf.append(rd.nextInt(10));
			}
			return buf.toString();
		} else {
			return "";
		}

	}

	/**
	 * 隐藏字符串中部分敏感信息
	 * 
	 * @param tg
	 *            目标字符串
	 * @param start
	 *            开始索引
	 * @param end
	 *            结束索引
	 * @return
	 */
	public static String hidepartChar(String tg, int start, int end) {
		return new StringBuilder(tg).replace(start, end, "**").toString();
	}

	/**
	 * 判断字符在字符串中出现的次数
	 * 
	 * @param tg
	 * @param fg
	 * @return
	 */
	public static Integer getCountInStr(String tg, char fg) {
		if (isNotEmpty(tg)) {
			int i = 0;
			if (isNotEmpty(tg)) {
				char ch[] = tg.toCharArray();
				for (char c : ch) {
					if (c == fg) {
						i++;
					}
				}
			}
			return i;
		}
		return 0;
	}

	/**
	 * 得到long类型集合 如果遇到不能转换为long类型的字符串跳过 返回能转换为long类型的long类型集合
	 * 
	 * @param strings
	 * @return
	 */
	public static List<Long> getListByStrs(String... strings) {
		List<Long> list = new ArrayList<Long>();
		if (strings != null) {
			for (String s : strings) {
				try {
					if (isNotEmpty(s)) {
						list.add(Long.valueOf(s));
					}
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}
		}
		return list;
	}

	/**
	 * 把首字母变成大写
	 * 
	 * @param str要转换的字符串
	 * @return
	 */
	public static String toUpcaseFist(String str) {
		if (str != null && !str.trim().equals("")) {
			if (str.length() == 1) {
				return str.toUpperCase();
			} else {
				return str.substring(0, 1).toUpperCase() + str.substring(1);
			}
		} else {
			return "";
		}

	}

	/**
	 * 根据键值对得到map<String,Object>对象
	 * 
	 * @param ag
	 * @return
	 */
	public static LinkedHashMap<String, Object> getMap(Object... ag) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<String, Object>();
		if (ag != null && ag.length > 0 && ag.length % 2 == 0) {
			int i = 0;
			for (@SuppressWarnings("unused")
			Object o : ag) {
				mp.put(String.valueOf(ag[i]), ag[++i]);
				i++;
				if (i == ag.length) {
					break;
				}

			}
		}
		return mp;
	}

	/**
	 * 根据键值对得到map<String,String>对象
	 * 
	 * @param ag
	 * @return
	 */
	public static LinkedHashMap<String, String> getStrValueMap(String... ag) {
		LinkedHashMap<String, String> mp = new LinkedHashMap<String, String>();
		if (ag != null && ag.length > 0 && ag.length % 2 == 0) {
			int i = 0;
			for (@SuppressWarnings("unused")
			String o : ag) {
				mp.put(ag[i], ag[++i]);
				i++;
				if (i == ag.length) {
					break;
				}

			}
		}
		return mp;
	}

	/**
	 * 动态图像转换成静态图片
	 * 
	 * @param file图片文件
	 */
	public static void convertImageToStatic(File file) {
		try {
			BufferedImage bufferedimage = ImageIO.read(file);
			if (bufferedimage != null) {
				ImageIO.write(bufferedimage, "gif", file);// 1131.gif是静态的
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private final static Pattern pt = Pattern.compile("^[1-9]+[0-9]*[.]?\\d*$");

	/**
	 * 判断是否为数字
	 * 
	 * @param tg
	 * @return
	 */
	public static Boolean isNumber(String tg) {
		return tg == null ? false : pt.matcher(tg.trim()).matches();
	}

	/**
	 * 处理浮点数相加运算
	 * 
	 * @param v
	 * @param v2
	 * @return
	 */
	public static Double floatAdd(Double v, Double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 处理浮点数相减运算
	 * 
	 * @param v
	 *            被减数
	 * @param v2
	 *            减数
	 * @return
	 */
	public static Double floatSubtract(Double v, Double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 处理浮点数相除
	 * 
	 * @param v
	 * @param v2
	 * @return
	 */
	public static Double floatDiv(Double v, Double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2).doubleValue();
	}

	/**
	 * 处理浮点数相乘
	 * 
	 * @param v
	 * @param v2
	 * @return
	 */
	public static Double floatMulply(Double v, Double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 对页面显示内容进行编码
	 * 
	 * @param str
	 * @return
	 */
	public static String htmlEncoding(String str) {
		StringBuffer bfu = new StringBuffer();
		if (str != null) {
			String s = "&#";
			char[] cs = str.toCharArray();
			for (char c : cs) {
				int it = c;
				bfu.append(s).append(it).append(";");
			}
		}
		return bfu.toString();

	}

	/***
	 * 自动属性赋值
	 * 
	 * @param clazz
	 * @param objs
	 * @param propertys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> packageObject(Class<T> clazz, List<Object> vlist, String... propertys) {
		try {
			List<T> list = new ArrayList<T>();
			if (propertys.length == 1) {
				for (Object ov : vlist) {
					Object obj = clazz.newInstance();

					Field fd = getfd(clazz, propertys[0]);

					fd.setAccessible(true);
					if (ov != null) {

						if (ov instanceof BigDecimal) {
							ov = ((BigDecimal) ov).doubleValue();
						} else if (ov instanceof BigInteger) {
							ov = ((BigInteger) ov).longValue();
						}

						if (fd.getType().isEnum()) {
							Class<Enum> cls = (Class<Enum>) fd.getType();
							if (ov instanceof Number) {
								Enum[] ccs = (Enum[]) fd.getType().getEnumConstants();
								fd.set(obj, Enum.valueOf(cls, ccs[Number.class.cast(ov).intValue()].name()));
							} else {
								fd.set(obj, Enum.valueOf(cls, ov.toString()));
							}

						} else {
							if(fd.getType().getName().equals("java.lang.Long")){
								fd.set(obj,((Double) ov).longValue());
							}else if(fd.getType().getName().equals("java.lang.Integer"))
								fd.set(obj,((Double) ov).intValue());
							else
								fd.set(obj, ov);
						}
					}
					list.add((T) obj);

				}

			} else {
				for (Object o : vlist) {
					Object[] ov = (Object[]) o;
					Object obj = clazz.newInstance();
					for (int i = 0; i < propertys.length; i++) {
						Field fd = getfd(clazz, propertys[i]);
						fd.setAccessible(true);
						if (ov[i] != null) {
							if (ov[i] instanceof BigDecimal) {
								ov[i] = ((BigDecimal) ov[i]).doubleValue();
							} else if (ov[i] instanceof BigInteger) {
								ov[i] = ((BigInteger) ov[i]).longValue();
							}

							if (fd.getType().isEnum()) {
								Class<Enum> cls = (Class<Enum>) fd.getType();

								if (ov[i] instanceof Number) {

									Enum[] ccs = (Enum[]) fd.getType().getEnumConstants();
									fd.set(obj, Enum.valueOf(cls, ccs[Number.class.cast(ov[i]).intValue()].name()));

								} else {
									fd.set(obj, Enum.valueOf(cls, ov[i].toString()));
								}

							} else {
								if(fd.getType().getName().equals("java.lang.Long")){
									fd.set(obj,((Double) ov[i]).longValue());
								}else if(fd.getType().getName().equals("java.lang.Integer"))
									fd.set(obj,((Double) ov[i]).intValue());
								else
									fd.set(obj, ov[i]);
							}
						}

					}

					list.add((T) obj);
				}
			}
			return list;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 得到类当中的属性
	 * 
	 * @param clazz
	 *            类型
	 * @param propertys
	 *            属性名称
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static <T> Field getfd(Class<T> clazz, String propertys) throws NoSuchFieldException {
		Field fd = null;
		try {
			fd = clazz.getDeclaredField(propertys);
		} catch (NoSuchFieldException sfe) {
			Class<?> superclass = clazz.getSuperclass();
			if (superclass != null && Object.class != superclass) {
				fd = superclass.getDeclaredField(propertys);
			} else {
				throw new IllegalArgumentException(sfe);
			}

		}
		return fd;
	}

	/**
	 * 得到json数据格式
	 * 
	 * @param flag
	 *            obj[key] key数组
	 * @param property
	 *            name,age..对象属性数组
	 * @param values
	 *            fudong,22.对象属性对应的值
	 * @return
	 */
	public static StringBuffer getJson(String[] flag, String[] property, List<String[]> values) {
		StringBuffer buf = new StringBuffer();
		if (flag != null && property != null && property.length > 0) {
			if (values != null && values.size() > 0 && property.length == values.get(0).length
					&& values.size() == flag.length) {
				Iterator<String[]> ite = values.iterator();
				buf.append("({");
				for (int j = 0; j < flag.length; j++) {
					buf.append("\"").append(flag[j]).append("\"").append(":");
					String[] ss = ite.next();
					buf.append("{");
					for (int i = 0; i < property.length; i++) {
						buf.append(property[i]).append(":").append("\"").append(ss[i]).append("\"");
						if (property.length - 1 > i) {
							buf.append(",");
						}
					}
					buf.append("}");
					if (ite.hasNext()) {
						buf.append(",");
					}
				}
				buf.append("})");
			}

		}
		return buf;
	}

	/**
	 * 生成唯一订单号
	 * 
	 * <p>
	 * MyUtils.genOrderId(10000000)>>>>> 20130806010000000
	 * </p>
	 * 
	 * @param srcId加入生成的数字
	 * @return
	 */
	public final static String genOrderId(long srcId) {
		DateFormat YMDALL = new SimpleDateFormat("yyyyMMdd");
		StringBuilder bd = new StringBuilder(YMDALL.format(new Date()));
		for (int i = 0; i < 6 - String.valueOf(srcId).length(); i++) {
			bd.append("0");
		}
		bd.append(srcId);
		return bd.toString();
	}

	/**
	 * 四舍五入
	 * 
	 * @param v
	 *            待四舍五入的值
	 * @param setPrecision
	 *            保留多少位小数
	 * @return
	 */
	public static double getRoundValue(double v, int setPrecision) {
		return new BigDecimal(Double.toString(v)).setScale(setPrecision, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 切分集合
	 * 
	 * @param list
	 *            要切分的集合
	 * @param size
	 *            最大切分大小
	 */
	public final static <T> List<List<T>> dividedList(List<T> list, int maxsize) {
		List<List<T>> all = new ArrayList<List<T>>();
		if (isNotEmpty(list) && maxsize > 0) {
			List<T> sublist = new ArrayList<T>();
			all.add(sublist);
			if (list.size() > maxsize) {
				for (T t : list) {
					if (sublist.size() >= maxsize) {
						sublist = new ArrayList<T>();
						all.add(sublist);
					}
					sublist.add(t);

				}
			} else {
				for (T t : list) {
					sublist.add(t);
				}
			}
		}
		return all;
	}

	/**
	 * 得到对象属性值列表
	 * 
	 * @param prop
	 *            属性名称
	 * @param ts
	 *            对象集合
	 * @return 返回 集合列表类型和属性类型一致
	 */
	public static <T, V> List<V> getListOVs(String prop, List<T> ts) {
		List<V> list = new ArrayList<V>();
		if (isNotEmpty(ts) && isNotEmpty(prop)) {
			try {
				Method m = ts.get(0).getClass()
						.getDeclaredMethod("get" + prop.toUpperCase().charAt(0) + prop.substring(1), new  Class[]{});
				for (T t : ts) {
					list.add((V) m.invoke(t, new  Object[]{}));
				}
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
		return list;
	}

	public static String generateUUID()
	{
		return UUID.randomUUID().toString();
	}

	/**
	 * 得到list中指定字段值的long 类型集合
	 * 
	 * @param prop
	 *            属性名称
	 * @param ts
	 *            对象集合
	 * @return
	 */
	public static <T> List<Long> getIds(String prop, List<T> ts) {
		List<Long> list = new ArrayList<Long>();
		if (isNotEmpty(ts) && isNotEmpty(prop)) {
			try {
				Field fd = ts.get(0).getClass().getDeclaredField(prop);
				Method m = ts.get(0).getClass()
						.getDeclaredMethod("get" + prop.toUpperCase().charAt(0) + prop.substring(1), new  Class[]{});
				if (fd.getType() == Long.class) {
					for (T t : ts) {
						list.add((Long) m.invoke(t, new  Object[]{}));
					}
				}
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
		return list;
	}

	/***
	 * 得到对象属性字符串的集合
	 * 
	 * @param prop
	 *            对象属性名称
	 * @param ts
	 *            对象集合
	 * @return
	 */
	public static <T> List<String> getStrVs(String prop, List<T> ts) {
		List<String> tts = new ArrayList<String>();
		if (isNotEmpty(ts) && isNotEmpty(prop)) {
			try {
				Field fd = ts.get(0).getClass().getDeclaredField(prop);
				Method m = ts.get(0).getClass()
						.getDeclaredMethod("get" + prop.toUpperCase().charAt(0) + prop.substring(1),  new  Class[]{});
				if (fd.getType() == String.class) {
					for (T t : ts) {
						tts.add((String) m.invoke(t, new  Object[]{}));
					}
				}
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
		return tts;
	}

	/**
	 * 数字前面补零
	 * 
	 * @param iv
	 *            初始值 1
	 * @param len
	 *            总长度 3
	 * @return 001
	 */
	public static String zeroFill(int iv, int len) {
		String v = String.valueOf(iv);
		if (v.length() < len) {
			StringBuilder bd = new StringBuilder(v);
			for (int i = 0; i < len - v.length(); i++) {
				bd.insert(0, '0');
			}
			return bd.toString();
		} else {
			return v;
		}
	}

	/**
	 * 去掉数字前面所有的0
	 * 
	 * @param 目标
	 * @return
	 */
	public static String spzr(String ss) {
		StringBuilder sb = new StringBuilder();
		if (ss != null) {
			char[] chs = ss.toCharArray();
			boolean f = false;
			for (char c : chs) {
				if (c != '0' || f) {
					if (!f) {
						f = true;
					}
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 得到字节数组
	 * 
	 * @param in
	 * @return
	 */
	public static byte[] getBytes(InputStream in) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		if (in != null) {
			try {
				byte[] bts = new byte[202400];
				int i = in.read(bts);
				while (i != -1) {
					bo.write(bts, 0, i);
					i = in.read(bts);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return bo.toByteArray();
	}

	/**
	 * 获取Integer类型的Map参数
	 *
	 * @param map
	 * @param param
	 * @param defaultValue
	 *            缺省值
	 * @return
	 */
	public static Integer getIntegerValue(Map map, String param, Integer defaultValue) {
		if (null == map) {
			return defaultValue;
		}
		String v = String.valueOf(map.get(param));
		if (MyUtils.isNotEmpty(v) && !"null".equals(v)) {
			try {
				return Integer.valueOf(v);
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 得到double类型Map参数
	 *
	 * @param map
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public static Double getDoubleValue(Map map, String param, Double defaultValue) {
		if (null == map) {
			return defaultValue;
		}
		String v = String.valueOf(map.get(param));
		if (MyUtils.isNotEmpty(v) && !"null".equals(v)) {
			try {
				return Double.valueOf(v);
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 得到Long类型Map参数
	 *
	 * @param map
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public static Long getLongValue(Map map, String param, Long defaultValue) {
		if (null == map) {
			return defaultValue;
		}
		String v = String.valueOf(map.get(param));
		if (MyUtils.isNotEmpty(v) && !"null".equals(v)) {
			try {
				return Long.valueOf(v);
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 集装箱号校验
	 * @param containerNumber
	 * @return
	 * @throws Exception
	 */
	public static boolean checkContainerNo(String containerNumber) throws Exception {
		if (containerNumber == null || containerNumber.trim().length() != 11) {
			throw new Exception("Not a container number");
		}
		Map<String, Integer> mapofCode = new HashMap<String, Integer>();

		mapofCode.put("A", 10);
		mapofCode.put("B", 12);
		mapofCode.put("C", 13);
		mapofCode.put("D", 14);
		mapofCode.put("E", 15);
		mapofCode.put("F", 16);
		mapofCode.put("G", 17);
		mapofCode.put("H", 18);
		mapofCode.put("I", 19);
		mapofCode.put("J", 20);
		mapofCode.put("K", 21);
		mapofCode.put("L", 23);
		mapofCode.put("M", 24);
		mapofCode.put("N", 25);
		mapofCode.put("O", 26);
		mapofCode.put("P", 27);
		mapofCode.put("Q", 28);
		mapofCode.put("R", 29);
		mapofCode.put("S", 30);
		mapofCode.put("T", 31);

		mapofCode.put("U", 32);
		mapofCode.put("V", 34);
		mapofCode.put("W", 35);
		mapofCode.put("X", 36);
		mapofCode.put("Y", 37);
		mapofCode.put("Z", 38);
		String constainerCode = containerNumber;
		int positon = 1;
		int sum = 0;
		for (int i = 0; i < constainerCode.length() - 1; i++) {
			if (mapofCode.containsKey(constainerCode.substring(i, i + 1))) {
				sum += Double.valueOf(mapofCode.get(constainerCode.substring(i,
						i + 1))) * Math.pow(2, positon - 1);
			} else {
				sum += Double.valueOf(constainerCode.substring(i, i + 1))
						* Math.pow(2, positon - 1);
			}
			positon++;
		}
		int checkdigit = sum % 11 % 10;
//		System.out.println("check container number:"
//				+ constainerCode
//				+ ";get check digit is "
//				+ checkdigit
//				+ ";origin check digit is "
//				+ constainerCode.substring(constainerCode.length() - 1,
//				constainerCode.length()));
		boolean result = checkdigit == Integer
				.valueOf(constainerCode.substring(constainerCode.length() - 1,
						constainerCode.length()));
		return result;
	}

	public static Boolean matchRegx(String regEx,String str){
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static String getZiMu(String str){
		Pattern p = Pattern.compile("[^A-Z]");
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static String getNumber(String str){
		Pattern p = Pattern.compile("[^0-9]");
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

}
