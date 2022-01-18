#include <jni.h>
#include <complex.h>
#include "com_xt_client_jni_CalculationJNITest.h"
#include <android/log.h>

#include <stdlib.h>

#define LOG_TAG "lxltestjni"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)


char *ConvertJcharArray2Chars(JNIEnv *env, jcharArray chars) {
    jchar *array = (env)->GetCharArrayElements(chars, NULL);
    jsize arraysize = (env)->GetArrayLength(chars);

    char *buf = new char[arraysize + 1];
//    char buf[arraysize + 1];
    LOGI("arraysize:%i", arraysize);
    int i = 0;
    for (i = 0; i < arraysize; i++) {
        buf[i] = static_cast<char>(array[i]);
    }
    buf[arraysize] = '\0';
    return buf;
}

char *ConvertJByteaArrayToChars(JNIEnv *env, jbyteArray bytearray, char *charsChar) {
    char *chars = NULL;
    jbyte *bytes;
    bytes = env->GetByteArrayElements(bytearray, 0);
    __android_log_print(0, "111%s", "111");
    LOGI("ConvertJByteaArrayToChars3:%s", charsChar);
    int chars_len = env->GetArrayLength(bytearray);
    LOGI("chars_len:%d", chars_len);
    LOGI("ConvertJByteaArrayToChars3.1:%s", charsChar);
    chars = new char[chars_len + 1];
    LOGI("ConvertJByteaArrayToChars3.2:%s,%s", charsChar, chars);
    memset(chars, 0, chars_len + 1);
    LOGI("ConvertJByteaArrayToChars3.3:%s,%s", charsChar, chars);
    memcpy(chars, bytes, chars_len);
    LOGI("ConvertJByteaArrayToChars3.4:%s", charsChar);
    chars[chars_len] = 0;
    env->ReleaseByteArrayElements(bytearray, bytes, 0);
    LOGI("ConvertJByteaArrayToChars3.5:%s", charsChar);
    return chars;
}

char *getChar(jint value1,
              jint value2, const char *str1, jdouble double1,
              char *chars, char *bytes) {

    char *buf = new char[10];


    char s1[10] = "wayne";
    char s2[10] = "22";

    strcat(s1, s2);//字符串拼接
    LOGI("s1:%s", s1);

    sprintf(buf, "%i", value1);
    LOGI("buff:%s", buf);//1

    char local[10];
    sprintf(local, "%i", value2);
    strcat(buf, local);
    LOGI("buff:%s local:%s", buf, local);//12

    sprintf(local, "%s", str1);
    strcat(buf, local);
    LOGI("buff:%s local:%s", buf, local);//123

    sprintf(local, "%.0lf", double1);
    strcat(buf, local);
    LOGI("buff:%s local:%s", buf, local);//1234

    LOGI("chars:%s", chars);//12345

    strcat(buf, chars);//这个转换有问题
    LOGI("buff:%s chars:%s", buf, chars);//12345

    strcat(buf, bytes);
    LOGI("buff:%s bytes:%s", buf, bytes);//123456

    return buf;
}

jstring JNICALL
Java_com_xt_client_jni_CalculationJNITest_calculationSum(JNIEnv *env, jobject instance, jint value1,
                                                         jint value2, jstring str1, jdouble double1,
                                                         jcharArray chars, jbyteArray bytes) {
    //因为jni中的字符串是char类型，所以先把第三个参数jstring转换为char类型
    const char *thirdStr = env->GetStringUTFChars(str1, 0);
    LOGI("%s", "ConvertJcharArray2Chars");
    //java中的char类型在JNI中为jcharArray，同样转换为jni中的char类型
    char *charsChar = ConvertJcharArray2Chars(env, chars);
    LOGI("ConvertJByteaArrayToChars2:%s", charsChar);
    //java中的byte类型在JNI中为jbyteArray，同样转换为jni中的char类型
    char *bytesChar = ConvertJByteaArrayToChars(env, bytes, charsChar);
    LOGI("ConvertJByteaArrayToChars4:%s", charsChar);
    LOGI("bytesChar:%s", bytesChar);
    //字符串拼接
    char *buf = getChar(value1, value2, thirdStr, double1, charsChar, bytesChar);
    LOGI("%s %s", "buf is:", buf);
    //转换完成中生成jstring类型
    jstring computerName = env->NewStringUTF(buf);
    //buf已经使用完没用了，进行内存地址的删除
    delete[] buf;
    return computerName;
}

JNICALL jobject
Java_com_xt_client_jni_CalculationJNITest_updateObjectValue(JNIEnv *env, jobject instance,
                                                            jobject model) {
    //1
    jclass cls = env->GetObjectClass(model);
    //2
    jfieldID fid = env->GetFieldID(cls, "name", "Ljava/lang/String;");
    //3.
    jstring jstr = static_cast<jstring>(env->GetObjectField(model, fid));
    //4
    const char *str = env->GetStringUTFChars(jstr, NULL);
    env->ReleaseStringUTFChars(jstr, str);
    //5
    jstr = env->NewStringUTF("lll");
    //6
    env->SetObjectField(model, fid, jstr);
    //7
    jmethodID id = env->GetMethodID(cls, "printJavaLog", "()V");
    //8
    env->CallVoidMethod(model, id);
    //9
    id = env->GetMethodID(cls, "setMoblie", "(Ljava/lang/String;)V");
    //10
    jstring mobile = env->NewStringUTF("187000000");
    //11
    env->CallVoidMethod(model, id, mobile);
    //12
    id = env->GetMethodID(cls, "getMoblie", "()Ljava/lang/String;");
    //13
    auto moblie2 = (env->CallObjectMethod(model, id));
    LOGI("getMoblie:%s", env->GetStringUTFChars(static_cast<jstring>(moblie2), 0));//
    return model;
}


void utils() {
    char buf[8];
    sprintf(buf, "%s", '1');
    int value = atoi(buf);//字符串转int
    LOGI("%s %i", "num value:", value);
}
//
//char getFromDouble(jobject double1) {
//    char s[2] = "0";
//    return s[2];
//}
//
//char getFromChar(char str) {
//
//}
//
//char getFromBytes(char str) {
//
//}