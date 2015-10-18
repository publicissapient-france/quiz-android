package fr.xebia.quiz.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import static fr.xebia.quiz.model.ParseConstant.CORRECT;
import static fr.xebia.quiz.model.ParseConstant.TEXT;
import static fr.xebia.quiz.model.ParseConstant.WRONG1;
import static fr.xebia.quiz.model.ParseConstant.WRONG2;

@ParseClassName(ParseConstant.TABLE_QUESTION)
public class Question extends ParseObject {

    public String getText() {
        return getString(TEXT);
    }

    public String getCorrect() {
        return getString(CORRECT);
    }

    public String getWrong1() {
        return getString(WRONG1);
    }

    public String getWrong2() {
        return getString(WRONG2);
    }

}
