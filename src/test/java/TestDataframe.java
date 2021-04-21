import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
	public void test() {
		assertTrue(true);
	}

	@Test
	public void testgetMaxSizeSeries(){

        Object[][] tab1 = {{"Tab1", 1,2,3,4}, {"Tab2"}};
		Dataframe data1;
		try {
			data1 = new Dataframe(tab1);
			assertEquals(4, data1.getMaxSizeSeries());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Object[][] tab2 = {{"Tab1"}, {"Tab2",2,4,5}};
		Dataframe data2;
		try {
			data2 = new Dataframe(tab2);
			assertEquals(3, data2.getMaxSizeSeries());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Object[][] tab3 = {{"Tab1", 1,2,3,4}, {"Tab2",2,4,5}};
		Dataframe data3;
		try {
			data3 = new Dataframe(tab3);
			assertEquals(4, data3.getMaxSizeSeries());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Object[][] tab4 = {{"Tab1",3,4}, {"Tab2",2,4,5}};
		Dataframe data4;
		try {
			data4 = new Dataframe(tab4);
			assertEquals(3, data4.getMaxSizeSeries());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Object[][] tab5 = {{"Tab1"}, {"Tab2"}};
		Dataframe data5;
		try {
			data5 = new Dataframe(tab5);
			assertEquals(0, data5.getMaxSizeSeries());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void testprintDataframe(){
        Object[][] tab1 = {{"Tab1", 1,2,3,4}, {"Tab2",2,4,5}};
		Dataframe data;
		try {
			data = new Dataframe(tab1);
			assertEquals("Index\t\tTab1\t\tTab2\t\t\n"
					+ "[0]\t\t1\t\t2\t\t\n"
					+ "[1]\t\t2\t\t4\t\t\n"
					+ "[2]\t\t3\t\t5\t\t\n"
					+ "[3]\t\t4\t\t\t\t\n",data.printDataframe());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testprintDataframeFirstLines(){
        Object[][] tab1 = {{"Tab1", 1,2,3,4}, {"Tab2",2,4,5}};
		Dataframe data;
		try {
			data = new Dataframe(tab1);
			
			assertEquals("Index\t\tTab1\t\tTab2\t\t\n",data.printDataframeFirstLines(0));
			
			assertEquals("Index\t\tTab1\t\tTab2\t\t\n"
					+"[0]\t\t1\t\t2\t\t\n" ,data.printDataframeFirstLines(1));
			
			assertEquals("Index\t\tTab1\t\tTab2\t\t\n"
					+"[0]\t\t1\t\t2\t\t\n"
					+"[1]\t\t2\t\t4\t\t\n",data.printDataframeFirstLines(2));
			
			assertEquals("Index\t\tTab1\t\tTab2\t\t\n"
					+ "[0]\t\t1\t\t2\t\t\n"
					+ "[1]\t\t2\t\t4\t\t\n"
					+ "[2]\t\t3\t\t5\t\t\n"
					+ "[3]\t\t4\t\t\t\t\n",data.printDataframeFirstLines(152));
			
			assertEquals("Index\t\tTab1\t\tTab2\t\t\n" ,data.printDataframeFirstLines(-1));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testprintDataframeLastLines(){
        Object[][] tab1 = {{"Tab1", 1,2,3,4}, {"Tab2",2,4,5}};
		Dataframe data;
		try {
			data = new Dataframe(tab1);
			
			assertEquals("Index\t\tTab1\t\tTab2\t\t\n",data.printDataframeLastLines(0));
			
			assertEquals("Index\t\tTab1\t\tTab2\t\t\n"
					+"[3]\t\t4\t\t\t\t\n" ,data.printDataframeLastLines(1));
			
			assertEquals("Index\t\tTab1\t\tTab2\t\t\n"
					+"[2]\t\t3\t\t5\t\t\n"
					+ "[3]\t\t4\t\t\t\t\n",data.printDataframeLastLines(2));
			
			assertEquals("Index\t\tTab1\t\tTab2\t\t\n"
					+ "[0]\t\t1\t\t2\t\t\n"
					+ "[1]\t\t2\t\t4\t\t\n"
					+ "[2]\t\t3\t\t5\t\t\n"
					+ "[3]\t\t4\t\t\t\t\n",data.printDataframeLastLines(152));
			
			assertEquals("Index\t\tTab1\t\tTab2\t\t\n" ,data.printDataframeLastLines(-1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}

