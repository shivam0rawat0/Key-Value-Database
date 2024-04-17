package com.example.keydb.manager;

public class Node {
    private String name;
    private String value;
    public Node next;

    public Node(String name, String value) {
        this.name = name;
        this.value = value;
        this.next = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
