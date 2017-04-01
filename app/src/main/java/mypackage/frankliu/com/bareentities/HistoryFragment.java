package mypackage.frankliu.com.bareentities;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private static final int HISTORY_LOADER_ID = 1;

    RecyclerView mRecyclerView;
    TextView mEmptyTextView;
    HistoryItemAdapter mAdapter;

    ArrayList<PastQuery> mPastQueries;

    public static HistoryFragment createFragment(){
        return new HistoryFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.past_query_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HistoryItemAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.GONE);

        mEmptyTextView = (TextView)view.findViewById(R.id.empty);

        getLoaderManager().initLoader(HISTORY_LOADER_ID, null, new LoaderManager.LoaderCallbacks<ArrayList<PastQuery>>() {

            @Override
            public Loader<ArrayList<PastQuery>> onCreateLoader(int id, Bundle args) {
                return new GetPastQueriesTask(getContext());
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<PastQuery>> loader, ArrayList<PastQuery> data) {
                mPastQueries = data;
                if(mPastQueries.size()>0){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyTextView.setVisibility(View.GONE);
                }else{
                    mEmptyTextView.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<PastQuery>> loader) {
                //Do nothing.
            }
        }).forceLoad();
    }

    private class HistoryItemAdapter extends RecyclerView.Adapter<PastQueryViewHolder>{

        @Override
        public PastQueryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_past_query,parent,false);
            return new PastQueryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final PastQueryViewHolder holder, int position) {
            holder.bindPastQuery(mPastQueries.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.pager_view, MainPagerViewFragment.createFragment(mPastQueries.get(holder.getAdapterPosition()).getQueryString()));
                    ft.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPastQueries.size();
        }
    }

    private class PastQueryViewHolder extends RecyclerView.ViewHolder{

        private final ImageView mIcon;
        private final TextView mDateLabel;
        private final TextView mConceptsLabel;
        private final TextView mQueryField;

        private PastQueryViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.query_image_preview);
            mDateLabel = (TextView) itemView.findViewById(R.id.date_label);
            mConceptsLabel = (TextView) itemView.findViewById(R.id.concept_count_label);
            mQueryField = (TextView) itemView.findViewById(R.id.query_string_field);
        }

        private void bindPastQuery(PastQuery query){
            if(query.getImageUri()!=null) {
                Picasso.with(getContext()).load(query.getImageUri()).into(mIcon);
            }
            mDateLabel.setText(query.getTimestamp());
            mConceptsLabel.setText(Integer.toString(query.getConceptCount()) + " " + getString(R.string.concepts));
            mQueryField.setText(query.getQueryString());
        }
    }

    public void requestReload(){
        if(getLoaderManager().getLoader(HISTORY_LOADER_ID)!=null){
            getLoaderManager().getLoader(HISTORY_LOADER_ID).forceLoad();
            if(mPastQueries.size()>0 && mEmptyTextView.isShown()){
                mEmptyTextView.setVisibility(View.GONE);
            }
        }
    }
}
