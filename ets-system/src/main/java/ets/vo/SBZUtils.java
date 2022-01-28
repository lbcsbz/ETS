package ets.vo;

import ets.vo.PageResult;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SBZUtils {
    //解析pageResult并添加分页条件
    public static Map<String,Object> dealWithQuery(PageResult pageResult){
        Object queryIn = pageResult.getQueryIn();
        String[] split = queryIn.toString()
                .substring(1,queryIn.toString().length()-1)
                .split(",");
        Map<String,Object> map=new HashMap<>();
        for (String s : split) {
            String key = s.split(":")[0].replace("\"", "");
            String value = s.split(":")[1].replace("\"", "");
            if (value.length() != 0) {
                map.put(key,value);
                continue;
            }
        }
        Integer pageIndex = pageResult.getPageIndex();
        Integer pageSize = pageResult.getPageSize();
        Integer startIndex = (pageIndex-1)*pageSize;
        map.put("pageIndex",pageIndex);
        map.put("startIndex",startIndex);
        map.put("pageSize",pageSize);
        return map;
    }
    //解析json串
    public static Map<String,Object> dealWithJson(String string){
        String[] split = string.toString()
                .substring(1,string.toString().length()-1)
                .split(",");
        Map<String,Object> map=new HashMap<>();
        for (String s : split) {
            String key = s.split(":")[0].replace("\"", "");
            String value = s.split(":")[1].replace("\"", "");
            if (value.length() != 0) {
                map.put(key,value);
                continue;
            }
        }
        return map;
    }
    //根据身份证号获取年龄
    public static int getAgeByResident(String pensonnelIdCard){
        //截取身份证中出行人出生日期中的年、月、日
        Integer personYear = Integer.parseInt(pensonnelIdCard.substring(6, 10));
        Integer personMonth = Integer.parseInt(pensonnelIdCard.substring(10, 12));
        Integer personDay = Integer.parseInt(pensonnelIdCard.substring(12, 14));

        Calendar cal = Calendar.getInstance();
        // 得到当前时间的年、月、日
        Integer yearNow = cal.get(Calendar.YEAR);
        Integer monthNow = cal.get(Calendar.MONTH) + 1;
        Integer dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        Integer yearMinus = yearNow - personYear;
        Integer monthMinus = monthNow - personMonth;
        Integer dayMinus = dayNow - personDay;

        Integer age = yearMinus; //先大致赋值

        if (yearMinus == 0) { //出生年份为当前年份
            age = 0;
        }else{ //出生年份大于当前年份
            if (monthMinus < 0) {//出生月份小于当前月份时，还没满周岁
                age = age - 1;
            }
            if (monthMinus == 0) {//当前月份为出生月份时，判断日期
                if (dayMinus < 0) {//出生日期小于当前月份时，没满周岁
                    age = age - 1;
                }
            }
        }
        return age;
    }
    static Integer num = 100000;
    public static String getUserCode() {
        num++;
        String code = "EM"+num;
        return code;
    }
}
