import java.util.ArrayList;

public class Dataframe {
	public static void main(String[] args) {
		Dataframe data = new Dataframe(20, 30);
		data.printDataframe();
	}
	
	@SuppressWarnings("rawtypes")
	ArrayList<Series> dataframe;
	
	public Dataframe(int size1, int size2) {
		dataframe = new ArrayList<>();
		Series<String> serie = new Series<>();
		serie.name = "TestStr";
		for (int i = 0; i < size1; i++) {
			serie.column.add("test"+i);
		}
		dataframe.add(serie);
		
		Series<Integer> serie2 = new Series<>();
		serie2.name = "TestInt";
		for (int i = 0; i < size2; i++) {
			serie2.column.add(i);
		}
		dataframe.add(serie2);
	}
	
	private int getMaxSeries() {
		int max = 0;
		for (Series series : dataframe) {
			if (series.column.size() > max) max = series.column.size();
		}
		return max;
	}
	
	private void printHeader() {
		System.out.print("Index\t\t");
		for (Series series : dataframe) 
			System.out.print(series.name + "\t\t");
		System.out.println();
	}
	
	private void printCore(int max) {
		for (int i = 0; i < max; i++) {
			System.out.print("["+i+"]\t\t");
			for (int j = 0; j < dataframe.size(); j++) {
				if(dataframe.get(j).column.size() > i)
					System.out.print(dataframe.get(j).column.get(i)+"\t\t");
				else
					System.out.print("\t\t");
			}
			System.out.println();
		}
	}
	
	public void printDataframe() {
		printHeader();
		printCore(getMaxSeries());
	}
	
	public void printDataframeFirstLines(int nb) {
		int max = getMaxSeries();
		printHeader();
		printCore(nb > max ? max : nb);
	}
	
	public void printDataframeLastLines(int nb) {
		int max = getMaxSeries();
		printHeader();
		
	}
}
