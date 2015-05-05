package com.ibm.watson.elementry.service;

import com.ibm.watson.elementry.model.Keyword;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

import java.util.List;

public class KeywordService
{
	private RestAdapter restAdapter;

	public interface IKeywordService
	{

		@POST( "/slides" )
		void slides(
			@Body
			KeywordSearchParams data, Callback<List<Keyword>> callback
		);
	}

	public static class KeywordSearchParams
	{
		public String text;
		public float  entities_threshold;

		public KeywordSearchParams()
		{
		}

		public KeywordSearchParams( final String text )
		{
			this.text = text;
		}
	}


	public void getKeywordData( String text, final Callback<List<Keyword>> callback )
	{
		restAdapter = new RestAdapter.Builder().setEndpoint( "http://pizza.mybluemix.net" ).setLogLevel(
			RestAdapter.LogLevel.FULL
		).build();

		final IKeywordService keywordService = restAdapter.create( IKeywordService.class );

		keywordService.slides( new KeywordSearchParams( text ), callback );
	}
}
