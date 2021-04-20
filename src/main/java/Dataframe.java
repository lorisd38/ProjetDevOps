import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class Dataframe {
	private ArrayList<Series> dataframe;
	
	//TODO gerer les exceptions
	public Dataframe(Object[][] tableau) throws Exception {
		if (tableau == null || tableau.length == 0)
			throw new Exception();
		this.dataframe = new ArrayList<>();
		for (int i = 0; i < tableau.length; i ++) {
			Object[] c = tableau[i];
			if (c.length == 1)
				dataframe.add(new Series<>((String)c[0]));
			else if (c.length > 1) {
				Object elem = c[1];
				if (elem instanceof String)
					ajoutColonne("String", c);
				else if (elem instanceof Integer)
					ajoutColonne("Integer", c);
				else if (elem instanceof Float)
					ajoutColonne("Float", c);
				//rajouter un else on renvoie une erreur (type non supporte)
			}
		}
	}

	//TODO gerer les exceptions
	private void ajoutColonne(String type, Object[] c) {
		switch (type) {
			case "String":
				Series<String> s = new Series<>();
				for (int i = 0; i < c.length; i ++) {
					if (i == 0) s.setName((String)c[0]);
					else s.add((String)c[i]);
				}
				dataframe.add(s);
				break;
			case "Integer":
				Series<Integer> si = new Series<>();
				for (int i = 0; i < c.length; i ++) {
					if (i == 0) si.setName((String)c[0]);
					else si.add((Integer)c[i]);
				}
				dataframe.add(si);
				break;
			default:
				Series<Float> sf = new Series<>();
				for (int i = 0; i < c.length; i ++) {
					if (i == 0) sf.setName((String)c[0]);
					else sf.add((Float)c[i]);
				}
				dataframe.add(sf);
		}
	}
	
	public void afficheDataframe() {
		for (Series s : dataframe) {
			System.out.print(s.getName() + "\t");
			for (Object o : s.getColumn())
				System.out.print(o + "\t");
			System.out.println();
		}
		System.out.println();
	}
	
	public ArrayList<Series> getDataframe() {
		return dataframe;
	}
	
	public Dataframe selection(int ligne) throws Exception {
		if (ligne > dataframe.get(0).getSize() || ligne < 0)
			throw new Exception();
		Object[][] data = new Object[dataframe.size()][2];
		int i = 0;
		for (Series s : dataframe) {
			data[i][0] = s.getName();
			data[i][1] = s.getElem(ligne);
			i ++;
		}
		return new Dataframe(data);
	}
	
	public Dataframe selectionLignes(ArrayList<Integer> lignes) throws Exception {
		if (lignes == null || lignes.size() == 0)
			throw new Exception();
		Object[][] data = new Object[dataframe.size()][lignes.size()+1];
		int i = 0, k;
		for (Series s : dataframe) {
			data[i][0] = s.getName();
			k = 1;
			for (int j : lignes) {
				if (j >= s.getSize() || j < 0)
					throw new Exception();
				data[i][k] = s.getElem(j);
				k ++;
			}
			i ++;
		}
		return new Dataframe(data);
	}
	
	public Dataframe selectionMasque(ArrayList<Boolean> masque) throws Exception {
		if (masque == null || masque.size() != dataframe.get(0).getSize())
			throw new Exception();
		ArrayList<ArrayList<Object>> l = new ArrayList<>();
		int i = 0;
		for (Series s : dataframe) {
			ArrayList<Object> l_inter = new ArrayList<>();
			l_inter.add(s.getName());
			for (int j = 0; j < masque.size(); j ++)
				if (masque.get(j)) 
					l_inter.add(s.getElem(j));
			l.add(l_inter);
		}
		Object[][] data = new Object[l.size()][l.get(0).size()];
		for (i = 0; i < l.size(); i++) {
			for (int k = 0; k < l.get(0).size(); k ++)
				data[i][k] = l.get(i).get(k);
		}
		return new Dataframe(data);
	}
	
	public Object selectionElement(int ligne, int colonne) throws Exception {
		if (colonne >= dataframe.size() || colonne < 0)
			throw new Exception();
		if (ligne >= dataframe.get(colonne).getSize() || ligne < 0)
			throw new Exception();
		return dataframe.get(colonne).getElem(ligne);
	}
	
	public Dataframe selectionElements(ArrayList<Integer> ligne, ArrayList<Integer> colonne) throws Exception {
		if (ligne == null || ligne.size() == 0 || colonne == null || colonne.size() == 0)
			throw new Exception();
		Object[][] data = new Object[colonne.size()][ligne.size()+1];
		int i = 0, j;
		for (int c : colonne) {
			j = 1;
			for (int l : ligne) {
				Series s = dataframe.get(c);
				if (c >= dataframe.size() || l >= s.getSize() || l < 0 || c < 0)
					throw new Exception();
				data[i][0] = s.getName();
				data[i][j] = s.getElem(l);
				j ++;
			}
			i ++;
		}
		return new Dataframe(data);
	}
	
	public Dataframe selectionColonne(String nom) throws Exception {
		Object[][] data = null;
		for (Series s : dataframe) {
			if (s.getName() == nom) {
				data = new Object[1][s.getSize()+1];
				data[0][0] = s.getName();
				int i = 1;
				for (Object o : s.getColumn()) {
					data[0][i] = o;
					i ++;
				}
				return new Dataframe(data);
			}
		}
		throw new Exception();
	}
	
	public Dataframe selectionColonnes(ArrayList<String> noms) throws Exception {
		if (noms == null || noms.size() == 0)
			throw new Exception();
		Object[][] data = null;
		int nbNom = 0;
		boolean trouve;
		for (String nom : noms) {
			trouve = false;
			for (Series s : dataframe) {
				if (nom == s.getName()) {
					trouve = true;
					if (data == null) data = new Object[noms.size()][s.getSize()+1];
					data[nbNom][0] = s.getName();
					int i = 1;
					for (Object o : s.getColumn()) {
						data[nbNom][i] = o;
						i ++;
					}
					nbNom ++;
				}
			}
			if (!trouve) throw new Exception();
		}
		return new Dataframe(data);
	}
}
