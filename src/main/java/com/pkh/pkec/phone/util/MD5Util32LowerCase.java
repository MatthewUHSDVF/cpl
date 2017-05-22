package com.pkh.pkec.phone.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util32LowerCase {
        public static void main(String[] args) {
            String str = encryption("123456");
            System.out.println("加密结果：" + str);
        }

        public static String encryption(String plain) {
            String re_md5 = new String();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(plain.getBytes());
                byte b[] = md.digest();

                int i;

                StringBuffer buf = new StringBuffer("");
                for (int offset = 0; offset < b.length; offset++) {
                    i = b[offset];
                    if (i < 0)
                        i += 256;
                    if (i < 16)
                        buf.append("0");
                    buf.append(Integer.toHexString(i));
                }

                re_md5 = buf.toString();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return re_md5;
        }
    }