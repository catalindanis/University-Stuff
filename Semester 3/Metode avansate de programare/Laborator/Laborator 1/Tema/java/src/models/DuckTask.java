package models;

import utils.DuckTaskStrategy;

public class DuckTask extends Task{
    private final Race race;
    private final DuckTaskStrategy strategy;
    private long elapsedTime;

    private boolean[] duckUsed;
    private Duck[] solution, finalSolution;

    public DuckTask(String id, String description, Race race, DuckTaskStrategy strategy) {
        super(id, description);
        this.race = race;
        this.strategy = strategy;
    }

    @Override
    public void execute() {
        long start = System.currentTimeMillis();

        initialize();

        switch (strategy) {
            case BINARY_SEARCH -> executeByBinarySearch();
            case BACKTRACKING -> executeByBacktracking();
        }

        long end = System.currentTimeMillis();
        elapsedTime = end - start;

        printResult();
    }

    private void executeByBinarySearch() {
        Duck[] sortedDucks = race.getDucks();

        for(int i = 0; i< sortedDucks.length; i++)
            for(int j = i+1; j< sortedDucks.length; j++)
                if(sortedDucks[i].getStamina() > sortedDucks[j].getStamina()) {
                    Duck temp = sortedDucks[i];
                    sortedDucks[i] = sortedDucks[j];
                    sortedDucks[j] = temp;
                }
                else if(sortedDucks[i].getStamina() == sortedDucks[j].getStamina())
                    if(sortedDucks[j].getSpeed() < sortedDucks[i].getSpeed()) {
                        Duck temp = sortedDucks[i];
                        sortedDucks[i] = sortedDucks[j];
                        sortedDucks[j] = temp;
                    }

        double left = 0, right = (1.0 * race.getLanes()[race.getNoLanes() - 1].getBuoyDistance() / race.getSlowestDuck().getSpeed()) * 2;
        while(left <= right) {
            double middle = (left + right) / 2;

            for(int i = race.getNoLanes() - 1; i >= 0; i--) {
                for(int j = sortedDucks.length - 1; j >= 0; j--)
                    if(!duckUsed[j] && (i == race.getNoLanes() - 1 || solution[i+1].getStamina() >= sortedDucks[j].getStamina())
                        && (1.0 * race.getLanes()[i].getBuoyDistance() / sortedDucks[j].getSpeed()) * 2 <= middle) {
                        duckUsed[j] = true;
                        solution[i] = sortedDucks[j];
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
                    double elapsedTime = (1.0 * race.getLanes()[i].getBuoyDistance() / solution[i].getSpeed()) * 2;
                    if(elapsedTime > maximumTime)
                        maximumTime = elapsedTime;
                }

            if(notFound)
                continue;

            for(int i=0;i<sortedDucks.length;i++)
                duckUsed[i] = false;

            race.setEndTime(maximumTime);
            for(int i=0;i<race.getNoLanes();i++) {
                finalSolution[i] = solution[i];
                solution[i] = null;
            }

            right = middle - 0.001;
        }
    }

    private void executeByBacktracking() {
        backtracking(0);
    }

    private void backtracking(int position) {
        if(position == race.getNoLanes()) {
            double maximumTime = 0;

            for(int i=0;i<position;i++) {
                double elapsedTime = (1.0 * race.getLanes()[i].getBuoyDistance() / solution[i].getSpeed()) * 2;
                if(elapsedTime > maximumTime)
                    maximumTime = elapsedTime;
            }

            if(race.getEndTime() == -1 || race.getEndTime() > maximumTime) {
                race.setEndTime(maximumTime);

                for(int i = 0; i < position; i++)
                    finalSolution[i] = solution[i];
            }

            return;
        }

        for(int i = 0; i < race.getNoDucks(); i++)
            if(!duckUsed[i]) {
                if(position > 0 && solution[position - 1].getStamina() > race.getDucks()[i].getStamina()) continue;

                duckUsed[i] = true;
                solution[position] = race.getDucks()[i];
                backtracking(position + 1);

                duckUsed[i] = false;
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

    private void printResult() {
        System.out.println("Duck task #" + super.getId() + " executed in " + (elapsedTime / 1000.0) + "s");
        System.out.println("Strategy used: " + strategy.name());
        System.out.println();

        for(int i = 0; i < race.getNoLanes(); i++) {
            double elapsedTime = (1.0 * race.getLanes()[i].getBuoyDistance() / finalSolution[i].getSpeed()) * 2;

            System.out.println("Lane #" + (i + 1) + " uses:");
            System.out.println(finalSolution[i]);
            System.out.println("Elapsed time: " + elapsedTime + "s");
        }

        System.out.println();
        System.out.println("Race minimum time: " + race.getEndTime() + "s");
    }
}
