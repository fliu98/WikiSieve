package mypackage.frankliu.com.bareentities;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TextAnalysisRestAdapter {

    private TextAnalysisApi mAnalysisApi;
    private static final String API_URL = "https://api.dandelion.eu";
    private static final String API_TOKEN = "d22fa346152a460db894588a95a950be";

    public TextAnalysisRestAdapter() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mAnalysisApi = retrofit.create(TextAnalysisApi.class);
    }

    public Call<TextAnalysis> asyncAnalyse(String queryText){
        return mAnalysisApi.getEntities(queryText,API_TOKEN,"image,lod");
    }
}
