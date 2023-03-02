#pragma once

#include <jni.h>
#include <string>
#include <android/log.h>
#include <chrono>
#include "jvmti.h"
#include "MemoryFile.h"

#define LOG_TAG "jvmti"

#define ALOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define ALOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

jvmtiEnv *mJvmtiEnv;
MemoryFile *memoryFile;
jlong tag = 0;

std::string mPackageName;

const char *getClassName(JNIEnv *env, jclass input_class) {
    _jclass *aClass = env->GetObjectClass(input_class);
    _jmethodID *getName = env->GetMethodID(aClass, "getName", "()Ljava/lang/String;");
    jstring className = static_cast<jstring>(env->CallObjectMethod(input_class, getName));
    const char *string = env->GetStringUTFChars(className, JNI_FALSE);
    return string;
}

char *getCharFromMethodID(jvmtiEnv *jvmti_env, jmethodID method) {
    char *signature;
    jclass clazz;
    jvmti_env->GetMethodDeclaringClass(method, &clazz);
    //获得类的签名
    jvmti_env->GetClassSignature(clazz, &signature, nullptr);
    return signature;
}


//查找过滤
jboolean findFilterObjectAlloc(JNIEnv *env, jobject obejct, jclass clazz) {
    const char *name = getClassName(env, clazz);
    std::string clazzName = name;
    int idx = clazzName.find("com.xt.client");
    if (idx != 0) {
        return JNI_FALSE;
    }
    jclass instanceClass = env->FindClass("android/app/Activity");
    if (env->IsInstanceOf(obejct, instanceClass)) {
        return JNI_TRUE;
    }
    instanceClass = env->FindClass("android/app/Service");
    if (env->IsInstanceOf(obejct, instanceClass)) {
        return JNI_TRUE;
    }
//
//    instanceClass = env->FindClass("androidx/fragment/app/Fragment");
//    if (env->IsInstanceOf(obejct, instanceClass)) {
//        return JNI_TRUE;
//    }
    return JNI_FALSE;
}

//查找过滤
jboolean findFilterThread(const char *name, const char *methodName) {
    std::string tmpClassName = name;
    std::string tmpMethodName = methodName;
    int idx;
    //先判断甩没有Error，有Error直接输出
    idx = tmpClassName.find("java/lang/Thread");
    if (idx != std::string::npos && tmpMethodName == "start") {
        return JNI_TRUE;
    }
    return JNI_FALSE;
}

//查找过滤
jboolean findFilterMethod(const char *name) {
    std::string tmpstr = name;
    int idx;
    //先判断甩没有Error，有Error直接输出
    idx = tmpstr.find("OutOfMemory");
    if (idx == std::string::npos) {
        idx = tmpstr.find("ryb/medicine/module_inventory");
        if (idx == std::string::npos)//不存在。
        {
            return JNI_FALSE;
        } else {
            return JNI_TRUE;
        }
    } else {
        return JNI_TRUE;
    }
}

// 获取当时系统时间
std::string GetCurrentSystemTime() {
    //auto t = std::chrono::system_clock::to_time_t(std::chrono::system_clock::now());
    auto now = std::chrono::system_clock::now();
    //通过不同精度获取相差的毫秒数
    uint64_t dis_millseconds =
            std::chrono::duration_cast<std::chrono::milliseconds>(now.time_since_epoch()).count()
            -
            std::chrono::duration_cast<std::chrono::seconds>(now.time_since_epoch()).count() * 1000;
    time_t tt = std::chrono::system_clock::to_time_t(now);
    struct tm *ptm = localtime(&tt);
    char date[60] = {0};
    sprintf(date, "%d-%02d-%02d %02d:%02d:%02d.%03d",
            (int) ptm->tm_year + 1900, (int) ptm->tm_mon + 1, (int) ptm->tm_mday,
            (int) ptm->tm_hour, (int) ptm->tm_min, (int) ptm->tm_sec, (int) dis_millseconds);
    return move(std::string(date));
}


