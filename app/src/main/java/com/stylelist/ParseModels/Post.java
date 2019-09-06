package com.stylelist.ParseModels;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.stylelist.Utils.Constants;

import java.util.ArrayList;
import java.util.Collection;

/*
 * An extension of ParseObject that makes
 * it more convenient to access information
 * about a given Post
 */

@ParseClassName("Post")
public class Post extends ParseObject {

	ParseFile postImage;

	public Post() {

	}

	public ArrayList<String> getLocations() {
		ArrayList<String> hashTags = new ArrayList<>();
		try {
			if (fetchIfNeeded().get(Constants.POST_LOCATION_COUNTRY) != null && fetchIfNeeded().get(Constants.POST_LOCATION_CITY) != null) {
				hashTags.addAll((Collection<? extends String>) get(Constants.POST_LOCATION_CITY));
				hashTags.addAll((Collection<? extends String>) get(Constants.POST_LOCATION_COUNTRY));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return hashTags;
	}

    public ArrayList<String> getConditions() {
        ArrayList<String> hashTags = new ArrayList<>();
        try {
            if (fetchIfNeeded().get(Constants.POST_CONDITION) != null) {
                hashTags.addAll((Collection<? extends String>) get(Constants.POST_CONDITION));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hashTags;
    }

	public ArrayList<String> getHashTags() {
	    ArrayList<String> hashTags = new ArrayList<>();
        try {
            if (fetchIfNeeded().get(Constants.POST_HASH_TAGS) != null) {
                hashTags.addAll((Collection<? extends String>) get(Constants.POST_HASH_TAGS));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hashTags;
    }

	public ArrayList<String> getSaleTypes() {
		ArrayList<String> saleTypes = new ArrayList<>();
		try {
			if (fetchIfNeeded().get(Constants.POST_SALE_TYPE) != null) {
				saleTypes.addAll((Collection<? extends String>) get(Constants.POST_SALE_TYPE));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return saleTypes;
	}

	public String getStylTag() {
		try {
			return fetchIfNeeded().getString(Constants.POST_STYLE_TAG);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	public void setStylTag(String stylTag) {
		put(Constants.POST_STYLE_TAG, stylTag);
	}

	public ParseFile getImage() {
		try {
			 postImage = fetchIfNeeded().getParseFile("image");
		} catch (ParseException e) {
			e.printStackTrace();
			postImage = null;
		}

		return postImage;
	}


	public void setImage(ParseFile file) {
		put("image", file);
	}


	public ParseUser getUser() {
		try {
			return fetchIfNeeded().getParseUser("user");
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}


	public void setUser(ParseUser user) {
		put("user", user);
	}


	public ParseFile getThumbnail() {
		try {
			postImage = fetchIfNeeded().getParseFile("thumbnail");
		} catch (ParseException e) {
			e.printStackTrace();
			postImage = null;
		}

		return postImage;
	}

	public void setThumbnail(ParseFile file) {
		put("thumbnail", file);
	}
}
