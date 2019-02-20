import java.util.Iterator;
import java.util.List;

public abstract class Prey extends Animal {

    static final int PLANT_FOOD_VALUE = 40;

    /**
     * Create a new prey. A rabbit may be created with age
     * zero (a new born) or with a random age.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param female If true, the animal is female.
     */
    public Prey(Field field, Location location, boolean female)
    {
        super(field, location, female);
        
    }


    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    protected abstract void incrementAge();

    abstract protected void incrementHunger();


    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newPreys A list to return newly born rabbits.
     */
    protected abstract void giveBirth(List<Animal> newPreys);


    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected abstract int breed();

    /**
     * A rabbit can breed if it has reached the breeding age and it is female.
     * @return true if the rabbit can breed, false otherwise.
     */
    protected abstract boolean canBreed();

}
