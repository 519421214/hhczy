package com.king.hhczy.common.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ningjinxiang
 * 反射类
 */
public class MethodUtil {
    public static Method getTargetMethod(Class clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods(); //获取所有方法，包含private
        if (methods != null && methods.length > 0) {
            String regEx = "^" + methodName + "$";//获取所要查找到的方法名称要匹配此正则
            Pattern pattern = Pattern.compile(regEx);
            for (Method method : methods) {
                Matcher matcher = pattern.matcher(method.getName());
                boolean rs = matcher.find();
                if (rs) {
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 静态可用
     * @param clazz
     * @param methodName
     * @param xmlData
     * @return
     */
    public static Object executeTargrtStaticMethod(Class clazz, String methodName, Object... xmlData) {
        System.out.println("反射入参：" + Arrays.toString(xmlData));
        Object obj = null;
        try {
            Method method = getTargetMethod(clazz, methodName);
            if (xmlData != null) {
                int length = xmlData.length;
                switch (length) {
                    case 0:
                        obj = method.invoke(clazz);
                        break;
                    case 1:
                        obj = method.invoke(clazz, xmlData[0]);
                        break;
                    case 2:
                        obj = method.invoke(clazz, xmlData[0], xmlData[1]);
                        break;
                    case 3:
                        obj = method.invoke(clazz, xmlData[0], xmlData[1], xmlData[2]);
                        break;
                    default:
                        System.out.println("找不到反射方法，入参数：" + length);
                        break;
                }
            } else {
                System.out.println("不接受入参为null");
            }
        } catch (Exception e) {

        }
        return obj;
    }

    /**
     * 非静态可用
     * @param cla
     * @param methodName
     * @param xmlData
     * @return
     */
    public static Object executeTargrtMethod(Class cla, String methodName, Object... xmlData) {
        Object obj = null;
        try {
            Class clazz = Class.forName(cla.getName());
            Constructor[] constructors = clazz.getDeclaredConstructors();
            AccessibleObject.setAccessible(constructors, true);
            for (Constructor con : constructors) {
                if (con.isAccessible()) {
                    Object classObject = con.newInstance();
                    Class[] classes = new Class[xmlData.length];
                    for (int i = 0; i < xmlData.length; i++) {
                        classes[i] = xmlData[i].getClass();
                    }
                    Method method = clazz.getMethod(methodName, classes);
                    if (xmlData != null) {
                        int length = xmlData.length;
                        switch (length) {
                            case 0:
                                obj = method.invoke(classObject);
                                break;
                            case 1:
                                obj = method.invoke(classObject, xmlData[0]);
                                break;
                            case 2:
                                obj = method.invoke(classObject, xmlData[0], xmlData[1]);
                                break;
                            case 3:
                                obj = method.invoke(classObject, xmlData[0], xmlData[1], xmlData[2]);
                                break;
                            default:
                                System.out.println("找不到反射方法，入参数：" + length);
                                break;
                        }
                    } else {
                        System.out.println("不接受入参为null");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
