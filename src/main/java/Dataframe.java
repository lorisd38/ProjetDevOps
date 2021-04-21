import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Dataframe {
	public static void main(String[] args) {
		Object[][] tab = {{"B", 3,6,9,2,5,8,1,4,7,0}, {"A", 1.5,4.1,7.6,2.3,5.1,8.7,3.9,6.5,9.1,1.5}, {"C","a","f","c","b","r","e","t","y","z","q"}};
		Dataframe data;
		try {
			data = new Dataframe(tab);
			data.afficheDataframe();
			System.out.println("=================");
			data.splitPercent_SortedData(34).afficheDataframe();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	ArrayList<Series> dataframe;
	public Dataframe() {
		this.dataframe = new ArrayList<>();
	}
	
	public Dataframe(Object[][] tableau) throws Exception {
		if (tableau == null || tableau.length == 0)
			throw new Exception();
		this.dataframe = new ArrayList<>();
		for (int i = 0; i < tableau.length; i ++) {
			Object[] c = tableau[i];
			if (c.length == 1) {
				dataframe.add(new Series<>((String)c[0]));
			} else if (c.length > 1) {
				Object elem = c[1];
				if (elem instanceof String)
					ajoutColonne("String", c);
				else if (elem instanceof Integer)
					ajoutColonne("Integer", c);
				else if (elem instanceof Double)
					ajoutColonne("Double", c);
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
                Series<Double> sf = new Series<>();
                for (int i = 0; i < c.length; i ++) {
                    if (i == 0) sf.setName((String)c[0]);
                    else sf.add((Double)c[i]);
                }
                dataframe.add(sf);
        }
    }
	
	private <E> void ajoutSeries(Series<E> s) {
		this.dataframe.add(s);		
	}

	public void afficheDataframe() {
		for (Series<?> s : dataframe) {
			System.out.print(s.getName() + "\t");
			for (Object o : s.getColumn())
				System.out.print(o + "\t");
			System.out.println();
		}
	}
	
	public double getMeanColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			float mean = 0;
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Double)
					mean += (Double) s.getElem(i);
				else if (s.getElem(i) instanceof Integer)
					mean += (Integer) s.getElem(i);
			}
			return mean / s.getSize();
		} else {
			return -1;//TODO Exception
		}
	}
	
	public Dataframe getMeanEachColumn() {
		Dataframe dataframe_mean = new Dataframe();
		for (int i = 0; i < dataframe.size(); i++) {
			Series<Double> serie_mean = new Series<>();
			serie_mean.setName(dataframe.get(i).getName());
			serie_mean.getColumn().add(getMeanColumn(i));
			dataframe_mean.ajoutSeries(serie_mean);
		}
		return dataframe_mean;
	}
	
	public double getMaxColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			double max = 0;
			if (s.getSize() > 0 && s.getElem(0) instanceof Double)
				max = (Double) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof Integer)
				max = (Integer) s.getElem(0);
			
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Double)
					max = max > (Double) s.getElem(i) ? max : (Double) s.getElem(i);
				else if (s.getElem(i) instanceof Integer)
					max = max > (Integer) s.getElem(i) ? max : (Integer) s.getElem(i);
			}
			return max;
		} else {
			return -1;//TODO Exception
		}
	}
	
	public Dataframe getMaxEachColumn() {
		Dataframe dataframe_max = new Dataframe();
		for (int i = 0; i < dataframe.size(); i++) {
			Series<Double> serie_max = new Series<>();
			serie_max.setName(dataframe.get(i).getName());
			serie_max.getColumn().add(getMaxColumn(i));
			dataframe_max.ajoutSeries(serie_max);
		}
		return dataframe_max;
	}
	
	public double getMinColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			double min = 0;
			if (s.getSize() > 0 && s.getElem(0) instanceof Double)
				min = (Double) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof Integer)
				min = (Integer) s.getElem(0);
			
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Double)
					min = min < (Double) s.getElem(i) ? min : (Double) s.getElem(i);
				else if (s.getElem(i) instanceof Integer)
					min = min < (Integer) s.getElem(i) ? min : (Integer) s.getElem(i);
			}
			return min;
		} else {
			return -1;//TODO Exception
		}
	}
	
	public Dataframe getMinEachColumn() {
		Dataframe dataframe_max = new Dataframe();
		for (int i = 0; i < dataframe.size(); i++) {
			Series<Double> serie_max = new Series<>();
			serie_max.setName(dataframe.get(i).getName());
			serie_max.getColumn().add(getMinColumn(i));
			dataframe_max.ajoutSeries(serie_max);
		}
		return dataframe_max;
	}
	
	public double getSdColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			double mean = getMinColumn(index);
			double standardDeviation  = 0;
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Double)
					standardDeviation  += Math.pow((Double) s.getElem(i) - mean, 2);
				else if (s.getElem(i) instanceof Integer)
					standardDeviation  += Math.pow((Integer) s.getElem(i) - mean, 2);
			}
			return (double) Math.sqrt(standardDeviation  / s.getSize());
		} else {
			return -1;//TODO Exception
		}
	}
	
	public Dataframe getSdEachColumn() {
		Dataframe dataframe_max = new Dataframe();
		for (int i = 0; i < dataframe.size(); i++) {
			Series<Double> serie_max = new Series<>();
			serie_max.setName(dataframe.get(i).getName());
			serie_max.getColumn().add(getSdColumn(i));
			dataframe_max.ajoutSeries(serie_max);
		}
		return dataframe_max;
	}

	public Dataframe splitPercent_SortedData(double percent) {
		if(percent < 0 || percent > 100) //TODO Exception 
			return null;
		Dataframe dataframeSplited = new Dataframe();
		for (int i = 0; i < dataframe.size(); i++) {
			Series<?> s = dataframe.get(i);
			//On trie par ordre croissant
			List<Object> ssorted = s.getColumn().stream().sorted().collect(Collectors.toList());
			//On creee 2 nouvelles listes
			ArrayList<Object> sStart = new ArrayList<>();
			ArrayList<Object> sEnd = new ArrayList<>();
			//On recupere le nombre de valeurs qu'on devra copier
			int limit = (int) Math.floor(ssorted.size() * (percent/100));
			//On copie les valeurs
			for (int j = 0; j < limit; j++) {
				sStart.add(ssorted.get(j));
				sEnd.add(ssorted.get(ssorted.size()-limit+j));
			}
			//On ajoute les deux nouvelles Series
			dataframeSplited.ajoutSeries((new Series<>(s.getName()+"_FIRST", sStart)));
			dataframeSplited.ajoutSeries((new Series<>(s.getName()+"_LAST", sEnd)));
		}
		return dataframeSplited;
	}
}