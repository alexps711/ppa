import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Lion extends Predator {
    // Characteristics shared by all Liones (class variables).

    // The age at which a Lion can start to breed.
    private static final int BREEDING_AGE = 19;
    // The age to which a Lion can live.
    private static final int MAX_AGE = 100;
    // The likelihood of a Lion breeding.
    private static final double BREEDING_PROBABILITY = 0.2;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a lion can go before it has to eat again.
    private static final int ZEBRA_FOOD_VALUE = 20;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).
    // The Lion's age.
    private int age;
    // The Lion's food level, which is increased by eating rabbits.
    private int foodLevel;

    /**
     * Create a Lion. A Lion can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the Lion will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Lion(boolean randomAge, Field field, Location location, boolean female)
    {
        super(field, location, female);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(ZEBRA_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel =ZEBRA_FOOD_VALUE;
        }
    }



    /**
     * Increase the age. This could result in the Lion's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this Lion more hungry. This could result in the Lion's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Zebra) {
                Zebra zebra = (Zebra) animal;
                if(zebra.isAlive()) {
                    zebra.setDead();
                    foodLevel = ZEBRA_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    protected Location findMate(List<Animal> newLions)
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Lion) {
                Lion adLion = (Lion) animal;
                if(adLion.isAlive() && (adLion.isFemale() && !isFemale()) || (!adLion.isFemale() && isFemale())) {
                    giveBirth(newLions);
                    return where;
                }
            }
        }
        return null;
    }
    /**
     * Check whether or not this Lion is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newLions A list to return newly born Liones.
     */
    protected void giveBirth(List<Animal> newLions)
    {
        // New Liones are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        List<Location> adjacentLocations =field.adjacentLocations(getLocation());
        for (Location location : adjacentLocations){
            Object object = field.getObjectAt(location);
            if (object instanceof Lion) {
                Lion adLion = (Lion) object;
                if ((adLion.isFemale() && !isFemale()) || (!adLion.isFemale() && isFemale())) {
                    int births = breed();
                    for (int b = 0; b < births && free.size() > 0; b++) {
                        Location loc = free.remove(0);
                        Lion young = new Lion(false, field, loc, rand.nextBoolean());
                        newLions.add(young);
                    }
                }
            }
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
     * A Lion can breed if it has reached the breeding age and it is a female.
     * @return true if it can breed, false otherwise.
     */
    protected boolean canBreed()
    {
        return age >= BREEDING_AGE && isFemale();
    }
}
