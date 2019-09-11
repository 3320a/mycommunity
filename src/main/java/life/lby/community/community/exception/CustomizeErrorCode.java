package life.lby.community.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001 ,"这个话题已经不存在了"),
    TARGET_PARAM_NOT_FOUND(2002 ,"未选择话题"),
    NO_LOGIN(2003 ,"未登录"),
    SYSTEM_ERROR(2004 ,"系统错误"),
    TYPE_PARAM_WRONG(2005 ,"回复类型错误或不存在"),
    COMMENT_NOT_FOUND(2006 ,"这个回复已经不存在了"),
    COMMENT_IS_EMPTY(2007 ,"回复内容不可为空"),
    ;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
