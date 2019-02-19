import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a Coyote.
 * Coyotees age, move, eat rabbits, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Coyote extends Predator
{
    // Characteristics shared by all Coyotes (class variables).

    // The age at which a Coyote can start to breed.
    private static final int BREEDING_AGE = 17;
    // The age to which a Coyote can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a Coyote breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a Coyote can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 20;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).
    // The Coyote's age.
    private int age;
    // The Coyote's food level, which is increased by eating rabbits.
    private int foodLevel;

    /**
     * Create a Coyote. A Coyote can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the Coyote will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Coyote(boolean randomAge, Field field, Location location, boolean female)
    {
        super(field, location, female);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }



    /**
     * Increase the age. This could result in the coyote's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this coyote more hungry. This could result in the Coyote's death.
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
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) {
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    protected Location findMate(List<Animal> newCoyotes)
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Coyote) {
                Coyote adCoyote = (Coyote) animal;
                if(adCoyote.isAlive() && (adCoyote.isFemale() && !isFemale()) || (!adCoyote.isFemale() && isFemale())) {
                    giveBirth(newCoyotes);
                    return where;
                }
            }
        }
        return null;
    }
    /**
     * Check whether or not this Coyote is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newCoyotes A list to return newly born Coyotees.
     */
    protected void giveBirth(List<Animal> newCoyotes)
    {
        // New Coyotees are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        List<Location> adjacentLocations =field.adjacentLocations(getLocation());
        for (Location location : adjacentLocations){
            Object object = field.getObjectAt(location);
            if (object instanceof Coyote) {
                Coyote adCoyote = (Coyote) object;
                if ((adCoyote.isFemale() && !isFemale()) || (!adCoyote.isFemale() && isFemale())) {
                    int births = breed();
                    for (int b = 0; b < births && free.size() > 0; b++) {
                        Location loc = free.remove(0);
                        Coyote young = new Coyote(false, field, loc, rand.nextBoolean());
                        newCoyotes.add(young);
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
     * A Coyote can breed if it has reached the breeding age and it is a female.
     * @return true if it can breed, false otherwise.
     */
    protected boolean canBreed()
    {
        return age >= BREEDING_AGE && isFemale();
    }
}
