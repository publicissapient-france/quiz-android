package fr.xebia.quiz.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import static fr.xebia.quiz.model.ParseConstant.EMAIL;
import static fr.xebia.quiz.model.ParseConstant.FIRSTNAME;
import static fr.xebia.quiz.model.ParseConstant.JOB;
import static fr.xebia.quiz.model.ParseConstant.NAME;
import static fr.xebia.quiz.model.ParseConstant.PHONE;
import static fr.xebia.quiz.model.ParseConstant.POSTCODE;
import static fr.xebia.quiz.model.ParseConstant.SCORE;
import static fr.xebia.quiz.model.ParseConstant.TIME;
import static fr.xebia.quiz.model.ParseConstant.YEAR;

@ParseClassName(ParseConstant.TABLE_GUEST)
public class Guest extends ParseObject {

    public void setName(String name) {
        put(NAME, name);
    }

    public void setFirstname(String firstname) {
        put(FIRSTNAME, firstname);
    }

    public void setEmail(String email) {
        put(EMAIL, email);
    }

    public void setPhone(String phone) {
        put(PHONE, phone);
    }

    public void setPostcode(String postcode) {
        put(POSTCODE, postcode);
    }

    public void setYear(String year) {
        put(YEAR, year);
    }

    public void setJob(String job) {
        put(JOB, job);
    }

    public void setScore(int score) {
        put(SCORE, score);
    }

    public void setTime(long time) {
        put(TIME, time);
    }

    public String getName() {
        return getString(NAME);
    }

    public String getFirstname() {
        return getString(FIRSTNAME);
    }

}
