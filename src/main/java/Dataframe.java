import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import exception.PandaCannotInstanciate;
import exception.PandaExceptions;
import exception.PandaNameNotFound;
import exception.PandaNoData;
import exception.PandaNotSupported;
import exception.PandaOutOfBound;
import exception.TooManyDataException;
import exception.TooManyValueException;

/**
 *
 * @author ALL Classe principale permettant de contruire le dataframe
 *
 */

@SuppressWarnings("rawtypes")
public class Dataframe {
	private ArrayList<Series> dataframe;

	protected final String DOUBLE = "DOUBLE";
	protected final String INTEGER = "Integer";
	protected final String STRING = "String";

	// Separateur, afin de pouvoir lire tous les types de fichiers CSV
	private String splitter = " ";

	// Representation de la vigule pour split les doubles
	private String VIRGULE = ",";

	public Dataframe() {
		this.splitter = ";";
	}
  
	/**
	 * @param tableau d'objets a deux dimensions
	 * @throws PandaNoData       tableau vide ou colonne vide
	 * @throws PandaNotSupported type non supporte
	 */
	public Dataframe(Object[][] tableau) throws Exception {
		if (tableau == null || tableau.length == 0)
			throw new PandaNoData();
		this.dataframe = new ArrayList<>();
		for (int i = 0; i < tableau.length; i++) {
			Object[] c = tableau[i];
			if (c.length == 1)
				dataframe.add(new Series<>((String) c[0]));
			else if (c.length > 1) {
				Object elem = c[1];
				if (elem instanceof String)
					ajoutColonne("String", c);
				else if (elem instanceof Integer)
					ajoutColonne("Integer", c);
				else if (elem instanceof Double)
					ajoutColonne("Double", c);
				else if (elem == null)
					ajoutColonne("null", c);
				else
					throw new PandaNotSupported();
			} else
				throw new PandaNoData();
		}
	}
  
