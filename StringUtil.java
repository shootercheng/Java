package com.scd.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chengdu
 * @date 2018/10/17.
 */
public class StringUtil {


    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    /**
     * 将字符串转成unicode
     * @param str 待转字符串
     * @return unicode字符串
     */
    public static String convert(String str)
    {
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++)
        {
            c = str.charAt(i);
            sb.append("\\u");
            j = (c >>>8); //取出高8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
            j = (c & 0xFF); //取出低8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);

        }
        return (new String(sb));
    }

    /**
     * 16进制数字字符集
     */
    private static String hexString="0123456789ABCDEF";

    /**
     * 转化字符串为十六进制编码
     */
    public static String toHexString(String s){
        String str="";
        for (int i=0;i<s.length();i++){
            int ch = (int)s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 转化十六进制编码为字符串
     */
    public static String toStringHex(String s){
        byte[] baKeyword = new byte[s.length()/2];
        for(int i = 0; i < baKeyword.length; i++){
            try{
                baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2),16));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        try{
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        }catch (Exception e1){
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     */
    public static String encode(String str){
        //根据默认编码获取字节数组
        byte[] bytes=str.getBytes();
        StringBuilder sb=new StringBuilder(bytes.length*2);
        //将字节数组中每个字节拆解成2位16进制整数
        for(int i=0;i<bytes.length;i++){
            sb.append(hexString.charAt((bytes[i]&0xf0)>>4));
            sb.append(hexString.charAt((bytes[i]&0x0f)>>0));
        }
        return sb.toString();
    }
    /**
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    public static String decode(String bytes)
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2);
        //将每2位16进制整数组装成一个字节
        for(int i=0;i<bytes.length();i+=2)
            baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1))));
        return new String(baos.toByteArray());
    }

    public static void getLogList(String log, List<String> loglist){
        int index = log.indexOf("|");
        if(index != -1){
            loglist.add(log.substring(0, index));
            log = log.substring(index + 1);
            getLogList(log, loglist);
        }else{
            loglist.add(log);
        }
    }

    public static void main(String[] args) throws Exception{
        String str = "\\xe5\\x8c\\x97\\xe4\\xba\\xac\\xe6\\xb5\\x8b\\xe9\\x80\\x9f\\xe8\\xae\\xb0\\xe5\\xbd\\x95";
//        String utf8str = toStringHex(str);
          String utf8str = new String(str.getBytes("GB2312"),"utf-8");
          System.out.println(utf8str);
//        System.out.println(utf8str);
//        System.out.println(getEncoding(str));
          String orign = "test";
          String hexstr = toHexString(orign);
          System.out.println(hexstr);
          long max = Long.MAX_VALUE;
          System.out.println(Long.MAX_VALUE);

          String log = "xxx_20181008.csv.gz|4116178|2018-10-09 08:00:23|2018-10-09 08:00:23|17813";
          List<String> logs = new ArrayList<String>();
          getLogList(log, logs);
          for(String rcvlog : logs){
              System.out.println(rcvlog);
          }
          String uset = "set([";
          String eset = "])";
          String uhash = "{";
          String ehash = "}";
          String ulist = "[";
          String elist = "]";
          String sets = "set(['ipnm_diaodu', 'btv_src_probe', 'onu_probe_game', 'ipnm_ty_card', 'nas_nms_statresource', 'stb_probe', 'nas_nms_porttraffic_m', 'nas_nms_common', 'ipnm_ty_rack', 'nas_nms_info', 'iosa_cem_zte_channel', 'nas_nms_nodecode', 'trf_province', 'nas_nms_snmptraffic', 'nas_nms_icmptraffic', 'ipran_pm_ig27_hw', 'nas_nms_nasippoolcnt_m', 'ipnm_ty_dev', 'nas_nms_info_olp', 'nas_nms_localcode', 'iptv_user_info', 'nas_nms_appendportinfo', 'onu_probe_ping', 'nas_nms_statporttraffic', 'uni_spdtest', 'iosa_cem_stb_newquality', 'nas_nms_portinfo', 'nas_nms_netype', 'ipran_port', 'nas_nms_nascpumem_m', 'iptv_stb_info', 'iosa_cem_stb_newwatch', 'ipnm_ty_port', 'ipran_group', 'iosa_cem_stb_oldquality', 'nas_nms_ippoolinfo', 'user_grid', 'iosa_cem_huawei_channel', 'ipnm_ty_slot', 'nas_nms_vlantraffic', 'onu_probe_http', 'iptv_srv_mngt_zte', 'nas_nms_slot_olp', 'ipran_device', 'ipnm_dev', 'hw_tv_src_probe', 'ipran_diaodu', 'cris_log', 'ipnm_cg', 'nas_nms_port_olp', 'bj_spdtest', 'onu_probe', 'nas_nms_centercode', 'nas_nms_config', 'ipnm_ty_ip_lldp', 'bras_board', 'iptv_stb_switch', 'trf_international', 'ipnm_cirflux', 'iosa_cem_btv_channel', 'ipran_topo', 'nas_nms_pmstraffic', 'zte_tv_src_probe', 'nas_nms_pppsscnt_m', 'iptv_srv_mngt_hw', 'nas_nms_portused'])";
          String members = sets.substring(uset.length(), sets.length() - eset.length());
//          String[] logAarray = log.split("|");
//          if(logAarray.length == 4){
//              System.out.println(logAarray[0]);
//          }
    }
}
