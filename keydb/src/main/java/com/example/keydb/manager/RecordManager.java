package com.example.keydb.manager;

import com.example.keydb.config.Logger;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RecordManager {
    static int ENTRY_BUFFER = 1024;
    static int HASH_SIZE = 10;
    static String SEP = "=>";
    static String END = ";";
    private String rootDirectory;
    private String nameSpace;
    private RandomAccessFile rdFile;
    private HashTable hashTable;
    private Logger logger;

    public RecordManager(String rootDirectory,String nameSpace) {
        logger = Logger.getLogger();
        this.rootDirectory = rootDirectory;
        this.hashTable = new HashTable(HASH_SIZE);
        this.nameSpace = nameSpace;
        Path path = Paths.get(rootDirectory + nameSpace);
        if (Files.notExists(path)) {
            File f1 = new File(path.toUri());
            f1.mkdir();
            makeNameSpace();
        }
        else loadDB();
    }

    private void makeNameSpace() {
        try {
            for(int index=0;index<HASH_SIZE;++index) {
                File file = new File(rootDirectory + nameSpace + "\\" + index);
                file.createNewFile();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String getSpacer(int i) {
        String filler = "";
        while(--i>0)
            filler += " ";
        return filler;
    }

    public boolean loadDB() {
        long START = System.currentTimeMillis();
        for (int i = 0; i < HASH_SIZE; ++i)
            readIndex(i);
        long END = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(currentTimestamp.toString()+"INFO DataBase ["+nameSpace+"] loaded in "+ (END-START)+" ms");
        return false;
    }

    private synchronized boolean readIndex(int index) {
        try {
            this.rdFile = new RandomAccessFile(rootDirectory + nameSpace + "\\" +index, "r");
            FileChannel channel = rdFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(ENTRY_BUFFER);
            int bytesRead;
            while ((bytesRead = channel.read(buffer)) != -1) {
                buffer.flip();
                String entryPair[] = StandardCharsets.UTF_8.decode(buffer).toString().split(SEP);
                if(entryPair.length>1){
                    String value = entryPair[1].split(END)[0];
                    hashTable.setValue(entryPair[0],value);
                }
                buffer.clear();
            }
            rdFile.close();
            return true;
        } catch (Exception e) {
            System.out.println("[read-index error] db="+nameSpace+", index="+index+"]");
            logger.rlog("[read-index error] db={"+nameSpace+"}, index={"+index+"}"+e.getMessage());
            return false;
        }
    }

    private synchronized String readIndexOffset(int index, int offset) {
        try {
            this.rdFile = new RandomAccessFile(rootDirectory + nameSpace + "\\" +index, "r");
            FileChannel channel = rdFile.getChannel();
            channel.position(offset*ENTRY_BUFFER);
            ByteBuffer buffer = ByteBuffer.allocate(ENTRY_BUFFER);
            int bytesRead = channel.read(buffer);
            String entryPair = null;
            if(bytesRead==ENTRY_BUFFER){
                buffer.flip();
                entryPair = StandardCharsets.UTF_8.decode(buffer).toString();
                buffer.clear();
            }
            rdFile.close();
            return entryPair;
        } catch (Exception e) {
            System.out.println("[read-index-offset][error][ db={"+nameSpace+"}, index={"+index+"}, offset={"+offset+"}");
            logger.rlog("[read-index-offset][error] db={"+nameSpace+"}, index={"+index+"}, offset={"+offset+"}"+e.getMessage());
            return null;
        }
    }

    private synchronized void writeIndexOffset(int index,int offset,String data){
        try {
            this.rdFile = new RandomAccessFile(rootDirectory + nameSpace + "\\" + index, "rw");
            FileChannel channel = rdFile.getChannel();
            channel.position(offset*ENTRY_BUFFER);
            rdFile.write(data.getBytes());
            rdFile.close();
            logger.wlog("[write-index-offset] db={"+nameSpace+"}, index={"+index+"}, offset={"+offset+"}");
        } catch (Exception e) {
            logger.wlog("[write-index-offset][error] db={"+nameSpace+"}, index={"+index+"}, offset={"+offset+"}");
            System.out.println(e.getMessage());
        }
    }

    public String readKey(String key){
        logger.rlog("[read] db={"+nameSpace+"}, key={"+key+"}");
        return hashTable.getValue(key);
    }

    public synchronized void writeKey(String key, String value) {
        int entrySize = key.length() + value.length() + 1;
        int entryOffset[] = hashTable.setValue(key, value);
        String entryData = key+ SEP +value+ END + getSpacer(ENTRY_BUFFER - entrySize);
        writeIndexOffset(entryOffset[0],entryOffset[1],entryData);
    }

    public List<String> getAllKeys() {
        List<String> res = new ArrayList<>();
        for(int i=0;i<HASH_SIZE;++i){
            Node trav = hashTable.getIndex(i);
            while(trav!=null){
                if(!hashTable.getValue(trav.getName()).equals(""))
                    res.add(trav.getName());
                trav=trav.next;
            }
        }
        return res;
    }

    public List<String[]> getAllPairs() {
        List<String[]> res = new ArrayList<>();
        for(int i=0;i<HASH_SIZE;++i){
            Node trav = hashTable.getIndex(i);
            String key = trav.getName();
            String value = hashTable.getValue(trav.getName());
            while(trav!=null){
                if(!value.equals(""))
                    res.add(new String[]{key,value});
                trav=trav.next;
            }
        }
        return res;
    }

    private synchronized void deleteIndexOffset(int [] entry){
        logger.wlog("[delete-index-offset] db={"+nameSpace+"}, index={"+entry[0]+"}, offset={"+entry[1]+"}");
        int lastOffset = hashTable.getIndexSize(entry[0]);
        if(lastOffset==entry[1]){
            writeIndexOffset(entry[0],entry[1],getSpacer(ENTRY_BUFFER));
        } else {
            String lastEntry = readIndexOffset(entry[0],lastOffset);
            writeIndexOffset(entry[0],lastOffset,getSpacer(ENTRY_BUFFER));
            writeIndexOffset(entry[0],entry[1],lastEntry);
        }
    }

    public synchronized void deleteKey(String key) {
        int [] entry = hashTable.getKeyEntry(key);
        if(entry!=null) {
            logger.wlog(" ******************** start procedure ******************** ");
            logger.wlog("[delete] db={"+nameSpace+"}, key={"+key+"}");
            deleteIndexOffset(entry);
            hashTable.deleteKeyEntry(entry);
            logger.wlog(" ******************** end procedure ******************** ");
        }
    }
}
