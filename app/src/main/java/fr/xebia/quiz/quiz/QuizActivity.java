package fr.xebia.quiz.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.xebia.quiz.R;
import fr.xebia.quiz.model.Question;
import timber.log.Timber;

public class QuizActivity extends Activity {

    public static final int QUIZ_QUESTION_COUNT = 10;

    private static final Random RANDOM = new Random();

    @Bind(R.id.questionText) TextView questionText;
    @Bind(R.id.answer0Text) TextView answer0Text;
    @Bind(R.id.answer1Text) TextView answer1Text;
    @Bind(R.id.answer2Text) TextView answer2Text;

    private Question[] quizQuestion;
    private int score = 0;
    private int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        setupQuestion();
    }

    private void setupQuestion() {
        ParseQuery<Question> query = new ParseQuery<>(Question.class);
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<Question>() {
            @Override
            public void done(List<Question> questions, ParseException e) {
                if (e == null) {
                    randomizeQuestion(questions);
                } else {
                    Timber.e(e, "Cannot get question");
                }
            }
        });
    }

    private void randomizeQuestion(List<Question> questions) {
        quizQuestion = new Question[QUIZ_QUESTION_COUNT];
        Question[] questionArray = questions.toArray(new Question[questions.size()]);
        for (int i = 0; i < QUIZ_QUESTION_COUNT; i++) {
            int position = RANDOM.nextInt(questions.size() - 1 - i);
            Question question = questionArray[position];
            questionArray[position] = questionArray[questions.size() - 1 - i];
            quizQuestion[i] = question;
        }

        startQuiz();
    }

    private void startQuiz() {
        next();
    }

    @OnClick({R.id.answer0Text, R.id.answer1Text, R.id.answer2Text})
    @SuppressWarnings("unused")
    public void onAnswerClick(Button answer) {
        String text = (String) answer.getText();
        if (quizQuestion[current].getCorrect().equals(text)) {
            score += 1;
        }
        current++;

        if (current < quizQuestion.length) {
            next();
        } else {
            result();
        }
    }

    private void result() {
        Timber.i("Quiz ended: " + score);
    }

    private void next() {
        Question question = quizQuestion[current];

        questionText.setText(question.getText());

        List<String> answers = new ArrayList<>();
        answers.add(question.getCorrect());
        answers.add(question.getWrong1());
        answers.add(question.getWrong2());
        Collections.shuffle(answers);

        answer0Text.setText(answers.get(0));
        answer1Text.setText(answers.get(1));
        answer2Text.setText(answers.get(2));
    }
}
