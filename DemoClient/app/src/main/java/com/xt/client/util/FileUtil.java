package com.xt.client.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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

    public static boolean unzipPack(String zipPath, String outFolder, String suffix) {
        FileOutputStream out;
        byte buf[] = new byte[16384];
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath));
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                String name = entry.getName();
                if (entry.isDirectory()) {
                    File newDir = new File(outFolder + entry.getName());
                    newDir.mkdir();
                } else if (name.endsWith(suffix)) {
                    File outputFile = new File(outFolder + File.separator + name);
                    String outputPath = outputFile.getCanonicalPath();
                    name = outputPath
                            .substring(outputPath.lastIndexOf("/") + 1);
                    outputPath = outputPath.substring(0, outputPath
                            .lastIndexOf("/"));
                    File outputDir = new File(outputPath);
                    outputDir.mkdirs();
                    outputFile = new File(outputPath, name);
                    outputFile.createNewFile();
                    out = new FileOutputStream(outputFile);

                    int numread = 0;
                    do {
                        numread = zis.read(buf);
                        if (numread <= 0) {
                            break;
                        } else {
                            out.write(buf, 0, numread);
                        }
                    } while (true);
                    out.close();
                }
                entry = zis.getNextEntry();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean copyFolder(File fromFolder, File toFolder, Boolean rewrite) throws Exception {
        if (!fromFolder.exists()) {
            throw new Exception("源目标路径：[" + fromFolder.getAbsolutePath() + "] 不存在...");
        }
        if (!toFolder.exists()) {
            toFolder.mkdirs();
        }
// 获取源文件夹下的文件夹或文件
        File[] resourceFiles = fromFolder.listFiles();
        if (resourceFiles == null) {
            return false;
        }
        for (File file : resourceFiles) {
            File file1 = new File(toFolder.getAbsolutePath() + File.separator + fromFolder.getName());
            // 复制文件
            if (file.isFile()) {
                System.out.println("文件" + file.getName());
                // 在 目标文件夹（B） 中 新建 源文件夹（A），然后将文件复制到 A 中
                // 这样 在 B 中 就存在 A
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                File targetFile1 = new File(file1.getAbsolutePath() + File.separator + file.getName());
                copyFile(file, targetFile1);
                continue;
            }
            // 复制文件夹
            if (file.isDirectory()) {// 复制源文件夹
                copyFolder(file, file1, rewrite);
            }
        }
        return true;
    }

    public static boolean copyFile(File fromFile, File toFile) throws IOException {
        return copyFile(fromFile, toFile, true);
    }

    public static boolean copyFile(File fromFile, File toFile, Boolean rewrite) throws IOException {
        if (!fromFile.exists()) {
            return false;
        }
        if (!fromFile.isFile()) {
            return false;
        }
        if (!fromFile.canRead()) {
            return false;
        }
        return copyFile(new FileInputStream(fromFile), toFile, rewrite);
    }

    public static boolean copyFile(InputStream is, File toFile, Boolean rewrite) throws IOException {
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }
        try {
            FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = is.read(bt)) > 0) {
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

}
