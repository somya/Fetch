package com.ibm.watson.elementry.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.ibm.watson.elementry.R;
import com.ibm.watson.elementry.model.Keyword;
import com.ibm.watson.elementry.util.StringUtils;
import com.squareup.picasso.Picasso;

/**
 * TODO: document your custom view class.
 */
public class KeywordListItemView extends RelativeLayout
{

	@InjectView( R.id.img_game )      ImageView m_gameImage;
	@InjectView( R.id.txt_game_name ) TextView  m_gameName;

	@InjectView( R.id.txt_game_time )   TextView m_gameTime;
	@InjectView( R.id.txt_description ) TextView m_description;
	@InjectView( R.id.txt_platform )    TextView m_platform;
	@InjectView( R.id.txt_gamer_style ) TextView m_gamerStyle;
	private                             Keyword  m_keyword;

	public KeywordListItemView( Context context )
	{
		super( context );
		init( null, 0 );
	}

	public KeywordListItemView( Context context, AttributeSet attrs )
	{
		super( context, attrs );
		init( attrs, 0 );
	}

	public KeywordListItemView( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
		init( attrs, defStyle );
	}

	private void init( AttributeSet attrs, int defStyle )
	{

		final LayoutInflater infalter = (LayoutInflater) getContext().getSystemService(
			Context.LAYOUT_INFLATER_SERVICE
		);
		final View view = infalter.inflate( R.layout.list_item_keyword, this, true );
		ButterKnife.inject( this, view );
	}

	public void setKeyword( final Keyword keyword )
	{
		m_keyword = keyword;

		if ( StringUtils.isNotEmpty( keyword.image_url ))
		{
			Picasso.with( getContext() ).load( keyword.image_url ).centerCrop().fit().into( m_gameImage );
		}

		m_gameName.setText( keyword.text);
//		m_gameTime.setText( DateUtils.getRelativeTimeSpanString( getContext(), keyword.getStartTime().getTime() ) );
		m_description.setText( keyword.description);
//		m_platform.setText( GamePlatformUtils.labelResIdForPlatform( keyword.getPlatform()) );
//		m_gamerStyle.setText( keyword.getPlayStyle().name());
	}

	public Keyword getKeyword()
	{
		return m_keyword;
	}
}
