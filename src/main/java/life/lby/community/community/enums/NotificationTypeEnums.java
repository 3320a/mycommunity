package life.lby.community.community.enums;

public enum NotificationTypeEnums {
    REPLY_QUESTION(1,"回复帖子"),
    REPLY_COMMENT(2,"回复评论"),
    ;
    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationTypeEnums(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
