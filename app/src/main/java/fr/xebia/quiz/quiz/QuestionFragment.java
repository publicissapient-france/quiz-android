package fr.xebia.quiz.quiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import fr.xebia.quiz.model.QuestionResult;
import fr.xebia.quiz.result.ResultFragment;
import timber.log.Timber;

public class QuestionFragment extends Fragment {

    public static final int QUIZ_QUESTION_COUNT = 10;

    private static final Random RANDOM = new Random();

    @Bind(R.id.questionText) TextView questionText;
    @Bind(R.id.answer0Text) TextView answer0Text;
    @Bind(R.id.answer1Text) TextView answer1Text;
    @Bind(R.id.answer2Text) TextView answer2Text;

    private QuestionResult[] results;

    private Question[] quizQuestion;
    private int current = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        results = new QuestionResult[QUIZ_QUESTION_COUNT];
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
        boolean correct = false;
        String text = (String) answer.getText();
        final Question question = quizQuestion[current];
        if (question.getCorrect().equals(text)) {
            correct = true;
        }

        results[current] = new QuestionResult(question.getText(), text, question.getCorrect());

        current++;

        if (correct) {
            correct();
        } else {
            wrong();
        }
    }

    private void wrong() {
        if (current < quizQuestion.length) {
            next();
        } else {
            result();
        }
    }

    private void correct() {
        if (current < quizQuestion.length) {
            next();
        } else {
            result();
        }
    }

    private void result() {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, ResultFragment.newInstance(results), ResultFragment.class.getSimpleName())
                .commit();
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
