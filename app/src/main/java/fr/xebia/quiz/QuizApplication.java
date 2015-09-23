package fr.xebia.quiz;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import fr.xebia.quiz.model.Guest;

public class QuizApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initParseClasses();
        initParse();
    }

    private void initParseClasses() {
        ParseObject.registerSubclass(Guest.class);
    }

    private void initParse() {
        Parse.initialize(this, BuildConfig.PARSE_APPLICATION_ID, BuildConfig.PARSE_CLIENT_KEY);
    }
}