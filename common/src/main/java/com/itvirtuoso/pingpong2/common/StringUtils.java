package com.itvirtuoso.pingpong2.common;

import java.util.List;

/**
 * Created by kenji on 15/02/15.
 */
public class StringUtils {
    public static String join(List<?> values, String separator) {
        boolean isFirst = true;
        StringBuilder builder = new StringBuilder();
        for(Object value : values) {
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append(separator);
            }
            builder.append(value.toString());
        }
        return builder.toString();
    }
}
