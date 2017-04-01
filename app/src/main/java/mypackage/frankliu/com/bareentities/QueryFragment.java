package mypackage.frankliu.com.bareentities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QueryFragment extends Fragment {

    private static final String EXTRA_QUERY_TEXT = "EXTRA_QUERY_TEXT";
    private static final String EXTRA_TEXT_ANALYSIS = "EXTRA_TEXT_ANALYSIS";
    private static final String EXTRA_QUERY_STRING_RECORD = "EXTRA_QUERY_STRING_RECORD";
    private static final String EXTRA_RESULT_RECEIVER = "EXTRA_RESULT_RECEIVER";

    private EditText mQueryTextEdit;
    private ImageButton mExpandButton;
    private ImageButton mCollapseButton;
    private Button mSiftButton;
    private RecyclerView mRecyclerView;
    private RelativeLayout mConceptsTitle;
    private TextView mEmptyText;
    private FloatingActionButton mResetButton;

    private TextAnalysisRestAdapter mRestAdapter;

    private ArrayList<Concept> mConcepts;

    private OnHistoryChangedListener mOnHistoryChangedListener;

    public static QueryFragment createFragment(String queryText){
        QueryFragment fragment = new QueryFragment();
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_QUERY_TEXT,queryText);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        try{
            mOnHistoryChangedListener = (OnHistoryChangedListener) getParentFragment();
        }catch(ClassCastException e){
            throw new RuntimeException(getParentFragment().getClass().getSimpleName() + " must implement OnHistoryChangedListener.");
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_query, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mQueryTextEdit = (EditText) view.findViewById(R.id.query_text_edit);
        mExpandButton = (ImageButton) view.findViewById(R.id.expand_button);
        mCollapseButton = (ImageButton) view.findViewById(R.id.collapse_button);
        mSiftButton = (Button) view.findViewById(R.id.sift_button);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.concept_recycler_view);
        mConceptsTitle = (RelativeLayout) view.findViewById(R.id.concepts_title);
        mEmptyText = (TextView) view.findViewById(R.id.empty);
        mResetButton = (FloatingActionButton) view.findViewById(R.id.reset_button);

        mExpandButton.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mConceptsTitle.setVisibility(View.GONE);
        mEmptyText.setVisibility(View.GONE);
        mResetButton.setVisibility(View.GONE);

        toggleLoadIndication();

        mQueryTextEdit.setText(getArguments().getString(EXTRA_QUERY_TEXT)!=null ? getArguments().getString(EXTRA_QUERY_TEXT) : "");

        mRestAdapter = new TextAnalysisRestAdapter();

        mExpandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandButton.setVisibility(View.GONE);
                mCollapseButton.setVisibility(View.VISIBLE);
                mQueryTextEdit.setVisibility(View.VISIBLE);
            }
        });

        mCollapseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollapseButton.setVisibility(View.GONE);
                mExpandButton.setVisibility(View.VISIBLE);
                mQueryTextEdit.setVisibility(View.GONE);
            }
        });

        mSiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mQueryTextEdit.getText().toString().length()!=0) {
                    beginQuery();
                }else{
                    new AlertDialog.Builder(getContext())
                            .setMessage(getString(R.string.empty_query))
                            .setNeutralButton(R.string.close,null)
                            .show();
                }
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmptyText.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mConceptsTitle.setVisibility(View.GONE);
                mExpandButton.setVisibility(View.GONE);
                mResetButton.setVisibility(View.GONE);

                mSiftButton.setVisibility(View.VISIBLE);
                mCollapseButton.setVisibility(View.VISIBLE);
                mQueryTextEdit.setVisibility(View.VISIBLE);
            }
        });

        //No external query text
        if(mQueryTextEdit.getText().toString().length()==0){
            mQueryTextEdit.setText(getString(R.string.prompt_enter_text));
            mQueryTextEdit.selectAll();
        }else{ // query text already in place, begin query
            beginQuery();
        }
    }

    private void beginQuery(){
        if(mRestAdapter==null){
            mRestAdapter = new TextAnalysisRestAdapter();
        }

        toggleLoadIndication();

        Call <TextAnalysis> call = mRestAdapter.asyncAnalyse(mQueryTextEdit.getText().toString());

        call.enqueue(new Callback<TextAnalysis>() {
            @Override
            public void onResponse(Call<TextAnalysis> call, Response<TextAnalysis> response) {
                displayQueryResults(response.body());

                //Store to database
                if(getContext()!=null) {
                    Intent intent = new Intent(getContext(), InsertPastQueryIntentService.class);
                    intent.putExtra(EXTRA_TEXT_ANALYSIS, response.body());
                    intent.putExtra(EXTRA_QUERY_STRING_RECORD, mQueryTextEdit.getText().toString());
                    ResultReceiver receiver = new InsertResultReceiver(null);
                    intent.putExtra(EXTRA_RESULT_RECEIVER, receiver);
                    getContext().startService(intent);
                }

                toggleLoadIndication();
            }

            @Override
            public void onFailure(Call<TextAnalysis> call, Throwable throwable) {
                new AlertDialog.Builder(getContext())
                        .setMessage(getString(R.string.query_failed))
                        .setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mQueryTextEdit.setVisibility(View.VISIBLE);
                                mSiftButton.setVisibility(View.VISIBLE);
                            }
                        })
                        .show();
                toggleLoadIndication();
            }
        });
    }

    private void toggleLoadIndication(){
        if(getView()!=null) {
            AVLoadingIndicatorView progressBar = (com.wang.avi.AVLoadingIndicatorView) getView().findViewById(R.id.progress_bar);
            if(progressBar.isShown()){
                progressBar.smoothToHide();
            }else{
                progressBar.smoothToShow();
                mSiftButton.setVisibility(View.GONE);
            }
        }

    }

    private void toggleKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void displayQueryResults(TextAnalysis analysis){
        if (analysis!=null&&analysis.getAnnotations()!=null&&analysis.getAnnotations().size()!=0){
            mConcepts = filterConcepts(analysis.getAnnotations());
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(new AnalysisViewAdapter());
            mRecyclerView.setVisibility(View.VISIBLE);
            mConceptsTitle.setVisibility(View.VISIBLE);
        }else{
            mEmptyText.setVisibility(View.VISIBLE);
        }
        mResetButton.setVisibility(View.VISIBLE);
    }

    private class AnalysisViewAdapter extends RecyclerView.Adapter<ConceptViewHolder>{

        @Override
        public ConceptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_concept,parent,false);
            return new ConceptViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ConceptViewHolder holder, int position) {
            holder.bindConcept(mConcepts.get(position));
        }

        @Override
        public int getItemCount() {
            return mConcepts.size();
        }

    }

    private class ConceptViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mIcon;
        private final TextView mConceptLabel;
        private final ImageButton mWiki;
        private final ImageButton mDb;

        private ConceptViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView)itemView.findViewById(R.id.concept_icon);
            mConceptLabel = (TextView) itemView.findViewById(R.id.concept_label);
            mWiki = (ImageButton) itemView.findViewById(R.id.wiki_icon);
            mDb = (ImageButton) itemView.findViewById(R.id.db_icon);
        }

        private void bindConcept(final Concept concept){
            if(concept.getImage().getThumbnail()!=null) {
                Picasso.with(getContext()).load(concept.getImage().getThumbnail()).into(mIcon);
            }
            mConceptLabel.setText(concept.getLabel());
            mWiki.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewLink = new Intent (Intent.ACTION_VIEW, Uri.parse(concept.getLod().getWikipedia()));
                    startActivity(viewLink);
                }
            });
            mDb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewLink = new Intent (Intent.ACTION_VIEW, Uri.parse(concept.getLod().getDbpedia()));
                    startActivity(viewLink);
                }
            });
        }
    }

    private class InsertResultReceiver extends ResultReceiver{


        public InsertResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            mOnHistoryChangedListener.onHistoryChanged();
        }
    }

    public interface OnHistoryChangedListener{
        void onHistoryChanged();
    }

    private ArrayList<Concept> filterConcepts(ArrayList<Concept> items){
        HashSet<Concept> uniqueConcepts = new HashSet<>(items);
        return new ArrayList<>(uniqueConcepts);
    }

}
