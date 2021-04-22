import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Dataframe {

	private ArrayList<Series> dataframe;

	protected final String FLOAT = "FLOAT";
	protected final String INTEGER = "Integer";
	protected final int MAX_DATA = 100;
	protected final int MAX_VALUES = 10000;
	private String splitter = " ";
	protected final String STRING = "String";
	private String VIRGULE = ",";

	protected Dataframe() {
		this.splitter = ";";
	}

	public Dataframe(String PATH) throws Exception {
		this.splitter = ";";
		initialisation(toTab(PATH));
	}

	public Dataframe(String[][] arguments) {
		this(arguments, ";");
	}

	public Dataframe(String[][] arguments, String splitter) {
		this.splitter = splitter;
		initialisation(arguments);
	}

	protected String ajout(String name, String[] listeObjet) {
		String valeur = listeObjet[0];
		if (isInt(valeur))
			return INTEGER;

		if (isFloat(valeur))
			return FLOAT;

		return STRING;

	}

	protected Series ajouterTout(Series s, String[][] arguments, int position) {
		for (int i = 1; i < arguments.length; i++)
			s.ajouter(arguments[i][position]);
		return s;
	}

	protected String getName(String[][] liste, int colonneActuelle) {
		return liste[0][colonneActuelle];
	}

	/**
	 * @param argument un tableau de donnees
	 * @return void initialise juste les dataframe
	 *
	 */
	protected void initialisation(String[][] arguments) {
		dataframe = new ArrayList<>();
		for (int i = 0; i < arguments.length; i++) {
			String name = getName(arguments, i);
			Series s = null;
			switch (ajout(name, arguments[i])) {
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

	protected boolean isFloat(String valeur) {
		String[] all = valeur.split(VIRGULE);

		return all.length == 2 && isInt(all[0]) && isInt(all[1]);
	}

	protected boolean isInt(String valeur) {
		return valeur.matches("-?\\d+");
	}

	private String[] nextLine(String line) throws TooManyDataException {
		String[] toRet = line.split(splitter);
		if (toRet.length >= MAX_DATA)
			throw new TooManyDataException();
		return toRet;
	}

	protected String[][] toTab(String PATH) throws Exception {
		String[][] tab = new String[MAX_VALUES][MAX_DATA];
		BufferedReader sc = new BufferedReader(new FileReader(PATH));
		int nbLine = 0;
		String line = sc.readLine();
		while (line != null) {
			tab[nbLine++] = nextLine(line);
			if (nbLine == MAX_VALUES)
				throw new TooManyValueException();
			line = sc.readLine();
		}
		sc.close();

		return tab;
	}

}
