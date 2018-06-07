//
// Created by Rust on 2018/6/7 0007.
//

#include <string.h>

#include "com_rustfisher_appndkground_jni_ContextUtils.h"

JNIEXPORT jstring JNICALL Java_com_rustfisher_appndkground_jni_ContextUtils_nGetPkgName
  (JNIEnv *env, jclass clz, jobject contextObject) {
    jclass context_clz = env->GetObjectClass(contextObject);
    jmethodID m_id_get_pkg_name = env->GetMethodID(context_clz, "getPackageName", "()Ljava/lang/String;");
    jstring pkg_name = static_cast<jstring>(env->CallObjectMethod(contextObject, m_id_get_pkg_name));
    return pkg_name;
  }

const char* g_pkg_names[] = {"com.demo.1","com.demo.6", "com.rustfisher.appndkground", "demo"};

// check if package name in the list
JNIEXPORT jboolean JNICALL Java_com_rustfisher_appndkground_jni_ContextUtils_nPkgNameInList
  (JNIEnv *env, jclass clz, jobject contextObject) {
    jclass context_clz = env->GetObjectClass(contextObject);
    jmethodID m_id_get_pkg_name = env->GetMethodID(context_clz, "getPackageName", "()Ljava/lang/String;");
    jstring jstr_pkg_name = static_cast<jstring>(env->CallObjectMethod(contextObject, m_id_get_pkg_name));
    const char* char_pkg_name = env->GetStringUTFChars(jstr_pkg_name, 0);
    int names_count = sizeof(g_pkg_names) / sizeof(g_pkg_names[0]);
    for(int i = 0; i < names_count; i++) {
        if(strcmp(g_pkg_names[i], char_pkg_name) == 0) {
            env->ReleaseStringChars(jstr_pkg_name, (const unsigned short *)char_pkg_name);
            return true;
        }
    }
    env->ReleaseStringChars(jstr_pkg_name, (const unsigned short *)char_pkg_name);
    return false;
  }


JNIEXPORT jstring JNICALL Java_com_rustfisher_appndkground_jni_ContextUtils_nGetApkSignedInfo
  (JNIEnv *env, jclass clz, jobject context_obj) {
    jclass context_clz = env->GetObjectClass(context_obj);
    jmethodID pm_id = env->GetMethodID(context_clz, "getPackageManager", "()Landroid/content/pm/PackageManager;");
    jobject pm_obj = env->CallObjectMethod(context_obj, pm_id);
    jclass pm_clz = env->GetObjectClass(pm_obj);
    jmethodID m_id_get_package_info = env->GetMethodID(pm_clz, "getPackageInfo","(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    jmethodID m_id_get_pkg_name = env->GetMethodID(context_clz, "getPackageName", "()Ljava/lang/String;");
    jstring pkg_str = static_cast<jstring>(env->CallObjectMethod(context_obj, m_id_get_pkg_name));
    
    jobject pkg_info_obj = env->CallObjectMethod(pm_obj, m_id_get_package_info, pkg_str, 64);
    jclass pkg_info_clz = env->GetObjectClass(pkg_info_obj);
    jfieldID signatures_fieldId = env->GetFieldID(pkg_info_clz, "signatures", "[Landroid/content/pm/Signature;");
    jobject signatures_obj = env->GetObjectField(pkg_info_obj, signatures_fieldId);
    
    jobjectArray signatures_array = (jobjectArray)signatures_obj;
    jsize size = env->GetArrayLength(signatures_array);
    jobject signature_obj = env->GetObjectArrayElement(signatures_array, 0);
    jclass signature_clz = env->GetObjectClass(signature_obj);
    jmethodID string_id = env->GetMethodID(signature_clz, "toCharsString", "()Ljava/lang/String;");
    jstring str = static_cast<jstring>(env->CallObjectMethod(signature_obj, string_id));
    return str;
  }