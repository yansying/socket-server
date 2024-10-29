package com.xzr.socket.utils;

import com.xzr.socket.ano.SocketUrl;
import com.xzr.socket.config.SocketConfig;
import com.xzr.socket.message.SocketMessageInfo;

import java.lang.reflect.Field;
import java.util.*;

public class AnnotationUtils {


       // 初始化
   private static final List<String> list = Arrays.asList("header", "commandIdUrl", "url");

   private final static SocketConfig socketConfig = SpringUtils.getBean(SocketConfig.class);
    // 开始查找
    public static String findSocketUrl1(Object obj) {
        Object currentObject = obj;
        LinkedHashMap<Class<?>, SocketMessageInfo> socketMessages
                = socketConfig.getSocketMessages();

        Collection<SocketMessageInfo> values = socketMessages.values();
        for (SocketMessageInfo value : values) {
            List<String> socketUrlPaths = value.getSocketUrlPaths();
            for (String fieldName : socketUrlPaths) {
                if (currentObject == null) {
                    return null; // 如果当前对象为null，返回null
                }

                try {
                    Field field = currentObject.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true); // 允许访问私有字段
                    currentObject = field.get(currentObject); // 获取当前字段的值

                    // 如果到达最后一个字段，且字段上有@SocketUrl注解，返回其值
                    if (field.isAnnotationPresent(SocketUrl.class)) {
                        return (String) currentObject; // 返回url字段的值
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                   break;
                }
            }

        }
        // 逐级查找
        return null; // 如果没有找到返回null

    }


    // 主方法，开始查找
    public static String findSocketUrl(Object obj) {

        if (obj == null) {
            return null;
        }
        Class<?> clazz = obj.getClass();
       return findSocketUrlInFields(clazz, obj);
    }

    // 查找字段及其嵌套对象
    private static String findSocketUrlInFields(Class<?> clazz, Object obj) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 排除自带的系统字段属性
            if (isUserDefinedField(field)) {
                if (field.isAnnotationPresent(SocketUrl.class)) {
                    // 如果字段上有@SocketUrl注解，获取其值
                    field.setAccessible(true);
                    try {
                        String url = (String) field.get(obj);
                        System.out.println("Found Socket URL: " + url);
                        return url;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 递归查找嵌套对象
                    try {
                        field.setAccessible(true);
                        Object nestedObject = field.get(obj);
                        if (nestedObject != null) {
                            // 递归调用，检查嵌套对象的字段
                            String nestedUrl = findSocketUrlInFields(nestedObject.getClass(), nestedObject);
                            if (nestedUrl != null) {
                                return nestedUrl; // 如果找到，返回结果
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static String findSocketUrlInFieldNames(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 排除自带的系统字段属性
            if (isUserDefinedField(field)) {
                if (field.isAnnotationPresent(SocketUrl.class)) {
                    return field.getName();// 如果字段上有@SocketUrl注解，获取其值

                } else {
                    //判断是否为自定义声名内嵌对象
                    Class<?> fieldType = field.getType();
                    if (isUserDefinedClass(fieldType)) {
                        // 递归查找嵌套对象
                        String socketUrlInFieldNames = findSocketUrlInFieldNames(fieldType);
                        if (socketUrlInFieldNames != null) {
                            return socketUrlInFieldNames; // 如果找到，返回结果
                        }
                    }

                }
            }
        }
        return null;
    }
    // 判断类是否为用户自定义类
    private static boolean isUserDefinedClass(Class<?> clazz) {
        return !clazz.getName().startsWith("java.") && !clazz.getName().startsWith("javax.");
    }

    // 判断字段是否是用户自定义的字段
    private static boolean isUserDefinedField(Field field) {
        Class<?> declaringClass = field.getDeclaringClass();
        return !declaringClass.getName().startsWith("java.") && !declaringClass.getName().startsWith("javax.");
    }
}
