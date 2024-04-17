package com.example.keydb.controller;

import com.example.keydb.bridge.RaidBridge;
import com.example.keydb.manager.KVPair;
import com.example.keydb.manager.KeyDBManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.keydb.config.ConfigMap;

@RestController
public class DbController {
    @Autowired
    private KeyDBManager keyDBManager;
    @Autowired
    private RaidBridge raidBridge;

    @CrossOrigin(origins = "*")
    @GetMapping("/{keyNameSpace}/getKeys")
    public List<String> getAllKeys(@PathVariable("keyNameSpace") String keyNameSpace){
        return keyDBManager.getAllKeys(keyNameSpace);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{keyNameSpace}/getPairs")
    public List<String[]> getKey(@PathVariable("keyNameSpace") String keyNameSpace){
        List<String[]> value = keyDBManager.getAllPairs(keyNameSpace);
        if(value!=null) {
            return value;
        }
        return null;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/{keyNameSpace}/getKey")
    public String getKey(@PathVariable("keyNameSpace") String keyNameSpace,@RequestBody KVPair data){
        String value = keyDBManager.readKey(keyNameSpace, data.getKey());
        if(value!=null) {
            return value;
        }
        return "";
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/{keyNameSpace}/setKey")
    public String setKey(@PathVariable("keyNameSpace") String keyNameSpace,@RequestBody KVPair data){
        keyDBManager.writeKey(keyNameSpace,data.getKey(),data.getValue());
        raidBridge.post(keyNameSpace,data);
        return "200";
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{keyNameSpace}/deleteKey")
    public String deleteKey(@PathVariable("keyNameSpace") String keyNameSpace,@RequestBody KVPair data){
        keyDBManager.deleteKey(keyNameSpace,data.getKey());
        raidBridge.delete(keyNameSpace,data);
        return "200";
    }
}
