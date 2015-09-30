package fr.xebia.quiz.result;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.xebia.quiz.R;
import fr.xebia.quiz.shared.Adapter;
import fr.xebia.quiz.model.QuestionResult;
import fr.xebia.quiz.shared.ItemView;

public class ResultFragment extends Fragment {

    public static final String ARG_RESULTS = "ARG_RESULTS";

    @Bind(R.id.resultListView) ListView resultListView;
    @Bind(R.id.scoreText) TextView scoreText;

    private Adapter<QuestionResult, ItemView<QuestionResult>> adapter;
    private QuestionResult[] results;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public static Fragment newInstance(QuestionResult[] results) {
        ResultFragment fragment = new ResultFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelableArray(ARG_RESULTS, results);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            results = (QuestionResult[]) arguments.getParcelableArray(ARG_RESULTS);
        } else {
            results = new QuestionResult[0];
        }
        adapter = new Adapter<>(getActivity(), results, R.layout.view_result_item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resultListView.setAdapter(adapter);
        scoreText.setText(String.format("%d/%d", getScore(), results.length));
    }

    public int getScore() {
        int score = 0;
        for (QuestionResult result : results) {
            if (result.isCorrect) {
                score++;
            }
        }
        return score;
    }
}
