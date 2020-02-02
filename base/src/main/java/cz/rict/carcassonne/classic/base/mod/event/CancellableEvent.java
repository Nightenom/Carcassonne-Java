package cz.rict.carcassonne.classic.base.mod.event;

/**
 * Base for events which can be cancelled.
 */
public abstract class CancellableEvent extends Event
{
    /**
     * @return true
     */
    @Override
    public boolean isCancellable()
    {
        return true;
    }
}
