package me.etki.es;

import me.etki.es.container.Event;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface EventStream<E, ID> {

    CompletableFuture<List<Event<E, ID>>> get(long skip, long size);

    CompletableFuture<Long> count();

    default CompletableFuture<Void> play(long skip, long size, Consumer<Event<E, ID>> consumer) {
        return get(skip, size).thenAccept(events -> events.forEach(consumer));
    }

    CompletableFuture<Boolean> append(Transition<E, ID> transition, ZonedDateTime timestamp);

    default CompletableFuture<Boolean> append(Event<E, ID> event) {
        return append(event.getTransition(), event.getOccurredAt());
    }
}
