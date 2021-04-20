import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;

//import java.io.FileWriter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDataframe {
	static Dataframe d;
	private static String PATH;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		d = new Dataframe();
		PATH = "src/test/ressources/test.csv";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private void remplirFile(int largeur, int hauteur, String PATH) throws IOException {
		FileWriter fw = new FileWriter(PATH);
		fw.append("FILE;VALUE1;VALUE2\n");
		for (int i = 0; i < hauteur - 1; i++) {
			for (int j = 1; j <= largeur; j++) {
				fw.append(i * j + ";");
			}
			fw.append("\n");
		}

		fw.close();
	}

	@Test
	public void testAjout() {
		String[] listeInt = { "1", "2", "3" };
		assertEquals(d.ajout("coucou", listeInt), d.INTEGER);
		String[] listeString = { "a", "b", "b" };
		assertEquals(d.ajout("coucou", listeString), d.STRING);
		String[] listeFloat = { "0.1", "0.2", "0.2" };
		assertEquals(d.ajout("coucou", listeFloat), d.FLOAT);
	}

	@Test(expected = TooManyDataException.class)
	public void testAjoutTropGrand() throws Exception {
		remplirFile(d.MAX_DATA, d.MAX_VALUES - 1, PATH);
		d.toTab(PATH);
	}

	@Test(expected = TooManyValueException.class)
	public void testAjoutTropLarge() throws Exception {
		remplirFile(d.MAX_DATA - 1, d.MAX_VALUES, PATH);
		d.toTab(PATH);
	}

	@Test
	public void testGetName() {
		String[][] all = new String[201][3];
		String[] contenu = { "FILE", "VALUE1", "VALUE2" };
		all[0] = contenu;
		for (int i = 0; i < 200; i++) {
			String[] toAdd = { Integer.toString(i), Integer.toString(i * 2), Integer.toString(i * 3) };
			all[i + 1] = toAdd;
		}
		for (int i = 0; i < 3; i++) {
			assertEquals(contenu[i], d.getName(all, i));
		}
	}
	
	@Test
	public void testToTab() {
		try {
			int hauteur = 201;
			int largeur = 3;
			remplirFile(largeur, hauteur, PATH);
			String[][] all = new String[201][3];
			String[] contenu = { "FILE", "VALUE1", "VALUE2" };
			all[0] = contenu;
			for (int i = 0; i < 200; i++) {
				String[] toAdd = { Integer.toString(i), Integer.toString(i * 2), Integer.toString(i * 3) };
				all[i + 1] = toAdd;
			}

			String[][] tab = d.toTab("src/test/ressources/test.csv");
			for (int i = 0; i < 201; i++) {
				for (int j = 0; j < 3; j++) {
					assertEquals(" verification a l'indice " + i + "" + j + ".", all[i][j], tab[i][j]);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(false);
		}
	}
}
