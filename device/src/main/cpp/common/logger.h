//
// Created by PangLi on 2024/4/16.
//

#ifndef DEVICE_ANDROID_LOGGER_H
#define DEVICE_ANDROID_LOGGER_H

#include <android/log.h>
#include "../utils.h"

#define TAG "Devicec_native"

#ifdef DEBUG

#define LOG(_level,...) do { \
        char logBuffer[4096];                                              \
        snprintf(logBuffer, sizeof(logBuffer), __VA_ARGS__);            \
        __android_log_print(_level,TAG,"[%s] %s",get_current_thread_name(),logBuffer);                            \
} while(false)

#define LOGI(...) LOG(ANDROID_LOG_INFO,__VA_ARGS__)
#define LOGV(...) LOG(ANDROID_LOG_VERBOSE,__VA_ARGS__)
#define LOGD(...) LOG(ANDROID_LOG_DEBUG, __VA_ARGS__)
#define LOGW(...) LOG(ANDROID_LOG_WARN,__VA_ARGS__)
#else
#define LOGI(...)
#define LOGV(...)
#define LOGD(...)
#define LOGW(...)
#endif

#endif //DEVICE_ANDROID_LOGGER_H
