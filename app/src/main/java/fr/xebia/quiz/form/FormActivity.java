package fr.xebia.quiz.form;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.xebia.quiz.BuildConfig;
import fr.xebia.quiz.R;
import fr.xebia.quiz.model.Guest;
import fr.xebia.quiz.model.Question;
import fr.xebia.quiz.quiz.QuizActivity;
import timber.log.Timber;

import static fr.xebia.quiz.model.ParseConstant.TABLE_QUESTION;

public class FormActivity extends AppCompatActivity implements Validator.ValidationListener {

    public static final String EXTRA_GUEST_ID = "EXTRA_GUEST_ID";

    private static final Handler HANDLER = new Handler();

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.formScrollView) ViewGroup formViewGroup;
    @Bind(R.id.progress) ProgressBar progressBar;

    @NotEmpty(messageResId = R.string.validation_empty)
    @Bind(R.id.nameText) EditText nameText;

    @NotEmpty(messageResId = R.string.validation_empty)
    @Bind(R.id.firstnameText) EditText firstnameText;

    @NotEmpty(messageResId = R.string.validation_empty)
    @Email(messageResId = R.string.validation_email)
    @Bind(R.id.emailText) EditText emailText;

    @Bind(R.id.phoneText) EditText phoneText;
    @Bind(R.id.postcodeText) EditText postcodeText;

    @NotEmpty(messageResId = R.string.validation_empty)
    @Bind(R.id.yearText) EditText yearText;

    @Bind(R.id.jobText) EditText jobText;

    private boolean hasToWaitForQuestion = true;
    private Validator validator;
    private ProgressDialog dialog;
    private String guestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        validator = new Validator(this);
        validator.setValidationListener(this);

        login();
    }

    private void login() {
        if (ParseUser.getCurrentUser() == null) {
            progressBar.setVisibility(View.VISIBLE);
            formViewGroup.setVisibility(View.GONE);
            ParseUser.logInInBackground(BuildConfig.PARSE_ADMIN_USERNAME, BuildConfig.PARSE_ADMIN_PASSWORD, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        syncQuestion();
                    } else {
                        Timber.e(e, "Cannot login user");
                    }
                }
            });
        } else {
            syncQuestion();
        }
    }

    private void syncQuestion() {
        ParseQuery<Question> query = new ParseQuery<>(Question.class);
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<Question>() {
            @Override
            public void done(List<Question> questions, ParseException e) {
                if (e == null) {
                    hasToWaitForQuestion = questions.isEmpty();
                    fetchQuestion();
                } else {
                    Timber.e(e, "Cannot get question");
                }
            }
        });
    }

    private void saveQuestion(final List<Question> questions) {
        ParseObject.unpinAllInBackground(TABLE_QUESTION, new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground(TABLE_QUESTION, questions, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            progressBar.setVisibility(View.GONE);
                            formViewGroup.setVisibility(View.VISIBLE);
                            if (e == null) {
                                hasToWaitForQuestion = false;
                                if (dialog != null) {
                                    tryQuiz();
                                }
                            } else {
                                Timber.e(e, "Cannot pin question");
                            }
                        }
                    });
                } else {
                    Timber.e(e, "Cannot flush locale question");
                }
            }
        });
    }

    private void tryQuiz() {
        if (!hasToWaitForQuestion) {
            if (dialog != null) {
                dialog.dismiss();
            }
            startActivity(QuizActivity.newInstance(this, guestId));
        } else {
            dialog = ProgressDialog.show(this, null, getString(R.string.dialog_quiz_loading));
        }
    }

    private void fetchQuestion() {
        ParseQuery<Question> query = new ParseQuery<>(Question.class);
        query.findInBackground(new FindCallback<Question>() {
            @Override
            public void done(List<Question> questions, ParseException e) {
                if (e == null) {
                    saveQuestion(questions);
                } else {
                    Timber.e(e, "Cannot get questions");
                }
            }
        });
    }

    @OnClick(R.id.submitButton)
    @SuppressWarnings("unused")
    public void onSubmitButtonClick() {
        if (BuildConfig.DEBUG) {
            onValidationSucceeded();
        } else {
            validator.validate();
        }
    }

    @Override
    public void onValidationSucceeded() {
        final Guest guest = new Guest();
        guest.setName(nameText.getText().toString());
        guest.setFirstname(firstnameText.getText().toString());
        guest.setEmail(emailText.getText().toString());
        guest.setPhone(phoneText.getText().toString());
        guest.setPostcode(postcodeText.getText().toString());
        guest.setYear(yearText.getText().toString());
        guest.setJob(jobText.getText().toString());

        guest.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Toast.makeText(FormActivity.this, R.string.text_quiz_about_start, Toast.LENGTH_SHORT).show();
                HANDLER.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FormActivity.this.guestId = guest.getObjectId();
                        resetForm();
                        tryQuiz();
                    }
                }, Toast.LENGTH_SHORT);
            }
        });
    }

    private void resetForm() {
        nameText.setText("");
        firstnameText.setText("");
        emailText.setText("");
        phoneText.setText("");
        postcodeText.setText("");
        yearText.setText("");
        jobText.setText("");

        firstnameText.requestFocus();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
