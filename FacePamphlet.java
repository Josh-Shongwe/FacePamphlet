/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class FacePamphlet extends Program 
implements FacePamphletConstants {
	private JLabel nameLabel;
	private JTextField nameText;
	private JButton deleteButton;
	private JButton lookUpButton;
	private JButton addButton;
	private JTextField statusText;
	private JTextField pictureText;
	private JTextField friendText;
	private JButton statusButton;
	private JButton pictureButton;
	private JButton friendButton;
	private FacePamphletProfile currentProfile = null; 
	private FacePamphletDatabase profileData = new FacePamphletDatabase();
	private FacePamphletCanvas canvas = new FacePamphletCanvas();

	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	
	public void init() {
		//Initializes and adds the North GUI interactors.
		nameLabel = new JLabel("Name");
		nameText = new JTextField(TEXT_FIELD_SIZE);
		deleteButton = new JButton("Delete");
		lookUpButton = new JButton("Lookup");
		addButton = new JButton("Add");
		add(nameLabel, NORTH);
		add(nameText, NORTH);
		add(addButton, NORTH);
		add(deleteButton, NORTH);
		add(lookUpButton, NORTH);
		//Initializes and adds the West GUI interactors.
		statusText = new JTextField(TEXT_FIELD_SIZE);
		pictureText = new JTextField(TEXT_FIELD_SIZE);
		friendText = new JTextField(TEXT_FIELD_SIZE);
		statusButton = new JButton("Change Status");
		pictureButton = new JButton("Change Picture");
		friendButton = new JButton("Add Friend");
		add(statusText, WEST);
		add(statusButton, WEST);
		add(new JLabel (EMPTY_LABEL_TEXT), WEST);
		add(pictureText, WEST);
		add(pictureButton, WEST);
		add(new JLabel (EMPTY_LABEL_TEXT), WEST);
		add(friendText, WEST);
		add(friendButton, WEST);
		
		addActionListeners();
		statusText.addActionListener(this);
		pictureText.addActionListener(this);
		friendText.addActionListener(this);
	
		add(canvas);
	}


	/**
	 * This class is responsible for detecting when the buttons are
	 * clicked or interactors are used, so you will have to add code
	 * to respond to these actions.
	 */
	public void actionPerformed(ActionEvent e) {
		//Makes entries non case sensitive.
		String entryName = nameText.getText();
		
		/*Waits for the Action Command to equal "Change Status" or gets the source of the 
		 * status text field when enter is clicked, and makes sure the JTextField is not empty.
		 */
		if(e.getActionCommand().equals("Change Status") || e.getSource() == statusText && !(statusText.getText().equals(""))) {
			String statusInfo = statusText.getText();
			//Makes sure the currentProfile exists.
			if(currentProfile != null) {
				FacePamphletProfile userProfile = profileData.getProfile(currentProfile.getName());
				userProfile.setStatus(userProfile.getName() + " is " + statusInfo);
				canvas.displayProfile(userProfile);
				canvas.showMessage("Status updated to " + statusInfo);
			}else {
				canvas.showMessage("Please select a profile to change status.");
			}
		}
		
		/*Waits for the Action Command to equal "Change Picture" or gets the source of the picture text
		 *  field when enter is clicked, and makes sure the picture text field is not empty.
		 */
		if(e.getActionCommand().equals("Change Picture") || e.getSource() == pictureText && !(pictureText.getText().equals(""))) {
			String pictureFile = pictureText.getText();
			//Makes sure the currentProfile exists.
			if(currentProfile != null) {
				FacePamphletProfile userProfile = profileData.getProfile(currentProfile.getName());
				GImage image = null;
				try {
					image = new GImage(pictureFile);
					userProfile.setImage(image);
				}
				catch(ErrorException ex) {
					image = null;
				}
				canvas.displayProfile(userProfile);
				if(image == null) {
					canvas.showMessage("Unable to open image file: " + pictureFile);
				}else {
					canvas.showMessage("Picture updated");
				}
			}else {
				canvas.showMessage("Please select a profile to change picutre");
			}
		}
		
		/*Waits for the Action Command to equal "Add Friend" or gets the source of the add friend
		 *  text field when enter is clicked, and makes sure the add friend text field is not empty. 
		 */
			if(e.getActionCommand().equals("Add Friend") || e.getSource() == friendText && !(friendText.getText().equals(""))) {
				String addedFriend = friendText.getText();
				if(currentProfile != null) {
					FacePamphletProfile userProfile = profileData.getProfile(currentProfile.getName());
					//Doesn't allow the user to add themselves as a friend.
					if(userProfile.getName().equals(addedFriend)) {
						canvas.showMessage("You cannot friend yourself");
					}else if(profileData.containsProfile(addedFriend)) {
						FacePamphletProfile addFriendProfile = profileData.getProfile(addedFriend);
						//Returns true if the addedFriend is not in the currentProfile's friend list.
						if(userProfile.addFriend(addedFriend)) {
							userProfile.addFriend(addedFriend);
							addFriendProfile.addFriend(nameText.getText());
							canvas.displayProfile(userProfile);
							canvas.showMessage(addedFriend + " added as a friend");
						}else {
							canvas.showMessage(userProfile.getName() + " already has " + addedFriend + " as a friend");
						}
						//If the FacePamphletDatabase does not contain the addedFriend's name, it prompts the user accordingly.
					} else {
						
						canvas.showMessage(addedFriend + " does not exist");
					}
				//If currentProfile is equal to null, the user must select a profile before trying to add a friend.
				}else {
					canvas.showMessage("Please select a profile to add friend");
				}
			}

			
	//Waits for the Action Command to equal "Add" and makes sure the name text field is not empty.
			if(e.getActionCommand().equals("Add") && !(nameText.getText().equals(""))) {
				if(!(profileData.containsProfile(entryName))){
					FacePamphletProfile userProfile = new FacePamphletProfile(entryName);
					profileData.addProfile(userProfile);
					canvas.displayProfile(userProfile);
					canvas.showMessage("New profile created");
					currentProfile = userProfile;
				}else {
					FacePamphletProfile userProfile = profileData.getProfile(entryName);
					canvas.displayProfile(userProfile);
					canvas.showMessage("A profile with the name " + nameText.getText() + " already exists");
					currentProfile = userProfile;
				}
			}
			
	//Waits for the Action Command to equal "Delete" and makes sure the name text field is not empty.
			 if(e.getActionCommand().equals("Delete") && !(nameText.getText().equals(""))) {
				canvas.removeAll();
				currentProfile = null;
				if(profileData.containsProfile(entryName)) {
					profileData.deleteProfile(entryName);
					canvas.showMessage("Profile of " + nameText.getText() + " deleted");
				}else {
					canvas.showMessage("A profile with name " + nameText.getText() + " does not exist");
				}
			 }
			 
		//Waits for the Action Command to equal "Lookup" and makes sure the name text field is not empty.
			 if(e.getActionCommand().equals("Lookup") && !(nameText.getText().equals(""))) {
				canvas.removeAll();
				if(profileData.containsProfile(entryName)) {
					FacePamphletProfile userProfile = profileData.getProfile(entryName);
					canvas.displayProfile(userProfile);
					canvas.showMessage("Displaying " + nameText.getText());
					currentProfile = userProfile;
				}
				else {
					canvas.showMessage("A profile with name " + nameText.getText() + " does not exist");
					currentProfile = null;
				}
			}
		}
}

