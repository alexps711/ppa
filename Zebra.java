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
    private static final int MAX_LITTER_SIZE = 3;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).

    // The rabbit's age.
    private int age;

    public Zebra(boolean randomAge, Field field, Location location, boolean female){
        super(field, location, female);
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