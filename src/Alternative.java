import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static Alternative findInHashMapByName(Map<Integer, Alternative> map, String name){
        List<Alternative> list = new ArrayList<>(map.values());
        Alternative result = null;
        for(Alternative c : list){
            if(c.getName().equals(name)) {
                result = c;
                break;
            }
        }
        return result;
    }

    public static void deletePriorityByTo(Alternative alt, String to){
        for(int i=0; i<alt.getPriorityList().size(); i++){
            if(alt.getPriorityList().get(i).getTo().equals(to))
                alt.getPriorityList().remove(i);
        }
    }
}
