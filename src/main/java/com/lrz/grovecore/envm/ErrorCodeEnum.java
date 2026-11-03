package com.lrz.grovecore.envm;

import com.lrz.grovecore.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lirenzhi
 * @since 2023/2/1 5:19 下午
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum implements IEnum {

    REQUEST_SUCCESS("200", "success"),
    REQUEST_ERROR("500", "fail"),
    UNKNOWN("0000", "发生了未知错误，请稍后重试或联系客服"),
    WRONG_ARG("0001", "参数不合法"),
    NO_SUCH_RESOURCE("0002", "资源不存在"),
    WRONG_METHOD("0003", "请求资源不支持此方法"),
    TOO_MANY_REQUEST("0004", "请求过多"),
    SYNC("0005", "请勿重复提交"),
    ILLEGAL_REQUEST_URL("0006", "非法的请求地址：{}"),

    //========================转换异常==========================
    JSON_CONVERT_ERROR("0101", "JSON转化错误"),
    TYPE_CONVERT_ERROR("0102", "对象类型转换出错"),

    //=======================认证===============================
    ACCOUNT_LOCK("0201", "账户锁定"),
    NEED_LOGIN("0202", "需要登录"),
    UNAUTHORIZED("0203", "签名认证失败"),
    SIGN_TIME_OUT("0204", "签名已过期"),
    PASSWORD_ERROR("0205", "密码错误"),
    ACCESS_TOKEN_NOT_EXIST("0206", "ACCESS_TOKEN不存在"),
    ACCESS_TOKEN_TIME_OUT("0207", "ACCESS_TOKEN已过期"),
    ACCESS_TOKEN_CHANGED("0208", "ACCESS_TOKEN被篡改"),
    MULTI_TENANT_IS_NULL("0209", "租户信息不存在或已禁用!"),
    MULTI_TENANT_NOT_FOUND("0210", "未加载到任何租户信息!"),
    MULTI_TENANT_ID_IS_NULL("0211", "租户ID为空!"),
    MEMBER_CENTER_NEED_AUTH_MOBILE("0212", "未授权手机号"),

    //========================业务异常==============================
    MIGRATION_ERROR("10001", "执行Migration操作失败"),
    CORE_DEPARTMENT_EXIST_SUB_DEP("10002", "存在子部门，不能删除"),
    MEMBER_CENTER_NEED_AUTH_BASE_INFO("10003", "未授权基础信息"),
    SPLITTING_PARAMETER_EXCEPTION("10004", "列表分割参数异常"),
    DATE_FORMAT_EXCEPTION("10005", "日期格式化错误，传入：");

    private final String code;

    private final String message;

    public String formatMessage(Object... params) {
        return StringUtil.format(this.message, params);
    }
}
