package exception;

@SuppressWarnings("serial")
public class PandaOutOfBound extends PandaExceptions {
	@Override
	@ExcludeFromJacocoGeneratedReport
	public void printStackTrace() {
		super.printStackTrace();
		System.err.println("L'indice est trop grand ou trop petit.");
	}
}