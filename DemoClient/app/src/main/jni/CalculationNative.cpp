#include <jni.h>
#include <complex.h>
#include "com_xt_client_jni_CalculationJNITest.h"
#include <android/log.h>
#include <stdlib.h>

#define LOG_TAG "lxltestjni"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)


//如何转换问题，后续再查
char *ConvertJcharArray2Chars(JNIEnv *env, jcharArray chars) {
    jchar *array = (env)->GetCharArrayElements(chars, NULL);
    jsize arraysize = (env)->GetArrayLength(chars);

    char buf[arraysize + 1];
    LOGI("arraysize:%i", arraysize);
    int i = 0;
    for (i = 0; i < arraysize; i++) {
        buf[i] = static_cast<char>(array[i]);
    }
    buf[arraysize] = '\0';
    return buf;
}

char *ConvertJByteaArrayToChars(JNIEnv *env, jbyteArray bytearray) {
    char *chars = NULL;
    jbyte *bytes;
    bytes = env->GetByteArrayElements(bytearray, 0);
    int chars_len = env->GetArrayLength(bytearray);
    LOGI("chars_len:%d", chars_len);
    chars = new char[chars_len + 1];
    memset(chars, 0, chars_len + 1);
    memcpy(chars, bytes, chars_len);
    chars[chars_len] = 0;
    env->ReleaseByteArrayElements(bytearray, bytes, 0);
    return chars;
}

char *getChar(jint value1,
              jint value2, const char *str1, jdouble double1,
              char *chars, char *bytes) {

    char buf[10];


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

    //两个数字相加，转换为string类型，然后拼接后面的所有字符串
    const char *thirdStr = env->GetStringUTFChars(str1, 0);
    LOGI("%s", "ConvertJcharArray2Chars");
    char *charsChar = ConvertJcharArray2Chars(env, chars);
    LOGI("ConvertJByteaArrayToChars:%s", charsChar);
    char *bytesChar = ConvertJByteaArrayToChars(env, bytes);
    LOGI("bytesChar:%s", bytesChar);//
    LOGI("ConvertJByteaArrayToChars2:%s", charsChar);
    char *buf = getChar(value1, value2, thirdStr, double1, charsChar, bytesChar);
    LOGI("%s %s", "buf is:", buf);
    jstring computerName = env->NewStringUTF(buf);
    return computerName;
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