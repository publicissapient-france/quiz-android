package fr.xebia.quiz.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import fr.xebia.quiz.R;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new QuestionFragment(), QuestionFragment.class.getSimpleName())
                .commit();
    }

}
