package org.zk.linkman.enums;

public enum Rule {
    USER("user"),
    ADMIN("admin");

    private String value;

    Rule(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
