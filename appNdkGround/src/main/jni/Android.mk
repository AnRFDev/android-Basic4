LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := ndkground
LOCAL_SRC_FILES := contextutils.cpp
include $(BUILD_SHARED_LIBRARY)