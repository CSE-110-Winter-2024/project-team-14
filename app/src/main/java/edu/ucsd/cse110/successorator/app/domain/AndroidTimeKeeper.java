package edu.ucsd.cse110.successorator.app.domain;

import android.app.Activity;
import android.content.Context;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.Subject;


public class AndroidTimeKeeper implements TimeKeeper {
    public AndroidTimeKeeper(Activity activity) {

    }

    @Override
    public Subject<LocalDateTime> getMarkedDateTime() {
        return null;
    }

    @Override
    public void markDateTime(LocalDateTime dateTime) {

    }
}

//persistence, when you load the application again it pulls from there