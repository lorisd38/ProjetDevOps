import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class TestDataframe {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	// Test methodes MeanEachColumn & MeanColumn
	
	@Test
	public void testMeanCol() throws Exception {
		Object[][] tab1 = { { "Tab1", 1, 2, 3, 4 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(2.5, (double) data1.getMeanColumn(0), 0);
	}

	@Test
	public void testMeanColZero() throws Exception {
		Object[][] tab1 = { { "Tab1", 0, 0, 0, 0 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(0.0, (double) data1.getMeanColumn(0), 0);
	}

	@Test
	public void testMeanColNeg() throws Exception {
		Object[][] tab1 = { { "Tab1", -2, -1, -4, -3 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(-2.5, (double) data1.getMeanColumn(0), 0);
	}

	@Test
	public void testMeanAll() throws Exception {
		Object[][] tab1 = { { "Tab1", -2, -1, 0, 1, 2 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(0.0, (double) data1.getMeanColumn(0), 0);
	}

	@Test
	public void testMeanEachCol() throws Exception {
		Object[][] tab1 = { { "Tab1", -2, -1, 0, 1, 2 }, { "Tab2", 0.5, 178.6, 1.65, 1.2 } };
		Dataframe data1 = new Dataframe(tab1);
		ArrayList<Series> res = data1.getMeanEachColumn().getDataframe();
		assertEquals(0.0, (double) res.get(0).getElem(0), 0);
		assertEquals(45.4875, (double) res.get(1).getElem(0), 0.0001);
	}

	@Test
	public void testMeanEachColVide() throws Exception {
		Object[][] tab1 = { { "Tab1" } };
		Dataframe data1 = new Dataframe(tab1);
		ArrayList<Series> res = data1.getMeanEachColumn().getDataframe();
		assertEquals("null", res.get(0).getElem(0));
	}

	// Test methodes MaxEachColumn & MaxColumn

	@Test
	public void testMaxCol() throws Exception {
		Object[][] tab1 = { { "Tab1", 1, 2, 3, 4 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(4.0, (double) data1.getMaxColumn(0), 0);
	}

	@Test
	public void testMaxColZero() throws Exception {
		Object[][] tab1 = { { "Tab1", 0, 0, 0, 0 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(0.0, (double) data1.getMaxColumn(0), 0);
	}

	@Test
	public void testMaxColNeg() throws Exception {
		Object[][] tab1 = { { "Tab1", -2, -1, -4, -3 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(-1.0, (double) data1.getMaxColumn(0), 0);
	}

	@Test
	public void testMaxAll() throws Exception {
		Object[][] tab1 = { { "Tab1", -2, -1, 0, 1, 2 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(2.0, (double) data1.getMaxColumn(0), 0);
	}

	@Test
	public void testMaxEachCol() throws Exception {
		Object[][] tab1 = { { "Tab1", -2, -1, 0, 1, 2 }, { "Tab2", 0.5, 178.6, 1.65, 1.2 },
				{ "Tab3", -0.5, -178.6, -1.65, -1.2 } };
		Dataframe data1 = new Dataframe(tab1);
		ArrayList<Series> res = data1.getMaxEachColumn().getDataframe();
		assertEquals(2.0, (double) res.get(0).getElem(0), 0);
		assertEquals(178.6, (double) res.get(1).getElem(0), 0);
		assertEquals(-0.5, (double) res.get(2).getElem(0), 0);
	}

	@Test
	public void testMaxEachColVide() throws Exception {
		Object[][] tab1 = { { "Tab1" } };
		Dataframe data1 = new Dataframe(tab1);
		ArrayList<Series> res = data1.getMaxEachColumn().getDataframe();
		assertEquals("null", res.get(0).getElem(0));
	}
	
	// Test methodes MinEachColumn & MinColumn
	
	@Test
	public void testMinCol() throws Exception {
		Object[][] tab1 = { { "Tab1", 1, 2, 3, 4 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(1.0, (double) data1.getMinColumn(0), 0);
	}

	@Test
	public void testMinColZero() throws Exception {
		Object[][] tab1 = { { "Tab1", 0, 0, 0, 0 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(0.0, (double) data1.getMinColumn(0), 0);
	}

	@Test
	public void testMinColNeg() throws Exception {
		Object[][] tab1 = { { "Tab1", -2, -1, -4, -3 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(-4.0, (double) data1.getMinColumn(0), 0);
	}

	@Test
	public void testMinAll() throws Exception {
		Object[][] tab1 = { { "Tab1", -2, -1, 0, 1, 2 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(-2.0, (double) data1.getMinColumn(0), 0);
	}
	
	@Test
	public void testgetMinEachCol() throws Exception {
		Object[][] tab1 = { { "Tab1", -2, -1, 0, 1, 2 }, { "Tab2", 0.5, 178.6, 1.65, 1.2 },
				{ "Tab3", -0.5, -178.6, -1.65, -1.2 } };
		Dataframe data1 = new Dataframe(tab1);
		ArrayList<Series> res = data1.getMinEachColumn().getDataframe();
		assertEquals(-2.0, (double) res.get(0).getElem(0), 0);
		assertEquals(0.5, (double) res.get(1).getElem(0), 0);
		assertEquals(-178.6, (double) res.get(2).getElem(0), 0);
	}
	
	@Test
	public void testMinEachColVide() throws Exception {
		Object[][] tab1 = { { "Tab1" } };
		Dataframe data1 = new Dataframe(tab1);
		ArrayList<Series> res = data1.getMinEachColumn().getDataframe();
		assertEquals("null", res.get(0).getElem(0));
	}

	// Test methodes SdEachColumn & SdColumn
	
	@Test
	public void testSdCol() throws Exception {
		Object[][] tab1 = { { "Tab1", 1, 2, 3, 4 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(1.87, (double) data1.getSdColumn(0), 0);
	}

	@Test
	public void testSdColZero() throws Exception {
		Object[][] tab1 = { { "Tab1", 0, 0, 0, 0 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(0.0, (double) data1.getSdColumn(0), 0);
	}

	@Test
	public void testSdColNeg() throws Exception {
		Object[][] tab1 = { { "Tab1", -2, -1, -4, -3 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(1.87, (double) data1.getSdColumn(0), 0);
	}

	@Test
	public void testSdAll() throws Exception {
		Object[][] tab1 = { { "Tab1", -2, -1, 0, 1, 2 } };
		Dataframe data1 = new Dataframe(tab1);
		assertEquals(2.45, (double) data1.getSdColumn(0), 0);
	}
	
	@Test
	public void testgetSdEachCol() throws Exception {
		Object[][] tab1 = { { "Tab1", -2, -1, 0, 1, 2 }, { "Tab2", 0.5, 178.6, 1.65, 1.2 },
				{ "Tab3", -0.5, -178.6, -1.65, -1.2 } };
		Dataframe data1 = new Dataframe(tab1);
		ArrayList<Series> res = data1.getSdEachColumn().getDataframe();
		assertEquals(2.45, (double) res.get(0).getElem(0), 0);
		assertEquals(89.05, (double) res.get(1).getElem(0), 0);
		assertEquals(153.71, (double) res.get(2).getElem(0), 0);
	}
	
	@Test
	public void testSdEachColVide() throws Exception {
		Object[][] tab1 = { { "Tab1" } };
		Dataframe data1 = new Dataframe(tab1);
		ArrayList<Series> res = data1.getSdEachColumn().getDataframe();
		assertEquals("null", res.get(0).getElem(0));
	}
}