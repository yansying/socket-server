package com.xzr.socket.domain;

/**
 * @author xzr
 */
public  class ResultMessageCh {
      public static final String AUTHENTICATION_SUCCESSFUL = "认证成功";
      public static final String AUTHENTICATION_ERROR = "认证失败，设备不存在";
      public static final String PARAMETER_IS_ILLEGAL = "参数不合法";
      public static final String SOCKET_INVALID = "Socket未初始化需认证登录";
      public static final String HEARTBEAT_DETECTION_FAILED = "心跳检测成功";
      public static final String HEARTBEAT_DETECTION_SUCCESSFUL = "心跳检测失败";
}
