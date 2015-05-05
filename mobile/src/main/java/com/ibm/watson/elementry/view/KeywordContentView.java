package com.ibm.watson.elementry.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.ibm.watson.elementry.R;
import com.ibm.watson.elementry.model.KeywordContent;
import com.ibm.watson.elementry.util.StringUtils;
import com.squareup.picasso.Picasso;

/**
 * TODO: document your custom view class.
 */
public class KeywordContentView extends FrameLayout
{

	@InjectView( R.id.img_keyword ) ImageView m_keywordImage;
	@InjectView( R.id.txt_keyword ) TextView  m_keywordText;

	@InjectView( R.id.txt_description ) TextView       m_description;
	private                             KeywordContent m_keywordContent;

	public KeywordContentView( Context context )
	{
		super( context );
		init( null, 0 );
	}

	public KeywordContentView( Context context, AttributeSet attrs )
	{
		super( context, attrs );
		init( attrs, 0 );
	}

	public KeywordContentView( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
		init( attrs, defStyle );
	}

	private void init( AttributeSet attrs, int defStyle )
	{


		final LayoutInflater infalter = (LayoutInflater) getContext().getSystemService(
			Context.LAYOUT_INFLATER_SERVICE
		);
		final View view = infalter.inflate( R.layout.view_keyword_content, this, true );
		ButterKnife.inject( this, view );
	}

	public void setKeywordContent( final KeywordContent keyword )
	{
		m_keywordContent = keyword;

		if ( StringUtils.isNotEmpty( keyword.image_url ))
		{
			Picasso.with( getContext() ).load( keyword.image_url ).fit().into( m_keywordImage );
		}

		m_keywordText.setText( keyword.headline );
	}

}
