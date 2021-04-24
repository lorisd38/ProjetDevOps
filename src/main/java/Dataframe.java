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

	/**
	 * @param s         Le Series a mettre a jour,
	 * @param arguments les valeurs
	 * @param position  l'index de la colonne courante
	 * @return Series Le Series est mit a jour avec la colonne ajoutee
	 *
	 */
	public Series ajouterTout(Series s, String[][] arguments, int position) {
		for (int i = 1; i < arguments.length; i++)
			s.ajouter(arguments[i][position]);
		return s;
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
			if (nbLine == PandaExceptions.MAX_VALUES)
				throw new TooManyValueException();
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
