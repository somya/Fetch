package com.ibm.watson.elementry.model;

import java.util.List;

/**
 * Created by somya on 5/5/15.
 */
public class Keyword
{
	//	@SerializedName( "startTimeUtc" )
	public String text;
	public float  relevance;
	public String group;
	public String image_url;
	public String description;
	public List<KeywordContent> content;

	public static class KeywordContent
	{
		public String url;
		public String type;
		public String image_url;
		public String headline;
	}
}
