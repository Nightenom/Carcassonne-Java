package cz.rict.carcassonne.classic.base.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TestEvent
{
    private static final List<Consumer<TestEvent>> listeners = new ArrayList<>();
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
        listeners.forEach(l -> l.accept(event));
    }
}