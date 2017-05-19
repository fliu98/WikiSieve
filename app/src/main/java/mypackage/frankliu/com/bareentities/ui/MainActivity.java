package mypackage.frankliu.com.bareentities.ui;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mypackage.frankliu.com.bareentities.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String externalQueryText = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            externalQueryText = getIntent().hasExtra(Intent.EXTRA_PROCESS_TEXT) ? getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString() : null;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out);
        transaction.add(R.id.fragment_space, MainPagerViewFragment.createFragment(externalQueryText));
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
