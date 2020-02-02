package cz.rict.carcassonne.classic.base.mod.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Class for subscribing and dispatching all game events.
 */
public final class EventBus
{
    private static final Map<Class<? extends Event>, List<Consumer<Event>>> eventListeners = new HashMap<>();

    private EventBus()
    {
    }

    public static <T extends Event> void post(final T event)
    {
        for (final Consumer<Event> listener : eventListeners.getOrDefault(event.getClass(), new ArrayList<>()))
        {
            listener.accept(event);
            if (event.wasCancelled())
            {
                return;
            }
        }
    }

    /**
     * @param eventClass Event subscriber class
     */
    public static void register(final Class<?> eventClass)
    {
        for (final Method method : eventClass.getMethods())
        {
            registerMethod(method);
        }
    }

    private static void registerMethod(final Method method)
    {
        if (!method.isAnnotationPresent(SubscribeEvent.class))
        {
            return;
        }

        final Class<?>[] params = method.getParameterTypes();

        if (params.length != 1 || !Event.class.isAssignableFrom(params[0]))
        {
            // TODO: logger warn
            System.err.println("Event subscribe method \"" + method + "\" has more than one argument or first argument is not Event type!");
        }

        final Class<? extends Event> eventClass = params[0].asSubclass(Event.class);
        final List<Consumer<Event>> eventListenerz = eventListeners.getOrDefault(eventClass, new ArrayList<>());

        if (eventListenerz.isEmpty())
        {
            eventListeners.put(eventClass, eventListenerz);
        }

        eventListenerz.add((Event event) -> {
            try
            {
                method.invoke(null, event);
            }
            catch (final IllegalArgumentException | NullPointerException e)
            {
                // TODO: logger error
                System.err.println("Like wtf is happening, event listener is not static or has changed arguments during runtime...");
            }
            catch (final IllegalAccessException | InvocationTargetException e)
            {
                // TODO: logger error, print stacktrace
                System.err.println("Error during executing event listener!");
            }
        });
    }
}
