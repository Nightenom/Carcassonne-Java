package cz.rict.carcassonne.classic.base.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TestEvent
{
    private static List<Consumer<TestEvent>> listeners = new ArrayList<>();
    private final String randomDataToPass;

    private TestEvent(final String randomDataToPass)
    {
        this.randomDataToPass = randomDataToPass;
    }

    public String getRandomDataToPass()
    {
        return randomDataToPass;
    }

    public static void registerListener(final Consumer<TestEvent> listener)
    {
        listeners.add(listener);
    }

    public static void post(final String randomDataToPass)
    {
        final TestEvent event = new TestEvent(randomDataToPass);
        try
        {
            listeners.forEach(l -> l.accept(event));
        }
        catch (final Exception e)
        {
            System.out.println("Shutdowning event cause of mod exception.");
            e.printStackTrace();
            // Can unsubscribe the specific erroring listener instead of shutdowning entire event bus
            listeners = null;
        }
    }
}
