package com.pkh.pkec.phone.util;

/**
 * Created by Administrator on 2016/9/7.
 */
public class CnUpperCaser {

    public static String numberToChinese(String input){
        String s1="零壹贰叁肆伍陆柒捌玖";
        String s4="分角整元拾佰仟万拾佰仟亿拾佰仟";
        String temp="";
        String result="";
        if (input==null) return "输入的字串不是数字串只能包括以下字符（'0'~'9','.'),输入字串最大只能精确到仟亿，小数点只能两位！";
        temp=input.trim();
        float f;
        try{
            f=Float.parseFloat(temp);
        }catch(Exception e){
            return "输入的字串不是数字串只能包括以下字符（'0'~'9','.'),输入字串最大只能精确到仟亿，小数点只能两位！";
        }
        int len=0;
        if(temp.indexOf(".")==-1) len=temp.length();
        else len=temp.indexOf(".");
        if(len>s4.length()-3) return("输入字串最大只能精确到仟亿，小数点只能两位！");
        int n1=0;
        String num="";
        String unit="";
        for(int i=0;i<temp.length();i++){
            if(i>len+2){break;}
            if(i==len) {continue;}
            n1=Integer.parseInt(String.valueOf(temp.charAt(i)));
            num=s1.substring(n1,n1+1);
            n1=len-i+2;
            unit=s4.substring(n1,n1+1);
            result=result.concat(num).concat(unit);
        }
        if((len==temp.length())||(len==temp.length()-1)) result=result.concat("整");
        if(len==temp.length()-2) result=result.concat("零分");

        if(result.endsWith("零拾零万零仟零佰零拾零元零角零分")){
            String s = result.substring(0,result.length()-16);
            result = s.concat("万元整");
        }else if(result.endsWith("零万零仟零佰零拾零元零角零分")){
            String s = result.substring(0,result.length()-14);
            result = s.concat("万元整");
        }else if(result.endsWith("零仟零佰零拾零元零角零分")){
            String s = result.substring(0,result.length()-12);
            result = s.concat("元整");
        }else if (result.endsWith("零佰零拾零元零角零分")){
            String s = result.substring(0,result.length()-10);
            result = s.concat("元整");
        } else if(result.endsWith("零拾零元零角零分")){
            String s = result.substring(0,result.length()-8);
            result = s.concat("元整");
        } else if (result.endsWith("零元零角零分")){
            String s = result.substring(0,result.length()-6);
            result = s.concat("元整");
        } else if (result.endsWith("零角零分")){
            String s = result.substring(0,result.length()-4);
            result = s.concat("整");
        }
            return result;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
//        System.out.println(numberToChinese("111111.11"));
//        System.out.println(numberToChinese("111111.10"));
//        System.out.println(numberToChinese("111111.00"));
//        System.out.println(numberToChinese("111110.00"));
//        System.out.println(numberToChinese("111100.00"));
//        System.out.println(numberToChinese("111000.00"));
//        System.out.println(numberToChinese("110000.00"));
//        System.out.println(numberToChinese("100000.00"));
        System.out.println(numberToChinese("1000000.00"));
        System.out.println(numberToChinese("100000.00"));
    }
}
