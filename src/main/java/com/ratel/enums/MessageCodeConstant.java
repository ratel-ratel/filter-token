package com.ratel.enums;

import java.io.Serializable;

/**
 * Created by kellen on 2017/6/20.
 * 国际化message基础常量
 */
public class MessageCodeConstant implements Serializable {

  /**
   * 成功
   */
  public static final String MESSAGE_COMMON_SUCCESS = "成功";

  /**
   * 失败
   */
  public static final String MESSAGE_COMMON_FAILED = "无权限访问";

  /**
   * 信息不存在
   */
  public static final String MESSAGE_COMMON_INFORMATION_NONEXISTENCE = "信息不存在";

  /**
   * 支付方式错误
   */
  public static final String MESSAGE_PAYMENT_TYPE_ERROR = "支付方式错误";

  /**
   * 服务器处理异常
   */
  public static final String MESSAGE_COMMON_SERVER_ERROR = "服务器处理异常";

  /**
   * 参数不全
   */
  public static final String MESSAGE_COMMON_PARAMETERS_MISSING = "参数不全";

  /**
   * 请求超时
   */
  public static final String MESSAGE_COMMON_TIMEOUT = "请求超时";

  /**
   * 信息已存在
   */
  public static final String MESSAGE_COMMON_INFORMATION_ALREADYEXISTS = "信息已存在";

  /**
   * 登录状态失效
   */
  public static final String MESSAGE_COMMON_SESSION_EXPIRED = "登录状态已失效, 请重新登录";

  /**
   * 验证失败
   */
  public static final String MESSAGE_SECURITY_AUTHFAILED = "身份验证失败";

  /**
   * 无权限访问
   */
  public static final String MESSAGE_SECURITY_UNAUTHORIZED = "无权限访问";

  /**
   * 插入失败
   */
  public static final String MESSAGE_COMMON_INSERTFAILD = "插入失败";

  /**
   * 删除失败
   */
  public static final String MESSAGE_COMMON_DELETEFAILD = "删除失败";



  /**
   * 签名验证失败
   */
  public static final String MESSAGE_APP_SIGN_AUTHFAILED = "签名验证失败";


  /**
   * 签名过期
   */
  public static final String MESSAGE_APP_SIGN_EXPIRED = "签名过期";


}
