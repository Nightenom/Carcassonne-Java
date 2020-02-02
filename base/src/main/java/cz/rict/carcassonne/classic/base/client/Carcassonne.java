package cz.rict.carcassonne.classic.base.client;

import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.mod.event.EventBus;
import cz.rict.carcassonne.classic.base.mod.event.SubscribeEvent;

public class Carcassonne
{
    public static final Carcassonne instance = new Carcassonne();

    public void run()
    {
        EventBus.register(Carcassonne.class);
        TestEvent.post(": Test event");
    }

    @SubscribeEvent
    public static void testEventListener(final TestEvent event)
    {
        System.out.println("Base game" + event.getRandomDataToPass());
    }
}
