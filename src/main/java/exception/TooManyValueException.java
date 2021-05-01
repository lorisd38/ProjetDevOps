package exception;

public class TooManyValueException extends PandaExceptions {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	@ExcludeFromJacocoGeneratedReport
	public void printStackTrace() {
		super.printStackTrace();
		System.err.println("Le nombre de valeur maximale est de " + MAX_VALUES);
	}
}
