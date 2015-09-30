package fr.xebia.quiz.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionResult implements Parcelable {

    public final String question;
    public final String givenAnswer;
    public final String correctAnswer;
    public final boolean isCorrect;

    public QuestionResult(String question, String givenAnswer, String correctAnswer) {
        this.question = question;
        this.givenAnswer = givenAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = givenAnswer.equals(correctAnswer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
        dest.writeString(this.givenAnswer);
        dest.writeString(this.correctAnswer);
        dest.writeByte(isCorrect ? (byte) 1 : (byte) 0);
    }

    protected QuestionResult(Parcel in) {
        this.question = in.readString();
        this.givenAnswer = in.readString();
        this.correctAnswer = in.readString();
        this.isCorrect = in.readByte() != 0;
    }

    public static final Creator<QuestionResult> CREATOR = new Creator<QuestionResult>() {
        public QuestionResult createFromParcel(Parcel source) {
            return new QuestionResult(source);
        }

        public QuestionResult[] newArray(int size) {
            return new QuestionResult[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionResult result = (QuestionResult) o;

        if (isCorrect != result.isCorrect) return false;
        if (!question.equals(result.question)) return false;
        if (!givenAnswer.equals(result.givenAnswer)) return false;
        return correctAnswer.equals(result.correctAnswer);

    }

    @Override
    public int hashCode() {
        int result = question.hashCode();
        result = 31 * result + givenAnswer.hashCode();
        result = 31 * result + correctAnswer.hashCode();
        result = 31 * result + (isCorrect ? 1 : 0);
        return result;
    }
}
