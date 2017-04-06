import java.util.ArrayList;

/**
 * Created by pawma on 15.03.2017.
 */
public class Alternative {
    private String name;
    private ArrayList<Weight> priorityList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPriority(Weight weight){
        priorityList.add(weight);
    }

    public ArrayList<Weight> getPriorityList() {
        return priorityList;
    }

    public Double findPriorityToByName(String to){
        for(Weight w : priorityList){
            if(w.getTo().equals(to))
                return w.getValue();
        }
        return null;
    }
}
