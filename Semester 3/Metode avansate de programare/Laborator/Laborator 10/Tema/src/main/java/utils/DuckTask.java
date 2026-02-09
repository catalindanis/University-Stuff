package utils;

import models.Duck;
import models.Event;
import models.Race;
import models.RaceEvent;
import org.example.tema.EventsController;

import java.util.ArrayList;
import java.util.List;

public class DuckTask implements Task<DuckTaskResult>, Observable<EventsController> {
    private final Race race;
    private final Event event;

    private boolean[] duckUsed;
    private Duck[] solution, finalSolution;
    private DuckTaskResult result = null;

    List<Observer<DuckTaskResult>> observers = new ArrayList<>();

    public DuckTask(RaceEvent event) {
        this.race = event.getRace();
        this.event = event;
    }

    @Override
    public DuckTaskResult execute() {
        initialize();
        solve();
        return computeFinalResult();
    }

    private void solve() {
        List<Duck> sortedDucks = race.getDucks();

        if(sortedDucks.isEmpty())
            return;

        sortByResistanceAndStamina(sortedDucks);

        double left = 0, right = (1.0 * race.getLanes().get(race.getNoLanes() - 1).getBuoyDistance() / race.getSlowestDuck().getSpeed()) * 2;
        while(left <= right) {
            double middle = (left + right) / 2;

            for(int i = race.getNoLanes() - 1; i >= 0; i--) {
                for(int j = sortedDucks.size() - 1; j >= 0; j--)
                    if(!duckUsed[j] && (i == race.getNoLanes() - 1 || solution[i+1].getResistance() >= sortedDucks.get(j).getResistance())
                            && (1.0 * race.getLanes().get(i).getBuoyDistance() / sortedDucks.get(j).getSpeed()) * 2 <= middle) {
                        duckUsed[j] = true;
                        solution[i] = sortedDucks.get(j);
                        break;
                    }

                if(solution[i] == null)
                    break;
            }

            boolean notFound = false;
            double maximumTime = 0;

            for(int i=0;i<race.getNoLanes();i++)
                if (solution[i] == null) {
                    left = middle + 0.001;
                    notFound = true;
                }
                else {
                    double elapsedTime = (1.0 * race.getLanes().get(i).getBuoyDistance() / solution[i].getSpeed()) * 2;
                    if(elapsedTime > maximumTime)
                        maximumTime = elapsedTime;
                }

            if(notFound)
                continue;

            for(int i=0;i<sortedDucks.size();i++)
                duckUsed[i] = false;

            race.setEndTime(maximumTime);
            for(int i=0;i<race.getNoLanes();i++) {
                finalSolution[i] = solution[i];
                solution[i] = null;
            }

            right = middle - 0.001;
        }
    }

    private void sortByResistanceAndStamina(List<Duck> sortedDucks) {
        for(int i = 0; i< sortedDucks.size(); i++)
            for(int j = i+1; j< sortedDucks.size(); j++)
                if(sortedDucks.get(i).getResistance() > sortedDucks.get(j).getResistance()) {
                    Duck temp = sortedDucks.get(i);
                    sortedDucks.set(i, sortedDucks.get(j));
                    sortedDucks.set(j, temp);
                }
                else if(sortedDucks.get(i).getResistance() == sortedDucks.get(j).getResistance())
                    if(sortedDucks.get(j).getSpeed() < sortedDucks.get(i).getSpeed()) {
                        Duck temp = sortedDucks.get(i);
                        sortedDucks.set(i, sortedDucks.get(j));
                        sortedDucks.set(j, temp);
                    }
    }

    private void initialize() {
        duckUsed = new boolean[race.getNoDucks()];

        for(int i = 0; i < race.getNoDucks(); i++) duckUsed[i] = false;

        solution = new Duck[race.getNoLanes()];
        finalSolution = new Duck[race.getNoLanes()];

        for(int i=0;i<race.getNoLanes(); i++)
            solution[i] = finalSolution[i] = null;
    }

    private DuckTaskResult computeFinalResult() {
        result = new DuckTaskResult();
        result.event = (RaceEvent) event;

        if(race.getDucks().isEmpty()) {
            notifyObservers();
            return null;
        }

        for(int i = 0; i < race.getNoLanes(); i++) {
            result.ducks.add(finalSolution[i]);
            result.lanes.add(race.getLanes().get(i));
            result.elapsedTimes.add(1.0 * race.getLanes().get(i).getBuoyDistance() / finalSolution[i].getSpeed() * 2);
        }

        result.endTime = race.getEndTime();

        notifyObservers();
        return result;
    }

    @Override
    public void subscribe(EventsController eventsController) {
        observers.add(eventsController);
    }

    @Override
    public void unsubscribe(EventsController eventsController) {
        observers.remove(eventsController);
    }

    @Override
    public void notifyObservers() {
        for(var o : observers)
            o.update(result);
    }
}
