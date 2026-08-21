#include <jni.h>
#include <string>
#include <fcntl.h>
#include <unistd.h>
#include <android/log.h>

#define LOG_TAG "DFG-Native"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jboolean JNICALL
Java_com_daisyforgaming_core_NativeHelper_writeSysfs(
        JNIEnv* env,
        jobject /* this */,
        jstring path,
        jstring value) {

    const char *nativePath = env->GetStringUTFChars(path, nullptr);
    const char *nativeValue = env->GetStringUTFChars(value, nullptr);

    jboolean success = JNI_FALSE;

    int fd = open(nativePath, O_WRONLY);
    if (fd != -1) {
        ssize_t bytesWritten = write(fd, nativeValue, strlen(nativeValue));
        if (bytesWritten != -1) {
            success = JNI_TRUE;
        } else {
            LOGE("Failed to write to %s", nativePath);
        }
        close(fd);
    } else {
        LOGE("Failed to open %s", nativePath);
    }

    env->ReleaseStringUTFChars(path, nativePath);
    env->ReleaseStringUTFChars(value, nativeValue);

    return success;
}
