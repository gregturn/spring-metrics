package org.springframework.metrics;

import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;

public class FieldMetric implements Metric {

    private Object object;
    private Field field;

    public FieldMetric(Object object, Field field) {
        this.object = object;
        this.field = field;
    }

    public Object read() {
        field.setAccessible(true);
        Object readValue = null;
        try {
            readValue = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return readValue;
    }

    @Override
    public String getName() {
        String name = "";
        if (ClassUtils.isCglibProxy(object)) {
            name += ClassUtils.getUserClass(object).getName();
        } else {
            name += object.getClass().getName();
        }

        name += "::" + field.getName();

        return name;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

}
