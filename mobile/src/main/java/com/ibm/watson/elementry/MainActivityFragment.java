package com.ibm.watson.elementry;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.ibm.watson.elementry.widget.RecordButton;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
	private static final String LOG_TAG = "MainActivityFragment";
	@InjectView( R.id.btn_rec_pause ) RecordButton m_recButton;
	@InjectView( R.id.txt_title )     EditText     m_title;


	private final int REQ_CODE_SPEECH_INPUT = 100;

	boolean isRecording = false;
	private MediaRecorder    mRecorder;
	private SpeechRecognizer m_speechRecognizer;

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
		//		mRecorder.stop();
		//		mRecorder.release();
		//		mRecorder = null;
		m_speechRecognizer.cancel();
		m_speechRecognizer.destroy();
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

	/**
	 * Receiving speech input
	 */
	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		super.onActivityResult( requestCode, resultCode, data );

		switch ( requestCode )
		{
			case REQ_CODE_SPEECH_INPUT:
			{
				if ( resultCode == Activity.RESULT_OK && null != data )
				{

					Log.d(
						"MainActivityFragment", String.format(
							"onActivityResult : data = %s", data
						)
					);
					ArrayList<String> result = data.getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS );
					final String s = result.get( 0 );


					//					txtSpeechInput.setText( s );
					//					promptSpeechInput();
					Log.d( "MainActivityFragment", String.format( "s : s = %s", s ) );
				}
				break;
			}

		}
	}

	public MainActivityFragment()
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
}
