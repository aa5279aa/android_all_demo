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

using namespace std;
using namespace std::chrono;
#define LOG_TAG "lxltestjni"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
// ------------------------------- 以下是动态注册 --------------------------------

//jstring nativeEncryptionStr(JNIEnv *env, jclass clazz) {
//    std::string hello = "Hello from C++  by 动态注册";
//    return env->NewStringUTF(hello.c_str());
//}


jstring nativeEncryptionStr(JNIEnv *env, jclass clazz, jstring value) {
    const char *str = env->GetStringUTFChars(value, JNI_FALSE);
    std::string result = str;
    result = result + result;
    return env->NewStringUTF(result.c_str());;
}

jstring nativeStaticEncryptionStr(JNIEnv *env, jclass clazz, jstring value) {
    const char *str = env->GetStringUTFChars(value, JNI_FALSE);
    std::string result = str;
    result = result + result;
    return env->NewStringUTF(result.c_str());;
}

//传入类型，
jclass nativeGetSuperClassName(JNIEnv *env, jobject object, jstring clazz) {
    LOGI("clazz1");
    const char *className = env->GetStringUTFChars(clazz, JNI_FALSE);
    LOGI("clazz2:%s", className);
    jclass aClass = env->FindClass(className);
    LOGI("clazz3");
    jclass pJclass = env->GetSuperclass(aClass);
    LOGI("clazz4");
    return pJclass;
}


int file_read(int fd, unsigned char *buf, int size) {
    return read(fd, buf, size);
}

int file_close(int fd) {
    return close(fd);
}

int file_open(const char *filename, int flags) {
    int fd;

    fd = open(filename, flags, 0666);
    if (fd == -1)
        return -1;

    return fd;
}


struct Param {
    const char *charTitle;
    jobject activity;
    jstring title;
    int i;
};

//jobject params;
Param param;
JavaVM *g_vm;

void *freshAndroidPage(void *arg) {
    JNIEnv *env;
    g_vm->AttachCurrentThread(&env, nullptr);
    Param tmp = param;
    jobject activity = tmp.activity;
    const char *title = env->GetStringUTFChars(tmp.title, JNI_FALSE);
    int i = 0;
    __android_log_print(ANDROID_LOG_INFO, "lxltestjni",
                        "freshAndroidPage start,tmp.charTitle:%s,title:%s", tmp.charTitle, title);
    while (i++ < 10) {
        __android_log_print(ANDROID_LOG_INFO, "lxltestjni", "i:%d,charTitle:%s", i, tmp.charTitle);
        jclass cls = env->GetObjectClass(activity);
        jmethodID id = env->GetMethodID(cls, "showMessage", "(Ljava/lang/String;)V");

        strchr(tmp.charTitle, i);
        std::string result = tmp.charTitle;
        result.append(std::to_string(i));
        jstring message = env->NewStringUTF(result.c_str());
        env->CallVoidMethod(activity, id, message);
        sleep(5);
    }
    g_vm->DetachCurrentThread();
}

void nativeRefresh(JNIEnv *env, jclass clazz, jstring title, jobject activity) {
    param.title = static_cast<jstring>(env->NewGlobalRef(title));
    param.charTitle = env->GetStringUTFChars(title, JNI_FALSE);
    param.activity = env->NewGlobalRef(activity);
    param.i = 1;
//    int param = 123;
    pthread_t pt;
    __android_log_print(ANDROID_LOG_INFO, "lxltestjni", "nativeRefresh start,tmp.c:%d", param.i);
    pthread_create(&pt, NULL, freshAndroidPage, &param);//这里的param无用
//    sleep(1);
}

jstring nativeReadStrByPath(JNIEnv *env, jobject object, jstring path) {
    const char *str = env->GetStringUTFChars(path, JNI_FALSE);
    FILE *file = fopen(str, "r");
//    LOGI("content%","111");
    __android_log_print(ANDROID_LOG_INFO, "lxltestjni", "111");
    if (file == NULL) {
//        LOGI("null2");
    }
    std::string result;
    char buffer[1024] = {0};
    while (fread(buffer, sizeof(char), 1024, file) != 0) {
//        LOGI("%s","content");
        result += buffer;
    }
    return env->NewStringUTF(result.c_str());
}

jstring nativeDecrypt(JNIEnv *env, jobject object, jstring ciphertext) {
    const char *charCiphertext = env->GetStringUTFChars(ciphertext, JNI_FALSE);
    const char split[] = ",";
    char outText[100];
    strcpy(outText, charCiphertext);
    char *res = strtok(outText, split);//203转为int
    std::string result;
    while (res != NULL) {
        //res转为int，-1之后除以2。
        int i = atoi(res);
        char u = (char) ((i - 1) / 2);
        string str(1, u);
        result.append(str);
        res = strtok(NULL, split);
    }
    return env->NewStringUTF(result.c_str());
}

//简单把字符串的assic码*2-1，生成一串int数组,转换为字符串返回
jstring nativeEncryption(JNIEnv *env, jobject object, jstring plaintext) {
    const char *charPlain = env->GetStringUTFChars(plaintext, JNI_FALSE);
    int i = 0;
    // int pos[] = new int[10];
    int pos[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    while (i < strlen(charPlain)) {
        // cout<<i<<","<< content[i]<<endl;
        // pos[i]=atoi(content[i]);
        pos[i] = (int) (charPlain[i]) * 2 + 1;
        i++;
    }
    //转为string
    std::string str;
    for (i = 0; i < sizeof(pos) / sizeof(int); i++) {
        str.append(std::to_string(pos[i]) + ",");
    }
    jstring pJstring = env->NewStringUTF(str.c_str());
    return pJstring;
}

/**
 * 所谓的动态注册 是指，动态注册JAVA的Native方法，使得c/c++里面方法名 可以和 java 的Native方法名可以不同，
 * 动态注册是将将二者方法名关联起来，以后在修改Native方法名时，只需修改动态注册关联的方法名称即可
 *  System.loadLibrary("xxx"); 这个方法还是必须要调用的，不管动态还是静态
 */
#define JNIREG_CLASS "com/xt/client/jni/DynamicRegister"  //Java类的路径：包名+类名
#define NUM_METHOES(x) ((int) (sizeof(x) / sizeof((x)[0]))) //获取方法的数量


static JNINativeMethod method_table[] = {
        // 第一个参数a 是java native方法名，
        // 第二个参数 是native方法参数,括号里面是传入参的类型，外边的是返回值类型，
        // 第三个参数 是c/c++方法参数,括号里面是返回值类型，
        {"encryptionStr",       "(Ljava/lang/String;)Ljava/lang/String;",      (jstring *) nativeEncryptionStr},
        {"staticencryptionStr", "(Ljava/lang/String;)Ljava/lang/String;",      (jstring *) nativeStaticEncryptionStr},
        {"readStrByPath",       "(Ljava/lang/String;)Ljava/lang/String;",      (jstring *) nativeReadStrByPath},
        {"refresh",             "(Ljava/lang/String;Landroid/app/Activity;)V", (jstring *) nativeRefresh},
        {"decrypt",             "(Ljava/lang/String;)Ljava/lang/String;",      (jstring *) nativeDecrypt},
        {"encryption",          "(Ljava/lang/String;)Ljava/lang/String;",      (jstring *) nativeEncryption},
        {"getSuperClassName",   "(Ljava/lang/String;)Ljava/lang/Class;",       (jclass *) nativeGetSuperClassName},


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
