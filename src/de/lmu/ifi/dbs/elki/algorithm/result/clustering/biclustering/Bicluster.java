package de.lmu.ifi.dbs.elki.algorithm.result.clustering.biclustering;

import de.lmu.ifi.dbs.elki.algorithm.result.Result;
import de.lmu.ifi.dbs.elki.data.RealVector;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.logging.AbstractLoggable;
import de.lmu.ifi.dbs.elki.logging.LoggingConfiguration;
import de.lmu.ifi.dbs.elki.utilities.Util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Wrapper class to provide the basic properties of a bicluster.
 *
 * @author Arthur Zimek
 * @param <V> the type of RealVector handled by this Result
 */
public class Bicluster<V extends RealVector<V, Double>> extends AbstractLoggable {
    /**
     * The ids of the rows included in the bicluster.
     */
    private int[] rowIDs;

    /**
     * The ids of the rows included in the bicluster.
     */
    private int[] colIDs;

    /**
     * The ids of inverted rows.
     */
    private int[] invertedRows;

    /**
     * The database this bilcuster is defined for.
     */
    private Database<V> database;

    /**
     * The underlying bicluster model.
     */
    private Result<V> model;

    /**
     * Defines a new bicluster for given parameters.
     *
     * @param rowIDs   the ids of the rows included in the bicluster
     * @param colIDs   the ids of the rows included in the bicluster
     * @param database the database this bilcuster is defined for
     */
    public Bicluster(int[] rowIDs, int[] colIDs, Database<V> database) {
        super(LoggingConfiguration.DEBUG);
        this.rowIDs = rowIDs;
        this.colIDs = colIDs;
        this.database = database;
    }

    /**
     * Sets the ids of the inverted rows.
     *
     * @param invertedRows the ids of the inverted rows
     */
    public void setInvertedRows(int[] invertedRows) {
        this.invertedRows = new int[invertedRows.length];
        System.arraycopy(invertedRows, 0, this.invertedRows, 0, invertedRows.length);
    }

    /**
     * Sorts the row and column ids (and - if applicable - the ids of inverted rows)
     * in ascending order.
     */
    public void sortIDs() {
        Arrays.sort(this.rowIDs);
        Arrays.sort(this.colIDs);
        if (this.invertedRows != null) {
            Arrays.sort(this.invertedRows);
        }
    }

    /**
     * The size of the cluster.
     * <p/>
     * The size of a bicluster is the number of included rows.
     *
     * @return the size of the bicluster, i.e., the number or rows included in the bicluster
     */
    public int size() {
        return rowIDs.length;
    }

    /**
     * Returns the model associated with this bicluster.
     *
     * @return the model associated with this bicluster
     */
    public Result<V> model() {
        return model;
    }

    /**
     * Associates the given model with this bicluster.
     *
     * @param model the model to associate with this bicluster
     */
    public void appendModel(Result<V> model) {
        this.model = model;
    }

    /**
     * Provides an iterator for the row ids.
     * <p/>
     * Note that the iterator is not guaranteed to touch all
     * elements if the {@link #sortIDs()} is called during the lifetime of the iterator.
     *
     * @return an iterator for the row ids
     */
    public Iterator<V> rowIterator() {
        return new Iterator<V>() {
            private int index = -1;

            public boolean hasNext() {
                return index + 1 < size();
            }

            @SuppressWarnings("synthetic-access")
            public V next() {
                return database.get(rowIDs[++index]);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    /**
     * Returns a list containing header information for printed outputs.
     *
     * @return a list containing header information for printed outputs
     */
    public List<String> headerInformation() {
        List<String> header = new LinkedList<String>();
        header.add("cluster size = " + size());
        header.add("cluster dimensions = " + colIDs.length);
        header.add("included row IDs = " + Util.format(rowIDs));
        header.add("included column IDs = " + Util.format(colIDs));
        if (this.invertedRows != null) {
            header.add("inverted rows (row IDs) = " + Util.format(this.invertedRows));
        }
        return header;
    }

}
