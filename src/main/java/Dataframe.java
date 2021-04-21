import java.util.ArrayList;

public class Dataframe {
	public static void main(String[] args) {
		Object[][] tab = {{"Tab1", 1,2,3,4}, {"Tab2", 2, 2, 3}};
		Dataframe data;
		try {
			data = new Dataframe(tab);
			data.getSdEachColumn().afficheDataframe();
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
	
	public float getMeanColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			float mean = 0;
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Float)
					mean += (Float) s.getElem(i);
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
			Series<Float> serie_mean = new Series<>();
			serie_mean.setName(dataframe.get(i).getName());
			serie_mean.getColumn().add(getMeanColumn(i));
			dataframe_mean.ajoutSeries(serie_mean);
		}
		return dataframe_mean;
	}
	
	public float getMaxColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			float max = 0;
			if (s.getSize() > 0 && s.getElem(0) instanceof Float)
				max = (Float) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof Integer)
				max = (Integer) s.getElem(0);
			
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Float)
					max = max > (Float) s.getElem(i) ? max : (Float) s.getElem(i);
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
			Series<Float> serie_max = new Series<>();
			serie_max.setName(dataframe.get(i).getName());
			serie_max.getColumn().add(getMaxColumn(i));
			dataframe_max.ajoutSeries(serie_max);
		}
		return dataframe_max;
	}
	
	public float getMinColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			float min = 0;
			if (s.getSize() > 0 && s.getElem(0) instanceof Float)
				min = (Float) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof Integer)
				min = (Integer) s.getElem(0);
			
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Float)
					min = min < (Float) s.getElem(i) ? min : (Float) s.getElem(i);
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
			Series<Float> serie_max = new Series<>();
			serie_max.setName(dataframe.get(i).getName());
			serie_max.getColumn().add(getMinColumn(i));
			dataframe_max.ajoutSeries(serie_max);
		}
		return dataframe_max;
	}
	
	public float getSdColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			float mean = getMinColumn(index);
			float standardDeviation  = 0;
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Float)
					standardDeviation  += Math.pow((Float) s.getElem(i) - mean, 2);
				else if (s.getElem(i) instanceof Integer)
					standardDeviation  += Math.pow((Integer) s.getElem(i) - mean, 2);
			}
			return (float) Math.sqrt(standardDeviation  / s.getSize());
		} else {
			return -1;//TODO Exception
		}
	}
	
	public Dataframe getSdEachColumn() {
		Dataframe dataframe_max = new Dataframe();
		for (int i = 0; i < dataframe.size(); i++) {
			Series<Float> serie_max = new Series<>();
			serie_max.setName(dataframe.get(i).getName());
			serie_max.getColumn().add(getSdColumn(i));
			dataframe_max.ajoutSeries(serie_max);
		}
		return dataframe_max;
	}

	
}