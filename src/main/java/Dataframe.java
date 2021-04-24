import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import exception.PandaExceptions;
import exception.TooManyDataException;
import exception.TooManyValueException;

/**
 *
 * @author ALL Classe principale permettant de contruire le dataframe
 *
 */

@SuppressWarnings("rawtypes")
public class Dataframe {

	private ArrayList<Series> dataframe;

	protected final String FLOAT = "FLOAT";
	protected final String INTEGER = "Integer";
	protected final String STRING = "String";

	// Separateur, afin de pouvoir lire tous les types de fichiers CSV
	private String splitter = " ";

	// Representation de la vigule pour split les floats
	private String VIRGULE = ",";

	public Dataframe() {
		this.splitter = ";";
	}

	public Dataframe(Object[][] tableau) throws Exception {
		if (tableau == null || tableau.length == 0)
			throw new Exception();
		this.dataframe = new ArrayList<>();
		for (int i = 0; i < tableau.length; i++) {
			Object[] c = tableau[i];
			if (c.length == 1)
				dataframe.add(new Series<>((String) c[0]));
			else {
				Object elem = c[1];
				if (elem instanceof String)
					ajoutColonne("String", c);
				else if (elem instanceof Integer)
					ajoutColonne("Integer", c);
				else if (elem instanceof Float)
					ajoutColonne("Float", c);
				else if (elem == null)
					ajoutColonne("null", c);
				else
					throw new Exception();
			}
		}
	}

	/**
	 * @param PATH le chemin vers un csv
	 *
	 */
	public Dataframe(String PATH) throws Exception {
		this.splitter = ";";
		initialisation(toTab(PATH));
	}

