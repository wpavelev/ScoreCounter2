package de.wpavelev.scorecounter2.util;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * only one Observer will be notified of changes
 * @param <T>
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {

    AtomicBoolean mPending = new AtomicBoolean(false);




    @MainThread
    public void observe(LifecycleOwner owner, @NonNull final Observer<? super T> observer) {

        // Observe the internal MutableLiveData
        super.observe(owner, t -> {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t);
            }
        });
    }

    @MainThread
    public void setValue(@Nullable T t) {
        mPending.set(true);
        super.setValue(t);
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    public void call() {
        setValue(null);
    }
}