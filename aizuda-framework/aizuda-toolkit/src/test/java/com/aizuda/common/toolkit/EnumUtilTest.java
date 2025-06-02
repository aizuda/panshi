package com.aizuda.common.toolkit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedHashMap;

public class EnumUtilTest {

    public static void main(String[] args) {
        testGetEnumMap();
        testGet(1);
        testToString();
    }

    static void testGetEnumMap(){
        LinkedHashMap<String, TestEnum> enumMap = EnumUtils.getEnumMap(TestEnum.class);
        enumMap.forEach((k,v)->{
            System.out.println("key="+k);
            System.out.println("value="+v);
        });

        LinkedHashMap<String, TestEnum> cacheEnumMap = EnumUtils.getCacheEnumMap(TestEnum.class, x -> x.value);
        cacheEnumMap.forEach((k,v)->{
            System.out.println("key="+k);
            System.out.println("value="+v);
        });
    }

    static void testGet(Integer key){
        TestEnum testEnum = EnumUtils.get(TestEnum.class, key, x -> x.getKey());
        System.out.println("get:"+testEnum);
        TestEnum testEnum1 = EnumUtils.getByCache(TestEnum.class, key, x -> x.getKey());
        System.out.println("getByKey:"+testEnum1);

        System.out.println("keyOf:"+TestEnum.keyOf(1));
    }


    static void testToString(){
        System.out.println("toString:"+EnumUtils.toString(TestEnum.test1));
        System.out.println("toString:"+EnumUtils.toString(TestEnum.test1,x->x.getValue()));
    }
    @AllArgsConstructor
    @Getter
    @ToString
    enum TestEnum{
        test1(1,"2"),
        test2(3,"4"),
        ;
        private Integer key;

        private String value;


        public static TestEnum keyOf(Integer key){
            return EnumUtils.getByCache(TestEnum.class,key, x->x.getKey());
        }
    }
}
