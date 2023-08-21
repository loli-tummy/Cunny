package pictures.cunny.client.framework.events;

import pictures.cunny.client.Cunny;

import java.lang.reflect.Method;
import java.util.*;

import static pictures.cunny.client.Cunny.mc;

/**
 * The type Event handler.
 */
public class EventBus {
    private final Map<Class<?>, ArrayList<EventWrapper>> eventMap = new HashMap<>();
    private final Map<Method, EventWrapper> methodWrappers = new HashMap<>();
    private final Map<Object, ArrayList<EventWrapper>> disabled = new HashMap<>();

    /**
     * Assigns an objects methods to events.
     *
     * @param eventObject an object with an event-listener.
     */
    public void add(Object eventObject) {
        for (Method method : eventObject.getClass().getDeclaredMethods()) {

            if (!method.isAnnotationPresent(EventListener.class)) {
                continue;
            }

            EventListener eventListener = method.getAnnotation(EventListener.class);
            Optional<Class<?>> param = Arrays.stream(method.getParameterTypes()).findFirst();

            if (param.isEmpty()) {
                return;
            }

            Class<?> clazz = param.get();
            mkEventMap(clazz);
            method.setAccessible(true);
            methodWrappers.put(
                    method,
                    new EventWrapper(
                            eventObject,
                            eventListener,
                            method));

            methodWrappers.get(method).setId(clazz);
            eventMap.get(clazz).add(methodWrappers.get(method));
            eventMap.get(clazz).sort(Comparator.comparingInt((c1) -> c1.event().priority()));
        }
    }

    /**
     * Invoke methods with this event, will also break the loop if needed.
     *
     * @param event the event
     */
    public void call(Event event) {
        Class<? extends Event> clazz = event.getClass();

        if (!eventMap.containsKey(clazz)) {
            return;
        }

        for (EventWrapper wrapper : eventMap.get(clazz)) {
            if (wrapper.event().inGame() && (mc.player == null || mc.level == null))
                continue;
            Cunny.EXECUTOR.submit(() -> {
                try {
                    wrapper.methodHandle().invoke(wrapper.getObject(), event);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });

            if (event.isCancelled() && wrapper.event().breakOnCancel())
                break;
        }
    }

    /**
     * Disable an objects events.
     *
     * @param eventObject the object with events
     */
    public void disable(Object eventObject) {
        if (disabled.containsKey(eventObject)) return;
        disabled.put(eventObject, new ArrayList<>());
        for (Method method : eventObject.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventListener.class)) continue;
            disabled.get(eventObject).add(methodWrappers.get(method));
        }

        for (Class<?> clazz : eventMap.keySet()) {
            eventMap.get(clazz).removeIf(wrapper -> disabled.get(eventObject).contains(wrapper));
        }
    }

    /**
     * Enable an objects events.
     *
     * @param eventObject the object with events
     */
    public void enable(Object eventObject) {
        if (!disabled.containsKey(eventObject)) return;
        for (EventWrapper wrapper : disabled.get(eventObject)) {
            mkEventMap(wrapper.id());
            eventMap.get(wrapper.id()).add(wrapper);
        }
        disabled.remove(eventObject);
    }

    /**
     * Remove an objects events.
     *
     * @param eventObject the object with events
     */
    public void remove(Object eventObject) {
        disabled.remove(eventObject);

        for (Method method : eventObject.getClass().getDeclaredMethods()) {
            if (methodWrappers.containsKey(method)) {
                eventMap.get(methodWrappers.get(method).id()).remove(methodWrappers.get(method));
            }
        }
    }

    private void mkEventMap(Class<?> event) {
        if (!eventMap.containsKey(event)) eventMap.put(event, new ArrayList<>());
    }
}
