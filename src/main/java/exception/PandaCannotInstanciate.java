package exception;

@SuppressWarnings("serial")
public class PandaCannotInstanciate extends PandaExceptions {
	@Override
	@ExcludeFromJacocoGeneratedReport
	public void printStackTrace() {
		super.printStackTrace();
		System.err.println("Les types ne correspondent pas.");
	}
}
