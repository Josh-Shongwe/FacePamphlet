/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */


import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas 
implements FacePamphletConstants {
	private GLabel nameDisplay;
	private double holderX = 0;
	private double holderY = 0;

	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the display
	 */
	public FacePamphletCanvas() {
		
	}


	/** 
	 * This method displays a message string near the bottom of the 
	 * canvas.  Every time this method is called, the previously 
	 * displayed message (if any) is replaced by the new message text 
	 * passed in.
	 */
	public void showMessage(String msg) {
		//Removes the previous message displayed every time a new message is displayed.
		if(getElementAt(holderX, holderY) != null) {
			remove(getElementAt(holderX, holderY));
		}
		GLabel msgPrompt = new GLabel(msg);
		double x = getWidth()/2 - msgPrompt.getWidth()*0.75;
		double y = getHeight() - BOTTOM_MESSAGE_MARGIN;
		/*Because the x and y-coordinates are adjusting values, holderX and holderY store these values in order to get the element at the newly 
		* assigned x and y coordinates.
		*/
		holderX = x;
		holderY = y;
		msgPrompt.setFont(MESSAGE_FONT);
		add(msgPrompt, x, y);
	}



	/** 
	 * This method displays the given profile on the canvas.  The 
	 * canvas is first cleared of all existing items (including 
	 * messages displayed near the bottom of the screen) and then the 
	 * given profile is displayed.  The profile display includes the 
	 * name of the user from the profile, the corresponding image 
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		removeAll();
		createName(profile.getName());	
		createImage(profile.getImage());
		createStatus(profile.getStatus());
		createFriendList(profile.getFriends());
	}
	
	private void createName(String enterName) {
		nameDisplay = new GLabel(enterName);
		nameDisplay.setFont(PROFILE_NAME_FONT);
		nameDisplay.setColor(Color.BLUE);
		double nameX = LEFT_MARGIN;
		double nameY = TOP_MARGIN + nameDisplay.getHeight();
		add(nameDisplay, nameX, nameY);
	}
	
	private void createImage(GImage image) {
		double imageX = LEFT_MARGIN;
		double imageY = TOP_MARGIN + nameDisplay.getHeight() + IMAGE_MARGIN;
		if(image != null) {
			image.setBounds(imageX, imageY, IMAGE_WIDTH, IMAGE_HEIGHT);
			add(image);
		}else {
			GRect imagePlace = new GRect(imageX, imageY, IMAGE_WIDTH, IMAGE_HEIGHT);
			add(imagePlace);
			GLabel emptyImage = new GLabel("No Image");
			double emptyTextX = imageX  + IMAGE_WIDTH/2 - emptyImage.getWidth();
			double emptyTextY = imageY + IMAGE_HEIGHT/2;
			emptyImage.setFont(PROFILE_IMAGE_FONT);
			add(emptyImage, emptyTextX, emptyTextY);
		}
	}

	private void createFriendList(Iterator<String> friendList) {
		GLabel friendLabel = new GLabel("Friends:");
		friendLabel.setFont(PROFILE_FRIEND_LABEL_FONT);
		double friendX = getWidth()/2;
		double friendY = nameDisplay.getHeight() + TOP_MARGIN;
		add(friendLabel, friendX, friendY);
		Iterator<String> friendIterator = friendList;
		for(int i = 1; friendIterator.hasNext(); i++) {
			GLabel friendNames = new GLabel (friendIterator.next());
			friendNames.setFont(PROFILE_FRIEND_FONT);
			double friendNameY = friendLabel.getHeight()*i + friendY;
			add(friendNames, friendX, friendNameY);
		}
	}
	
	private void createStatus(String statusMsg) {
		GLabel statusLabel = new GLabel(statusMsg);
		statusLabel.setFont(PROFILE_IMAGE_FONT);
		double statusX = LEFT_MARGIN;
		double statusY = IMAGE_MARGIN + IMAGE_HEIGHT + STATUS_MARGIN + TOP_MARGIN + nameDisplay.getHeight() +  statusLabel.getHeight();
		if(getElementAt(statusX,statusY) != null) {
			remove(getElementAt(statusX, statusY));
		}
		add(statusLabel, statusX, statusY);
	}
}


