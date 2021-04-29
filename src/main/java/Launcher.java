
public class Launcher {
	public static void main(String[] args) throws Exception {
		Object[][] data = { {"A", -15.35, 34.4, -45.9, 5.92, 7.3, 60.07, 117.5, -5.1 },
				{"B", 12, -51, 37, -435, 645},
				{"C", 42, 751, -3844} };
		Dataframe d = new Dataframe(data);
		System.out.println("===========\n"
				+ "Dataframe :\n"
				+ "===========\n"
				+ d.printDataframe());
		
		System.out.println("=================================\n"
				+ "3 premieres lignes du Dataframe :\n"
				+ "=================================\n"
				+ d.printDataframeFirstLines(3));
		
		System.out.println("================================\n"
				+ "5 derniers lignes du Dataframe :\n"
				+ "================================\n"
				+ d.printDataframeLastLines(5));
		
		System.out.println("\n===============================================================\n"
				+ "Calcul de l'écart type du Dataframe + affichage de ce dernier :\n"
				+ "===============================================================\n"
				+ d.getSdEachColumn().printDataframe());
		
		System.out.println("\n===================================================================================\n"
				+ "Séparation de chaque colonne (trié) du Dataframe à 60 % + affichage de ce dernier :\n"
				+ "===================================================================================\n"
				+ d.splitPercent_SortedData(60).printDataframe());
	}
}
