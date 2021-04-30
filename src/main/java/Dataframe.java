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

	// Representation du point pour split les doubles
	private String DOUBLE_SPLITTER = "[.]";

	public Dataframe() {
		this.splitter = ";";
	}

	/**
	 * @param tableau d'objets a deux dimensions
	 * @throws PandaNoData       tableau vide ou colonne vide
	 * @throws PandaNotSupported type non supporte
	 */
	public Dataframe(Object[][] tableau) throws PandaExceptions {
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
	 */
	public Dataframe(String PATH) throws Exception {
		this.splitter = ";";
		initialisation(toTab(PATH));
	}

	/**
	 * @param type le type de la colonne,
	 * @param c    un tableau d'objet a une dimension
	 * @throws PandaCannotInstanciate type dans la même colonne different
	 * @throws PandaNoData            colonne vide
	 * @throws PandaNotSupported      type non supporte
	 */
	@SuppressWarnings("unchecked")
	private void ajoutColonne(String type, Object[] c) throws PandaExceptions {
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

	/**
	 * @return le dataframe
	 *
	 */
	public ArrayList<Series> getDataframe() {
		return dataframe;
	}
	
	/**
	 * @param name nom d'une colonne
	 * @return indice de la colonne dont le nom est passe en parametre
	 * @throws PandaNoData si le nom n'est pas contenu dans le dataframe 
	 */
    private int getIndiceCol(String name) throws PandaNoData {
        int ind = 0;
        for (Series elem : dataframe) {
            if (name.equals(elem.getName()))
                return ind;
            ind++;
        }
        throw new PandaNoData();
    }

	/**
	 * @return Taille de la plus longue colonne du dataframe
	 */
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
	
	/**
   * @param name - le nom sur le quel les donnees seront groupee
   * @return GroupBy - Un ensemble de dataframe groupe par name
   * @throws PandaExceptions si le nom n'est pas bon 
   */
	public GroupBy groupBy(String name) throws PandaExceptions {
      GroupBy toRet = new GroupBy();
      int indiceCol = getIndiceCol(name);
      ArrayList<Object> ao = new ArrayList<Object>();
      for (Object o : this.dataframe.get(indiceCol).getColumn()) {
          if (!ao.contains(o)) {
              toRet.add(selectionParObjet(name, o));
              ao.add(o);
          }
      }
      return toRet;
	}

  /**
	 * @param indice de la colonne
	 * @return moyenne de la colonne d'indice "indice"
	 * <br> si indice > size => NaN
	 */
	public Object getMeanColumn(int indice) {
		if(indice < dataframe.size()) {
			Series<?> s = dataframe.get(indice);
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
	
	/**
	 * @return Nouveau dataframe contenant la moyenne pour chaque colonne
	 * <br>Moyenne d'une colonne String => NaN
	 * <br>Moyenne d'une colonne vide => null
	 */
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
	
	/**
	 * @param s -> nouvelle Series (colonne) à ajouter au dataframe 
	 */
	private void ajoutSeries(Series<?> s) {
		if(dataframe==null)
			dataframe = new ArrayList<>();
		this.dataframe.add(s);
	}
	
	/**
	 * @param indice de la colonne
	 * @return max de la colonne d'indice "indice"
	 * <br> si indice > size => NaN
	 */
	public Object getMaxColumn(int indice) {
		if(indice < dataframe.size()) {
			Series<?> s = dataframe.get(indice);
			double max = 0;
			if (s.getSize() > 0 && s.getElem(0) instanceof Double)
				max = (Double) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof Integer)
				max = (Integer) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof String)
				return "NaN";
			
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
	
	/**
	 * @return Nouveau dataframe contenant la valeur max pour chaque colonne
	 * <br>Max d'une colonne String => NaN
	 * <br>Max d'une colonne vide => null
	 */
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
	
	/**
	 * @param indice de la colonne
	 * @return min de la colonne d'indice "indice"
	 * <br> si indice > size => NaN
	 */
	public Object getMinColumn(int indice) {
		if(indice < dataframe.size()) {
			Series<?> s = dataframe.get(indice);
			double min = 0;
			if (s.getSize() > 0 && s.getElem(0) instanceof Double)
				min = (Double) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof Integer)
				min = (Integer) s.getElem(0);
			else if (s.getSize() > 0 && s.getElem(0) instanceof String)
				return "NaN";
			
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
	
	/**
	 * @return Nouveau dataframe contenant la valeur min pour chaque colonne
	 * <br>Min d'une colonne String => NaN
	 * <br>Min d'une colonne vide => null
	 */
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
	
	/**
	 * @param indice de la colonne
	 * @return ecart type de la colonne d'indice "indice"
	 * <br> si indice > size => NaN
	 */
	public Object getSdColumn(int indice) {
		if(indice < dataframe.size()) {
			Series<?> s = dataframe.get(indice);
			if (s.getSize() > 0 && (s.getElem(0) instanceof Double || s.getElem(0) instanceof Integer)) {
				double mean = (double) getMinColumn(indice);
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
	
	/**
	 * @return Nouveau dataframe contenant l'écart type pour chaque colonne
	 * <br>Ecart type d'une colonne String => NaN
	 * <br>Ecart type d'une colonne vide => null
	 */
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

	/**
	 * @param pourcentage de decoupage des colonnes du dataframe.
	 * @return Nouveau dataframe ayant le double des colonnes initiales et contenant le pourcentage de valeurs (préalablement trié par ordre croissant) de chaque colonne en partant du début et de la fin.
	 * Ex : [[5,9,1,2,3,7,8,4,6,0]] avec pourcentage = 34% | Retourne : [[0,1,2], [7,8,9]]
	 */
	public Dataframe splitPercent_SortedData(double pourcentage) {
		if(pourcentage < 0 || pourcentage > 100) //TODO Exception 
			pourcentage = 0;
		Dataframe dataframeSplited = new Dataframe();
		for (int i = 0; i < dataframe.size(); i++) {
			Series<?> s = dataframe.get(i);
			//On trie par ordre croissant
			List<Object> ssorted = s.getColumn().stream().sorted().collect(Collectors.toList());
			//On creee 2 nouvelles listes
			ArrayList<Object> sStart = new ArrayList<>();
			ArrayList<Object> sEnd = new ArrayList<>();
			//On recupere le nombre de valeurs qu'on devra copier
			int limit = (int) Math.floor(ssorted.size() * (pourcentage/100));
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
	 */
	@SuppressWarnings("unchecked")
 	public void initialisation(String[][] arguments) {
      dataframe = new ArrayList<>();
      for (int i = 0; i < arguments[0].length; i++) {
          String name = getName(arguments, i);
          Series s = null;
          switch (type(arguments[1][i])) {
          case INTEGER:
              s = new Series<Integer>();
              for (int j = 1; j < arguments.length; j++)
            	  s.add( Integer.valueOf(arguments[j][i]) );
              break;
          case STRING:
              s = new Series<String>();
              for (int j = 1; j < arguments.length; j++)
            	  s.add( arguments[j][i] );
              break;
          case DOUBLE:
              s = new Series<Double>();
              for (int j = 1; j < arguments.length; j++)
            	  s.add( Double.valueOf(arguments[j][i]) );
              break;
          default:
              System.err.println("LA COLONNE " + i + " CONTIENT UN TYPE INCONNU");
              System.exit(0);
          }

          s.setName(name);
          //s = ajouterTout(s, arguments, i);
          dataframe.add(s);
      }
  }

	/**
	 *
	 * @param valeur
	 * @return 1 si valeur est un double, 0 sinon
	 */
	public boolean isDouble(String valeur) {
		String[] all = valeur.split(DOUBLE_SPLITTER);
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

	/**
	 * 
	 * @param min -> ligne à partir de laquelle on doit afficher
	 * @param max -> ligne à partir de laquelle on doit s'arrêter d'afficher
	 * @return chaine de charactere à afficher
	 */
	private String toStringCore(int min, int max) {
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

	/**
	 * @return chaine de charactere contenant l'ensemble du dataframe à afficher
	 */
	@Override
	public String toString() {
		return toStringHeader() + toStringCore(0, getMaxSizeSeries());
	}

	/**
	 * @param nb -> Nombre de ligne à afficher depuis le début
	 * <br> Si nb > la taille max possible alors nb = taille max possible
	 * @return chaine de charactere à afficher
	 */
	public String toStringFirstLines(int nb) {
		int max = getMaxSizeSeries();
		return toStringHeader() + toStringCore(0, nb > max ? max : nb);
	}

	/**
	 * @param nb -> Nombre de ligne à afficher depuis la fin
	 * <br> Si nb > la taille max possible alors nb = taille max possible
	 * @return chaine de charactere à afficher
	 */
	public String toStringLastLines(int nb) {
		int max = getMaxSizeSeries();
		return toStringHeader() + toStringCore(nb < max ? max - nb : 0, max);
	}

	/**
	 * @return chaine de charactere contenant l'entete du dataframe à afficher (nom de chaque colonne)
	 */
	private String toStringHeader() {
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
	public Dataframe selection(int ligne) throws PandaExceptions {
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
	public Dataframe selectionColonne(String nom) throws PandaExceptions {
		Object[][] data = null;
		for (Series s : dataframe) {
			if (s.getName().equals(nom)) {
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
	public Dataframe selectionColonnes(ArrayList<String> noms) throws PandaExceptions {
		if (noms == null || noms.size() == 0)
			throw new PandaNoData();
		Object[][] data = null;
		int nbNom = 0;
		boolean trouve;
		for (String nom : noms) {
			trouve = false;
			for (Series s : dataframe) {
				if (s.getName().equals(nom)) {
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
	public Object selectionElement(int ligne, int colonne) throws PandaExceptions {
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
	public Dataframe selectionElements(ArrayList<Integer> ligne, ArrayList<Integer> colonne) throws PandaExceptions {
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
	public Dataframe selectionLignes(ArrayList<Integer> lignes) throws PandaExceptions {
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
	public Dataframe selectionMasque(ArrayList<Boolean> masque) throws PandaExceptions {
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

	/**
	 * @param nomColonne nom de la colonne que l'on veut selectionner
	 * @param elem objet dont on veut selectionner la ligne
	 * @return un dataframe contenant les lignes correspondant a l'element passe en parametre
	 * @throws PandaNameNotFound si le nom de colonne n'existe pas
	 * @throws PandaNoData si elem n'existe pas dans la colonne nomColonne
	 */
	public Dataframe selectionParObjet(String nomColonne, Object elem) throws PandaExceptions {
		for (Series s : dataframe) {
			if (s.getName().equals(nomColonne))
				return searchIndice(s, elem);
		}
		throw new PandaNameNotFound();
	}
	
	/**
	 * @param s une colonne du dataframe
	 * @param elem l'element qu'on cherche dans la colonne
	 * @return un dataframe dont on a selectionner les lignes correspondant a element
	 * @throws PandaNoData si elem n'existe pas dans la colonne 
	 */
	private Dataframe searchIndice(Series s, Object elem) throws PandaExceptions {
		ArrayList<Integer> l = new ArrayList<>();
		int indice = 0;
		for (Object o : s.getColumn()) {
			if (estEgale(elem, o))
				l.add(indice);
			indice++;
		}
		return selectionLignes(l);
	}
	
	/**
	 * @param elem premier objet a comparer
	 * @param o deuxième objet a comparer
	 * @return true si ils sont egaux et de meme type
	 * </br> faux sinon
	 */
	private boolean estEgale(Object elem, Object o) {
		if (elem == null && o == null)
			return true;
		if (elem instanceof Integer && o instanceof Integer)
			return (int)elem == (int)o;
		if (elem instanceof String && o instanceof String)
			return elem.equals(o);
		if (elem instanceof Double && o instanceof Double)
			return (Double)elem == (Double)o;
		return false;
	}
  
	/**
	 *
	 * @param PATH le chemin vers un fichier CSV
	 * @return le tableau de chaine de caractere correspondant aux valeurs dans le
	 *         fichier
	 * @throws TooManyValueException si les colonnes sont trop grandes
	 * @throws Exception              si le fichier n'existe pas, geree par le
	 *                                lecteur de fichier
	 */
	public String[][] toTab(String PATH) throws Exception {
        String[][] tab = new String[PandaExceptions.MAX_VALUES][PandaExceptions.MAX_DATA];
        BufferedReader sc = new BufferedReader(new FileReader(PATH));
        int nbLine = 1;
        String line = sc.readLine();
        tab[0] = nextLine(line);
        line = sc.readLine();
        int nbElem = tab[0].length;
        while (line != null) {
            tab[nbLine++] = nextLine(line);
            if (nbLine == PandaExceptions.MAX_VALUES) {
                sc.close();
                throw new TooManyValueException();
            }
            line = sc.readLine();
        }
        sc.close();
        String[][] toRet = new String[nbLine][nbElem];
        for (int i = 0; i < nbLine; i++) {
            for (int j = 0; j < nbElem; j++) {
                toRet[i][j] = tab[i][j];
            }
        }
        return toRet;
    }

	/**
	 * @param valeur du tableau
	 * @return une chaine de caracteres correspondant au types de donnees
	 */
	 public String type(String valeur) {
	        if (isInt(valeur))
	            return INTEGER;
	        if (isDouble(valeur))
	            return DOUBLE;
	        return STRING;
	}
}
