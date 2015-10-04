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

    public void bind(int index, QuestionResult result) {
        if (result.equals(this.result)) {
            return;
        }
        this.result = result;

        questionText.setText(getContext().getString(R.string.text_question, index, result.question));
        givenText.setText(getContext().getString(R.string.text_answer, result.givenAnswer));
        correctText.setText(getContext().getString(R.string.text_answer_correct, result.correctAnswer));
    }

}
