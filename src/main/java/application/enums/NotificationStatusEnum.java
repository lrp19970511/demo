package application.enums;

public enum NotificationStatusEnum {
	UNREAD(0),READ(1);
	
	private int status;
	public int getStatus() {
		return status;
	}
	private NotificationStatusEnum(int status) {
		// TODO Auto-generated constructor stub
		this.status = status;
	}	
}
