package fr.xebia.quiz.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import fr.xebia.quiz.R;

import static fr.xebia.quiz.form.FormActivity.EXTRA_GUEST_ID;

public class QuizActivity extends AppCompatActivity {

    private String guestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            guestId = intent.getStringExtra(EXTRA_GUEST_ID);
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, QuestionFragment.newInstance(guestId), QuestionFragment.class.getSimpleName())
                .commit();
    }

    public static Intent newInstance(Context context, String guestId) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(EXTRA_GUEST_ID, guestId);
        return intent;
    }
}
