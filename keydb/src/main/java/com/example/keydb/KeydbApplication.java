package com.example.keydb;

import com.example.keydb.config.ConfigMap;
import com.example.keydb.config.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileWriter;
import java.lang.management.ManagementFactory;

@SpringBootApplication
public class KeydbApplication {
	public static void main(String[] args) {
		ConfigMap.readConfig(args[0]);
		String processName = ManagementFactory.getRuntimeMXBean().getName();
		long pid = Long.parseLong(processName.split("@")[0]);
		Logger.getLogger().rlog(" *************** [ db start : pid={"+pid+"} ] *************** ");
		System.out.println("PID: " + pid);
		try {
			FileWriter writer = new FileWriter(ConfigMap.get("db.pid"));
			writer.write(""+pid);
			writer.close();
		} catch (Exception e) {
			System.err.println("Error writing pid: " + e.getMessage());
		}
		SpringApplication.run(KeydbApplication.class, args);
	}
}