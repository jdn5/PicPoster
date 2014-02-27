package ca.ualberta.cs.picposter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.ualberta.cs.picposter.model.PicPostModel;

public class ElasticSearchOperation {
	
	private HttpClient httpclient = new DefaultHttpClient();

	public static void pushPicPostModel(final PicPostModel model)
	{
		Thread thread = new Thread()
		{
			@Override
			public void run()
			{
				Gson gson = new Gson();
				HttpClient client = new DefaultHttpClient();
				//HttpPut request = new HttpPut("http://cmput301.softwareprocess.es:8080/testing/jdn5/1");
				HttpPost request = new HttpPost("http://cmput301.softwareprocess.es:8080/testing/jdn5/1");

				try
				{
					request.setEntity(new StringEntity(gson.toJson(model)));
					HttpResponse response = client.execute(request);

					Log.e("ElasticSearch", response.getStatusLine().toString());

					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));

					String output = reader.readLine();
					while(output != null)
					{
						Log.e("ElasticSearch", output);
						output = reader.readLine();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		};

		thread.start();
	}
	
	public static void searchResponse(String str) throws ClientProtocolException, IOException{
			Gson gson = new Gson();
			HttpClient client = new DefaultHttpClient();
			HttpPost search = new HttpPost("http://cmput301.softwareprocess.es:8080/testing/jdn5/1");
			String query = 	"{\"query\" : {\"query_string\" : {\"default_field\" : \"picPostList\",\"query\" : \"" + str + "\"}}}";
			
			try
			{
				search.setEntity(new StringEntity(query));
				HttpResponse response = client.execute(search);

				Log.e("ElasticSearch", response.getStatusLine().toString());

				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));

				String output = reader.readLine();
				while(output != null)
				{
					Log.e("ElasticSearch", output);
					output = reader.readLine();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
}