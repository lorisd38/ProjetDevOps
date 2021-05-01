package exception;

@SuppressWarnings("serial")
public class PandaNoData extends PandaExceptions {
	@Override
	@ExcludeFromJacocoGeneratedReport
	public void printStackTrace() {
		super.printStackTrace();
		System.err.println("Liste non initialisée.");
	}
}
