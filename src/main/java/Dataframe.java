import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Dataframe {
	
	
	public static void main(String[] args) {
		Object[][] data = new Object[8][8 + 1];
		for (int i = 0; i < 8; i++) {
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
				data[2][i] = i;
				data[3][i] = i * 10;
				data[4][i] = Integer.toString(i * 10);
				data[5][i] = (float) i * 10;
				data[6][i] = i * 100;
				data[7][i] = Integer.toString(i * 100);
			}
		}
		try {
			Dataframe d = new Dataframe(data);
			d.afficheDataframe();
			System.out.println("Max");
			d.getMaxEachColumn().afficheDataframe();
			System.out.println("Min");
			d.getMinEachColumn().afficheDataframe();
			System.out.println("Mean");
			d.getMeanEachColumn().afficheDataframe();
			System.out.println("Sd");
			d.getSdEachColumn().afficheDataframe();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	public void afficheDataframe() {
		for (Series<?> s : dataframe) {
			System.out.print(s.getName() + "\t");
			for (Object o : s.getColumn())
				System.out.print(o + "\t");
			System.out.println();
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

	@SuppressWarnings("rawtypes")
	public ArrayList<Series> getDataframe() {
		return dataframe;
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
	
	private void ajoutSeries(Series<?> s) {
		this.dataframe.add(s);
	}
	
	public Object getMeanColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			if (s.getSize() > 0 && s.getElem(0) instanceof String)
				return "NaN";
			float mean = 0;
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Float)
					mean += (Float) s.getElem(i);
				else if (s.getElem(i) instanceof Integer)
					mean += (Integer) s.getElem(i);
			}
			return mean / s.getSize();
		} 
		return "NaN";
	}
	
	public Dataframe getMeanEachColumn() {
		Dataframe dataframe_mean = new Dataframe();
		for (int i = 0; i < dataframe.size(); i++) {
			Series<?> s = dataframe.get(i);
			if (s.getSize() > 0 && (s.getElem(0) instanceof Float || s.getElem(0) instanceof Integer)) {
				Series<Float> serie_mean = new Series<>();
				serie_mean.setName(dataframe.get(i).getName());
				serie_mean.getColumn().add((Float) getMeanColumn(i));
				dataframe_mean.ajoutSeries(serie_mean);
			} else if (s.getSize() > 0 && s.getElem(0) instanceof String) {
				Series<String> serie_mean = new Series<>();
				serie_mean.setName(dataframe.get(i).getName());
				serie_mean.getColumn().add((String) getMeanColumn(i));
				dataframe_mean.ajoutSeries(serie_mean);
			}
		}
		return dataframe_mean;
	}
	
	public Object getMaxColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			float max = 0;
			if (s.getSize() > 0 && s.getElem(0) instanceof Float)
				max = (Float) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof Integer)
				max = (Integer) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof String)
				return "Nan";
			
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Float)
					max = max > (Float) s.getElem(i) ? max : (Float) s.getElem(i);
				else if (s.getElem(i) instanceof Integer)
					max = max > (Integer) s.getElem(i) ? max : (Integer) s.getElem(i);
			}
			return max;
		}
		return "NaN";
	}
	
	public Dataframe getMaxEachColumn() {
		Dataframe dataframe_max = new Dataframe();
		for (int i = 0; i < dataframe.size(); i++) {
			Series<?> s = dataframe.get(i);
			if (s.getSize() > 0 && (s.getElem(0) instanceof Float || s.getElem(0) instanceof Integer)) {
				Series<Float> serie_max = new Series<>();
				serie_max.setName(dataframe.get(i).getName());
				serie_max.getColumn().add((Float) getMaxColumn(i));
				dataframe_max.ajoutSeries(serie_max);
			} else if (s.getSize() > 0 && s.getElem(0) instanceof String) {
				Series<String> serie_max = new Series<>();
				serie_max.setName(dataframe.get(i).getName());
				serie_max.getColumn().add((String) getMaxColumn(i));
				dataframe_max.ajoutSeries(serie_max);
			}
		}
		return dataframe_max;
	}
	
	public Object getMinColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			float min = 0;
			if (s.getSize() > 0 && s.getElem(0) instanceof Float)
				min = (Float) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof Integer)
				min = (Integer) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof String)
				return "Nan";
			
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Float)
					min = min < (Float) s.getElem(i) ? min : (Float) s.getElem(i);
				else if (s.getElem(i) instanceof Integer)
					min = min < (Integer) s.getElem(i) ? min : (Integer) s.getElem(i);
			}
			return min;
		}
		return "NaN";
	}
	
	public Dataframe getMinEachColumn() {
		Dataframe dataframe_max = new Dataframe();
		for (int i = 0; i < dataframe.size(); i++) {
			Series<?> s = dataframe.get(i);
			if (s.getSize() > 0 && (s.getElem(0) instanceof Float || s.getElem(0) instanceof Integer)) {
				Series<Float> serie_max = new Series<>();
				serie_max.setName(dataframe.get(i).getName());
				serie_max.getColumn().add((Float) getMinColumn(i));
				dataframe_max.ajoutSeries(serie_max);
			} else if (s.getSize() > 0 && s.getElem(0) instanceof String) {
				Series<String> serie_max = new Series<>();
				serie_max.setName(dataframe.get(i).getName());
				serie_max.getColumn().add((String) getMinColumn(i));
				dataframe_max.ajoutSeries(serie_max);
			}
		}
		return dataframe_max;
	}
	
	public Object getSdColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			if (s.getSize() > 0 && (s.getElem(0) instanceof Float || s.getElem(0) instanceof Integer)) {
				float mean = (float) getMinColumn(index);
				float standardDeviation  = 0;
				for (int i = 0; i < s.getSize(); i++) {
					if (s.getElem(i) instanceof Float)
						standardDeviation  += Math.pow((Float) s.getElem(i) - mean, 2);
					else if (s.getElem(i) instanceof Integer)
						standardDeviation  += Math.pow((Integer) s.getElem(i) - mean, 2);
				}
				return (float) Math.sqrt(standardDeviation  / s.getSize());
			} else if (s.getSize() > 0 && s.getElem(0) instanceof String) {
				return "NaN";
			}
		}
		return "NaN";
	}
	
	public Dataframe getSdEachColumn() {
		Dataframe dataframe_max = new Dataframe();
		for (int i = 0; i < dataframe.size(); i++) {
			Series<?> s = dataframe.get(i);
			if (s.getSize() > 0 && (s.getElem(0) instanceof Float || s.getElem(0) instanceof Integer)) {
				Series<Float> serie_max = new Series<>();
				serie_max.setName(dataframe.get(i).getName());
				serie_max.getColumn().add((Float) getSdColumn(i));
				dataframe_max.ajoutSeries(serie_max);
			} else if (s.getSize() > 0 && s.getElem(0) instanceof String) {
				Series<String> serie_max = new Series<>();
				serie_max.setName(dataframe.get(i).getName());
				serie_max.getColumn().add((String) getSdColumn(i));
				dataframe_max.ajoutSeries(serie_max);
			}
		}
		return dataframe_max;
	}

	public Dataframe splitPercent_SortedData(float percent) {
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
