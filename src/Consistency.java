import Jama.Matrix;
/**
 * Created by pawma on 25.03.2017.
 */
public class Consistency {

    public static Double calculateConsistencyRatio(Matrix matrix) throws Exception{
        int dim = matrix.getColumnDimension();
        if(dim != matrix.getRowDimension())
            throw new Exception("Matrix not square!");
        if(dim < 1)
            throw new Exception("Matrix dimension lower than 1!");
        if(dim == 1 || dim == 2)
            return 0d;
        if(dim > 10)
            throw new Exception("Matrix dimension bigger than 10!");
        double[] randomIndex = {0d,0d,0d,0.5247,0.8816,1.1086,1.2479,1.3417,1.4057,1.4499,1.4854};
        double[] eigenvalues = getDiag(matrix.eig().getD());
        double maxEigenvalue = maxFromArray(eigenvalues);
        double result = (maxEigenvalue-dim)/(dim-1)/randomIndex[dim];
        return result;
    }

    public static double maxFromArray(double[] array){
        double max =0;
        for(int i=0; i<array.length; i++){
            if(array[i]>max){
                max = array[i];
            }
        }
        return max;
    }

    public static double[] getDiag(Matrix matrix)throws Exception{
        int dim = matrix.getColumnDimension();
        if(dim != matrix.getRowDimension())
            throw new Exception("Matrix not square!");
        double[] diag = new double[dim];
        for(int i=0; i<dim; i++){
            diag[i] = matrix.get(i,i);
        }
        return diag;
    }
}
