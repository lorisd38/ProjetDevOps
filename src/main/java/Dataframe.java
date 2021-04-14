import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Dataframe {

	private ArrayList<Series> dataframe;
	private final int MAX_VALUES = 10000;
	private final int MAX_DATA = 100;
	private String splitter = "";
	
	private Series ajout(String name, String[] listeObjet) {
		String valeur = listeObjet[0];
		if (valeur.matches("-?\\d+")) 
			return new Series<Integer>();	
		
		if (valeur.contains(".")) 
			return new Series<Float>();
		
		return new Series<String>();
	}
	
	private String getName(String[][] liste, int colonneActuelle) {
		return (String) liste[0][colonneActuelle];
	}
	
	private Series ajouterTout(Series s, String[][] arguments, int position) {
		for (int i = 1; i < arguments.length; i++) 
			s.ajouter(arguments[i][position]);
		return s;
	}	
	
	/**
	 * @param argument un tableau de donnees
	 * @return void initialise juste les dataframe
	 * 
	 */
	public void initialisation(String[][] arguments) {
		dataframe = new ArrayList();
		for (int i = 0; i < arguments.length; i++) {
			String name = getName(arguments, i);
			Series s = ajout(name, arguments[i]);
			s = ajouterTout(s, arguments, i);
			dataframe.add(s);
		}
	}
	
	
	public Dataframe(String[][] arguments, String splitter) {
		this.splitter = splitter;
		initialisation(arguments);
	}
	
	public Dataframe(String[][] arguments) {
		this(arguments, ";");
	}
	
	public Dataframe(String PATH) throws Exception {
		this.splitter = ";";
		initialisation(toTab(PATH));
	}
	
	private String[] nextLine(String line) throws TooManyDataException {
		String[] toRet = line.split(splitter);
		if (toRet.length >= MAX_DATA)
			throw new TooManyDataException();
		return toRet;
	}
		

	
	private String[][] toTab(String PATH) throws Exception{
		String[][] tab = new String[MAX_DATA][MAX_VALUES];
		Scanner sc = new Scanner(PATH);
			/*
			 * Recuperer toutes les lignes et les mettres dans un tableau
			 * Rejeter une exception si la taille est trop grande
			 * 
			 */
		int nbLine = 0;
		while (sc.hasNext()) {
			tab[nbLine ++] = nextLine(sc.next());
			if(nbLine == MAX_VALUES)
				throw new TooManyValueException();
		}
		
		return tab;
	}
	
}
