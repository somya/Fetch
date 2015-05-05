package com.ibm.watson.elementry.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ibm.watson.elementry.R;

/**
 * TODO: document your custom view class.
 */
public class RecordButton extends RelativeLayout
{

	private ProgressBar m_progressBar;
	private TextView    m_buttonText;
	private String      m_text;
	private ImageView   m_statusImage;
	private int         m_resRecord;
	private int         m_resPause;
	private int         m_resLoading;

	Status m_status;


	public static enum Status
	{
		Loading,
		Recording,
		Paused
	}

	public RecordButton( Context context )
	{
		super( context );
		init( null, 0 );
	}

	public RecordButton( Context context, AttributeSet attrs )
	{
		super( context, attrs );
		init( attrs, 0 );
	}

	public RecordButton( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
		init( attrs, defStyle );
	}

	private void init( AttributeSet attrs, int defStyle )
	{

		TypedArray a = getContext().obtainStyledAttributes( attrs, R.styleable.RecordButton );
		try {
			m_resLoading = a.getResourceId( R.styleable.RecordButton_img_loading, 0 );
			m_resRecord = a.getResourceId( R.styleable.RecordButton_img_recording, 0 );
			m_resPause = a.getResourceId( R.styleable.RecordButton_img_paused, 0 );
		} finally {
			a.recycle();
		}

		final LayoutInflater infalter = (LayoutInflater) getContext().getSystemService(
			Context.LAYOUT_INFLATER_SERVICE
		);
		final View view = infalter.inflate( R.layout.view_progress_button, this, true );

		m_progressBar = (ProgressBar) view.findViewById( R.id.progress_bar );
		m_buttonText = (TextView) view.findViewById( R.id.txt_btn_label );
		m_statusImage = (ImageView) view.findViewById( R.id.img_status );

		m_progressBar.getIndeterminateDrawable().setColorFilter(
			getResources().getColor( R.color.border_grey ), android.graphics.PorterDuff.Mode.SRC_IN
		);


//		m_buttonText.setText( m_text );
		setStatus( Status.Loading );
	}





	public void setStatus(Status status)
	{
		m_status = status;
//		setEnabled( false );
		switch ( status )
		{
			case Loading:
				m_progressBar.setVisibility( VISIBLE );
				m_statusImage.setImageResource(m_resLoading );
				m_buttonText.setTextColor( getResources().getColor( R.color.border_grey ) );
				break;
			case Recording:
				m_progressBar.setVisibility( GONE );
				m_statusImage.setImageResource( m_resPause );
				break;
			case Paused:
				m_progressBar.setVisibility( GONE );
				m_statusImage.setImageResource( m_resRecord );
				m_buttonText.setTextColor( getResources().getColor( R.color.red ) );
				setEnabled( true ); // Only enable click on error
				break;
		}
	}

	public Status getStatus()
	{
		return m_status;
	}

	public void setText( final CharSequence text )
	{
		m_buttonText.setText( text );
	}


	public boolean isLoading()
	{
		return  m_status == Status.Loading;
	}

	public boolean isRecording()
	{
		return  m_status == Status.Recording;
	}

	public boolean isPaused()
	{
		return  m_status == Status.Paused;
	}
}
