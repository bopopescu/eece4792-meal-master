package com.app.eece4792mealmaster.utils;

import com.app.eece4792mealmaster.constants.Consts;

import java.lang.reflect.Method;

public final class Utils {
    private Utils() {}

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Helper method to copy over modifiable fields from a payload into the database.
     *
     * @param source The original data
     * @param newData The new data to copy over
     */
    public static void updateModel(Object source, Object newData) {
        if (source.getClass().equals(newData.getClass())) {
            Class model = source.getClass();
            String className = model.getSimpleName();
            String[] patchableFields = Consts.PATCHABLE_FIELDS.get(className);
            Method getMethod;
            Method setMethod;
            try {
                for (String field : patchableFields) {
                    String capitalizedField = capitalize(field);
                    getMethod = model.getMethod("get" + capitalizedField);
                    setMethod = model.getMethod("set" + capitalizedField, getMethod.getReturnType());
                    Object value = getMethod.invoke(newData);
                    setMethod.invoke(source, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
