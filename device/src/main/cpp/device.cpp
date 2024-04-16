#include <jni.h>
#include "utils.h"

// Define JNI methods to be registered
static JNINativeMethod jniMethods[] = {
        {"call", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
         (void *) get_property},
};

// Define JNI library registration function
JNIEXPORT jint

JNICALL JNI_OnLoad(JavaVM *vm, void *__unused) {
    JNIEnv *env = nullptr;
    jclass clazz = NULL;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        goto err_exit;
    }

    clazz = env->FindClass("com/zorro/device/NativeLib");

    if (clazz == nullptr) {
        goto err_exit;
    }
    if (env->RegisterNatives(clazz, jniMethods, sizeof(jniMethods) / sizeof(jniMethods[0])) < 0) {
        goto err_exit;
    }
    LOGD("JNI_OnLoad called!");

    return JNI_VERSION_1_6;
    err_exit:
    return JNI_ERR;
}