package com.example.keydb.manager;

import com.example.keydb.config.ConfigMap;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeyDBManager {
    Map<String,RecordManager> records;
    private String rootDirectory;

    public KeyDBManager(){
        this.rootDirectory = ConfigMap.get("db.path");
        setup(ConfigMap.get("db.name"));
    }
    public KeyDBManager(String rootDirectory,String nameSpace) {
        this.rootDirectory = rootDirectory;
        setup(nameSpace);
    }

    private void setup(String nameSpace){
        records = new HashMap<>();
        String recordIDs[] = nameSpace.split(",");
        for(int i=0;i<recordIDs.length;++i){
            String rcId = rootDirectory + recordIDs[i];
            if(!records.containsKey(rcId))
                records.put(rcId,new RecordManager(rootDirectory,recordIDs[i]));
        }
    }

    public String readKey(String nameSpace,String key){
        String rcId = rootDirectory + nameSpace;
        if(!records.containsKey(rcId))
            records.put(rcId,new RecordManager(rootDirectory,nameSpace));
        return records.get(rcId).readKey(key);
    }

    public synchronized void writeKey(String nameSpace, String key, String value) {
        String rcId = rootDirectory + nameSpace;
        if(!records.containsKey(rcId))
            records.put(rcId,new RecordManager(rootDirectory,nameSpace));
        records.get(rcId).writeKey(key, value);
    }

    public List<String> getAllKeys(String nameSpace) {
        String rcId = rootDirectory + nameSpace;
        if(!records.containsKey(rcId))
            records.put(rcId,new RecordManager(rootDirectory,nameSpace));
        return records.get(rcId).getAllKeys();
    }

    public List<String[]> getAllPairs(String nameSpace) {
        String rcId = rootDirectory + nameSpace;
        if(!records.containsKey(rcId))
            records.put(rcId,new RecordManager(rootDirectory,nameSpace));
        return records.get(rcId).getAllPairs();
    }

    public synchronized void deleteKey(String nameSpace, String key) {
        String rcId = rootDirectory + nameSpace;
        if(!records.containsKey(rcId))
            records.put(rcId,new RecordManager(rootDirectory,nameSpace));
        records.get(rcId).deleteKey(key);
    }
}
