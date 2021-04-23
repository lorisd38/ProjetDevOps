import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class TestDataframe {
	private static Dataframe d;
	private static Object[][] data;
	private static final int nbColonnes = 8;
	private static final int nbLignes = 8;

	@Before
	public void setUpBefore() throws Exception {
		data = new Object[nbColonnes][nbLignes + 1];
		for (int i = 0; i < 6; i++) {
			if (i == 0) {
				data[0][i] = "A";
				data[1][i] = "B";
				data[2][i] = "C";
				data[3][i] = "D";
				data[4][i] = "E";
				data[5][i] = "F";
				data[6][i] = "G";
				data[7][i] = "H";
			} else {
				data[0][i] = i;
				data[1][i] = Integer.toString(i);
				data[2][i] = (float) i;
				data[3][i] = i * 10;
				data[4][i] = Integer.toString(i * 10);
				data[5][i] = (float) i * 10;
				data[6][i] = i * 100;
				data[7][i] = Integer.toString(i * 100);
			}
		}
		d = new Dataframe(data);
	}

	@After
	public void tearDownAfter() throws Exception {
	}

	@Test
	public void testDataframe() {
		int k = 0, l;
		for (Series s : d.getDataframe()) {
			assertEquals(s.getName(), data[k][0]);
			l = 1;
			for (Object o : s.getColumn()) {
				assertEquals(o, data[k][l]);
				l++;
			}
			k++;
		}
	}

	@Test
	public void testSelection() throws Exception {
		Dataframe newData = d.selection(0);
		int k = 0;
		for (Series s : newData.getDataframe()) {
			assertEquals(s.getName(), data[k][0]);
			for (Object o : s.getColumn()) {
				assertEquals(o, data[k][1]);
			}
			k++;
		}
	}

	@Test
	public void testSelectionColonne() throws Exception {
		Dataframe newData;
		String[] tab = { "A", "B", "C" };
		int i = 0, j;
		Series s;
		for (String t : tab) {
			newData = d.selectionColonne(t);
			assertTrue(newData.getDataframe().size() == 1);
			s = newData.getDataframe().get(0);
			assertEquals(data[i][0], s.getName());
			j = 1;
			for (Object o : s.getColumn()) {
				assertEquals(data[i][j], o);
				j++;
			}
			i++;
		}
	}

	@Test(expected = Exception.class)
	public void testSelectionColonneNomInconnu() throws Exception {
		d.selectionColonne("Inconnu");
	}

	@Test
	public void testSelectionColonnes() throws Exception {
		ArrayList<String> selection = new ArrayList<>();
		selection.add("A");
		selection.add("E");
		selection.add("C");
		selection.add("H");
		int[] indices = { 0, 4, 2, 7 };
		Dataframe newData = d.selectionColonnes(selection);
		int i = 0, j;
		for (Series s : newData.getDataframe()) {
			assertEquals(data[indices[i]][0], s.getName());
			j = 1;
			for (Object o : s.getColumn()) {
				assertEquals(data[indices[i]][j], o);
				j++;
			}
			i++;
		}
	}

	@Test(expected = Exception.class)
	public void testSelectionColonnesListNull() throws Exception {
		ArrayList<String> selection = null;
		d.selectionColonnes(selection);
	}

	@Test(expected = Exception.class)
	public void testSelectionColonnesListVide() throws Exception {
		ArrayList<String> selection = new ArrayList<>();
		d.selectionColonnes(selection);
	}

	@Test(expected = Exception.class)
	public void testSelectionColonnesNomInconnu() throws Exception {
		ArrayList<String> selection = new ArrayList<>();
		selection.add("A");
		selection.add("Inconnu");
		selection.add("C");
		selection.add("H");
		d.selectionColonnes(selection);
	}

	@Test
	public void testSelectionElement() throws Exception {
		assertEquals(data[0][1], d.selectionElement(0, 0));
		assertEquals(data[1][3], d.selectionElement(2, 1));
		assertEquals(data[2][7], d.selectionElement(6, 2));
	}

	@Test(expected = Exception.class)
	public void testSelectionElementColonneNegatif() throws Exception {
		d.selectionElement(0, -1);
	}

	@Test(expected = Exception.class)
	public void testSelectionElementColonneTropGrand() throws Exception {
		d.selectionElement(0, nbColonnes);
	}

	@Test(expected = Exception.class)
	public void testSelectionElementLigneNegatif() throws Exception {
		d.selectionElement(-1, 0);
	}

	@Test(expected = Exception.class)
	public void testSelectionElementLigneTropGrand() throws Exception {
		d.selectionElement(nbLignes, 0);
	}

	@Test
	public void testSelectionElements() throws Exception {
		ArrayList<Integer> colonne = new ArrayList<>();
		ArrayList<Integer> ligne = new ArrayList<>();
		colonne.add(5);
		colonne.add(4);
		ligne.add(nbLignes - 1);
		ligne.add(5);
		Dataframe newData = d.selectionElements(ligne, colonne);
		int k = 0, m;
		for (Series s : newData.getDataframe()) {
			assertEquals(data[colonne.get(k)][0], s.getName());
			m = 0;
			for (Object o : s.getColumn()) {
				assertEquals(data[colonne.get(k)][ligne.get(m)], o);
				m++;
			}
			k++;
		}
	}

	@Test(expected = Exception.class)
	public void testSelectionElementsColonneNegatif() throws Exception {
		ArrayList<Integer> colonne = new ArrayList<>();
		ArrayList<Integer> ligne = new ArrayList<>();
		colonne.add(5);
		colonne.add(-1);
		ligne.add(1);
		d.selectionElements(ligne, colonne);
	}

	@Test(expected = Exception.class)
	public void testSelectionElementsColonneNull() throws Exception {
		ArrayList<Integer> colonne = null;
		ArrayList<Integer> ligne = new ArrayList<>();
		ligne.add(5);
		d.selectionElements(ligne, colonne);
	}

	@Test(expected = Exception.class)
	public void testSelectionElementsColonneTropGrand() throws Exception {
		ArrayList<Integer> colonne = new ArrayList<>();
		ArrayList<Integer> ligne = new ArrayList<>();
		colonne.add(5);
		colonne.add(nbColonnes);
		ligne.add(1);
		d.selectionElements(ligne, colonne);
	}

	@Test(expected = Exception.class)
	public void testSelectionElementsColonneVide() throws Exception {
		ArrayList<Integer> colonne = new ArrayList<>();
		ArrayList<Integer> ligne = new ArrayList<>();
		ligne.add(5);
		d.selectionElements(ligne, colonne);
	}

	@Test(expected = Exception.class)
	public void testSelectionElementsLigneNegatif() throws Exception {
		ArrayList<Integer> colonne = new ArrayList<>();
		ArrayList<Integer> ligne = new ArrayList<>();
		colonne.add(5);
		colonne.add(4);
		ligne.add(1);
		ligne.add(-1);
		d.selectionElements(ligne, colonne);
	}

	@Test(expected = Exception.class)
	public void testSelectionElementsLigneNull() throws Exception {
		ArrayList<Integer> colonne = new ArrayList<>();
		ArrayList<Integer> ligne = null;
		colonne.add(5);
		colonne.add(2);
		d.selectionElements(ligne, colonne);
	}

	@Test(expected = Exception.class)
	public void testSelectionElementsLigneTropGrand() throws Exception {
		ArrayList<Integer> colonne = new ArrayList<>();
		ArrayList<Integer> ligne = new ArrayList<>();
		colonne.add(5);
		colonne.add(4);
		ligne.add(nbLignes);
		ligne.add(5);
		d.selectionElements(ligne, colonne);
	}

	@Test(expected = Exception.class)
	public void testSelectionElementsLigneVide() throws Exception {
		ArrayList<Integer> colonne = new ArrayList<>();
		ArrayList<Integer> ligne = new ArrayList<>();
		colonne.add(5);
		colonne.add(2);
		d.selectionElements(ligne, colonne);
	}

	@Test(expected = Exception.class)
	public void testSelectionIndexNegatif() throws Exception {
		d.selection(-1);
	}

	@Test(expected = Exception.class)
	public void testSelectionIndexTropGrand() throws Exception {
		d.selection(nbLignes + 1);
	}

	@Test
	public void testSelectionLignes() throws Exception {
		ArrayList<Integer> selection = new ArrayList<>();
		selection.add(0);
		selection.add(3);
		selection.add(7);
		selection.add(6);
		Dataframe newData = d.selectionLignes(selection);
		int i = 0, j;
		for (Series s : newData.getDataframe()) {
			j = 0;
			assertEquals(data[i][0], s.getName());
			for (Object o : s.getColumn()) {
				assertEquals(data[i][selection.get(j) + 1], o);
				j++;
			}
			i++;
		}
	}

	@Test(expected = Exception.class)
	public void testSelectionLignesIndexNegatif() throws Exception {
		ArrayList<Integer> selection = new ArrayList<>();
		selection.add(0);
		selection.add(-1);
		selection.add(7);
		selection.add(8);
		d.selectionLignes(selection);
	}

	@Test(expected = Exception.class)
	public void testSelectionLignesIndexTropGrand() throws Exception {
		ArrayList<Integer> selection = new ArrayList<>();
		selection.add(0);
		selection.add(nbLignes + 1);
		selection.add(7);
		selection.add(8);
		d.selectionLignes(selection);
	}

	@Test
	public void testSelectionMasque() throws Exception {
		ArrayList<Boolean> tab = new ArrayList<>();
		for (int i = 0; i < 8; i++)
			tab.add(false);
		tab.set(1, true);
		tab.set(5, true);
		tab.set(7, true);
		int i = 0, j, k;
		Dataframe newData = d.selectionMasque(tab);
		for (Series s : newData.getDataframe()) {
			assertEquals(data[i][0], s.getName());
			j = 1;
			k = 0;
			for (Boolean b : tab) {
				if (b) {
					assertEquals(data[i][j], s.getElem(k));
					k++;
				}
				j++;
			}
			i++;
		}
	}

	@Test(expected = Exception.class)
	public void testSelectionMasqueNull() throws Exception {
		ArrayList<Boolean> tab = null;
		d.selectionMasque(tab);
	}

	@Test(expected = Exception.class)
	public void testSelectionMasqueTailleDifferente() throws Exception {
		ArrayList<Boolean> tab = new ArrayList<>();
		tab.add(false);
		d.selectionMasque(tab);
	}

	@Test(expected = Exception.class)
	public void testSelectionMasqueVide() throws Exception {
		ArrayList<Boolean> tab = new ArrayList<>();
		d.selectionMasque(tab);
	}
}
