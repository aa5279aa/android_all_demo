#include <jni.h>
#include "com_xt_client_jni_Java2CJNI.h"
#include<android/log.h>
#define LOG_TAG "lxltestjni"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

JNIEXPORT jstring JNICALL Java_com_xt_client_jni_Java2CJNI_java2C(JNIEnv* env, jobject instance)
{
    LOGI("%s", "Java_com_xt_client_jni_Java2CJNI_java2C");
    return (env)->NewStringUTF("I am From Native C");
}