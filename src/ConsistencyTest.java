import Jama.Matrix;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by pawma on 25.03.2017.
 */
public class ConsistencyTest {

    @Test
    public void calculateConsistencyRatioTest(){
        double[][] array = {{1d,2d,9d,1d},{0.5d,1d,0.33d,0.16d},{0.11d,3d,1d,2d},{1d,6d,0.5d,1d}};
        Matrix matrix = new Matrix(array);
        try{
            double result = Consistency.calculateConsistencyRatio(matrix);
            double expected = 0.48;
            double eps = 0.01;
            Assert.assertTrue(Math.abs(result-expected)<eps);
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            Assert.assertTrue(false);
        }
    }
}