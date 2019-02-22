import java.util.Iterator;
import java.util.List;

public abstract class Prey extends Animal {

    // The food value of a single plant. In effect, this is the
    // number of steps a prey can go before it has to eat again.
    static final int PLANT_FOOD_VALUE = 20;

    /**
     * Create a new prey.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param female If true, the animal is female.
     */
    public Prey(Field field, Location location, boolean female, boolean isSick)
    {
        super(field, location, female, isSick);
        
    }

    public void act(List<Animal> newPreys) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {

            // Move towards a source of food if found.
            Location newLocation = findMate(newPreys);
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


    abstract protected Location findFood();

    protected abstract void incrementAge();

    abstract protected void incrementHunger();

    abstract protected void giveBirth(List<Animal> newPreys);

    abstract protected int breed();


    abstract protected boolean canBreed();

}
