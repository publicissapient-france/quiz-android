package fr.xebia.quiz.result;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.Bind;
import fr.xebia.quiz.R;
import fr.xebia.quiz.model.QuestionResult;
import fr.xebia.quiz.shared.ItemView;

public class ResultItemView extends ItemView<QuestionResult> {

    @Bind(R.id.questionText) TextView questionText;
    @Bind(R.id.givenText) TextView givenText;
    @Bind(R.id.correctText) TextView correctText;

    private QuestionResult result;

    public ResultItemView(Context context) {
        this(context, null);
    }

    public ResultItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResultItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(QuestionResult result) {
        if (result.equals(this.result)) {
            return;
        }
        this.result = result;

        questionText.setText(result.question);
        givenText.setText(result.givenAnswer);
        correctText.setText(result.correctAnswer);
    }
}
