package winemerchant.inventory;

import java.util.ArrayList;
import java.util.List;

public class Case {
    private List<Wine> wineCase;
    private Wine.WineType type;
    private boolean isMixed;

    public Case(Wine.WineType type) {
        wineCase = new ArrayList<>();
        this.type = type;
        addWinesToCase(type);
        isMixed = false;
    }
    public Case(List<Wine> firstWineList, List<Wine> secondWineList) {
        wineCase = new ArrayList<>();
        wineCase.addAll(firstWineList);
        wineCase.addAll(secondWineList);
        isMixed = true;
    }

    public List<Wine> getWineCase() {
        return wineCase;
    }

    public Wine.WineType getType() {
        return type;
    }

    public int getCaseSize() {
        return wineCase.size();
    }

    //Fills a case with 12 bottles of wine (default)
    public void addWinesToCase(Wine.WineType type) {
        for (int i = 0; i < 12; i++) {
            wineCase.add(new Wine(type));
        }
    }

    //Not sure if I'll use this method
//    public void takeWineFromCase(int amount) {
//        if(amount > wineCase.size()) {
//            System.out.println("There aren't enough wines in this case");
//        } else {
//            for(int i = 0; i < amount; i++) {
//                wineCase.remove(i);
//            }
//            System.out.println("Removed " + amount + " wines from the case.");
//        }
//    }
}
