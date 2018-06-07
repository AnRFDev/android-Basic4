package com.rustfisher.appndkground.jni;

// javah com.rustfisher.appndkground.jni.ContextUtils
public class ContextUtils {

    static {
        System.loadLibrary("ndkground");
    }

    public static native String nGetPkgName(Object context);

    public static native boolean nPkgNameInList(Object context); // check pkg name whether in the list

    public static native String nGetApkSignedInfo(Object context); // get key info
}
