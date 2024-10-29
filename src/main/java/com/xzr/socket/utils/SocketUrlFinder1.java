package com.xzr.socket.utils;

import com.xzr.socket.ano.SocketUrl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SocketUrlFinder1 {

    // 存储字段及其所在类的关系
    public static class FieldRelation {
        private String fieldName;
        private Class<?> declaringClass;

        public FieldRelation(String fieldName, Class<?> declaringClass) {
            this.fieldName = fieldName;
            this.declaringClass = declaringClass;
        }

        // Getter 和 toString 方法
        public String getFieldName() {
            return fieldName;
        }

        public Class<?> getDeclaringClass() {
            return declaringClass;
        }

        @Override
        public String toString() {
            return "FieldRelation{" +
                    "fieldName='" + fieldName + '\'' +
                    ", declaringClass=" + declaringClass.getName() +
                    '}';
        }
    }

    public static List<FieldRelation> findSocketUrlInFieldNames(Class<?> clazz) {
        List<FieldRelation> fieldRelations = new ArrayList<>();
        findSocketUrlInFieldNames(clazz, fieldRelations);
        return fieldRelations; // 返回收集到的关系
    }

    private static void findSocketUrlInFieldNames(Class<?> clazz, List<FieldRelation> fieldRelations) {
        Field[] fields = clazz.getDeclaredFields();
        Class<?> lastFieldType = null;
        String lastFieldName = null;
        for (Field field : fields) {
            // 排除自带的系统字段属性
            if (isUserDefinedField(field)) {
                // 检查字段上是否有@SocketUrl注解
                if (field.isAnnotationPresent(SocketUrl.class)) {
                    fieldRelations.add(new FieldRelation(field.getName(), clazz)); // 收集关系
                } else {
                    // 判断字段是否为自定义声明的内嵌对象
                    Class<?> fieldType = field.getType();
                    if (isUserDefinedClass(fieldType)) {
                        lastFieldType = fieldType;
                        lastFieldName = field.getName();
                        // 递归查找嵌套对象，并传递当前类的关系
                        findSocketUrlInFieldNames(fieldType, fieldRelations);
                    }
                }
            }
        }
    }

    // 判断字段是否是用户自定义的字段
    private static boolean isUserDefinedField(Field field) {
        Class<?> declaringClass = field.getDeclaringClass();
        return !declaringClass.getName().startsWith("java.") && !declaringClass.getName().startsWith("javax.");
    }

    // 判断类是否为用户自定义类
    private static boolean isUserDefinedClass(Class<?> clazz) {
        return !clazz.getName().startsWith("java.") && !clazz.getName().startsWith("javax.");
    }
}