void JNICALL allocFree(jvmtiEnv *jvmti_env, JNIEnv *env, jthread thread,
                       jobject object, jclass object_klass, jlong size) {

    jlong readTag;
    jvmti_env->GetTag(object, &readTag);
    ALOGI("allocFree");
    if (findFilterObjectAlloc(env, object_klass, object_klass)) {
        if (readTag == 0) {
            ALOGI("allocFree_readTag:%lld", readTag);
            return;
        }
        char *classSignature;
        jvmti_env->GetClassSignature(object_klass, &classSignature, nullptr);
        jvmti_env->SetTag(object, tag++);
        char str[500];
        const char *format = "allocFree;%s;class:{%s};size:{%lld};tag:{%lld} \r\n";
        const std::string &timsStr = GetCurrentSystemTime();
        ALOGI(format, timsStr.c_str(), classSignature, size, tag);
        sprintf(str, format, timsStr.c_str(), classSignature, size,
                tag);
        MemoryFile::Write(str, sizeof(char) * strlen(str));
        jvmti_env->Deallocate((unsigned char *) classSignature);
    }


}

void JNICALL objectAlloc(jvmtiEnv *jvmti_env, JNIEnv *env, jthread thread,
                         jobject object, jclass object_klass, jlong size) {
    if (findFilterObjectAlloc(env, object, object_klass)) {
        char *classSignature;
        jvmti_env->GetClassSignature(object_klass, &classSignature, nullptr);
        jvmti_env->SetTag(object, tag++);
        char str[500];
        const char *format = "objectAlloc;%s;class:{%s};size:{%lld};tag:{%lld} \r\n";
        const std::string &timsStr = GetCurrentSystemTime();
        ALOGI(format, timsStr.c_str(), classSignature, size, tag);
        sprintf(str, format, timsStr.c_str(), classSignature, size,
                tag);
        MemoryFile::Write(str, sizeof(char) * strlen(str));
        jvmti_env->Deallocate((unsigned char *) classSignature);
    }
}

jstring getMethodResult(JNIEnv *env, jobject object, jstring methodName) {
    jclass cls = env->GetObjectClass(object);
    ALOGI("cls");
    jmethodID id = env->GetMethodID(cls, "getName", "()Ljava/lang/String;");
    ALOGI("cls:id");
    jstring string = static_cast<jstring>(env->CallObjectMethod(object, id));
//    ALOGI("string:{%s}", env->GetStringUTFChars(string, JNI_FALSE));
    return string;
}

void JNICALL threadStart(jvmtiEnv *jvmti_env, JNIEnv *env, jthread thread) {
//    char *threadName;
//    char *methodName;
    ALOGI("threadStart before");
    jstring name = getMethodResult(env, thread, env->NewStringUTF("getName"));
    ALOGI("threadStart after");
    jvmtiThreadInfo threadInfo;
    jvmti_env->GetThreadInfo(thread, &threadInfo);
//写日志文件
//    char str[500];
    char *format = "%s: threadStart threadName:{ %s },parentName:{ %s }\r\n";
    ALOGI(format, GetCurrentSystemTime().c_str(), env->GetStringUTFChars(name, JNI_FALSE),
          threadInfo.name);
//    sprintf(str, format, GetCurrentSystemTime().c_str(), threadInfo.name, threadInfo.thread_group);
//    MemoryFile::Write(str, sizeof(char) * strlen(str));

}


void JNICALL methodEntry(jvmtiEnv *jvmti_env, JNIEnv *env, jthread thread, jmethodID method) {
    jclass clazz;
    char *methodName;
    //获得方法对应的类
    char *signature = getCharFromMethodID(jvmti_env, method);
    jvmti_env->GetMethodDeclaringClass(method, &clazz);
    //获得类的签名
    jvmti_env->GetClassSignature(clazz, &signature, nullptr);
    //获得方法名字
    jvmti_env->GetMethodName(method, &methodName, nullptr, nullptr);

    if (findFilterThread(signature, methodName)) {
        //写日志文件
        jvmtiThreadInfo threadInfo;
        jvmti_env->GetThreadInfo(thread, &threadInfo);

        jvmtiFrameInfo frames[5];
        jint count;
        jvmti_env->GetStackTrace(thread, 0, 5, frames, &count);
        jint index = 0;
        std::string recordBuilder = "";
        while (index < count) {
            char *name;
            char *localClassName = getCharFromMethodID(jvmti_env, frames[index].method);
            jvmti_env->GetMethodName(frames[index++].method, &name, NULL, NULL);
            recordBuilder.append(localClassName).append(".").append(name).append(
                    "|");
//            ALOGI("localstack:%s.%s", localClassName, name);
        }
//        ALOGI("stack:%s", recordBuilder.c_str());
        char str[500];
        char *format = "startThread;%s:function:{%s %s},parentThread:{%s},where:{%s}\r\n";
        const std::string &dataStr = GetCurrentSystemTime();
        ALOGI(format, dataStr.c_str(), signature, methodName, threadInfo.name,
              recordBuilder.c_str());
        //下面这句报错
        sprintf(str, format, dataStr.c_str(), signature, methodName,
                threadInfo.name, recordBuilder.c_str());

        MemoryFile::Write(str, sizeof(char) * strlen(str));
        ALOGI("MemoryFile::Write");
    }
    jvmti_env->Deallocate((unsigned char *) methodName);
    jvmti_env->Deallocate((unsigned char *) signature);
}


