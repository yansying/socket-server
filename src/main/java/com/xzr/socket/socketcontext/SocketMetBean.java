package com.xzr.socket.socketcontext;

import lombok.Data;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

@Data
public class SocketMetBean implements Serializable {
    private int heartbeatCount; // 心跳检测计数

    private PrintWriter clientWriters ;
    private BufferedReader clientReaders;
    private Socket socket;

    public SocketMetBean(Socket socket, PrintWriter clientWriters, BufferedReader clientReaders) {
        this.socket = socket;
        this.clientWriters = clientWriters;
        this.clientReaders = clientReaders;
        this.heartbeatCount = 0;
    }

    // 增加心跳计数
    public void incrementHeartbeatCount() {
        this.heartbeatCount++;
    }

    // 重置心跳计数
    public void resetHeartbeatCount() {
        this.heartbeatCount = 0;
    }

}
