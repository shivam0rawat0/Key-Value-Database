package com.example.keydb.bridge;
import com.example.keydb.config.ConfigMap;
import com.example.keydb.manager.KVPair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

@Service
public class RaidBridge {
    private boolean isTypeActive;
    private String[] URL;
    static RestTemplate restTemplate = new RestTemplate();

    public RaidBridge() {
        setup(ConfigMap.get("db.raid.port"),ConfigMap.get("db.raid.count"));
    }
    public RaidBridge(String port,String count) {
        setup(port, count);
    }
    private void setup(String port,String count){
        try {
            if (isTypeActive = Boolean.valueOf(ConfigMap.get("db.active"))) {
                int size = Integer.parseInt(count);
                URL = new String[size];
                for (int i = 0; i < size; ++i){
                    URL[i] = "http://raid" + i + ":" + port + "/";
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                    System.out.println(currentTimestamp.toString()+" INFO [raid-bridge] " + URL[i]);
                }
            }
        } catch (Exception e){
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            System.out.println(currentTimestamp.toString()+" ERROR [raid-bridge] " + e.getStackTrace());
        }
    }

    public void post(String keyNameSpace,KVPair data){
        if(isTypeActive){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<KVPair> requestEntity = new HttpEntity<>(data, headers);
            for(String url : URL) {
                String Url = url + keyNameSpace + "/setKey";
                restTemplate.postForEntity(Url, requestEntity, String.class);
            }
        }
    }

    public void delete(String keyNameSpace,KVPair data) {
        if (isTypeActive) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<KVPair> requestEntity = new HttpEntity<>(data, headers);
            for(String url : URL) {
                String Url = url + keyNameSpace + "/deleteKey";
                restTemplate.postForEntity(Url, requestEntity, String.class);
            }
        }
    }
}
