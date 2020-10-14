package com.ratel.enums;


/**
 * 接口返回值枚举
 * <p>
 * Created by admin on 2016/1/23.
 */
public enum ReturnCodeEnum {
  CODE_1000(1000, MessageCodeConstant.MESSAGE_COMMON_SUCCESS),// 数据处理成功
  CODE_1001(1001, MessageCodeConstant.MESSAGE_SECURITY_UNAUTHORIZED),// 系统应用间对签名或TOKEN验证不通过
  CODE_1002(1002, MessageCodeConstant.MESSAGE_COMMON_INFORMATION_NONEXISTENCE),// 数据信息不存在
  CODE_1003(1003, MessageCodeConstant.MESSAGE_PAYMENT_TYPE_ERROR),// 数据错误，数据校验不通过，数据不符合接口规则
  CODE_1004(1004, MessageCodeConstant.MESSAGE_COMMON_SERVER_ERROR),// 异常
  CODE_1005(1005, MessageCodeConstant.MESSAGE_COMMON_FAILED),// 数据处理失败，如：保存、发送
  CODE_1006(1006, MessageCodeConstant.MESSAGE_COMMON_PARAMETERS_MISSING),// 用户输入或接口入参缺少
  CODE_1007(1007, MessageCodeConstant.MESSAGE_COMMON_TIMEOUT),// 系统应用间通讯超时
  CODE_1008(1008, MessageCodeConstant.MESSAGE_COMMON_INFORMATION_ALREADYEXISTS),//数据重复
  CODE_1009(1009, MessageCodeConstant.MESSAGE_COMMON_SESSION_EXPIRED),// 客户端TOKEN验证不通过或用户登录账户相关问题
  CODE_1010(1010, MessageCodeConstant.MESSAGE_SECURITY_AUTHFAILED),//短信或者邮箱验证失败
  CODE_1011(1011, MessageCodeConstant.MESSAGE_APP_SIGN_AUTHFAILED),//签名验证失败
  CODE_1012(1012, MessageCodeConstant.MESSAGE_APP_SIGN_EXPIRED);//签名过期

  /**
   * 业务编号
   */
  private Integer code;

  /**
   * 业务值
   */
  private String value;

  private ReturnCodeEnum(Integer code, String value) {
    this.code = code;
    this.value = value;
  }

  public final Integer getCode() {
    return code;
  }

  public final String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return this.code + "=" + this.value;
  }
}
