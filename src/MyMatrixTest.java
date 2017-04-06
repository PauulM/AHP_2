import Jama.Matrix;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by pawma on 03.04.2017.
 */
public class MyMatrixTest {

    MyMatrix matrix;

    @Before
    public void setUp() {
        matrix = new MyMatrix(3, 3);
        matrix.initializeWithInt(1);
        matrix.setValueByIndex(0.75f, 0, 1);
        matrix.setValueByIndex(3f, 0, 2);
        matrix.setValueByIndex(1.333333f, 1, 0);
        matrix.setValueByIndex(2f, 1, 2);
        matrix.setValueByIndex(0.333333f, 2, 0);
        matrix.setValueByIndex(0.5f, 2, 1);
    }

    @Test
    public void toMatrixTest(){
        Matrix matrix2 = matrix.toMatrix();
        matrix.printMatrixToSout();
    }
}