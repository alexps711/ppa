import java.util.List;

public abstract class Predator extends Animal {

    /**
     * Create a new predator at location in field.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param female The sex of the predator.
     */
    public Predator(Field field, Location location, boolean female, boolean isSick){
        super(field, location, female, isSick);
    }


    /**
     * This is what the predator does most of the time: it hunts for
     * preys. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newPredators A list to return newly born predators.
     */
    public void act(List<Animal> newPredators)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            // Move towards a source of food if found.
            Location newLocation = findMate(newPredators);
            if(newLocation != null && getField().getFreeAdjacentLocations(newLocation) != null) {
                // No food found - try to move to a free location.
                setLocation(newLocation);
                return;
            }
            // See if it was possible to move.
            if(newLocation == null) {
                newLocation = findFood();
            }
            if(newLocation == null) {
                newLocation = getField().freeAdjacentLocation(getLocation());
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



    abstract protected void incrementAge();

    abstract protected void incrementHunger();

    abstract protected Location findFood();

    abstract protected void giveBirth(List<Animal> newPredators);

    abstract protected int breed();

    abstract protected boolean canBreed();
}
