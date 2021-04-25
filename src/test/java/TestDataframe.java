import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDataframe {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testMeanCol() {
	    Object[][] tab1 = {{"Tab1", 1,2,3,4}, {"Tab2"}};
		Dataframe data1;
		try {
			data1 = new Dataframe(tab1);
			assertEquals(2.5f, data1.getMeanColumn(0),0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testMeanColZero() {
	    Object[][] tab1 = {{"Tab1", 0,0,0,0}, {"Tab2"}};
		Dataframe data1;
		try {
			data1 = new Dataframe(tab1);
			assertEquals(0f, data1.getMeanColumn(0),0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMeanColneg() {
	    Object[][] tab1 = {{"Tab1", -1,-1,-1,-1}, {"Tab2"}};
		Dataframe data1;
		try {
			data1 = new Dataframe(tab1);
			assertEquals(-1f, data1.getMeanColumn(0),0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testMeanall() {
	    Object[][] tab1 = {{"Tab1", -2,-1,0,1,2}, {"Tab2"}};
		Dataframe data1;
		try {
			data1 = new Dataframe(tab1);
			assertEquals(0f, data1.getMeanColumn(0),0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMeanEachCol() {
	    Object[][] tab1 = {{"Tab1", -2,-1,0,1,2}, {"Tab2",0.5,178.6,1.65,1.2 }};
		Dataframe data1;
		try {
			data1 = new Dataframe(tab1);
			ArrayList<Series> res = data1.getMeanEachColumn().getDataframe();
			assertEquals(0d,res.get(0).getElem(0));
			assertEquals(45.4875,(double)res.get(1).getElem(0),0.0001);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMeanEachColvide() {
	    Object[][] tab1 = {{"Tab1"}};
		Dataframe data1;
		try {
			data1 = new Dataframe(tab1);
			ArrayList<Series> res = data1.getMeanEachColumn().getDataframe();
			assertEquals(Double.NaN,res.get(0).getElem(0));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//d'autres tests a faire mais j'ai pas d'inspi ( mean each col )
	
	@Test
	public void testgetMaxEachCol() {
	    Object[][] tab1 = {{"Tab1", -2,-1,0,1,2}, {"Tab2",0.5,178.6,1.65,1.2 }, {"Tab3",-0.5,-178.6,-1.65,-1.2 }};
		Dataframe data1;
		try {
			data1 = new Dataframe(tab1);
			ArrayList<Series> res = data1.getMaxEachColumn().getDataframe();
			assertEquals(2d,res.get(0).getElem(0));
			assertEquals(178.6,(double)res.get(1).getElem(0),0);
			assertEquals(-0.5,(double)res.get(2).getElem(0),0);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertFalse(true);
		}
	}

	//d'autres tests a faire mais j'ai pas d'inspi ( max each col )
	@Test
	public void testgetMinEachCol() {
	    Object[][] tab1 = {{"Tab1", -2,-1,0,1,2}, {"Tab2",0.5,178.6,1.65,1.2 }, {"Tab3",-0.5,-178.6,-1.65,-1.2 }};
		Dataframe data1;
		try {
			data1 = new Dataframe(tab1);
			ArrayList<Series> res = data1.getMinEachColumn().getDataframe();
			assertEquals(-2d,res.get(0).getElem(0));
			assertEquals(0.5,(double)res.get(1).getElem(0),0);
			assertEquals(-178.6,(double)res.get(2).getElem(0),0);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//d'autres tests a faire mais j'ai pas d'inspi ( min each col )
}