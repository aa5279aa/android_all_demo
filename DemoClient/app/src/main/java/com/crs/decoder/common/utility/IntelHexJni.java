package com.crs.decoder.common.utility;

public class IntelHexJni {

    static {
        try {
            System.loadLibrary("intelhex");
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }

    public int getLastError(){
        return NGetLastError();
    }

    //native方法
    private static native String NBase64DesDecode(String var0);

    private static native String NReplaceTemplate(String[] var0, int[] var1, int[] var2, String var3);

    private static native int NBin2CRSF(int var0, byte[] var1, String var2, int var3);

    private static native String NBin2Hex(int var0, byte[] var1);

    private static native int NHex2CRSF(String var0, String var1, int var2);

    private static native int NHexArray2CRSF(String[] var0, String var1, int var2);

    private static native byte[] NBin2SRecord(int[] var0, byte[][] var1);

    private static native String NCRSF2Hex(String var0);

    private static native int NPack(byte[] var0, String var1);

    private static native byte[] NUnpack(byte[] var0);

    private static native int NGetLastError();
}
