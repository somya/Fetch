package com.ibm.watson.elementry;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.ibm.watson.elementry.model.Keyword;
import com.ibm.watson.elementry.view.KeywordContentView;


/**
 * A placeholder fragment containing a simple view.
 */
public class KeywordDetailFragment extends Fragment
{
	private static final String LOG_TAG = "KeywordDetailFragment";

	@InjectView( R.id.list_keywords ) public RecyclerView m_keywordRecycleView;

	Keyword m_keyword = null;


	private final int REQ_CODE_SPEECH_INPUT = 100;

	private KeywordDetailAdapter m_keywordAdapter;
	private LinearLayoutManager  mLayoutManager;


	public KeywordDetailFragment()
	{
	}

	@Override
	public View onCreateView(
		LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
	)
	{
		final View view = inflater.inflate( R.layout.fragment_keyword_list, container, false );
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

		m_keywordAdapter = new KeywordDetailAdapter( getActivity() );
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


	}

	private class KeywordDetailAdapter extends RecyclerView.Adapter<KeywordDetailAdapter.ViewHolder>
	{
		private Keyword m_keyword = null;
		Context m_context;

		private KeywordDetailAdapter( final Context context )
		{
			m_context = context;
		}

		public void setKeyword( final Keyword keyword )
		{
			m_keyword = keyword;
			notifyDataSetChanged();
		}


		// Provide a reference to the views for each data item
		// Complex data items may need more than one view per item, and
		// you provide access to all the views for a data item in a view holder
		public class ViewHolder extends RecyclerView.ViewHolder
		{
			// each data item is just a string in this case
			public KeywordContentView m_keywordContentView;

			public ViewHolder( final KeywordContentView keywordContentView )
			{
				super( keywordContentView );
				m_keywordContentView = keywordContentView;
				m_keywordContentView.setOnClickListener(
					new View.OnClickListener()
					{
						@Override
						public void onClick( final View v )
						{
							//							Navigator.ToKeyword( keywordDetailView.getKeyword().getId
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
			return new ViewHolder( new KeywordContentView( m_context ) );
		}

		@Override
		public void onBindViewHolder( final ViewHolder viewHolder, final int i )
		{
			viewHolder.m_keywordContentView.setKeywordContent( m_keyword.content.get( i ) );
		}

		@Override
		public long getItemId( final int position )
		{
			return position;
		}

		@Override
		public int getItemCount()
		{
			if ( null == m_keyword || null == m_keyword.content )
			{
				return 0;
			}
			return m_keyword.content.size();
		}


	}
}