	/**
	 * @param PATH le chemin vers un csv
	 *
	 */
	public Dataframe(String PATH) throws Exception {
		this.splitter = ";";
		initialisation(toTab(PATH));
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

	/**
	 * @param type le type de la colonne,
	 * @param c    un tableau d'objet a une dimension
	 * @throws PandaCannotInstanciate type dans la même colonne different
	 * @throws PandaNoData            colonne vide
	 * @throws PandaNotSupported      type non supporte
	 */
	@SuppressWarnings("unchecked")
	private void ajoutColonne(String type, Object[] c) throws Exception {
        switch (type) {
        case "String":
            Series<String> s = new Series<>();
            for (int i = 0; i < c.length; i++) {
                if (i == 0)
                    s.setName((String) c[0]);
                else if (c[i] instanceof String || c[i] == null)
                    s.add((String) c[i]);
                else
                    throw new PandaCannotInstanciate();
            }
            dataframe.add(s);
            break;
        case "Integer":
            Series<Integer> si = new Series<>();
            for (int i = 0; i < c.length; i++) {
                if (i == 0)
                    si.setName((String) c[0]);
                else if (c[i] instanceof Integer || c[i] == null)
                    si.add((Integer) c[i]);
                else 
                    throw new PandaCannotInstanciate();
            }
            dataframe.add(si);
            break;
        case "null":
            int j = 2;
            while (j < c.length && c[j] == null)
                j++;
            if (j >= c.length) {
                Series sn = new Series();
                for (int i = 0; i < c.length; i++) {
                    if (i == 0)
                        sn.setName((String) c[0]);
                    else
                        sn.add(null);
                }
                dataframe.add(sn);
            }
            else if (c[j] instanceof String)
                ajoutColonne("String", c);
            else if (c[j] instanceof Integer)
                ajoutColonne("Integer", c);
            else if (c[j] instanceof Double)
                ajoutColonne("Double", c);
            else
                throw new PandaNotSupported();
            break;
        default:
            Series<Double> sf = new Series<>();
            for (int i = 0; i < c.length; i++) {
                if (i == 0)
                    sf.setName((String) c[0]);
                else if (c[i] instanceof Double || c[i] == null)
                    sf.add((Double) c[i]);
                else 
                    throw new PandaCannotInstanciate();
            }
            dataframe.add(sf);
        }
    }
	/**
	 * @param s         Le Series a mettre a jour,
	 * @param arguments les valeurs
	 * @param position  l'index de la colonne courante
	 * @return Series Le Series est mit a jour avec la colonne ajoutee
	 *
	 */
	@SuppressWarnings("unchecked")
	public Series ajouterTout(Series s, String[][] arguments, int position) {
		for (int i = 1; i < arguments.length; i++)
			s.ajouter(arguments[i][position]);
		return s;
	}

	private boolean estEgale(Object elem, Object o) {
		if (elem instanceof Integer && o instanceof Integer)
			return (int) elem == (int) o;
		if (elem instanceof String && o instanceof String)
			return elem.equals(o);
		if (elem instanceof Double && o instanceof Double)
			return (double) elem == (double) o;
		return false;
	}

	/**
	 * @return le dataframe
	 *
	 */
	public ArrayList<Series> getDataframe() {
		return dataframe;
	}

	public int getMaxSizeSeries() {
		int max = 0;
		for (Series series : dataframe) {
			if (series.getColumn().size() > max)
				max = series.getColumn().size();
		}
		return max;
	}
  
	public String getName(String[][] liste, int colonneActuelle) {
		return liste[0][colonneActuelle];
	}
	
	public Object getMeanColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			if (s.getSize() > 0 && s.getElem(0) instanceof String)
				return "NaN";
			double mean = 0;
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Double)
					mean += (Double) s.getElem(i);
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
			if (s.getSize() > 0 && (s.getElem(0) instanceof Double || s.getElem(0) instanceof Integer)) {
				Series<Double> serie_mean = new Series<>();
				serie_mean.setName(dataframe.get(i).getName());
				serie_mean.getColumn().add((Double) getMeanColumn(i));
				dataframe_mean.ajoutSeries(serie_mean);
			} else if (s.getSize() > 0 && s.getElem(0) instanceof String) {
				Series<String> serie_mean = new Series<>();
				serie_mean.setName(dataframe.get(i).getName());
				serie_mean.getColumn().add((String) getMeanColumn(i));
				dataframe_mean.ajoutSeries(serie_mean);
			} else {
				Series<String> serie_null = new Series<>();
				serie_null.setName(dataframe.get(i).getName());
				serie_null.getColumn().add("null");
				dataframe_mean.ajoutSeries(serie_null);
			}
		}
		return dataframe_mean;
	}
	
	private void ajoutSeries(Series<?> s) {
		if(dataframe==null)
			dataframe = new ArrayList<>();
		this.dataframe.add(s);
	}
	
	public Object getMaxColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			double max = 0;
			if (s.getSize() > 0 && s.getElem(0) instanceof Double)
				max = (Double) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof Integer)
				max = (Integer) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof String)
				return "Nan";
			
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Double)
					max = max > (Double) s.getElem(i) ? max : (Double) s.getElem(i);
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
			if (s.getSize() > 0 && (s.getElem(0) instanceof Double || s.getElem(0) instanceof Integer)) {
				Series<Double> serie_max = new Series<>();
				serie_max.setName(dataframe.get(i).getName());
				serie_max.getColumn().add((Double) getMaxColumn(i));
				dataframe_max.ajoutSeries(serie_max);
			} else if (s.getSize() > 0 && s.getElem(0) instanceof String) {
				Series<String> serie_max = new Series<>();
				serie_max.setName(dataframe.get(i).getName());
				serie_max.getColumn().add((String) getMaxColumn(i));
				dataframe_max.ajoutSeries(serie_max);
			} else {
				Series<String> serie_null = new Series<>();
				serie_null.setName(dataframe.get(i).getName());
				serie_null.getColumn().add("null");
				dataframe_max.ajoutSeries(serie_null);
			}
		}
		return dataframe_max;
	}
	
	public Object getMinColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			double min = 0;
			if (s.getSize() > 0 && s.getElem(0) instanceof Double)
				min = (Double) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof Integer)
				min = (Integer) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof String)
				return "Nan";
			
			for (int i = 0; i < s.getSize(); i++) {
				if (s.getElem(i) instanceof Double)
					min = min < (Double) s.getElem(i) ? min : (Double) s.getElem(i);
				else if (s.getElem(i) instanceof Integer)
					min = min < (Integer) s.getElem(i) ? min : (Integer) s.getElem(i);
			}
			return min;
		}
		return "NaN";
	}
	
	public Dataframe getMinEachColumn() {
		Dataframe dataframe_min = new Dataframe();
		for (int i = 0; i < dataframe.size(); i++) {
			Series<?> s = dataframe.get(i);
			if (s.getSize() > 0 && (s.getElem(0) instanceof Double || s.getElem(0) instanceof Integer)) {
				Series<Double> serie_min = new Series<>();
				serie_min.setName(dataframe.get(i).getName());
				serie_min.getColumn().add((Double) getMinColumn(i));
				dataframe_min.ajoutSeries(serie_min);
			} else if (s.getSize() > 0 && s.getElem(0) instanceof String) {
				Series<String> serie_min = new Series<>();
				serie_min.setName(dataframe.get(i).getName());
				serie_min.getColumn().add((String) getMinColumn(i));
				dataframe_min.ajoutSeries(serie_min);
			} else {
				Series<String> serie_null = new Series<>();
				serie_null.setName(dataframe.get(i).getName());
				serie_null.getColumn().add("null");
				dataframe_min.ajoutSeries(serie_null);
			}
		}
		return dataframe_min;
	}
	
	public Object getSdColumn(int index) {
		if(index < dataframe.size()) {
			Series<?> s = dataframe.get(index);
			if (s.getSize() > 0 && (s.getElem(0) instanceof Double || s.getElem(0) instanceof Integer)) {
				double mean = (double) getMinColumn(index);
				double standardDeviation  = 0;
				for (int i = 0; i < s.getSize(); i++) {
					if (s.getElem(i) instanceof Double)
						standardDeviation  += Math.pow((Double) s.getElem(i) - mean, 2);
					else if (s.getElem(i) instanceof Integer)
						standardDeviation  += Math.pow((Integer) s.getElem(i) - mean, 2);
				}
				double res = (double) Math.sqrt(standardDeviation  / s.getSize());
				return (double) Math.round(res * 100) / 100;
			} else if (s.getSize() > 0 && s.getElem(0) instanceof String) {
				return "NaN";
			}
		}
		return "NaN";
	}
	
	public Dataframe getSdEachColumn() {
		Dataframe dataframe_sd = new Dataframe();
		for (int i = 0; i < dataframe.size(); i++) {
			Series<?> s = dataframe.get(i);
			if (s.getSize() > 0 && (s.getElem(0) instanceof Double || s.getElem(0) instanceof Integer)) {
				Series<Double> serie_sd = new Series<>();
				serie_sd.setName(dataframe.get(i).getName());
				serie_sd.getColumn().add( (double) getSdColumn(i) );
				dataframe_sd.ajoutSeries(serie_sd);
			} else if (s.getSize() > 0 && s.getElem(0) instanceof String) {
				Series<String> serie_sd = new Series<>();
				serie_sd.setName(dataframe.get(i).getName());
				serie_sd.getColumn().add((String) getSdColumn(i));
				dataframe_sd.ajoutSeries(serie_sd);
			} else {
				Series<String> serie_null = new Series<>();
				serie_null.setName(dataframe.get(i).getName());
				serie_null.getColumn().add("null");
				dataframe_sd.ajoutSeries(serie_null);
			}
		}
		return dataframe_sd;
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

	/**
	 * @param arguments un tableau de donnees
	 * @return void - initialise juste les dataframe
	 *
	 */
	public void initialisation(String[][] arguments) {
		dataframe = new ArrayList<>();
		for (int i = 0; i < arguments.length; i++) {
			String name = getName(arguments, i);
			Series s = null;
			switch (type(arguments[i])) {
			case INTEGER:
				s = new Series<Integer>();
				break;
			case STRING:
				s = new Series<String>();
				break;
			case DOUBLE:
				s = new Series<Double>();
				break;
			default:
				System.err.println("LA COLONNE " + i + " CONTIENT UN TYPE INCONNU");
				System.exit(0);
			}
	
			s.setName(name);
			s = ajouterTout(s, arguments, i);
			dataframe.add(s);
		}
	}

	/**
	 *
	 * @param valeur
	 * @return 1 si valeur est un double, 0 sinon
	 */
	public boolean isDouble(String valeur) {
		String[] all = valeur.split(VIRGULE);

		return all.length == 2 && isInt(all[0]) && isInt(all[1]);
	}

	/**
	 *
	 * @param valeur
	 * @return 1 si valeur est un entier, 0 sinon
	 */
	public boolean isInt(String valeur) {
		return valeur.matches("-?\\d+");
	}

	/**
	 *
	 * @param line
	 * @return un tableau de chaine de caractere correspondant a la ligne decoupee
	 * @throws TooManyDataException si trop de donnees sont a gerer
	 */
	public String[] nextLine(String line) throws TooManyDataException {
		String[] toRet = line.split(splitter);
		if (toRet.length >= PandaExceptions.MAX_DATA)
			throw new TooManyDataException();
		return toRet;
	}

	private Dataframe searchIndice(Series s, Object elem) throws Exception {
		ArrayList<Integer> l = new ArrayList<>();
		int indice = 0;
		for (Object o : s.getColumn()) {
			if (o != null && estEgale(elem, o))
				l.add(indice);
			indice++;
		}
		return selectionLignes(l);
	}

	private String printCore(int min, int max) {
		String out = "";
		for (int i = min; i < max; i++) {
			out += "[" + i + "]\t\t";
			for (int j = 0; j < dataframe.size(); j++) {
				if (dataframe.get(j).getColumn().size() > i)
					out += dataframe.get(j).getColumn().get(i) + "\t\t";
				else
					out += ("\t\t");
			}
			out += "\n";
		}
		return out;
	}

	public String printDataframe() {
		return printHeader() + printCore(0, getMaxSizeSeries());
	}

	public String printDataframeFirstLines(int nb) {
		int max = getMaxSizeSeries();
		return printHeader() + printCore(0, nb > max ? max : nb);
	}

	public String printDataframeLastLines(int nb) {
		int max = getMaxSizeSeries();
		return printHeader() + printCore(nb < max ? max - nb : 0, max);
	}

	private String printHeader() {
		String out;
		out = "Index\t\t";
		for (Series series : dataframe)
			out += series.getName() + "\t\t";
		out += "\n";
		return out;
	}

	/**
	 * @param ligne numero de ligne
	 * @return un dataframe contenant la ligne passee en argument
	 * @throws PandaOutOfBound indice trop grand ou trop petit
	 */
	public Dataframe selection(int ligne) throws Exception {
		if (ligne > dataframe.get(0).getSize() || ligne < 0)
			throw new PandaOutOfBound();
		Object[][] data = new Object[dataframe.size()][2];
		int i = 0;
		for (Series s : dataframe) {
			data[i][0] = s.getName();
			data[i][1] = s.getElem(ligne);
			i++;
		}
		return new Dataframe(data);
	}

	/**
	 * @param nom nom de la colonne
	 * @return un dataframe contenant la colonne du nom passe en argument
	 * @throws PandaNameNotFound nom non present dans le dataframe
	 */
	public Dataframe selectionColonne(String nom) throws Exception {
		Object[][] data = null;
		for (Series s : dataframe) {
			if (s.getName() == nom) {
				data = new Object[1][s.getSize() + 1];
				data[0][0] = s.getName();
				int i = 1;
				for (Object o : s.getColumn()) {
					data[0][i] = o;
					i++;
				}
				return new Dataframe(data);
			}
		}
		throw new PandaNameNotFound();
	}

	/**
	 * @param noms liste contenant les noms des colonnes a renvoyer
	 * @return un dataframe contenant les colonnes correspondant aux noms donnes en
	 *         argument
	 * @throws PandaNameNotFound nom non present dans le dataframe
	 * @throws PandaNoData       liste vide ou nulle
	 */
	public Dataframe selectionColonnes(ArrayList<String> noms) throws Exception {
		if (noms == null || noms.size() == 0)
			throw new PandaNoData();
		Object[][] data = null;
		int nbNom = 0;
		boolean trouve;
		for (String nom : noms) {
			trouve = false;
			for (Series s : dataframe) {
				if (nom == s.getName()) {
					trouve = true;
					if (data == null)
						data = new Object[noms.size()][s.getSize() + 1];
					data[nbNom][0] = s.getName();
					int i = 1;
					for (Object o : s.getColumn()) {
						data[nbNom][i] = o;
						i++;
					}
					nbNom++;
				}
			}
			if (!trouve)
				throw new PandaNameNotFound();
		}
		return new Dataframe(data);
	}

	/**
	 * @param ligne   indice de la ligne contenant l'element
	 * @param colonne indice de la colonne contenant l'element
	 * @return l'element a l'emplacement donne
	 * @throws PandaOutOfBound indice trop grand ou trop petit
	 */
	public Object selectionElement(int ligne, int colonne) throws Exception {
		if (colonne >= dataframe.size() || colonne < 0)
			throw new PandaOutOfBound();
		if (ligne >= dataframe.get(colonne).getSize() || ligne < 0)
			throw new PandaOutOfBound();
		return dataframe.get(colonne).getElem(ligne);
	}

	/**
	 * @param ligne   liste d'indices des lignes a selectionner
	 * @param colonne liste d'indices des colonne a selectionner
	 * @return un dataframe contenant les elements des lignes et colonnes a
	 *         selectionner
	 * @throws PandaNoData     liste vide ou nulle
	 * @throws PandaOutOfBound indice trop grand ou trop petit
	 */
	public Dataframe selectionElements(ArrayList<Integer> ligne, ArrayList<Integer> colonne) throws Exception {
		if (ligne == null || ligne.size() == 0 || colonne == null || colonne.size() == 0)
			throw new PandaNoData();
		Object[][] data = new Object[colonne.size()][ligne.size() + 1];
		int i = 0, j;
		for (int c : colonne) {
			j = 1;
			for (int l : ligne) {
				if (c >= dataframe.size() || c < 0)
					throw new PandaOutOfBound();
				Series s = dataframe.get(c);
				if (l >= s.getSize() || l < 0)
					throw new PandaOutOfBound();
				data[i][0] = s.getName();
				data[i][j] = s.getElem(l);
				j++;
			}
			i++;
		}
		return new Dataframe(data);
	}

	/**
	 * @param lignes liste d'indices des lignes a selectionner
	 * @return un dataframe contenant les lignes a selectionner
	 * @throws PandaNoData     liste vide ou nulle
	 * @throws PandaOutOfBound indice trop grand ou trop petit
	 */
	public Dataframe selectionLignes(ArrayList<Integer> lignes) throws Exception {
		if (lignes == null || lignes.size() == 0)
			throw new PandaNoData();
		Object[][] data = new Object[dataframe.size()][lignes.size() + 1];
		int i = 0, k;
		for (Series s : dataframe) {
			data[i][0] = s.getName();
			k = 1;
			for (int j : lignes) {
				if (j >= s.getSize() || j < 0)
					throw new PandaOutOfBound();
				data[i][k] = s.getElem(j);
				k++;
			}
			i++;
		}
		return new Dataframe(data);
	}

	/**
	 * @param masque liste de booleens
	 * @return un dataframe contenant les lignes a selectionner
	 * @throws PandaNoData liste vide ou pas de la bonne taille
	 */
	public Dataframe selectionMasque(ArrayList<Boolean> masque) throws Exception {
		if (masque == null || masque.size() != dataframe.get(0).getSize())
			throw new PandaNoData();
		ArrayList<ArrayList<Object>> l = new ArrayList<>();
		int i = 0;
		for (Series s : dataframe) {
			ArrayList<Object> l_inter = new ArrayList<>();
			l_inter.add(s.getName());
			for (int j = 0; j < masque.size(); j++)
				if (masque.get(j))
					l_inter.add(s.getElem(j));
			l.add(l_inter);
		}
		Object[][] data = new Object[l.size()][l.get(0).size()];
		for (i = 0; i < l.size(); i++) {
			for (int k = 0; k < l.get(0).size(); k++)
				data[i][k] = l.get(i).get(k);
		}
		return new Dataframe(data);
	}
	
	public Dataframe selectionParObjet(String nomColonne, Object elem) throws Exception {
		for (Series s : dataframe) {
			String name = s.getName();
			if (nomColonne.equals(name)) {
				return searchIndice(s, elem);
			}
		}
		throw new PandaNameNotFound();
	}
	/**
	 *
	 * @param PATH le chemin vers un fichier CSV
	 * @return le tableau de chaine de caractere correspondant aux valeurs dans le
	 *         fichier
	 * @throws TooManyValuesException si les colonnes sont trop grandes
	 * @throws Exception              si le fichier n'existe pas, geree par le
	 *                                lecteur de fichier
	 */
	public String[][] toTab(String PATH) throws Exception {
		String[][] tab = new String[PandaExceptions.MAX_VALUES][PandaExceptions.MAX_DATA];
		BufferedReader sc = new BufferedReader(new FileReader(PATH));
		int nbLine = 0;
		String line = sc.readLine();
		while (line != null) {
			tab[nbLine++] = nextLine(line);
			if (nbLine == PandaExceptions.MAX_VALUES) {
				sc.close();
				throw new TooManyValueException();
			}

			line = sc.readLine();
		}
		sc.close();

		return tab;
	}

	/**
    *
    * @param listeObjet, une colonne du tableau
    * @return une chaine de caracteres correspondant au types de donnees
    */
	public String type(String[] listeObjet) {
		String valeur = listeObjet[0];
		if (isInt(valeur))
			return INTEGER;

		if (isDouble(valeur))
			return DOUBLE;

		return STRING;

	}
}