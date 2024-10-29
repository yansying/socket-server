package com.xzr.socket.utils;

import cn.hutool.json.JSONUtil;

import java.io.PrintWriter;

public final class WriterUtil {
    private WriterUtil() {}

    /**
     * 写
     * @param writer
     * @param data
     */
    public static void write2(PrintWriter writer, Object data) {
        String jsonStr = JSONUtil.toJsonStr(data);
        writer.write(jsonStr);
        writer.flush();
    }

    public static void write(PrintWriter writer, String data) {
        writer.println(data);
//        writer.write(data);
//        writer.flush();
    }
}
