package exception;

@SuppressWarnings("serial")
public class PandaNoData extends PandaExceptions {
	@Override
	public void printStackTrace() {
		super.printStackTrace();
		System.err.println("Liste non initialisée.");
	}
}
