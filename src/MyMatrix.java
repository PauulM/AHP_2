/**
 * Created by pawma on 03.04.2017.
 */
import Jama.Matrix;

import java.util.ArrayList;
import java.util.HashMap;

public class MyMatrix {
    private ArrayList<ArrayList<Float>> rowsVector;
    private int rows;
    private int cols;
    private boolean initialized;

    public MyMatrix(int rows, int cols){
        initialized = false;
        this.rows = rows;
        this.cols = cols;
        rowsVector = new ArrayList<>();
    }

    public MyMatrix copy(){
        MyMatrix result = new MyMatrix(getRows(), getCols());
        result.setInitialized(true);
        result.setRowsVector(getRowsVector());//czy tu tworzy sie kopia czy referencja?
        return result;
    }

    public void initializeWithInt(float val){
        try {
            if (initialized) throw new Exception();
            ArrayList<Float> nextRow;
            for (int i = 0; i < rows; i++) {
                nextRow = new ArrayList<>();
                for (int j = 0; j < cols; j++) {
                    nextRow.add(val);
                }
                rowsVector.add(nextRow);
            }
        }
        catch(Exception ex){
            System.out.println("MATRIX ALREADY INITIALIZED");
        }
    }

    public Float getValueByIndex(int rowIndex, int colIndex) throws RuntimeException{
        return rowsVector.get(rowIndex).get(colIndex);
    }

    public void setValueByIndex(Float value, int rowIndex, int colIndex)throws RuntimeException{
        rowsVector.get(rowIndex).set(colIndex, value);
    }

    public ArrayList<Float> getRowByIndex(int rowIndex)throws RuntimeException{
        return rowsVector.get(rowIndex);
    }

    public ArrayList<Float> getColByIndex(int colIndex)throws RuntimeException{
        ArrayList<Float> result = new ArrayList<>();
        for(int i=0;i<rows;i++){
            result.add(rowsVector.get(i).get(colIndex));
        }
        return result;
    }

    public void setRowByIndex(int rowIndex, ArrayList<Float> row)throws RuntimeException{
        rowsVector.set(rowIndex, row);
    }

    public void setColByIndex(int colIndex, ArrayList<Float> col)throws RuntimeException{
        for(int i=0;i<rows;i++){
            rowsVector.get(i).set(colIndex, col.get(i));
        }
    }

    private ArrayList<ArrayList<Float>> getRowsVector() {
        return rowsVector;
    }

    private void setRowsVector(ArrayList<ArrayList<Float>> rowsVector) {
        this.rowsVector = rowsVector;
    }
    private void setInitialized(boolean init){
        this.initialized = init;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public void printMatrixToSout(){
        for(ArrayList<Float> tmpRow : rowsVector){
            for(Float tmp : tmpRow){
                System.out.print(tmp + "      ");
            }
            System.out.print("\n");
        }
    }

    public Matrix toMatrix(){
        Matrix result = new Matrix(rows, cols, 1d);
        int i = 0;
        int j;
        for(ArrayList<Float> a : rowsVector){
            j = 0;
            for(Float b : a){
                result.set(i,j,Double.parseDouble(b.toString()));
                j++;
            }
            i++;
        }
        return result;
    }

    public static MyMatrix createWeightMatrixFromSiblingCriteria(ArrayList<Criterion> siblingsCriteria) {
        HashMap<Integer, String> criteriaIDs = applyIdToCriteria(siblingsCriteria);
        MyMatrix matrix = new MyMatrix(siblingsCriteria.size(), siblingsCriteria.size());
        matrix.initializeWithInt(1f);
        try {
            for (int i = 0; i < matrix.getRows(); i++) {
                for (int j = 0; j < matrix.getCols(); j++) {
                    if (i == j)
                        continue;
                    String numeratorName = criteriaIDs.get(i);
                    String denominatorName = criteriaIDs.get(j);
                    matrix.setValueByIndex(findWeightByNumAndDenomNames(numeratorName,
                            denominatorName, siblingsCriteria), i, j);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    private static Float findWeightByNumAndDenomNames(String num, String denom, ArrayList<Criterion> criteria) {
        for (Criterion c : criteria) {
            if (c.getName().equals(num)) {
                return Float.parseFloat(c.findWeightValueToByName(denom).toString());
            }
        }
        return null;
    }

    public static HashMap<Integer, String> applyIdToCriteria(ArrayList<Criterion> criteria) {
        HashMap<Integer, String> map = new HashMap<>();
        int c = 0;
        for (Criterion a : criteria) {
            map.put(c, a.getName());
            c++;
        }
        return map;
    }

    public static MyMatrix createPriorityMatrixFromAlternativesList(ArrayList<Alternative> alternatives) {//, HashMap<Integer, String> map){
        HashMap<Integer, String> alternativesIDs = applyIdToAlternatives(alternatives);
        MyMatrix matrix = null;
        try {
            matrix = new MyMatrix(alternatives.size(), alternatives.size());
            matrix.initializeWithInt(1f);
            for (int i = 0; i < matrix.getRows(); i++) {
                for (int j = 0; j < matrix.getCols(); j++) {
                    if (i == j)
                        continue;
                    String numeratorName = alternativesIDs.get(i);
                    String denominatorName = alternativesIDs.get(j);
                    matrix.setValueByIndex(
                            findPriorityByNumAndDenomNames(numeratorName, denominatorName, alternatives), i, j);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    public static HashMap<Integer, String> applyIdToAlternatives(ArrayList<Alternative> alternatives) {
        HashMap<Integer, String> map = new HashMap<>();
        int c = 0;
        for (Alternative a : alternatives) {
            map.put(c, a.getName());
            c++;
        }
        return map;
    }

    private static Float findPriorityByNumAndDenomNames(String num, String denom, ArrayList<Alternative> alternatives) {
        for(Alternative a : alternatives){
            if(a.getName().equals(num)){
                return Float.parseFloat(a.findPriorityToByName(denom).toString());
            }
        }
        return null;
        //return Float.parseFloat(criterium.findAlternativeByName(num).findPriorityToByName(denom).toString());
    }
}
