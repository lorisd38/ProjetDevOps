import java.util.ArrayList;

public class Dataframe {
	
	
	public static void main(String[] args) {
        Object[][] tab = {{"Tab1", 1,2,3,4}, {"Tab2"}};
        Dataframe data;
        try {
            data = new Dataframe(tab);
            data.afficheDataframe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("rawtypes")
    ArrayList<Series> dataframe;
    
    public Dataframe(Object[][] tableau) throws Exception {
        if (tableau == null || tableau.length == 0)
            throw new Exception();
        this.dataframe = new ArrayList<>();
        for (int i = 0; i < tableau.length; i ++) {
            Object[] c = tableau[i];
            if (c.length == 1)
                dataframe.add(new Series<>((String)c[0]));
            else if (c.length > 1) {
                Object elem = c[1];
                if (elem instanceof String)
                    ajoutColonne("String", c);
                else if (elem instanceof Integer)
                    ajoutColonne("Integer", c);
                else if (elem instanceof Float)
                    ajoutColonne("Float", c);
                //rajouter un else on renvoie une erreur (type non supporte)
            }
        }
    }
    
    private void ajoutColonne(String type, Object[] c) {
        switch (type) {
            case "String":
                Series<String> s = new Series<>();
                for (int i = 0; i < c.length; i ++) {
                    if (i == 0) s.setName((String)c[0]);
                    else s.add((String)c[i]);
                }
                dataframe.add(s);
                break;
            case "Integer":
                Series<Integer> si = new Series<>();
                for (int i = 0; i < c.length; i ++) {
                    if (i == 0) si.setName((String)c[0]);
                    else si.add((Integer)c[i]);
                }
                dataframe.add(si);
                break;
            default:
                Series<Float> sf = new Series<>();
                for (int i = 0; i < c.length; i ++) {
                    if (i == 0) sf.setName((String)c[0]);
                    else sf.add((Float)c[i]);
                }
                dataframe.add(sf);
        }
    }
	
	
	public int getMaxSizeSeries() {
		int max = 0;
		for (Series series : dataframe) {
			if (series.getColumn().size() > max) max = series.getColumn().size();
		}
		return max;
	}
	
	
	private String printHeader() {
		String out;
		out = "Index\t\t";
		for (Series series : dataframe) 
			out += series.getName() + "\t\t";
		out += "\n";
		return out;
	}
	
	private String printCore(int min, int max) {
		String out="";
		for (int i = min; i < max; i++) {
			out += "["+i+"]\t\t";
			for (int j = 0; j < dataframe.size(); j++) {
				if(dataframe.get(j).getColumn().size() > i)
					out += dataframe.get(j).getColumn().get(i)+"\t\t";
				else
					out += ("\t\t");
			}
			out += "\n";
		}
		return out;
	}
	
	public String printDataframe() {
		return printHeader()+printCore(0, getMaxSizeSeries());
	}
	
	public String printDataframeFirstLines(int nb) {
		int max = getMaxSizeSeries();
		return printHeader() + printCore(0, nb > max ? max : nb) ;
	}
	
	public String printDataframeLastLines(int nb) {
		int max = getMaxSizeSeries();
		return printHeader() + printCore(nb < max ? max - nb : 0, max);
	}
	
	public void afficheDataframe() {
        for (Series<?> s : dataframe) {
            System.out.print(s.getName() + "\t");
            for (Object o : s.getColumn())
                System.out.print(o + "\t");
            System.out.println();
        }
        System.out.println();
    }
}
