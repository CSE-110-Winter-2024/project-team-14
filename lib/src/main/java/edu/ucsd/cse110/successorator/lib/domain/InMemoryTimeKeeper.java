package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class InMemoryTimeKeeper implements TimeKeeper {
    private final MutableSubject<LocalDateTime> markedDateTime
            = new SimpleSubject<>();

    @Override
    public Subject<LocalDateTime> getMarkedDateTime() {
        return markedDateTime;
    }

    @Override
    public void markDateTime(LocalDateTime dateTime) {
        markedDateTime.setValue(dateTime);
    }
}
