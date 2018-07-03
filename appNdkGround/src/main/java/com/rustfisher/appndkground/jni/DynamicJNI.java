package com.rustfisher.appndkground.jni;

/**
 * Try dynamic way to jni
 * No need to generate .h file
 * Created by Rust on 2018/7/3.
 */
public class DynamicJNI {
    static {
        System.loadLibrary("ndkground");
    }

    public static native String getHello();

    public static native int meaningOfTheUniverse();

}
