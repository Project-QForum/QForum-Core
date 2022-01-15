package cn.jackuxl.qforum.constants.e;


public enum Code {
    ERROR(500),
    ILLEGAL_PARAMETER(400),
    NOT_LOGIN(403),
    SUCCESS(200);
    int code;
    public int value() {
        return code;
    }
    private Code(int code){
        this.code=code;
    }
}
