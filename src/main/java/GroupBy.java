import java.util.ArrayList;

import exception.ExcludeFromJacocoGeneratedReport;

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
	
	@Override
	@ExcludeFromJacocoGeneratedReport
	public String toString() {
		String s = "";
		for (Dataframe dataframe : all) {
			s += dataframe.toString();
		}
		return s;
	}
}