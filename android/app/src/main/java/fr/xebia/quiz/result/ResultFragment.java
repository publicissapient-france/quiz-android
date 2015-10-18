package fr.xebia.quiz.result;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.xebia.quiz.R;
import fr.xebia.quiz.model.Guest;
import fr.xebia.quiz.model.QuestionResult;
import fr.xebia.quiz.model.Rank;
import fr.xebia.quiz.shared.Adapter;
import fr.xebia.quiz.shared.ItemView;
import timber.log.Timber;

import static fr.xebia.quiz.form.FormActivity.EXTRA_GUEST_ID;

public class ResultFragment extends Fragment {

    public static final String EXTRA_RESULTS = "EXTRA_RESULTS";

    @Bind(R.id.resultListView) ListView resultListView;
    @Bind(R.id.scoreTotalText) TextView scoreTotalText;
    @Bind(R.id.scoreText) TextView scoreText;

    private Adapter<QuestionResult, ItemView<QuestionResult>> adapter;
    private QuestionResult[] results;
    private String guestId;
    private int score;
    private long time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public static Fragment newInstance(String guestId, QuestionResult[] results) {
        ResultFragment fragment = new ResultFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelableArray(EXTRA_RESULTS, results);
        arguments.putString(EXTRA_GUEST_ID, guestId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            results = (QuestionResult[]) arguments.getParcelableArray(EXTRA_RESULTS);
            guestId = arguments.getString(EXTRA_GUEST_ID);
        } else {
            guestId = "";
            results = new QuestionResult[0];
        }
        adapter = new Adapter<>(getActivity(), results, R.layout.view_result_item);

        aggregateQuizInfo();

        submitScore();
    }

    private void submitScore() {
        ParseQuery<Guest> query = new ParseQuery<>(Guest.class);
        query.getInBackground(guestId, new GetCallback<Guest>() {
            @Override
            public void done(Guest guest, ParseException e) {
                if (e == null) {
                    guest.setScore(score);
                    guest.setTime(time);
                    guest.saveInBackground();

                    Rank rank = new Rank();
                    rank.setScore(score);
                    rank.setTime(time);
                    rank.setGuest(guest);
                    rank.setName(aggregateRankName(guest));
                    rank.saveInBackground();
                } else {
                    Timber.e(e, "Cannot retrieve guest");
                }
            }
        });
    }

    @NonNull
    private String aggregateRankName(Guest guest) {
        final String firstname = guest.getFirstname();
        if (!TextUtils.isEmpty(firstname) && firstname.length() > 1) {
            return firstname.substring(0, 1) + ". " + guest.getName();
        } else {
            return guest.getName();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resultListView.setAdapter(adapter);
        scoreTotalText.setText(getContext().getString(R.string.text_score, score, results.length));
        scoreText.setText(String.valueOf(score));
    }

    public void aggregateQuizInfo() {
        score = 0;
        time = 0;
        for (QuestionResult result : results) {
            if (result.isCorrect) {
                score++;
            }
            time += result.time;
        }
    }
}
