import java.util.ArrayList;

public class Dataframe {

	private ArrayList<Series> dataframe;

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
	public Dataframe(String[][] arguments) {
		dataframe = new ArrayList();
		for (int i = 0; i < arguments.length; i++) {
			String name = getName(arguments, i);
			Series s = ajout(name, arguments[i]);
			s = ajouterTout(s, arguments, i);
			dataframe.add(s);
		}
	}
}
