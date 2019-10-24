package application.exception;

@SuppressWarnings("serial")
public class CustomizeException extends RuntimeException {
	private String message;
	private Integer code;

	public CustomizeException(ICustomizeErrorCode errorCode) {
		this.message = errorCode.getMessage();
		this.code = errorCode.getCode();
	}

	public CustomizeException(String message) {
		// TODO Auto-generated constructor stub
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public Integer getCode() {
		// TODO Auto-generated method stub
		return code;
	}
}
