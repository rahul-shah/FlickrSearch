package flickrsearch.rahulshah.com.flickrsearch.activity;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import flickrsearch.rahulshah.com.flickrsearch.R;

public class WelcomeIntroActivity extends AppIntro {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Flickr Search", "Smartly named app that lets you search flickr for images.", R.drawable.howtotest, Color.parseColor("#2196F3") ));
        addSlide(AppIntroFragment.newInstance("Search Images", "Test description", R.drawable.howtotest, Color.parseColor("#2196F3") ));
        addSlide(AppIntroFragment.newInstance("View them", "Test description", R.drawable.howtotest, Color.parseColor("#2196F3") ));
        addSlide(AppIntroFragment.newInstance("Share them with your friends", "Test description", R.drawable.howtotest, Color.parseColor("#2196F3") ));

        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // SHOW or HIDE the statusbar
        showStatusBar(true);

        // Hide Skip/Done button
        showSkipButton(false);
        showDoneButton(true);

        // Animations -- use only one of the below. Using both could cause errors.
        setFadeAnimation();
    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when slide is changed
    }
}