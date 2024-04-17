package com.example.keydb.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class ConfigMap {
    private static HashMap<String,String> configMap;
    public static void readConfig(String path) {
        configMap = new HashMap<>();
        File file = new File(path);
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String[] pair = sc.nextLine().split("=");
                configMap.put(pair[0], pair[1]);
                //System.out.println(pair[0] + ":" + pair[1]);
            }
        } catch (Exception e) {
        }
    }

    public static String get(String key){
        return configMap.get(key);
    }
}
