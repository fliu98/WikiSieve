package mypackage.frankliu.com.bareentities;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainPagerViewFragment extends Fragment implements QueryFragment.OnHistoryChangedListener{

    private static final String EXTRA_EXTERNAL_QUERY_TEXT = "extra_external_query_text";

    private MainPagerViewAdapter mPagerAdapter;

    public static MainPagerViewFragment createFragment(String externalQueryText){

        MainPagerViewFragment fragment = new MainPagerViewFragment();
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_EXTERNAL_QUERY_TEXT,externalQueryText);
        fragment.setArguments(arguments);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_pager_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new MainPagerViewAdapter(getChildFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onHistoryChanged() {
        ((HistoryFragment)mPagerAdapter.getRegisteredFragment(1)).requestReload();
    }

    private class MainPagerViewAdapter extends FragmentStatePagerAdapter {

        private final int TAB_COUNT = 2;
        private String[] tabNames = new String[] {getString(R.string.current_query),getString(R.string.history)};
        private int[] imageResId = new int[] {R.drawable.ic_sift_24, R.drawable.ic_clock_24};

        private ArrayList<Fragment> registeredFragments = new ArrayList<Fragment>();

        public MainPagerViewAdapter (FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return QueryFragment.createFragment(getArguments().getString(EXTRA_EXTERNAL_QUERY_TEXT));
                case 1:
                    return HistoryFragment.createFragment();
                default:
                    return null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.add(fragment);
            return fragment;
        }

        public Fragment getRegisteredFragment(int position){
            return registeredFragments.get(position);
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable image = ResourcesCompat.getDrawable(getResources(),imageResId[position],null);
            if(image==null){
                return tabNames[position];
            }
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            // Replace blank spaces with image icon
            SpannableString sb = new SpannableString("   " + tabNames[position]);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }

    }
}
