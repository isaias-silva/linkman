package org.zk.linkman.tools;

import java.util.function.Consumer;


public class ValuesUtils {

    public static <T> void setIf(T value, boolean conditional, Consumer<T> set){
    if(conditional) set.accept(value);
    }
}
