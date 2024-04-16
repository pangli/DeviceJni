//
// Created by PangLi on 2024/4/16.
//

#ifndef DEVICE_ANDROID_UTILS_H
#define DEVICE_ANDROID_UTILS_H

#include <jni.h>
#include <string>
#include <sys/prctl.h>
#include "common/logger.h"

jstring get_property(JNIEnv *env, jobject clazz, jstring _key, jstring _default_value);

bool mem_read_access_by_maps(void *read_addr, size_t len);

char *get_current_thread_name();

#endif //DEVICE_ANDROID_UTILS_H
