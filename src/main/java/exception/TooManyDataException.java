package exception;

@SuppressWarnings("serial")
public class TooManyDataException extends PandaExceptions {

	@Override
	@ExcludeFromJacocoGeneratedReport
	public void printStackTrace() {
		super.printStackTrace();
		System.err.println("Le nombre de donnees maximale est de " + MAX_DATA);
	}
}