/**
 * 挂载
 */
extern "C"
JNIEXPORT jint JNICALL
Agent_OnAttach(JavaVM *vm, char *options, void *reserved) {
    int error;
    //准备JVMTI环境
    vm->GetEnv((void **) &mJvmtiEnv, JVMTI_VERSION_1_2);

    //开启JVMTI的能力
    jvmtiCapabilities caps;
    mJvmtiEnv->GetPotentialCapabilities(&caps);
    mJvmtiEnv->AddCapabilities(&caps);
    ALOGI("Agent_OnAttach Finish");
    return JNI_OK;
}

/**
 * 添加监听
 */
void init_jvmti(JNIEnv *env, jclass jclass, jstring _path) {
    const char *path = env->GetStringUTFChars(_path, JNI_FALSE);
    ALOGI("init_jvmti:%s", path);
    MemoryFile::Init(path);
//
    jvmtiEventCallbacks callbacks;
    memset(&callbacks, 0, sizeof(callbacks));
    callbacks.VMObjectAlloc = &objectAlloc;
    callbacks.ObjectFree = &allocFree;
//    callbacks.ThreadStart = &threadStart;
    callbacks.MethodEntry = &methodEntry;
//
    ALOGI("SetEventCallbacks");
//    //向JZVM注册回调
    mJvmtiEnv->SetEventCallbacks(&callbacks, sizeof(callbacks));
//
    //对象申请
    mJvmtiEnv->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_VM_OBJECT_ALLOC, nullptr);
//    //释放内存
    mJvmtiEnv->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_OBJECT_FREE, nullptr);
//    //开始线程
//    mJvmtiEnv->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_THREAD_START, nullptr);
//    //方法调用
    mJvmtiEnv->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_METHOD_ENTRY, nullptr);
}

/**
 * 取消监听
 */
void release_jvmti(JNIEnv *env, jclass jclass) {
    ALOGI("release_jvmti");
    //对象申请
    mJvmtiEnv->SetEventNotificationMode(JVMTI_DISABLE, JVMTI_EVENT_VM_OBJECT_ALLOC, nullptr);
//    //释放内存
    mJvmtiEnv->SetEventNotificationMode(JVMTI_DISABLE, JVMTI_EVENT_OBJECT_FREE, nullptr);
//    //开始线程
//    mJvmtiEnv->SetEventNotificationMode(JVMTI_DISABLE, JVMTI_EVENT_THREAD_START, nullptr);
//    //方法调用
    mJvmtiEnv->SetEventNotificationMode(JVMTI_DISABLE, JVMTI_EVENT_METHOD_ENTRY, nullptr);
}

void set_jdwpAllowed(JNIEnv *env, jclass jclass) {
    ALOGI("set_jdwpallowed");
}
/**
 * 所谓的动态注册 是指，动态注册JAVA的Native方法，使得c/c++里面方法名 可以和 java 的Native方法名可以不同，
 * 动态注册是将将二者方法名关联起来，以后在修改Native方法名时，只需修改动态注册关联的方法名称即可
 *  System.loadLibrary("xxx"); 这个方法还是必须要调用的，不管动态还是静态
 */
#define JNIREG_CLASS "com/xt/client/jni/JVMTIMonitor"  //Java类的路径：包名+类名
#define NUM_METHOES(x) ((int) (sizeof(x) / sizeof((x)[0]))) //获取方法的数量


static JNINativeMethod method_table[] = {
        // 第一个参数a 是java native方法名，
        // 第二个参数 是native方法参数,括号里面是传入参的类型，外边的是返回值类型，
        // 第三个参数 是c/c++方法参数,括号里面是返回值类型，
        {"native_init",     "(Ljava/lang/String;)V", (jstring *) init_jvmti},
        {"native_release",  "()V",                   (jstring *) release_jvmti},
        {"set_jdwpAllowed", "()V",                   (jstring *) set_jdwpAllowed},

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


extern "C"
JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
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


