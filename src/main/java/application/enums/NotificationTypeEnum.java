package application.enums;

public enum NotificationTypeEnum {

	REPLY_QUESTION(1,"回复了问题"),
	REPLY_COMMENT(2,"回复了评论");
	private int type;
	private String name;
	public int getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	private NotificationTypeEnum(int type,String name) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.name = name;
	}
	public static String nameOfType(int type) {
		for(NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
			if(notificationTypeEnum.getType() == type) {
				return notificationTypeEnum.getName();
			}
		}
		return "";
	}
}
