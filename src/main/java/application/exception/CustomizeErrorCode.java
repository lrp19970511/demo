package application.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
	QUESTION_NOT_FOUND(2001, "你找的问题不在了，要不要换个试试？"), TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
	NO_LOGIN(2003, "当前操作需要登录，请先登录"), SYS_ERROR(2004, "服务冒烟了,要不然你稍后再试试!!!"),TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),COMMENT_NOT_FOUND(2006,"回复的评论不存在了，要不要换个试试"),
	COMMENT_IS_EMPTY(2007,"输入内容不能为空"),READ_NOTIFICATION_FAIL(2008,"兄弟，你这是要读别人的信息呢?"),NOTIFICATION_NOT_FOUND(2009,"消息莫非不翼而飞了?"),FILE_UPLOAD_FAIL(2010,"图片上传失败");
	@Override
	public String getMessage() {
		return message;
	}

	private Integer code;
	private String message;

	CustomizeErrorCode(Integer code, String message) {
		// TODO Auto-generated constructor stub
		this.message = message;
		this.code = code;
	}

	@Override
	public Integer getCode() {
		// TODO Auto-generated method stub
		return code;
	}
}
