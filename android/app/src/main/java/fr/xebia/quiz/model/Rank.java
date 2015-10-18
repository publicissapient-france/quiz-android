package fr.xebia.quiz.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import static fr.xebia.quiz.model.ParseConstant.NAME;
import static fr.xebia.quiz.model.ParseConstant.SCORE;
import static fr.xebia.quiz.model.ParseConstant.TIME;

@ParseClassName(ParseConstant.TABLE_RANK)
public class Rank extends ParseObject {

    public void setScore(int score) {
        put(SCORE, score);
    }

    public void setTime(long time) {
        put(TIME, time);
    }

    public void setName(String name) {
        put(NAME, name);
    }

    public void setGuest(Guest guest) {
        put(ParseConstant.GUEST, guest);
    }

}
