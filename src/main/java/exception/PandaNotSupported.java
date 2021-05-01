package exception;

@SuppressWarnings("serial")
public class PandaNotSupported extends PandaExceptions {
	@Override
	@ExcludeFromJacocoGeneratedReport
	public void printStackTrace() {
		super.printStackTrace();
		System.err.println("Les types possibles pour les colonnes du dataframe sont String, Float ou Int.");
	}
}
