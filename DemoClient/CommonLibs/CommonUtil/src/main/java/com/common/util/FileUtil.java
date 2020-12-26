package com.common.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;

/**
 * 针对文件的操作，比如移动文件，移动文件夹，复制文件等等
 */
public class FileUtil {

    private FileUtil() {

    }

    public static List<String> readListByFile(File file) {
        return readListByFile(file, Charset.defaultCharset().name());
    }

    public static List<String> readListByFile(File file, String code) {
        List<String> contentList = new ArrayList<>();
        if (!file.exists()) {
            return contentList;
        }
        try (FileInputStream fis = new FileInputStream(file); BufferedReader reader = new BufferedReader(new InputStreamReader(fis, code))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentList;
    }

    public static String readStringByFile(File file) {
        StringBuilder builder = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file); BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
            String temp;
            while ((temp = br.readLine()) != null) {
                builder.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static boolean copyfile(File fromFile, File toFile, Boolean rewrite) throws IOException {
        if (!fromFile.exists()) {
            return false;
        }
        if (!fromFile.isFile()) {
            return false;
        }
        if (!fromFile.canRead()) {
            return false;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }
        try (FileInputStream fosfrom = new FileInputStream(fromFile); FileOutputStream fosto = new FileOutputStream(toFile)) {
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); //将内容写到新文件当中
            }
//            fromFile.delete();
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 计算目标文件的MD5值
     *
     * @param filename
     * @return
     * @throws
     */
    public static String getMD5Checksum(String filename) throws Exception {
        File file = new File(filename);
        if (file.exists()) {
            return getMD5Checksum(new FileInputStream(file));
        } else {
            return "";
        }
    }

    /**
     * 校验文件是否与目标MD5相等
     *
     * @param fileAbsPath
     * @param md5
     * @return
     */
    public static boolean checkFileMD5(String fileAbsPath, String md5) {
        if (TextUtils.isEmpty(md5)) {
            return false;
        }
        File file = new File(fileAbsPath);
        try {
            if (file.exists()) {
                return md5.equals(getMD5Checksum(new FileInputStream(file)));
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getMD5Checksum(InputStream fis) throws Exception {
        byte[] b = createChecksum(fis);
        String result = "";

        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static byte[] createChecksum(InputStream fis) throws Exception {
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static long getFileLength(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return 0;
        }
        File file = new File(filePath);
        if (file.exists()) {
            return file.length();
        }
        return 0;
    }

    public static byte[] InputStream2ByteArray(String filePath) {
        byte[] data = null;
        try (InputStream in = new FileInputStream(filePath)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            data = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String readFile(String filePath) {
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file); BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
            StringBuilder fileContent = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                fileContent.append(temp);
            }
            return fileContent.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    public static boolean isExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 计算zip文件解压后大小
     *
     * @param filePath
     * @return
     */
    public static long getZipTrueSize(String filePath) {
        long size = 0;
        java.util.zip.ZipFile f;
        try {
            f = new java.util.zip.ZipFile(filePath);
            Enumeration<? extends ZipEntry> en = f.entries();
            while (en.hasMoreElements()) {
                size += en.nextElement().getSize();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    public static File getApk(String path) {
        FileOutputStream fout = null;
        FileInputStream fin = null;
        File file = new File(path + ".apk");
        File cf = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fout = new FileOutputStream(file);
            fin = new FileInputStream(cf);

            byte[] temp = new byte[1024];
            int len = fin.read(temp);
            if (len == -1 || len < 284) {
                return null;
            }
            byte[] head = Arrays.copyOfRange(temp, 0, 284);
            String h = new String(head, "utf-8");
            String s = Base64Util.des3DecodeECB(Base64Util.RAIL_TOOL_UPGRADE_DES_KEY, h);
            for (int j = 0; j < s.length(); j = j + 2) {
                fout.write(Integer.parseInt(s.substring(j, j + 2), 16));
            }
            fout.write(Arrays.copyOfRange(temp, 284, len));

            for (; (len = fin.read(temp)) != -1; ) {
                fout.write(temp, 0, len);
            }

        } catch (Exception e) {

        } finally {
            try {
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     *      * 获取文件大小
     *      * 
     *      * @param size
     *      * @return
     *     
     */
    public static String getPrintSize(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            // 因为如果以MB为单位的话，要保留最后1位小数，
            // 因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }

    /**
     * 读取txt文件的内容
     *
     * @param file 想要读取的文件路径
     * @return 返回文件内容
     */
    public static String txt2String(String file) {
        Log.i("文件路径", "文件路径" + file);
        File file1 = new File(file);
        if (!file1.exists())
            return "";
        StringBuilder result = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    /**
     * 把文件写入到指定目录中
     *
     * @param data
     * @param path
     */
    public static void write2CurrentFile(String data, String path) {
        write2CurrentFile(data, path, true);
    }

    /**
     * 把文件写入到指定目录中
     *
     * @param data
     * @param path
     */
    public static void write2CurrentFile(String data, String path, boolean append) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path, append);
            out.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static File createFile(String filePath) {
        File file = new File(filePath);
        File outputDir = file.getParentFile();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    public static void fileSave(List<String> cmds, List<String> paths) {
        long date = System.currentTimeMillis();
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : paths) {
            stringBuffer.append("【" + s.trim() + "】");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String decodeDir = AppUtils.isDecodeInternalTestApp ? "/DecodeTest" : "/Decode";
        String decodeDir = "/Decode";
        String path = "/storage/emulated/0" + decodeDir + "/cumminsCalibration/" + stringBuffer.toString() + "备份时间：" + simpleDateFormat.format(date);
        System.out.println(path + "-----------------------------");
        StringBuffer sb = new StringBuffer();
        for (String cmd : cmds) {
            sb.append(cmd).append("\n");
        }
        write2CurrentFile(sb.toString(), path);
    }

    public static List<String> readFileByCumminsBackUps(String filePath) {
        File file = new File(filePath);
        List<String> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file); BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
            String temp;
            while ((temp = br.readLine()) != null) {
                list.add(temp);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void weiChaiC15FileSave(String WeiChaiUploadHex, String kvId, String fileName) {
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String decodeDir = "/Decode";
        if (StringUtil.isEmpty(kvId)) {
            kvId = "nokvId";
        }
        String path = "/storage/emulated/0" + decodeDir + "/weichaiWiseBackups/" + kvId + "/版本号:" + fileName + "备份时间：" + simpleDateFormat.format(date);
        write2CurrentFile(WeiChaiUploadHex, path);
    }

    public static String weiChaiC15Read(String filePath) {
        File file = new File(filePath);
        StringBuffer sb = new StringBuffer();
        try (FileInputStream fis = new FileInputStream(file); BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFilePath(String systemId,String kvId) {
        String protocol = StringUtil.getProtocol(systemId);
        if (StringUtil.isEmpty(protocol)){
            return "";
        }else if (protocol.equals("cummins")){
            return "/storage/emulated/0/Decode/cumminsCalibration";
        }else if (protocol.equals("weiChaiC15")){
            if (StringUtil.isEmpty(kvId)){
                kvId = "nokvId";
            }
            String path = "/storage/emulated/0/Decode/weichaiWiseBackups/" + kvId;
            return path;
        }else {
            return "";
        }

    }


}
