package com.example.keydb.config;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger logger = null;
    private String directory;
    private boolean isReadLogEnabled;
    private boolean isWriteLogEnabled;
    private DateTimeFormatter dtf;
    public static Logger getLogger(){
        if(logger==null)
            logger = new Logger(ConfigMap.get("db.log.dir"), ConfigMap.get("db.log.type"));
        return logger;
    }

    private Logger(String directory,String type){
        System.out.println("Logger create @[ dir="+directory+", type="+type+" ]");
        dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SS");
        isReadLogEnabled = false;
        isWriteLogEnabled = false;
        this.directory = directory;
        switch(type) {
            case "read":
                isReadLogEnabled = true;
                break;
            case "write":
                isWriteLogEnabled = true;
                break;
            case "read+write":
            case "write+read":
                isReadLogEnabled = true;
                isWriteLogEnabled = true;
                break;
        }
    }

    public void rlog(String log){
        if(isReadLogEnabled) writeLog(log);
    }

    public void wlog(String log){
        if(isWriteLogEnabled) writeLog(log);
    }

    private void writeLog(String log){
        try{
            FileWriter fw=new FileWriter(directory,true);
            fw.write("["+dtf.format(LocalDateTime.now())+"] "+log+"\n");
            fw.close();
        }catch(Exception e){
            System.out.println("Logger ["+directory+"][error] "+e);
        }
    }
}
