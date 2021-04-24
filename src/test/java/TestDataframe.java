import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDataframe {
	private Dataframe d;
	private static String PATH;

	@Before
	public void setUpBefore() throws Exception {
		d = new Dataframe();
		PATH = "src/test/resources/test.csv";
	}

	@After
	public void tearDownAfter() throws Exception {
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
		assertEquals(d.ajout("forTest", listeInt), d.INTEGER);
		String[] listeString = { "a", "b", "b" };
		assertEquals(d.ajout("forTest", listeString), d.STRING);
		String[] listeFloat = { "0,1", "0,2", "0,2" };
		assertEquals(d.ajout("forTest", listeFloat), d.FLOAT);
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
	public void testIsFloat() throws Exception {
		String s = "0";
		assertFalse(d.isFloat(s));
		s = "0,2";
		assertTrue(d.isFloat(s));
		s = "BLABLA";
		assertFalse(d.isFloat(s));
	}

	@Test
	public void testIsInt() throws Exception {
		String s = "0";
		assertTrue(d.isInt(s));
		s = "0,2";
		assertFalse(d.isInt(s));
		s = "Test";
		assertFalse(d.isInt(s));
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

			String[][] tab = d.toTab(PATH);
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
