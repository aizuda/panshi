package com.aizuda.boot.modules.gen.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenZipContext {
    private String name;
    private String context;

    public static GenZipContext of(String name, String context) {
        GenZipContext gzc = new GenZipContext();
        gzc.setName(name);
        gzc.setContext(context);
        return gzc;
    }
}
