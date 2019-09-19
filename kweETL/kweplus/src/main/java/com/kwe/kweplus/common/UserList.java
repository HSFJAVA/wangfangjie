package com.kwe.kweplus.common;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kwe.kweplus.dao.UserMapper;
import com.kwe.kweplus.modal.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class UserList {

    @Autowired
    private UserMapper userMapper;
    //静态对象
    @Autowired
    private static UserList UserList;

    @PostConstruct
    public void init(){
        //将注入的对象交给静态对象管理
        UserList = this;
    }

    public  Map<String,String> loginMap(){
        Map<String,String> map = new HashMap<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status","已停用");
        List<User> users = UserList.userMapper.selectList(queryWrapper);
        for (int i = 0; i < users.size(); i++) {
            map.put(users.get(i).getUname(),users.get(i).getPwd()+","+users.get(i).getChinesename());
        }
        return map;
    }



//    public static  Map<String,String> loginMap = new HashMap(){{
//        put("wuf","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,吴非");
//        put("wanxl","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,万晓磊");
//        put("luhw","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,陆欢旺");
//        put("niwj","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,倪文娟");
//        put("chenglq","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,程丽琴");
//        put("zhengz","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,郑志");
//        put("xush","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,徐松华");
//        put("xinjie","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,新杰");
//        put("yangqq","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,杨倩倩");
//        put("zhangxc","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,张学诚");
//        put("yuww","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,余雯雯");
//        put("mazl","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,马仲良");
//        put("jixt","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,计霄婷");
//        put("lingbl","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,凌蓓丽");
//        put("heyh","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,贺艳华");
//        put("xujg","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,徐剑钢");
//        put("maxl","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,马相丽");
//        put("chenq","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,陈琴");
//        put("yiny","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,殷怡");
//        put("linqq","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,林倩倩");
//        put("xujg1","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,徐剑钢");
//        put("zhangyy","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,张医医");
//        put("fanlb","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,范丽蓓");
//        put("jinxiaomin","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,机场金晓敏");
//        put("guozh","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,郭正华");
//        put("yeh","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,叶红");
//        put("wusn","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,吴胜男");
//        put("wux","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,吴旭");
//        put("yeq","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,叶青");
//        put("guanyj","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,管佑洁");
//        put("zhuf","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,朱峰");
//        put("fanhq","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,樊海清");
//        put("fuyj","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,傅一峻");
//        put("miaoyw","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,缪严伟");
//        put("shens","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,沈桑");
//        put("pengjj","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,彭静静");
//        put("yuanjt","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,袁江滔");
//        put("caojq","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,曹建奇");
//        put("chenyw","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,陈瑜薇");
//        put("sheny","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,沈宇");
//        put("zhouh","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,周昊");
//        put("xul","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,许莉");
//        put("shenxh","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,沈旭辉");
//        put("guanjl","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,管佳龙");
//        put("yangmx","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,杨明秀");
//        put("lujm","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,陆家黎");
//        put("taozj","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,陶智君");
//        put("lixt","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,李学涛");
//        put("dongrm","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,董人鸣");
//        put("wanghk","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,王宏康");
//        put("zhangsy","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,张苏月");
//        put("xinyg","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,辛勇国");
//        put("chenbl","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,陈博凌");
//        put("yanxueting","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,严雪婷");
//        put("zhaobing","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,赵彬");
//        put("huangxj","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,黄绣佳");
//        put("miaokeqin","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,苗克芹");
//        put("yiyl","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,尹彦琳");
//        put("wanxiaolei","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,万晓磊");
//        put("huzh","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,胡振浩");
//        put("jimx","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,吉牧星");
//        put("sunm","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,孙萌");
//        put("gaoj","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,高健");
//        put("yangyf","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,杨怡帆");
//        put("intel","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,英特尔");
//        put("liucz","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,刘琛舟");
//        put("huangm","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,黄明");
//        put("gaosg","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,高盛刚");
//        put("chenxl","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,陈晓璐");
//        put("zhangf","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,张芳");
//        put("wangfj","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,王方杰");
//        put("cheny","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,陈悦");
//        put("abbyy","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,abbyy");
//        put("liw","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,李威");
//        put("dg","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,兑观");
//        put("wanghj","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,王红娟");
//        put("xujg","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,徐建刚");
//        put("huangm","0ee0e5a64cefba5a305b34865702b35903b9fad8e9c573ccae322fef76bd9716,黄明");
//    }};



}
