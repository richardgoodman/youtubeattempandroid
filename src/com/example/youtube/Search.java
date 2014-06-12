package com.example.youtube;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Search {

    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    Iterator<SearchResult> iteratorSearchResults;
    String results;

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    public YouTube youtube;
    
    public Search() {
    	
    }

    /**
     * Initialize a YouTube object to search for videos on YouTube. Then
     * display the name and thumbnail image of each video in the result set.
     *
     * @param args command line args.
     */
    public String main() {
    	
        results = "hi";
    	try 
    	{
	        // This object is used to make YouTube Data API requests. The last
	        // argument is required, but since we don't need anything
	        // initialized when the HttpRequest is initialized, we override
	        // the interface and provide a no-op function.
	
	        // Prompt the user to enter a query term.
	        String queryTerm = "squirrel";
	
	        // Define the API request for retrieving search results.
	        YouTube.Search.List search = youtube.search().list("id,snippet");
	
	        // Set your developer key from the {{ Google Cloud Console }} for
	        // non-authenticated requests. See:
	        // {{ https://cloud.google.com/console }}
	        String apiKey = "AIzaSyBlU6ptjpaO64CRUaBdhWtWqbuxWsSjy0Q";
	        search.setKey(apiKey);
	        search.setQ(queryTerm);
	
	        // Restrict the search results to only include videos. See:
	        // https://developers.google.com/youtube/v3/docs/search/list#type
	        search.setType("video");
	
	        // To increase efficiency, only retrieve the fields that the
	        // application uses.
	        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
	        search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
	
	        // Call the API and print results.
	        SearchListResponse searchResponse = search.execute();
	        List<SearchResult> searchResultList = searchResponse.getItems();
	        
	        if (searchResultList != null) 
	        {
	        	results = "\n\n=============================================================";
	            results +="\n\n   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + queryTerm + "\".";
	            results += "\n\n =============================================================\n";
	
	            if (!iteratorSearchResults.hasNext()) 
	            {
	                results += "\n\n  There aren't any results for your query.";
	            }
	
	            while (iteratorSearchResults.hasNext()) 
	            {
	
	                SearchResult singleVideo = iteratorSearchResults.next();
	                ResourceId rId = singleVideo.getId();
	
	                // Confirm that the result represents a video. Otherwise, the
	                // item will not contain a video ID.
	                if (rId.getKind().equals("youtube#video")) 
	                {
	                    Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
	
	                    results += "\n\n  Video Id" + rId.getVideoId();
	                    results += "\n\n  Title: " + singleVideo.getSnippet().getTitle();
	                    results += "\n\n  Thumbnail: " + thumbnail.getUrl();
	                    results += "\n\n\n -------------------------------------------------------------\n";
	                }
	            }
	        }
    	}
    	catch (GoogleJsonResponseException e) 
    	{
    	      results = "There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage();
    	} 
    	catch (IOException e) 
    	{
    	      results = "There was an IO error: " + e.getCause() + " : " + e.getMessage();
    	} 
    	catch (Throwable t) 
    	{
    	      results = t.toString();
    	}
        
        return results;
    }
}