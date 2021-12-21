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
    __android_log_print(0,"111%s","111");
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
    //std::string 
    //两个数字相加，转换为string类型，然后拼接后面的所有字符串
    const char *thirdStr = env->GetStringUTFChars(str1, 0);
    LOGI("%s", "ConvertJcharArray2Chars");
    char *charsChar = ConvertJcharArray2Chars(env, chars);
    LOGI("ConvertJByteaArrayToChars:%s", charsChar);
    LOGI("ConvertJByteaArrayToChars2:%s", charsChar);
    char *bytesChar = ConvertJByteaArrayToChars(env, bytes, charsChar);
    LOGI("ConvertJByteaArrayToChars4:%s", charsChar);
    LOGI("bytesChar:%s", bytesChar);//
    char *buf = getChar(value1, value2, thirdStr, double1, charsChar, bytesChar);
    LOGI("%s %s", "buf is:", buf);
    jstring computerName = env->NewStringUTF(buf);
    delete[] buf;
    return computerName;
}

JNICALL jobject
Java_com_xt_client_jni_CalculationJNITest_updateObjectValue(JNIEnv *env, jobject instance,
                                                            jobject model) {

    jclass cls = env->GetObjectClass(model);
    jfieldID fid = env->GetFieldID(cls, "name", "Ljava/lang/String;");
    jstring jstr = static_cast<jstring>(env->GetObjectField(model, fid));
    const char *str = env->GetStringUTFChars(jstr, NULL);
    env->ReleaseStringUTFChars(jstr, str);
    jstr = env->NewStringUTF("lll");
    env->SetObjectField(model, fid, jstr);
    jmethodID id = env->GetMethodID(cls, "printJavaLog", "()V");
    env->CallVoidMethod(model, id);
    id = env->GetMethodID(cls, "setMoblie", "(Ljava/lang/String;)V");
    jstring mobile = env->NewStringUTF("187000000");
    env->CallVoidMethod(model, id, mobile);
    id = env->GetMethodID(cls, "getMoblie", "()Ljava/lang/String;");
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