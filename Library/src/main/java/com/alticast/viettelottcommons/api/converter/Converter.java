package com.alticast.viettelottcommons.api.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class Converter {
    protected String src;

    public Converter(String src) {
        this.src = src;
    }

    public static <T extends Converter> T instantiate(Class<T> converterClass, String src) {
        try {
            Constructor<T> constructor = converterClass.getConstructor(String.class);
            return constructor.newInstance(src);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("NoSuchMethodException", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("InvocationTargetException", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("InstantiationException", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("IllegalAccessException", e);
        }
    }

    protected abstract Object convert();
}
