package cz.rict.carcassonne.classic.base.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.mod.event.EventBus;
import cz.rict.carcassonne.classic.base.mod.event.SubscribeEvent;

public class Carcassonne
{
    private static final Carcassonne INSTANCE = new Carcassonne();
    private static final String MOD_ID = "Carcassonne";

    private final Logger logger = LogManager.getLogger(MOD_ID);

    public void run()
    {
        EventBus.register(this);
        TestEvent.post(": Test event");
    }

    @SubscribeEvent
    public void testEventListener(final TestEvent event)
    {
        logger.info("Base game" + event.getRandomDataToPass());
    }

    public static Carcassonne getInstance()
    {
        return INSTANCE;
    }

    public static Logger getLogger()
    {
        return INSTANCE.logger;
    }
}
