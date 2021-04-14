import java.util.ArrayList;

public class Dataframe {
	public static void main(String[] args) {
		Dataframe data = new Dataframe();
		data.printDataframe();
	}
	
	@SuppressWarnings("rawtypes")
	ArrayList<Series> dataframe;
	
	public Dataframe() {
		dataframe = new ArrayList<>();
		Series<String> serie = new Series<>();
		serie.name = "TestStr";
		for (int i = 0; i < 20; i++) {
			serie.column.add("test"+i);
		}
		dataframe.add(serie);
		
		Series<Integer> serie2 = new Series<>();
		serie2.name = "TestInt";
		for (int i = 0; i < 30; i++) {
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
		for (Series series : dataframe) 
			System.out.print(series.name + "\t\t");
		System.out.println();
	}
	
	private void printCore(int max) {
		for (int i = 0; i < max; i++) {
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
		
	}
}
