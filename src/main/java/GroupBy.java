import java.util.ArrayList;

public class GroupBy {
    private ArrayList<Dataframe> all;

    public GroupBy() {
        all = new ArrayList<>();
    }

    public void add(Dataframe selectionParObjet) {
        all.add(selectionParObjet);
    }

	public ArrayList<Dataframe> getAll() {
		return all;
	}
}