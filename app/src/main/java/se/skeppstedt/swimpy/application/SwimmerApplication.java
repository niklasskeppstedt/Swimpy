package se.skeppstedt.swimpy.application;

/**
 * Created by niske on 2015-11-09.
 */
public class SwimmerApplication {

    static SwimmerApplication instance;

    String testOctoId = "297358";

    static public SwimmerApplication getInstance() {
        if(instance == null)
            instance = new SwimmerApplication();
        return instance;
    }

    private SwimmerApplication() {

    }

}
