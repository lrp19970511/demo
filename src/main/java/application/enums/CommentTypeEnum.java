package application.enums;

public enum CommentTypeEnum {
	QUESTION(1), COMMENT(2);
	private Integer type;

	public Integer getType() {
		return type;
	}

	CommentTypeEnum(Integer type) {
		this.type = type;
	}

	public static boolean isExist(Integer type2) {
		for(CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
			if(commentTypeEnum.getType() == type2) {
				return true;
			}
		}
		// TODO Auto-generated method stub
		return false;
	}
}
