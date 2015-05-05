package com.ibm.watson.elementry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.ibm.watson.elementry.model.Keyword;
import com.ibm.watson.elementry.service.KeywordService;
import com.ibm.watson.elementry.view.KeywordListItemView;
import com.ibm.watson.elementry.widget.RecordButton;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A placeholder fragment containing a simple view.
 */
public class KeywordListFragment extends Fragment
{
	private static final String LOG_TAG = "MainActivityFragment";
	@InjectView( R.id.btn_rec_pause ) RecordButton m_recButton;
	@InjectView( R.id.txt_title )     EditText     m_title;

	@InjectView( R.id.list_keywords ) public RecyclerView m_keywordRecycleView;


	private final int REQ_CODE_SPEECH_INPUT = 100;

	boolean isRecording = false;
	private SpeechRecognizer    m_speechRecognizer;
	private KeywordAdapter      m_keywordAdapter;
	private LinearLayoutManager mLayoutManager;

	@OnClick( R.id.btn_rec_pause )
	public void toggleAudioRecording()
	{

		if ( !isRecording )
		{
			// Start Recording
			startRecording();
		}
		else
		{
			// Stop Recording
			stopRecording();
		}
	}


	private void startRecording()
	{

		isRecording = true;

		m_speechRecognizer = SpeechRecognizer.createSpeechRecognizer( getActivity() );
		m_recButton.setStatus( RecordButton.Status.Loading );

		m_speechRecognizer.setRecognitionListener(
			new RecognitionListener()
			{
				@Override
				public void onReadyForSpeech( final Bundle params )
				{

				}

				@Override
				public void onBeginningOfSpeech()
				{
					m_recButton.setStatus( RecordButton.Status.Recording );
				}

				@Override
				public void onRmsChanged( final float rmsdB )
				{

				}

				@Override
				public void onBufferReceived( final byte[] buffer )
				{

				}

				@Override
				public void onEndOfSpeech()
				{
					m_recButton.setStatus( RecordButton.Status.Paused );
				}

				@Override
				public void onError( final int error )
				{
					Log.e( "MainActivityFragment", "Error in onError: " + error );
					stopRecording();
				}

				@Override
				public void onResults( final Bundle results )
				{
					ArrayList<String> allRecongnitions = results.getStringArrayList(
						SpeechRecognizer.RESULTS_RECOGNITION
					);

					if ( null != allRecongnitions && allRecongnitions.size() > 0 )
					{
						String s = allRecongnitions.get( 0 ); // highest prob match

						KeywordService keywordService = new KeywordService();
						keywordService.getKeywordData( s, new Callback<List<Keyword>>() {
							                               @Override
							                               public void success(
								                               final List<Keyword> keywords,
								                               final Response response
							                               )
							                               {
								                               m_keywordAdapter.setKeywords(keywords);
							                               }

							                               @Override
							                               public void failure( final RetrofitError error )
							                               {

							                               }
						                               } );

						Log.d( "MainActivityFragment", String.format( "onResults : s = %s", s ) );
					}
				}

				@Override
				public void onPartialResults( final Bundle partialResults )
				{
					ArrayList<String> allRecongnitions = partialResults.getStringArrayList(
						SpeechRecognizer.RESULTS_RECOGNITION
					);
					if ( null != allRecongnitions && allRecongnitions.size() > 0 )
					{
						String s = allRecongnitions.get( 0 ); // highest prob match
						Log.d( "MainActivityFragment", String.format( "onPartialResults : s = %s", s ) );
					}


				}

				@Override
				public void onEvent( final int eventType, final Bundle params )
				{

				}
			}
		);

		m_speechRecognizer.startListening( getIntentForSpeech() );
	}

	private void stopRecording()
	{
		if (m_speechRecognizer != null)
		{
			m_speechRecognizer.stopListening();
			m_speechRecognizer.cancel();
			m_speechRecognizer.destroy();
		}
		m_speechRecognizer = null;
		isRecording = false;
	}


	private Intent getIntentForSpeech()
	{
		Intent intent = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
		intent.putExtra(
			RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
		);
		intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault() );
		intent.putExtra( RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 5000 );
		intent.putExtra( RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000 );
		intent.putExtra( RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 5000 );
		intent.putExtra(
			RecognizerIntent.EXTRA_PROMPT, getString( R.string.speech_prompt )
		);
		return intent;
	}



	public KeywordListFragment()
	{
	}

	@Override
	public View onCreateView(
		LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
	)
	{
		final View view = inflater.inflate( R.layout.fragment_main, container, false );
		ButterKnife.inject( this, view );
		return view;
	}

	@Override
	public void onViewCreated(
		final View view,
		@Nullable
		final Bundle savedInstanceState
	)
	{
		super.onViewCreated( view, savedInstanceState );
		m_recButton.setStatus( RecordButton.Status.Paused );

		m_keywordAdapter = new KeywordAdapter( getActivity() );
		m_keywordRecycleView.setAdapter( m_keywordAdapter );

		mLayoutManager = new LinearLayoutManager( getActivity() );
		m_keywordRecycleView.setLayoutManager( mLayoutManager );

	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		ButterKnife.reset( this );

	}

	@Override
	public void onStop()
	{
		super.onStop();

		// TODO Stop recording.
		if ( isRecording )
		{
			stopRecording();
		}
	}

	private class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.ViewHolder>
	{
		private List<Keyword> m_keywords = new ArrayList<>();
		Context m_context;

		private KeywordAdapter( final Context context )
		{
			m_context = context;
		}

		public void setKeywords( final List<Keyword> keywords )
		{
			m_keywords = keywords;
			notifyDataSetChanged();
		}

		public List<Keyword> getKeywords()
		{
			return m_keywords;
		}

		// Provide a reference to the views for each data item
		// Complex data items may need more than one view per item, and
		// you provide access to all the views for a data item in a view holder
		public class ViewHolder extends RecyclerView.ViewHolder
		{
			// each data item is just a string in this case
			public KeywordListItemView m_keywordListItemView;

			public ViewHolder( final KeywordListItemView keywordListItemView )
			{
				super( keywordListItemView );
				m_keywordListItemView = keywordListItemView;
				m_keywordListItemView.setOnClickListener(
					new View.OnClickListener()
					{
						@Override
						public void onClick( final View v )
						{
							//							Navigator.ToKeyword( keywordListItemView.getKeyword().getId
							// () );
						}
					}
				);
			}
		}

		@Override
		public ViewHolder onCreateViewHolder(
			final ViewGroup viewGroup, final int i
		)
		{
			return new ViewHolder( new KeywordListItemView( m_context ) );
		}

		@Override
		public void onBindViewHolder( final ViewHolder viewHolder, final int i )
		{
			viewHolder.m_keywordListItemView.setKeyword( m_keywords.get( i ) );
		}

		@Override
		public long getItemId( final int position )
		{
			return position;
		}

		@Override
		public int getItemCount()
		{
			return m_keywords.size();
		}


	}
}
