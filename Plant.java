import java.util.List;

public class Plant {

    private boolean alive;
    private Field field;
    private Location location;

    public Plant(Field field, Location location) {
        alive = true;
        this.field = field;
        setLocation(location);
    }

    /**
     * Place the plant at the given location
     * @param location The plant's  location.
     */
    protected void setLocation(Location location)
    {
        if(location != null) {
            field.clear(location);
        }
        this.location = location;
        field.place(this, location);
    }

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }


    /**
     * Indicate that the plant is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }


    protected Field getField() {
        return field;
    }

    protected Location getLocation() {
        return location;
    }

}
