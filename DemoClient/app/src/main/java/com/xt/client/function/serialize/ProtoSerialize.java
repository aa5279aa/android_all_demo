package com.xt.client.function.serialize;

import com.xt.client.annotations.ProtoAnno;
import com.xt.client.viewmodel.base.BasePBModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProtoSerialize {

    static HashMap<ProtoAnno.Label, Integer> labelMap = new HashMap<>();

    static {
        labelMap.put(ProtoAnno.Label.INT, 0);
        labelMap.put(ProtoAnno.Label.LONG, 0);
        labelMap.put(ProtoAnno.Label.STRING, 2);
    }

    // 目前序列化一级节点下面的int和string就好
    public static byte[] serialize(BasePBModel pbModel) throws Exception {
        Class object = pbModel.getClass(); // 获取activity的Class
        Field[] fields = object.getDeclaredFields(); // 通过Class获取activity的所有字段
        List<ProtoAnnoModel> protoAnnoList = new ArrayList<>();
        for (Field field : fields) { // 遍历所有字段
            // 获取字段的注解，如果没有ViewInject注解，则返回null
            ProtoAnno viewInject = field.getAnnotation(ProtoAnno.class);
            if (viewInject != null) {
                // Class<?> type = field.getType();
                int tag = viewInject.tag(); // 获取加倍属性
                boolean isArray = viewInject.isArray();
                ProtoAnno.Label type2 = viewInject.type();
                protoAnnoList.add(new ProtoAnnoModel(tag, type2, isArray, field));
            }
        }

        List<Byte> byteList = new ArrayList<>();

        for (int i = 0; i < protoAnnoList.size(); i++) {
            ProtoAnnoModel protoAnnoModel = protoAnnoList.get(i);
            int mTag = protoAnnoModel.mTag;
            ProtoAnno.Label mType = protoAnnoModel.mType;
            Object value = protoAnnoModel.mField.get(pbModel);

            // 生成value的byte
            if (mType == ProtoAnno.Label.INT | mType == ProtoAnno.Label.LONG | mType == ProtoAnno.Label.BOOLEAN) {
                long valueInt;
                if (mType == ProtoAnno.Label.BOOLEAN) {
                    valueInt = (boolean) value ? 1 : 0;
                } else if (mType == ProtoAnno.Label.LONG) {
                    valueInt = (long) value;
                } else {
                    valueInt = (int) value;
                }
                byte tagByte = (byte) (mTag << 3 | 0);
                byteList.add(tagByte);
                while (true) {
                    // 每次都取后7位添加到字节当中
                    long byteInt = valueInt & 0b01111111;
                    if (valueInt >> 7 == 0) {
                        byteList.add((byte) byteInt);
                        break;
                    } else {
                        byteInt = valueInt | 0b10000000;
                        valueInt = valueInt >> 7;
                        byteList.add((byte) byteInt);
                    }
                }
            } else if (mType == ProtoAnno.Label.STRING) {
                byte tagByte = (byte) (mTag << 3 | 2);
                // 如果字节不为空，则添加
                String valueString = String.valueOf(value);
                if (valueString == null || valueString.length() == 0) {
                    continue;
                }
                byteList.add(tagByte);
                byte[] bytes = valueString.getBytes("utf-8");
                byteList.add((byte) bytes.length);// 添加字符串长度， 这里有bug，如果中文超过127会有问题，不过暂时先不管了
                for (int k = 0; k < bytes.length; k++) {
                    byteList.add(bytes[k]);
                }
            } else if (mType == ProtoAnno.Label.MESSAGE) {
                //解析model

            }

        }
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        return bytes;
    }

    // 反序列化流程
    public static void unSerialize(BasePBModel model, byte[] bytes) throws Exception {
        Class object = model.getClass(); // 获取activity的Class
        Field[] fields = object.getDeclaredFields(); // 通过Class获取activity的所有字段
        Map<Integer, ProtoAnnoModel> protoAnnoMap = new HashMap<>();
        for (Field field : fields) { // 遍历所有字段
            // 获取字段的注解，如果没有ViewInject注解，则返回null
            ProtoAnno viewInject = field.getAnnotation(ProtoAnno.class);
            if (viewInject != null) {
                // Class<?> type = field.getType();
                int tag = viewInject.tag(); // 获取加倍属性
                boolean isArray = viewInject.isArray();
                ProtoAnno.Label type2 = viewInject.type();
                protoAnnoMap.put(tag, new ProtoAnnoModel(tag, type2, isArray, field));
            }
        }

        int readIndex = 0;
        while (readIndex < bytes.length) {
            byte b = bytes[readIndex];
            int tag = b >> 3;// 获取tag
            byte type = (byte) (b & 0b00000111);// 获取tag type
            ProtoAnnoModel protoAnnoModel = protoAnnoMap.get(tag);

            readIndex++;
            if (type == 0) {// 数字
                int intValue = 0;
                boolean isMove = false;
                while (true) {
                    if (isMove) {
                        int ii = bytes[readIndex] << 7;
                        intValue = ii | intValue;
                    } else {
                        intValue = bytes[readIndex] & 0b00000000000000000000000001111111;// 获取后7位的值 0110 1000
                    }

                    int isEnd = bytes[readIndex] & 0b00000000000000000000000010000000;// 是否结束
                    if (isEnd == 0) {
                        break;
                    }
                    isMove = true;
                    readIndex++;
                }

                if (protoAnnoModel != null) {
                    protoAnnoModel.mField.setAccessible(true);
                    if (protoAnnoModel.mType == ProtoAnno.Label.BOOLEAN) {
                        protoAnnoModel.mField.set(model, intValue == 1);
                    } else {
                        protoAnnoModel.mField.set(model, intValue);
                    }
                } else {
                    System.out.println("客户端契约没有对应字段，不参与序列化");
                }
            } else if (type == 2) {// 字符串
                int length = bytes[readIndex];// 获取字符串长度
                byte[] stringbytes = new byte[length];
                readIndex++;//字节开始index
                for (int i = 0; i < stringbytes.length; i++) {
                    stringbytes[i] = bytes[readIndex + i];
                }
                readIndex = readIndex + length - 1;//直接加长度会导致多一位，所以这里减1
                String string = new String(stringbytes, "utf-8");
                if (protoAnnoModel != null) {
                    protoAnnoModel.mField.setAccessible(true);
                    protoAnnoModel.mField.set(model, string);
                } else {
                    System.out.println("客户端契约没有对应字段，不参与序列化");
                }

            } else {
                break;
            }
            readIndex++;
        }

    }

    static class ProtoAnnoModel {
        int mTag;
        boolean mIsArray;
        ProtoAnno.Label mType;
        Field mField;

        protected ProtoAnnoModel(int tag, ProtoAnno.Label type, boolean isArray, Field field) {
            mTag = tag;
            mType = type;
            mIsArray = isArray;
            mField = field;
        }

    }

}
