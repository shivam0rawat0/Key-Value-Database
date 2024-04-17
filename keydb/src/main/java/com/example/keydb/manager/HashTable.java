package com.example.keydb.manager;

public class HashTable {
    private int size;
    private Node[] table;
    private int[] indexSize;

    public HashTable(int size) {
        this.size = size;
        this.table = new Node[size];
        this.indexSize = new int[size];
    }

    public int[] setValue(String key,String value){
        int hcode = 0;
        int entryIndex = 0;
        for(int i=0;i<key.length();++i)
            hcode += key.charAt(i);
        hcode %= size;
        Node temp = new Node(key,value);;
        if(table[hcode]!=null){
            Node prev = null;
            Node trav = table[hcode];
            do{
                if(trav.getName().equals(key)){
                    trav.setValue(value);
                    return new int[]{hcode,entryIndex};
                }
                ++entryIndex;
                prev = trav;
                trav = trav.next;
            }while(trav!=null);
            prev.next = temp;
        } else {
            table[hcode] = temp;
        }
        indexSize[hcode] = entryIndex;
        return new int[]{hcode,entryIndex};
    }

    public int[] getKeyEntry(String key){
        int hcode = 0;
        int entryIndex = 0;
        for(int i=0;i<key.length();++i)
            hcode += key.charAt(i);
        hcode %= size;
        if(table[hcode]!=null) {
            Node prev = null;
            Node trav = table[hcode];
            do {
                if (trav.getName().equals(key))
                    return new int[]{hcode, entryIndex};
                ++entryIndex;
                prev = trav;
                trav = trav.next;
            } while (trav != null);
        }
        return null;
    }

    public String getValue(String key){
        int hcode = 0;
        for(int i=0;i<key.length();++i)
            hcode += key.charAt(i);
        hcode %= size;
        if(table[hcode]!=null){
            Node trav = table[hcode];
            do{
                if(trav.getName().equals(key)){
                    return trav.getValue();
                }
                trav = trav.next;
            }while(trav!=null);
        }
        return null;
    }

    public void deleteKeyEntry(int[] entry){
        int index = entry[0];
        int entryIndex = entry[1];
        if(entryIndex==0){
            Node trav = table[index];
            if(trav.next==null){
                table[index].next =null;
            } else {
                while (trav.next.next != null) {
                    trav = trav.next;
                }
                Node nHead = trav.next;
                trav.next = null;
                nHead.next = table[index].next;
                table[index] = nHead;
            }
        } else if(entryIndex==indexSize[index]){
            Node lastHead = table[index];
            while (--entryIndex>0){
                lastHead = lastHead.next;
            }
            lastHead.next = null;
        } else {
            int currIndex = entryIndex;
            Node currHead = table[index];
            while (--entryIndex>0){
                currHead = currHead.next;
            }
            Node lastHead = currHead;
            int lastIndex = indexSize[index] - currIndex;
            while (--lastIndex>=0){
                lastHead = lastHead.next;
            }
            if(currIndex+1==indexSize[index]){
                currHead.next = lastHead.next;
                lastHead.next = null;
            } else {
                lastHead.next.next = currHead.next.next;
                currHead.next = lastHead.next;
                lastHead.next = null;
            }
        }
        --indexSize[index];
    }

    public Node getIndex(int index){
        if(index<table.length) return table[index];
        return null;
    }

    public int getIndexSize(int index){
        if(index<table.length) return indexSize[index];
        return -1;
    }
}
