package financeiro.model.bean;

public class FinanceiroException extends RuntimeException {

	public FinanceiroException(String message) {
		super(message);
	}

	public FinanceiroException(Throwable cause) {
		super(cause);
	}

	public FinanceiroException(String message, Exception e) {
		super(message, e);
	}

	
}
