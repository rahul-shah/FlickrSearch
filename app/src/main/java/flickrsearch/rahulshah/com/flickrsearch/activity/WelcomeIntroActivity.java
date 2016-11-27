package flickrsearch.rahulshah.com.flickrsearch.activity;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import flickrsearch.rahulshah.com.flickrsearch.R;

public class WelcomeIntroActivity extends AppIntro {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Tour 1
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name), getString(R.string.tour_1_desc), R.drawable.howtotest, R.color.colorPrimary ));

        //Tour 2
        addSlide(AppIntroFragment.newInstance(getString(R.string.tour_2_header), getString(R.string.tour_2_desc), R.drawable.maxresdefault, R.color.colorPrimary ));

        //Tour 3
        addSlide(AppIntroFragment.newInstance(getString(R.string.tour_3_header), getString(R.string.tour_3_desc), R.drawable.share, R.color.colorPrimary ));

        // show the statusbar
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