	/**
	 * @param arguments tableau a deux dimensions de chaine de caracteres
	 *
	 */
	public Dataframe(String[][] arguments) {
		initialisation(arguments);
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

	private void ajoutColonne(String type, Object[] c) throws Exception {
		switch (type) {
		case "String":
			Series<String> s = new Series<>();
			for (int i = 0; i < c.length; i++) {
				if (i == 0)
					s.setName((String) c[0]);
				else
					s.add((String) c[i]);
			}
			dataframe.add(s);
			break;
		case "Integer":
			Series<Integer> si = new Series<>();
			for (int i = 0; i < c.length; i++) {
				if (i == 0)
					si.setName((String) c[0]);
				else
					si.add((Integer) c[i]);
			}
			dataframe.add(si);
			break;
		case "null":
			int j = 2;
			while (j < c.length && c[j] == null)
				j++;
			if (j >= c.length)
				throw new Exception();
			if (c[j] instanceof String)
				ajoutColonne("String", c);
			else if (c[j] instanceof Integer)
				ajoutColonne("Integer", c);
			else if (c[j] instanceof Float)
				ajoutColonne("Float", c);
			else
				throw new Exception();
			break;
		default:
			Series<Float> sf = new Series<>();
			for (int i = 0; i < c.length; i++) {
				if (i == 0)
					sf.setName((String) c[0]);
				else
					sf.add((Float) c[i]);
			}
			dataframe.add(sf);
		}
	}

	/**
	 * @param s         Le Series a mettre a jour,
	 * @param arguments les valeurs
	 * @param position  l'index de la colonne courante
	 * @return Series Le Series est mit a jour avec la colonne ajoutee
	 *
	 */
	@SuppressWarnings("unchecked")
	public Series ajouterTout(Series s, String[][] arguments, int position) {
		for (int i = 1; i < arguments.length; i++)
			s.ajouter(arguments[i][position]);
		return s;
	}

	public ArrayList<Series> getDataframe() {
		return dataframe;
	}

	public String getName(String[][] liste, int colonneActuelle) {
		return liste[0][colonneActuelle];
	}

	/**
	 * @param arguments un tableau de donnees
	 * @return void - initialise juste les dataframe
	 *
	 */
	public void initialisation(String[][] arguments) {
		dataframe = new ArrayList<>();
		for (int i = 0; i < arguments.length; i++) {
			String name = getName(arguments, i);
			Series s = null;
			switch (type(arguments[i])) {
			case INTEGER:
				s = new Series<Integer>();
				break;
			case STRING:
				s = new Series<String>();
				break;
			case FLOAT:
				s = new Series<Float>();
				break;
			default:
				System.err.println("LA COLONNE " + i + " CONTIENT UN TYPE INCONNU");
				System.exit(0);
			}

			s.setName(name);
			s = ajouterTout(s, arguments, i);
			dataframe.add(s);
		}
	}

	/**
	 *
	 * @param valeur
	 * @return 1 si valeur est un float, 0 sinon
	 */
	public boolean isFloat(String valeur) {
		String[] all = valeur.split(VIRGULE);

		return all.length == 2 && isInt(all[0]) && isInt(all[1]);
	}

	/**
	 *
	 * @param valeur
	 * @return 1 si valeur est un entier, 0 sinon
	 */
	public boolean isInt(String valeur) {
		return valeur.matches("-?\\d+");
	}

	/**
	 *
	 * @param line
	 * @return un tableau de chaine de caractere correspondant a la ligne decoupee
	 * @throws TooManyDataException si trop de donnees sont a gerer
	 */
	public String[] nextLine(String line) throws TooManyDataException {
		String[] toRet = line.split(splitter);
		if (toRet.length >= PandaExceptions.MAX_DATA)
			throw new TooManyDataException();
		return toRet;
	}

	public Dataframe selection(int ligne) throws Exception {
		if (ligne > dataframe.get(0).getSize() || ligne < 0)
			throw new Exception();
		Object[][] data = new Object[dataframe.size()][2];
		int i = 0;
		for (Series s : dataframe) {
			data[i][0] = s.getName();
			data[i][1] = s.getElem(ligne);
			i++;
		}
		return new Dataframe(data);
	}

	public Dataframe selectionColonne(String nom) throws Exception {
		Object[][] data = null;
		for (Series s : dataframe) {
			if (s.getName() == nom) {
				data = new Object[1][s.getSize() + 1];
				data[0][0] = s.getName();
				int i = 1;
				for (Object o : s.getColumn()) {
					data[0][i] = o;
					i++;
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
					if (data == null)
						data = new Object[noms.size()][s.getSize() + 1];
					data[nbNom][0] = s.getName();
					int i = 1;
					for (Object o : s.getColumn()) {
						data[nbNom][i] = o;
						i++;
					}
					nbNom++;
				}
			}
			if (!trouve)
				throw new Exception();
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
		Object[][] data = new Object[colonne.size()][ligne.size() + 1];
		int i = 0, j;
		for (int c : colonne) {
			j = 1;
			for (int l : ligne) {
				if (c >= dataframe.size() || c < 0)
					throw new Exception();
				Series s = dataframe.get(c);
				if (l >= s.getSize() || l < 0)
					throw new Exception();
				data[i][0] = s.getName();
				data[i][j] = s.getElem(l);
				j++;
			}
			i++;
		}
		return new Dataframe(data);
	}

	public Dataframe selectionLignes(ArrayList<Integer> lignes) throws Exception {
		if (lignes == null || lignes.size() == 0)
			throw new Exception();
		Object[][] data = new Object[dataframe.size()][lignes.size() + 1];
		int i = 0, k;
		for (Series s : dataframe) {
			data[i][0] = s.getName();
			k = 1;
			for (int j : lignes) {
				if (j >= s.getSize() || j < 0)
					throw new Exception();
				data[i][k] = s.getElem(j);
				k++;
			}
			i++;
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
			for (int j = 0; j < masque.size(); j++)
				if (masque.get(j))
					l_inter.add(s.getElem(j));
			l.add(l_inter);
		}
		Object[][] data = new Object[l.size()][l.get(0).size()];
		for (i = 0; i < l.size(); i++) {
			for (int k = 0; k < l.get(0).size(); k++)
				data[i][k] = l.get(i).get(k);
		}
		return new Dataframe(data);
	}

	/**
	 *
	 * @param PATH le chemin vers un fichier CSV
	 * @return le tableau de chaine de caractere correspondant aux valeurs dans le
	 *         fichier
	 * @throws TooManyValuesException si les colonnes sont trop grandes
	 * @throws Exception              si le fichier n'existe pas, geree par le
	 *                                lecteur de fichier
	 */
	public String[][] toTab(String PATH) throws Exception {
		String[][] tab = new String[PandaExceptions.MAX_VALUES][PandaExceptions.MAX_DATA];
		BufferedReader sc = new BufferedReader(new FileReader(PATH));
		int nbLine = 0;
		String line = sc.readLine();
		while (line != null) {
			tab[nbLine++] = nextLine(line);
			if (nbLine == PandaExceptions.MAX_VALUES) {
				sc.close();
				throw new TooManyValueException();
			}

			line = sc.readLine();
		}
		sc.close();

		return tab;
	}

	/**
	 *
	 * @param listeObjet, une colonne du tableau
	 * @return une chaine de caracteres correspondant au types de donnees
	 */
	public String type(String[] listeObjet) {
		String valeur = listeObjet[0];
		if (isInt(valeur))
			return INTEGER;

		if (isFloat(valeur))
			return FLOAT;

		return STRING;

	}
}