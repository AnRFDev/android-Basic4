package com.rustfisher.basic4.annotation;

import android.app.Activity;

import java.lang.reflect.Field;

/**
 * 注解帮助类
 * Created by Rust on 2018/7/6.
 */
public class FindViewHelper {
    public static void initFindView(Activity activity) {
        Class<Activity> clz = (Class<Activity>) activity.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(FindView.class)) {
                FindView inject = field.getAnnotation(FindView.class);
                int id = inject.idValue();
                if (0 != id) {
                    field.setAccessible(true);
                    try {
                        field.set(activity, activity.findViewById(id));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
