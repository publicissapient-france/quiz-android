package fr.xebia.quiz.form;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.xebia.quiz.R;
import fr.xebia.quiz.model.Guest;

public class FormActivity extends Activity implements Validator.ValidationListener {

    public static final Handler HANDLER = new Handler();

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

    private Validator validator;

    private final Runnable startQuiz = new Runnable() {
        @Override
        public void run() {
            // TODO start quiz
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ButterKnife.bind(this);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @OnClick(R.id.submitButton)
    @SuppressWarnings("unused")
    public void onSubmitButtonClick() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        Guest guest = new Guest();
        guest.setName(nameText.getText().toString());
        guest.setFirstname(firstnameText.getText().toString());
        guest.setEmail(emailText.getText().toString());
        guest.setPhone(phoneText.getText().toString());
        guest.setPostcode(postcodeText.getText().toString());
        guest.setYear(yearText.getText().toString());
        guest.setJob(jobText.getText().toString());

        final WeakReference<ProgressDialog> weakProgress = new WeakReference<>(ProgressDialog.show(this, null, getString(R.string.label_save)));
        final WeakReference<FormActivity> weakActivity = new WeakReference<>(this);
        guest.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                ProgressDialog dialog = weakProgress.get();
                if (dialog != null) {
                    dialog.dismiss();
                }
                FormActivity activity = weakActivity.get();
                if (activity == null) {
                    return;
                }
                Toast.makeText(activity, R.string.text_quiz_about_start, Toast.LENGTH_SHORT).show();
                HANDLER.postDelayed(startQuiz, Toast.LENGTH_SHORT);
            }
        });
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
