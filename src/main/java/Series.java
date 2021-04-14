import java.util.ArrayList;

public class Series<E> {
	String name;
	ArrayList<E> column;
	
	public Series() {
		name = "";
		column = new ArrayList<E>();
	}
}