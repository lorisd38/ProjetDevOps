package exception;

@SuppressWarnings("serial")
public class PandaNameNotFound extends PandaExceptions {
	@Override
	public void printStackTrace() {
		super.printStackTrace();
		System.err.println("Le nom n'est pas present dans la dataframe.");
	}
}
