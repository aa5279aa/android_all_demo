package com.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class Base64Util {
    public static final String RAIL_TOOL_UPGRADE_DES_KEY = "=R6cc,&fK.g!m1(~%:a2N$z5D.9PP)%8bhaB[|0-z%^dp]=@J&#&9}L6}8hB+aa?x</bDEz(Pae>QpPG.FA9|2>&inx=Sdr0-oym8rkPb|djx+qkC+2opl5MRMtv9,'d3/pSDEOJGBAJQ8}$hy_n]&I[&J)H,'u>{!Oq[cw9)w'8#lLwa|:)T2x]p~z1CAEz39>zd&bAC=+-li?)k9+7loLzQh23T^!K/=PG>><)(s/MoB-cI9KR4y6@>20Ly&6x";
    private static char[] folderLegalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+-".toCharArray();
    public static final String DECODER_FILE_DES_KEY = "G2&=9%8+y(3psbm_pNKA~x{:|_~P-^AM(xm_l5l<8W{4wtko<QqnVH.M0G!)sPi;`vzakE8j" +
            "!zF3dF/AmkqN1`r!4Pq8FIlj6$j1Mfu`cCvj0^.n.,F>=rgJ{3FdJ;O46+KBRWxx||ACmK-6ezK0_XEIwG0qJ1BI?vHs]3};$s^!.hZ~tgfygmA[%Z9a,2WP" +
            "/svb%SDT)9nYIK??VfbnS?7KTT<VIg-4k1xQzfZv!RjLIcz'1(yDz!0Q[g_7<J^#VZdh8SDH3NEIH0L'S,H+9'C/AlmDDi@gKNG2+OoJI";
    public static final String RAIL_TOOL_DES_KEY = "ujmik,*&^yhnRTFvbgcxzp;123324sd87dtr";

    public static String encode(byte[] data) {
        int start = 0;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);

        int end = len - 3;
        int i = start;
        int n = 0;

        while (i <= end) {
            int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 0x0ff) << 8) | (((int) data[i + 2]) & 0x0ff);

            buf.append(folderLegalChars[(d >> 18) & 63]);
            buf.append(folderLegalChars[(d >> 12) & 63]);
            buf.append(folderLegalChars[(d >> 6) & 63]);
            buf.append(folderLegalChars[d & 63]);

            i += 3;

            if (n++ >= 14) {
                n = 0;
                buf.append(" ");
            }
        }

        if (i == start + len - 2) {
            int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 255) << 8);

            buf.append(folderLegalChars[(d >> 18) & 63]);
            buf.append(folderLegalChars[(d >> 12) & 63]);
            buf.append(folderLegalChars[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == start + len - 1) {
            int d = (((int) data[i]) & 0x0ff) << 16;

            buf.append(folderLegalChars[(d >> 18) & 63]);
            buf.append(folderLegalChars[(d >> 12) & 63]);
            buf.append("==");
        }

        return buf.toString();
    }

    private static int decode(char c) {
        if (c >= 'A' && c <= 'Z')
            return ((int) c) - 65;
        else if (c >= 'a' && c <= 'z')
            return ((int) c) - 97 + 26;
        else if (c >= '0' && c <= '9')
            return ((int) c) - 48 + 26 + 26;
        else
            switch (c) {
                case '+':
                    return 62;
                case '/':
                    return 63;
                case '-':
                    return 63;
                case '=':
                    return 0;
                default:
                    throw new RuntimeException("unexpected code: " + c);
            }
    }

    public static byte[] decode(String s) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            decode(s, bos);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        byte[] decodedBytes = bos.toByteArray();
        try {
            bos.close();
            bos = null;
        } catch (IOException ex) {
            System.err.println("Error while decoding BASE64: " + ex.toString());
        }
        return decodedBytes;
    }

    private static void decode(String s, OutputStream os) throws IOException {
        int i = 0;

        int len = s.length();

        while (true) {
            while (i < len && s.charAt(i) <= ' ')
                i++;

            if (i == len)
                break;

            int tri = (decode(s.charAt(i)) << 18) + (decode(s.charAt(i + 1)) << 12) + (decode(s.charAt(i + 2)) << 6)
                    + (decode(s.charAt(i + 3)));

            os.write((tri >> 16) & 255);
            if (s.charAt(i + 2) == '=')
                break;
            os.write((tri >> 8) & 255);
            if (s.charAt(i + 3) == '=')
                break;
            os.write(tri & 255);

            i += 4;
        }
    }

    public static byte[] des3DecodeECB(byte[] key, byte[] data) throws Exception {

        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, deskey);

        byte[] bOut = cipher.doFinal(data);

        return bOut;

    }

    public static String des3DecodeECB(String key, String data) throws Exception {
        return new String(des3DecodeECB(key.getBytes(), Base64Util.decode(data)));
    }

    /**
     * ECB加密,不要IV
     *
     * @param key  密钥
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static byte[] des3EncodeECB(byte[] key, byte[] data) throws Exception {

        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);

        return bOut;
    }

    public static String des3DecodeECB(String data) throws Exception {
        return des3DecodeECB(DECODER_FILE_DES_KEY, data);
    }

    public static String des3EncodeECB(String data) {
        try {
            return new String(Base64Util.encode(des3EncodeECB(DECODER_FILE_DES_KEY.getBytes("UTF-8"), data.getBytes("UTF-8"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void des3DecodeFile(InputStream fin, OutputStream fout, String key) throws Exception {
        byte[] temp = new byte[1024];
        int len = fin.read(temp);
        if (len == -1 || len < 284) {
            return;
        }
        byte[] head = Arrays.copyOfRange(temp, 0, 284);
        String h = new String(head, "utf-8");
        String s = des3DecodeECB(key, h);
        for (int j = 0; j < s.length(); j = j + 2) {
            fout.write(Integer.parseInt(s.substring(j, j + 2), 16));
        }
        fout.write(Arrays.copyOfRange(temp, 284, len));
        for (; (len = fin.read(temp)) != -1; ) {
            fout.write(temp, 0, len);
        }
    }
}
