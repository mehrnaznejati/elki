package experimentalcode.lucia;

import org.junit.Test;
import de.lmu.ifi.dbs.elki.JUnit4Test;
import de.lmu.ifi.dbs.elki.algorithm.AbstractSimpleAlgorithmTest;
import de.lmu.ifi.dbs.elki.algorithm.outlier.LOF;
import de.lmu.ifi.dbs.elki.data.DoubleVector;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.database.connection.FileBasedDatabaseConnection;
import de.lmu.ifi.dbs.elki.distance.distancevalue.DoubleDistance;
import de.lmu.ifi.dbs.elki.result.outlier.OutlierResult;
import de.lmu.ifi.dbs.elki.utilities.ClassGenericsUtil;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.ParameterException;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameterization.ListParameterization;


/**
 * Tests the LOF algorithm. 
 * @author Lucia Cichella
 * 
 */
public class TestLOF extends AbstractSimpleAlgorithmTest implements JUnit4Test{

  static String dataset = "src/experimentalcode/lucia/datensaetze/hochdimensional.csv";
  static int k = 10;


  @Test
  public void testLOF() throws ParameterException {
    //get Database
    ListParameterization paramsDB = new ListParameterization();
    paramsDB.addParameter(FileBasedDatabaseConnection.SEED_ID, 1);
    Database<DoubleVector> db = makeSimpleDatabase(dataset, 1345, paramsDB);

    //Parameterization
    ListParameterization params = new ListParameterization();
    params.addParameter(LOF.K_ID, k);

    //setup Algorithm
    LOF<DoubleVector, DoubleDistance> lof = ClassGenericsUtil.parameterizeOrAbort(LOF.class, params);
    testParameterizationOk(params);

    //run LOF on database
    OutlierResult result = lof.run(db);
    db.getHierarchy().add(db, result);


    //check Outlier Score of Point 1280
    int id = 1280;
    testSingleScore(result, id, 1.1945314199156365);

    //test ROC AUC
    testAUC(db, "Noise", result, 0.8921680672268908);
    
  }
}
