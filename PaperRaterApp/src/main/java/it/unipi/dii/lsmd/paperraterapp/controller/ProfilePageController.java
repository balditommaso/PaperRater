package it.unipi.dii.lsmd.paperraterapp.controller;

import it.unipi.dii.lsmd.paperraterapp.model.ReadingList;
import it.unipi.dii.lsmd.paperraterapp.model.Session;
import it.unipi.dii.lsmd.paperraterapp.model.User;
import it.unipi.dii.lsmd.paperraterapp.persistence.MongoDBManager;
import it.unipi.dii.lsmd.paperraterapp.persistence.MongoDriver;
import it.unipi.dii.lsmd.paperraterapp.persistence.Neo4jDriverE;
import it.unipi.dii.lsmd.paperraterapp.persistence.Neo4jManagerE;
import it.unipi.dii.lsmd.paperraterapp.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Iterator;

public class ProfilePageController {
    private User user;
    private MongoDBManager mongoMan;
    private Neo4jManagerE neoMan;

    @FXML private ImageView backIcon;
    @FXML private ImageView editIcon;
    @FXML ImageView profileImg;
    @FXML private Label username;
    @FXML private Text email;
    @FXML private Text firstName;
    @FXML private Text lastName;
    @FXML private Text nFollower;
    @FXML private Text nFollowing;
    @FXML private Button followBtn;
    @FXML private VBox readingListsBox;
    @FXML private Button addReadingListBtn;


    public void initialize () {
        neoMan = new Neo4jManagerE(Neo4jDriverE.getInstance().openConnection());
        mongoMan = new MongoDBManager(MongoDriver.getInstance().openConnection());

        backIcon.setOnMouseClicked(mouseEvent -> clickOnBackIcon(mouseEvent));
        followBtn.setOnMouseClicked(mouseEvent -> clickOnFollowBtn(mouseEvent));
        nFollowing.setOnMouseClicked(mouseEvent -> clickOnFollowing(mouseEvent));
        nFollower.setOnMouseClicked(mouseEvent -> clickOnFollower(mouseEvent));
        editIcon.setOnMouseClicked(mouseEvent -> clickOnEditIcon(mouseEvent));
        addReadingListBtn.setOnMouseClicked(mouseEvent -> clickOnAddReadingListBtn(mouseEvent));
    }

    public void setProfilePage (User user) {
        this.user = user;

        username.setText(user.getUsername());
        email.setText(user.getEmail());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        nFollower.setText("100"); // neoMan.getNumberOfFollowers(u.getUsername())
        nFollowing.setText("2000"); // neoMan.getNumberOfFollowing(u.getUsername())

        if (user.getPicture() != null)
            profileImg.setImage(new Image(user.getPicture()));

        /*if (man.userAFollowsUserB(Session.user, user))
            followBtn.setText("Unfollow");*/


         if (user.getUsername().equals(Session.getInstance().getUser().getUsername())) {
            followBtn.setVisible(false);
            editIcon.setVisible(true);
            addReadingListBtn.setVisible(true);
         }
         else {
             followBtn.setVisible(true);
             editIcon.setVisible(false);
             addReadingListBtn.setVisible(false);
         }

        if (!user.getReadingLists().isEmpty()) {
            Iterator<ReadingList> it = user.getReadingLists().iterator();

            while(it.hasNext()) {
                HBox row = new HBox();
                row.setStyle("-fx-padding: 10px");
                ReadingList r = it.next();
                Pane p = loadReadingListCard(r);

                row.getChildren().addAll(p);
                readingListsBox.getChildren().add(row);
            }
        }
        else {
            readingListsBox.setAlignment(Pos.CENTER);
            readingListsBox.getChildren().add(new Label("No Reading Lists :("));
        }
    }

    private Pane loadReadingListCard (ReadingList r) {
        Pane pane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unipi/dii/lsmd/paperraterapp/layout/readinglist_card.fxml"));
            pane = loader.load();
            ReadingListCtrl ctrl = loader.getController();
            ctrl.setReadingListCard(r);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return pane;
    }

    private void clickOnBackIcon (MouseEvent mouseEvent) {
        System.out.println("Back");
        Utils.changeScene("/it/unipi/dii/lsmd/paperraterapp/layout/homepage.fxml", mouseEvent);
    }

    private void clickOnFollowBtn (MouseEvent mouseEvent) {
        String tmp = followBtn.getText();
        if (tmp.equals("Follow")) {
            //neoMan.follow(Session.getInstance().getUser().getUsername(), user.getUsername());
            System.out.println("Follow");
            followBtn.setText("Unfollow");
        }
        else {
            //neoMan.unfollow(Session.getUsername(), user.getUsername());
            System.out.println("Unfollow");
            followBtn.setText("Follow");
        }
    }

    private void clickOnFollower (MouseEvent mouseEvent) {
        System.out.println("Browse Follower");
    }

    private void clickOnFollowing (MouseEvent mouseEvent) {
        System.out.println("Browse Following");
    }

    private void clickOnEditIcon (MouseEvent mouseEvent) {
        System.out.println("Edit profile Info");
    }

    private void clickOnAddReadingListBtn (MouseEvent mouseEvent) {
        // create a text input dialog
        TextInputDialog td = new TextInputDialog("enter any text");

        // setHeaderText
        td.setHeaderText("enter your name");

        td.show();
    }
}
