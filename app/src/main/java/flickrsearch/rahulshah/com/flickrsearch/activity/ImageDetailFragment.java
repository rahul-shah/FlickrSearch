package flickrsearch.rahulshah.com.flickrsearch.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import flickrsearch.rahulshah.com.flickrsearch.R;
import flickrsearch.rahulshah.com.flickrsearch.model.ImageHolder;


public class ImageDetailFragment extends DialogFragment
{
    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.fragment_image_detail_count) TextView mImageCount;
    @BindView(R.id.fragment_image_detail_title) TextView mImageTitle;
    @BindView(R.id.fragment_image_detail_date) TextView mImageDate;
    @BindView(R.id.fragment_image_detail_fab) FloatingActionButton mShareImageBtn;

    private ArrayList<ImageHolder> mListOfImages;
    private MyViewPagerAdapter mViewPagerAdapter;
    private int mSelectedPosition = 0;

    static ImageDetailFragment newInstance()
    {
        ImageDetailFragment f = new ImageDetailFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_image_detail, container, false);
        ButterKnife.bind(this,v);
        mListOfImages = (ArrayList<ImageHolder>) getArguments().getSerializable("images");
        mSelectedPosition = getArguments().getInt("position");

        mViewPagerAdapter = new MyViewPagerAdapter();
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(mSelectedPosition);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpViews();
    }

    private void setUpViews()
    {
        mShareImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSharingMenu(mSelectedPosition);
            }
        });
    }

    private void createSharingMenu(int imagePosition)
    {
        //Uri uri = Uri.parse(images.get(imagePosition).getFullImage());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mListOfImages.get(imagePosition).getFullImage());
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent,"send to"));
    }

    private void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position, false);
        displayMetaInfo(mSelectedPosition);
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            mSelectedPosition = position;
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        mImageCount.setText((position + 1) + " of " + mListOfImages.size());

        ImageHolder image = mListOfImages.get(position);
        mImageTitle.setText(image.getName());
        mImageDate.setText(image.getTimestamp());
    }

    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            ImageHolder image = mListOfImages.get(position);

            Glide.with(getActivity()).load(image.getImage())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return mListOfImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
