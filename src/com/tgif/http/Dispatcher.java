package com.tgif.http;

public class Dispatcher {
	
	/*private TGIFRequest tgifRequest = TGIFRequest.getInstance();
	private Priority mPriority;
	private static JSONObject responseObject;
	private static Bitmap responseImage;
	private static String reponseString;
	//private TGIFRequest tgifRequest = TGIFRequest.getInstance();
	
	
	
	public String getStringReponse() {
		StringRequest strReq = new StringRequest(
				Request.Method.GET, EndPoints.FOOD_MENUS, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						Dispatcher.reponseString = response;
					}
					
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						error.printStackTrace();
						
					}
				}
	);
		return Dispatcher.reponseString;
	}
	
	public Bitmap getBitmap(String imageUrl) {
	    // Retrieves an image specified by the URL, and displays it in the UI
	    ImageRequest request = new ImageRequest(imageUrl,
	            new Response.Listener<Bitmap>() {
	                @Override
	                public void onResponse(Bitmap bitmap) {
	                	//imgView.setImageBitmap(bitmap);
	                	Dispatcher.responseImage = bitmap;
	                }
	            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
	            new Response.ErrorListener() {
	                public void onErrorResponse(VolleyError error) {
	                }
	            });
	 
	    // we don't need to set the priority here;
	    // ImageRequest already comes in with
	    // priority set to LOW, that is exactly what we need.
	    tgifRequest.add(request);
	    return Dispatcher.responseImage;
	}
	
	public void setPriority(Priority priority) {
		mPriority = priority;
	}
	
	public Priority getPriority() {
		return mPriority == null ? Priority.NORMAL : mPriority;
	}*/

}
