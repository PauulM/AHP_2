import java.util.ArrayList;

/**
 * Created by pawma on 15.03.2017.
 */
public class Criterion {
    private String name;
    private ArrayList<Criterion> subCriteriaList = new ArrayList<>();
    private Criterion parentCriterium = null;
    private ArrayList<Weight> weightsList = new ArrayList<>();
    private ArrayList<Alternative> alternativesList = new ArrayList<>();

    public Alternative findAlternativeByName(String name){
        for(Alternative a : alternativesList){
            if(a.getName().equals(name))
                return a;
        }
        return null;
    }

    public Float findWeightValueToByName(String to){
        for(Weight w : weightsList){
            if (w.getTo().equals(to))
                return w.getValue();
        }
        return null;
    }

    public ArrayList<Criterion> getSubCriteriaList() {
        return subCriteriaList;
    }

    public void addWeight(Weight weight){
        weightsList.add(weight);
    }

    public void addAlternative(Alternative alternative){
        alternativesList.add(alternative);
    }

    public void addSubCriteria(Criterion criterium){
        subCriteriaList.add(criterium);
    }

    public void setSubCriteriaList(ArrayList<Criterion> subCriteriaList) {
        this.subCriteriaList = subCriteriaList;
    }

    public Criterion getParentCriterium() {
        return parentCriterium;
    }

    public void setParentCriterium(Criterion parentCriterium) {
        this.parentCriterium = parentCriterium;
    }

    public boolean hasSubcriteria(){
        return !subCriteriaList.isEmpty();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Weight> getWeightsList() {
        return weightsList;
    }

    public void setWeightsList(ArrayList<Weight> weightsList) {
        this.weightsList = weightsList;
    }

    public ArrayList<Alternative> getAlternativesList() {
        return alternativesList;
    }

    public void setAlternativesList(ArrayList<Alternative> alternativesList) {
        this.alternativesList = alternativesList;
    }

    public static Criterion findCriterionInListByName(ArrayList<Criterion> list, String name){
        Criterion result = null;
        for(Criterion c : list){
            if(c.getName().equals(name)) {
                result = c;
                break;
            }
        }
        return result;
    }
}