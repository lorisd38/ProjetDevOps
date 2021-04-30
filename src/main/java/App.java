import java.util.ArrayList;
import java.util.Arrays;

public class App {
	public static void main(String[] args) throws Exception {
		Object[][] data = { {"A", -15.35, 34.4, -45.9, 5.92, 7.3, 60.07, 117.5, -5.1 },
				{"B", 12, 42, -51, 37, -435, 645},
				{"C", 42, 751, -3844} };
		Dataframe dataframeTab = new Dataframe(data);
		System.out.println("=========================================================================================\n"
				+ "Dataframe creer a partir d'un tableau d'Object avec des colonnes de tailles differentes :\n"
				+ "=========================================================================================\n"
				+ dataframeTab);
		
		System.out.println("=================================\n"
				+ "3 premieres lignes du Dataframe :\n"
				+ "=================================\n"
				+ dataframeTab.toStringFirstLines(3));
		
		System.out.println("================================\n"
				+ "5 derniers lignes du Dataframe :\n"
				+ "================================\n"
				+ dataframeTab.toStringLastLines(5));
		
		System.out.println("===================================================================================\n"
				+ "Calcul du max / min / ecart type / moyenne du Dataframe + affichage de ce dernier :\n"
				+ "===================================================================================\n"
				+ "Max : \n" + dataframeTab.getMaxEachColumn() + "\n"
				+ "Min : \n" + dataframeTab.getMinEachColumn() + "\n"
				+ "Sd: \n" + dataframeTab.getSdEachColumn() + "\n"
				+ "Moy : \n" + dataframeTab.getMeanEachColumn());

		System.out.println("===================================================================================\n"
				+ "Separation de chaque colonne (trie) du Dataframe à 60 % + affichage de ce dernier :\n"
				+ "===================================================================================\n"
				+ dataframeTab.splitPercent_SortedData(60));
		
		Object[][] dataGrp = { {"A", 1, 1, 3, 3, 4},
				{"B", 5, 6, 7, 8, 9},
				{"C", 10, 11, 12, 13, 14} };
		Dataframe dataframeGrp = new Dataframe(dataGrp);
		System.out.println("=================================================================\n"
				+ "Dataframe creer a partir d'un tableau d'Object pour le group by :\n"
				+ "=================================================================\n"
				+ dataframeGrp);
		
		System.out.println("================\n"
				+ "Group by sur A :\n"
				+ "================\n"
				+ dataframeGrp.groupBy("A"));
		
		Dataframe dataframeCSV = new Dataframe("src/main/resources/res.csv");
		System.out.println("===============================================================================================================================================\n"
				+ "Dataframe creer a partir d'un CSV recupere sur le dernier TP de PAP montrant l'acceleration du produit matricel en fonction du nombre de thread\n"
				+ "===============================================================================================================================================\n"
				+ dataframeCSV);
		
		dataframeCSV = dataframeCSV.selectionColonnes(new ArrayList<String>(Arrays.asList(new String[] { "2", "4", "8", "16" })));
		System.out.println("============================================\n"
				+ "Selection des colonnes \"2\", \"4\", \"8\", \"16\" :\n"
				+ "============================================\n"
				+ dataframeCSV);
		
		System.out.println("============================================================\n"
				+ "Recherche de l'acceleration maximale / minimale par thread :\n"
				+ "============================================================\n"
				+ "Max : \n" + dataframeCSV.getMaxEachColumn() + "\n"
				+ "Min : \n" + dataframeCSV.getMinEachColumn());
		
	}
}
