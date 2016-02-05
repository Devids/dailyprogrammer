import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by David Nagy on 2/5/2016.
 */
public class Application {

    List<Entity> entityList = new ArrayList<>();
    String goal = "Hello world!";

    private static final Random random = new Random();

    private static final int POPULATION = 100;
    private static final int NEGATIVE_MUTATION_PERCENTAGE = 3;
    private static final int POSITIVE_MUTATION_PERCENTAGE = 10;
    private static final int TOP_SELECTION = 30;
    private static final int LAST_SELECTION = 10;
    private static final int MAX_COPULATION_NUMBER = 2;

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }

    public void run() {
        initPopulation();
        boolean fit = false;
        int c = 0;
        while (!fit) {
            calculateDistances();
            sortPopulation();

            StringBuilder builder = new StringBuilder();
            for (Entity entity : entityList) {
                builder.append(entity.getValue()).append("-").append(entity.getFit()).append(";");
            }
            System.out.println(String.format("Iteration #%d", c));
            System.out.println(builder.toString());

            if (entityList.get(0).getFit() == 0) {
                fit = true;
            }
            breeding();
            c++;
        }
    }

    public void initPopulation() {
        RandomString rs = new RandomString(goal.length());
        for (int i = 0; i < POPULATION; i++) {
            entityList.add(new Entity(rs.nextString()));
        }
    }

    public void calculateDistances() {
        for (int i = 0; i < entityList.size(); i++) {
            calculateDistance(entityList.get(i));
        }
    }

    public void calculateDistance(Entity entity) {
        int distance = 0;
        for (int i = 0; i < goal.length(); i++) {
            distance += Math.abs(((int) entity.getValue().charAt(i) - (int) goal.charAt(i)));
        }
        entity.setFit(distance);
    }

    public void sortPopulation() {
        entityList.sort((o1, o2) -> Integer.compare(o1.getFit(), o2.getFit()));
    }

    public void breeding() {
        List<Entity> newGeneration = new ArrayList<>();
        List<Entity> breedingList = createBreedingList();
        for (int i = 0; i < POPULATION; i++) {
            Entity firstParent = getRandomParent(breedingList);
            Entity secondParent = null;
//            while (firstParent != secondParent) {
            secondParent = getRandomParent(breedingList);
//            }
            newGeneration.add(mate2Crossover(firstParent, secondParent));
        }
        entityList = newGeneration;
    }

    private List<Entity> createBreedingList() {
        List<Entity> breedingList = new ArrayList<>();
        breedingList.addAll(entityList.subList(0, TOP_SELECTION));
        List<Integer> randList = new ArrayList<>();
        for (int i = 0; i < LAST_SELECTION; i++) {
            int rand;
            while (randList.contains(rand = random.nextInt(entityList.size() - TOP_SELECTION) + TOP_SELECTION)) {
            }
            breedingList.add(entityList.get(rand));
            randList.add(rand);
        }
        return breedingList;
    }

    private Entity getRandomParent(List<Entity> breedingList) {
        int copNum = MAX_COPULATION_NUMBER + 1;
        Entity firstParent = null;
        while (copNum >= MAX_COPULATION_NUMBER) {
            firstParent = breedingList.get(random.nextInt(breedingList.size()));
            copNum = firstParent.getCopulations();
        }
        return firstParent;
    }

    private Entity mate(Entity firstParent, Entity secondParent) {
        int crossoverPoint = random.nextInt(goal.length());
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < goal.length(); i++) {
            if (i < crossoverPoint) {
                calculateChar(firstParent, builder, i);
            } else {
                calculateChar(secondParent, builder, i);
            }

        }
        return new Entity(builder.toString());
    }

    private Entity mate2Crossover(Entity firstParent, Entity secondParent) {
        int crossoverPoint1 = random.nextInt(goal.length());
        int crossoverPoint2 = random.nextInt(goal.length());
        if (crossoverPoint2 < crossoverPoint1) {
            crossoverPoint1 ^= crossoverPoint2;
            crossoverPoint2 = crossoverPoint1 ^ crossoverPoint2;
            crossoverPoint1 ^= crossoverPoint2;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < goal.length(); i++) {
            if (i < crossoverPoint1 || i > crossoverPoint2) {
                calculateChar(firstParent, builder, i);
            } else {
                calculateChar(secondParent, builder, i);
            }

        }
        return new Entity(builder.toString());
    }

    private void calculateChar(Entity firstParent, StringBuilder builder, int i) {
        if (isMutating(POSITIVE_MUTATION_PERCENTAGE)) {
            builder.append(new Character((char) (firstParent.getValue().charAt(i) + getMutationValue(i, firstParent.getValue()))));
        } else if (isMutating(NEGATIVE_MUTATION_PERCENTAGE)) {
            builder.append(new Character((char) (firstParent.getValue().charAt(i) + (-1 * getMutationValue(i, firstParent.getValue())))));
        } else {
            builder.append(firstParent.getValue().charAt(i));
        }
    }

    private boolean isMutating(int percentage) {
        return random.nextInt(100) < percentage;
    }

    private int getMutationValue(int i, String parent) {
        int val = goal.charAt(i) - parent.charAt(i);
        if (val == 0) {
            return 0;
        }
        if (val < 0) {
            return -1;
        }
        return +1;
    }
}
