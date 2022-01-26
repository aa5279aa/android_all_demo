#include <jni.h>
#include <string>
#include <assert.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/time.h>
#include <fcntl.h>
#include <android/log.h>
#include <stdlib.h>
#include <thread>
#include <cstring>
#include <iostream>

// ------------------------------- 以下是动态注册 --------------------------------

jstring nativePluginSpliceString(JNIEnv *env, jobject object, jstring str1,jstring str2){
    const char *chars1 = env->GetStringUTFChars(str1, JNI_FALSE);
    const char *chars2 = env->GetStringUTFChars(str2, JNI_FALSE);
    std::string result1 = chars1;
    std::string result2 = chars2;
    result1 = result1 + result2;
    return env->NewStringUTF(result1.c_str());
}

/**
 * 所谓的动态注册 是指，动态注册JAVA的Native方法，使得c/c++里面方法名 可以和 java 的Native方法名可以不同，
 * 动态注册是将将二者方法名关联起来，以后在修改Native方法名时，只需修改动态注册关联的方法名称即可
 *  System.loadLibrary("xxx"); 这个方法还是必须要调用的，不管动态还是静态
 */
#define JNIREG_CLASS "com/xt/client/jni/PluginJni"  //Java类的路径：包名+类名
#define NUM_METHOES(x) ((int) (sizeof(x) / sizeof((x)[0]))) //获取方法的数量


static JNINativeMethod method_table[] = {
        // 第一个参数a 是java native方法名，
        // 第二个参数 是native方法参数,括号里面是传入参的类型，外边的是返回值类型，
        // 第三个参数 是c/c++方法参数,括号里面是返回值类型，
        {"pluginSpliceString",        "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",      (jstring *) nativePluginSpliceString},
};

static int registerMethods(JNIEnv *env, const char *className,
                           JNINativeMethod *gMethods, int numMethods) {
    jclass clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    //注册native方法
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

JavaVM *g_vm;

extern "C"
JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    g_vm = vm;
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    assert(env != NULL);

// 注册native方法
    if (!registerMethods(env, JNIREG_CLASS, method_table, NUM_METHOES(method_table))) {
        return JNI_ERR;
    }

    return JNI_VERSION_1_6;
}
