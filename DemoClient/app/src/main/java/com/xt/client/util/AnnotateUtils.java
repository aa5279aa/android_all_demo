package com.xt.client.util;

import android.app.Activity;

import com.xt.client.annotations.CountAnno;
import com.xt.client.model.CountModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotateUtils {

    public static void toString(CountModel model) throws Exception {
        Class object = model.getClass(); // 获取activity的Class
        Field[] fields = object.getDeclaredFields(); // 通过Class获取activity的所有字段
        for (Field field : fields) { // 遍历所有字段
            // 获取字段的注解，如果没有ViewInject注解，则返回null
            CountAnno viewInject = field.getAnnotation(CountAnno.class);
            if (viewInject != null) {
                Class<?> type = field.getType();
                int count = viewInject.count(); //获取加倍属性
                if (count > 0) {
                    if (type.getSimpleName().equalsIgnoreCase("int")) {
                        int anInt = field.getInt(model);
//                            field.setAccessible(true);
                        field.set(model, anInt * count);
                    }

                }
                CountAnno.Label labelType = viewInject.type();//文字获取加倍属性

                if (type.getSimpleName().equalsIgnoreCase("String")) {
                    String aChar = String.valueOf(field.get(model));
                    if (labelType == CountAnno.Label.SINGLE) {

                    } else if (labelType == CountAnno.Label.DOUBLE) {
                        field.set(model, aChar + aChar);
                    } else if (labelType == CountAnno.Label.THRICE) {
                        field.set(model, aChar + aChar + aChar);
                    }

                }

            }
        }

    }

    public static void injectViews(Activity activity) {
        Class<? extends Activity> object = activity.getClass(); // 获取activity的Class
        Field[] fields = object.getDeclaredFields(); // 通过Class获取activity的所有字段
        for (Field field : fields) { // 遍历所有字段
            // 获取字段的注解，如果没有ViewInject注解，则返回null
            CountAnno viewInject = field.getAnnotation(CountAnno.class);
            if (viewInject != null) {
                int viewId = viewInject.count(); // 获取字段注解的参数，这就是我们传进去控件Id
                if (viewId != -1) {
                    try {
                        // 获取类中的findViewById方法，参数为int
                        Method method = object.getMethod("findViewById", int.class);
                        // 执行该方法，返回一个Object类型的View实例
                        Object resView = method.invoke(activity, viewId);
                        field.setAccessible(true);
                        // 把字段的值设置为该View的实例
                        field.set(activity, resView);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
