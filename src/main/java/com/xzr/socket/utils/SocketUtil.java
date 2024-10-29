package com.xzr.socket.utils;


import com.xzr.socket.socketcontext.SocketServer;
import lombok.extern.slf4j.Slf4j;

import java.net.Socket;

/**
 * @author xzr
 * Socket 工具类
 */
@Slf4j
public final  class SocketUtil {

    private static final SocketServer socketServer = SpringUtils.getBean(SocketServer.class);

    private SocketUtil() {}


//
//
//
//    public static void clearSocketCache(String sessionId) {
//        try {
//            socketServer.getClientWriters().remove(sessionId);
//            socketServer.getClientReaders().remove(sessionId);
//            socketServer.getHeartbeatCountMap().remove(sessionId);
//        } catch (Exception e) {
//            log.error("清除:{}缓存失败",sessionId);
//            throw new RuntimeException("清除缓存失败");
//        }
//    }
    public  static  String getSocketIPPort(Socket socket){
        String hostAddress = socket.getInetAddress().getHostAddress();
        int port = socket.getPort();
        return hostAddress + ":" + port;
    }
//
//    // 关闭客户端
//    public static void close(Socket socket, String sessionId) {
//
//        try {
//            socket.close();
//        } catch (IOException e) {
//            log.error("关闭客户端:{}失败",sessionId);
//            throw new RuntimeException("关闭客户端");
//        }
//        clearSocketCache(sessionId);
//
//    }
//    public static void close(Socket socket) {
//        try {
//            socket.close();
//        } catch (IOException e) {
//            throw new RuntimeException("关闭客户端失败");
//        }
//    }
//
//    public static String sendMessageAndAwaitResponse(String clientId, String message) {
//        try {
//
//            SocketServer socketServerBean = SpringUtils.getBean(SocketServer.class);
//            PrintWriter writer = socketServerBean.getClientWriters().get(clientId);
//            BufferedReader reader =socketServerBean.getClientReaders().get(clientId);
//            if (writer == null || reader == null) {
//                throw new RuntimeException("客户端未连接");
//            }
//
//            writer.println(message);
//            writer.flush();
//            // 等待客户端响应
//            return reader.readLine();
//        } catch (IOException e) {
//            log.error("发送消息或接收响应时出错: {}", e.getMessage());
//            throw new RuntimeException("发送消息或接收响应时出错: " + e.getMessage());
//        }
//    }
//    /**
//     * 清空所有socket
//     */
//    public static void clearAllSocketCache() {
//        SocketServer socketServerBean = SpringUtils.getBean(SocketServer.class);
//        socketServerBean.getClientWriters().clear();
//        socketServerBean.getClientReaders().clear();
//        socketServerBean.getHeartbeatCountMap().clear();
//    }
}
