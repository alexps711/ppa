import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Zebra extends Prey {

    // Characteristics shared by all rabbits (class variables).

    // The age at which a zebra can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a zebra can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a zebra breeding.
    private static final double BREEDING_PROBABILITY = 0.09;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).

    // The rabbit's age.
    private int age;

    private int foodLevel;


    public Zebra(boolean randomAge, Field field, Location location, boolean female){
        super(field, location, female);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(PLANT_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = PLANT_FOOD_VALUE;
        }
    }

    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * This is what the predator does most of the time: it hunts for
     * preys. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newPreys A list to return newly born foxes.
     */
    /**
     * This is what the rabbit does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits.
     */
    /**
     * This is what the rabbit does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits.
     */
    public void act(List<Animal> newRabbits)
    {
        incrementAge();
        incrementHunger();

        if(isAlive()) {
            if(findMate()) {
                giveBirth(newRabbits);
            }
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            List<Location> adLocations = getField().adjacentLocations(getLocation());
            Iterator<Location> it = adLocations.iterator();
            while(it.hasNext()){
                Location where = it.next();
                Object object = getField().getObjectAt(where);
                if(object instanceof Plant) {
                    Plant plant = (Plant) object;
                    plant.setDead();
                    setLocation(where);
                }
            }
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }


    protected boolean findMate()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object object = field.getObjectAt(where);
            if(object instanceof Zebra) {
                Zebra adZebra = (Zebra) object;
                if(adZebra.isAlive() && (adZebra.isFemale() && !this.isFemale()) || (!adZebra.isFemale() && this.isFemale())) {
                    return true;
                }
            }
        }
        return false;
    }



    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newZebras A list to return newly born rabbits.
     */
    protected void giveBirth(List<Animal> newZebras)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Zebra young = new Zebra(false, field, loc, rand.nextBoolean());
            newZebras.add(young);
        }
    }


    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age and it is female.
     * @return true if the rabbit can breed, false otherwise.
     */
    protected boolean canBreed()
    {
        return age >= BREEDING_AGE && isFemale();
    }
}
