package life.lby.community.community.enums;

public enum CommentTypsEnums {
    QUESTION(1),
    COMMENT(2);
    private Integer type;

    public static boolean isExist(Integer type) {
        for (CommentTypsEnums commentTypsEnums : CommentTypsEnums.values()) {
            if(commentTypsEnums.getType() == type){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    CommentTypsEnums(Integer type){
        this.type = type;
    }
}
