package com.aizuda.boot.modules.system.entity.enums;

import java.util.Arrays;
import java.util.Objects;

/**
 * 资源类型枚举类
 *
 * @author 青苗
 * @since 1.0.0
 */
public enum ResourceType {
    menu(0),
    iframe(1),
    link(2),
    button(3);

    private final Integer type;

    ResourceType(final Integer type) {
        this.type = type;
    }

    /**
     * 资源类型数值转换为字符串类型
     *
     * @param type 资源类型
     * o@return
     */
    public static String convert(final Integer type) {
        return Arrays.stream(ResourceType.values()).filter(t -> Objects.equals(t.type, type))
                .findFirst().orElse(menu).name();
    }
}
