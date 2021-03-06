package mypackage.frankliu.com.bareentities.web;

import mypackage.frankliu.com.bareentities.model.TextAnalysis;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TextAnalysisApi {
    @GET("/datatxt/nex/v1")
    Call<TextAnalysis> getEntities(
            @Query("text") String queryText,
            @Query("token") String token,
            @Query("include") String includeTypes
    );
}
