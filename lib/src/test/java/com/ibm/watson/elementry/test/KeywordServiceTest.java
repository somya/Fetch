package com.ibm.watson.elementry.test;


import com.ibm.watson.elementry.model.Keyword;
import com.ibm.watson.elementry.service.KeywordService;
import org.junit.Test;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class KeywordServiceTest
{

	@Test
	public void filterTest() throws InterruptedException
	{
		final CountDownLatch signal = new CountDownLatch( 1 );

		KeywordService service = new KeywordService();
		service.getKeywordData(
			"Hello I'd like to use Watson from IBM", new Callback<List<Keyword>>()
			{


				@Override
				public void success( final List<Keyword> keywords, final Response response )
				{

					signal.countDown();
				}

				@Override
				public void failure( final RetrofitError error )
				{
					signal.countDown();
				}
			}
		);

//		signal.await( 5, TimeUnit.SECONDS );
		signal.await();
	}
}
