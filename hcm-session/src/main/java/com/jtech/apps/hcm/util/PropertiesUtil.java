package com.jtech.apps.hcm.util;

import java.util.ResourceBundle;


public class PropertiesUtil {

    private static PropertiesUtil INSTANCE;
    private ResourceBundle bundle;

    private PropertiesUtil() {
        bundle = ResourceBundle.getBundle("application");
    }

    public static PropertiesUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PropertiesUtil();
        }
        return INSTANCE;
    }

    public String getConfig(String key) {
        return bundle.getString(key);
    }

}