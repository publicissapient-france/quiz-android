package fr.xebia.quiz;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseObject;

import fr.xebia.quiz.model.Guest;
import fr.xebia.quiz.model.Question;
import fr.xebia.quiz.model.Rank;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class QuizApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initCrashlytics();
        initTimber();
        initParseClasses();
        initParse();
    }

    private void initCrashlytics() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initParseClasses() {
        ParseObject.registerSubclass(Guest.class);
        ParseObject.registerSubclass(Rank.class);
        ParseObject.registerSubclass(Question.class);
    }

    private void initParse() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, BuildConfig.PARSE_APPLICATION_ID, BuildConfig.PARSE_CLIENT_KEY);
    }
}