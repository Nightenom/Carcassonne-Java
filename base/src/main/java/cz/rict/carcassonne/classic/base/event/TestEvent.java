package cz.rict.carcassonne.classic.base.event;

import cz.rict.carcassonne.classic.base.mod.event.Event;
import cz.rict.carcassonne.classic.base.mod.event.EventBus;

public class TestEvent extends Event
{
    private final String randomDataToPass;

    private TestEvent(final String randomDataToPass)
    {
        this.randomDataToPass = randomDataToPass;
    }

    public String getRandomDataToPass()
    {
        return randomDataToPass;
    }

    public static void post(final String randomDataToPass)
    {
        EventBus.post(new TestEvent(randomDataToPass));
    }
}
