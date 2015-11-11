package se.skeppstedt.swimpy.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * This adapter class provides empty implementations of the methods from {@link android.app.Application.ActivityLifecycleCallbacks}.
 * Any custom listener that cares only about a subset of the methods of this listener can simply subclass this
 * adapter class instead of implementing the interface directly.
 */
public class ActivityLifecycleCallbacksAdapter implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}