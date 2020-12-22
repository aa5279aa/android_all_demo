package com.common.util;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public MD5Util() {
    }

    public static String getMessageDigest(byte[] buffer) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance(CipherType.MD5.getType());
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for(int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var9) {
            return null;
        }
    }

    public static byte[] getRawDigest(byte[] buffer) {
        try {
            MessageDigest mdTemp = MessageDigest.getInstance(CipherType.MD5.getType());
            mdTemp.update(buffer);
            return mdTemp.digest();
        } catch (Exception var2) {
            return null;
        }
    }

    private static String getMD5(InputStream is, int bufLen) {
        if (is != null && bufLen > 0) {
            try {
                MessageDigest md = MessageDigest.getInstance(CipherType.MD5.getType());
                StringBuilder md5Str = new StringBuilder(32);
                byte[] buf = new byte[bufLen];
                boolean var5 = false;

                int readCount;
                while((readCount = is.read(buf)) != -1) {
                    md.update(buf, 0, readCount);
                }

                byte[] hashValue = md.digest();

                for(int i = 0; i < hashValue.length; ++i) {
                    md5Str.append(Integer.toString((hashValue[i] & 255) + 256, 16).substring(1));
                }

                return md5Str.toString();
            } catch (Exception var8) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static String getMD5(String filePath) {
        if (filePath == null) {
            return null;
        } else {
            File f = new File(filePath);
            return f.exists() ? getMD5(f, 102400) : null;
        }
    }

    public static String getMD5(File file) {
        return getMD5(file, 102400);
    }

    private static String getMD5(File file, int bufLen) {
        if (file != null && bufLen > 0 && file.exists()) {
            FileInputStream fin = null;

            String var4;
            try {
                fin = new FileInputStream(file);
                String md5 = getMD5((InputStream)fin, (int)((long)bufLen <= file.length() ? (long)bufLen : file.length()));
                fin.close();
                var4 = md5;
                return var4;
            } catch (Exception var14) {
                var4 = null;
            } finally {
                try {
                    if (fin != null) {
                        fin.close();
                    }
                } catch (IOException var13) {
                }

            }

            return var4;
        } else {
            return null;
        }
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        } else {
            MessageDigest md5 = null;

            try {
                md5 = MessageDigest.getInstance("MD5");
                byte[] bytes = md5.digest(string.getBytes());
                StringBuilder result = new StringBuilder();
                byte[] var4 = bytes;
                int var5 = bytes.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    byte b = var4[var6];
                    String temp = Integer.toHexString(b & 255);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }

                    result.append(temp);
                }

                return result.toString();
            } catch (NoSuchAlgorithmException var9) {
                var9.printStackTrace();
                return "";
            }
        }
    }
}
