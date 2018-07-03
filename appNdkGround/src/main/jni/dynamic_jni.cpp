//
// Created by Rust on 2018/7/3.
//

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <jni.h>

#define JNI_REG_CLASS "com/rustfisher/appndkground/jni/DynamicJNI" // path of Java file

JNIEXPORT jstring JNICALL get_hello(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF("hello from jni");
}

JNIEXPORT jint JNICALL meaning_of_the_universe(JNIEnv *env, jclass clazz) {
    return 42;
}

static JNINativeMethod g_methods[] = {
    { "getHello", "()Ljava/lang/String;", (void*)get_hello},
    { "meaningOfTheUniverse", "()I", (void*)meaning_of_the_universe},
};

// must define this function
JNIEXPORT int JNICALL JNI_OnLoad(JavaVM *vm,void *reserved) {
  JNIEnv *env;
  if (vm->GetEnv((void **) &env,JNI_VERSION_1_6) != JNI_OK) {
    return JNI_ERR;
  }

  jclass javaClass = env->FindClass(JNI_REG_CLASS);
  if (javaClass == NULL){
    return JNI_ERR;
  }

  int method_count = sizeof(g_methods) / sizeof(g_methods[0]);
  if (env->RegisterNatives(javaClass, g_methods, method_count) < 0) {
    return JNI_ERR;
  }

  return JNI_VERSION_1_6;
}