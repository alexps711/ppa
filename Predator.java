import java.util.List;

public abstract class Predator extends Animal {


    public Predator(Field field, Location location, boolean female){
        super(field, location, female);
    }


    /**
     * This is what the predator does most of the time: it hunts for
     * preys. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newPredators A list to return newly born foxes.
     */
    public void act(List<Animal> newPredators)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newPredators);
            // Move towards a source of food if found.
            Location newLocation = findMate(newPredators);
            if(newLocation != null && getField().getFreeAdjacentLocations(newLocation) != null) {
                // No food found - try to move to a free location.
               setLocation((newLocation));
               return;
            }
            if(newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = findFood();
            }
            if(newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age. This could result in the predator's death.
     */
    abstract protected void incrementAge();

    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    abstract protected void incrementHunger();

    abstract protected Location findMate(List<Animal> newPredators);
    abstract protected Location findFood();

    abstract protected void giveBirth(List<Animal> newPredators);

    abstract protected int breed();

    abstract protected boolean canBreed();
}
