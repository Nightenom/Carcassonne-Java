package cz.rict.carcassonne.classic.base.mod.event;

/**
 * Base event class from which should every game event extend.
 * Event is dispatched in unpredictable order to the registered listeners until someone cancels it.
 * Once event is cancelled noone will ever receive it.
 */
public abstract class Event
{
    private boolean cancelled = false;

    /**
     * Cancels event. See class documentation for further info.
     */
    public void cancel()
    {
        if (!isCancellable())
        {
            throw new UnsupportedOperationException("Event \"" + this.getClass().getCanonicalName() + "\" can't be cancelled!");
        }

        cancelled = true;
    }

    /**
     * If event was cancelled during its dispatch phase.
     *
     * @return true if cancelled, false otherwise
     */
    protected boolean wasCancelled()
    {
        return cancelled;
    }

    /**
     * See {@link CancellableEvent}
     *
     * @return true if event can be cancelled, false otherwise
     */
    public boolean isCancellable()
    {
        return false;
    }
}
