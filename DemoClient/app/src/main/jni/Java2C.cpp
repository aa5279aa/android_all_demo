#include <jni.h>
#include "com_xt_client_jni_Java2CJNI.h"
#include<android/log.h>

JNIEXPORT jstring JNICALL Java_com_xt_client_jni_Java2CJNI_java2C(JNIEnv* env, jobject instance)
{
    return (env)->NewStringUTF("I am From Native C");
}