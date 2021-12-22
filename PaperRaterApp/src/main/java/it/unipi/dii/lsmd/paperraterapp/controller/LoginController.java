package it.unipi.dii.lsmd.paperraterapp.controller;

import it.unipi.dii.lsmd.paperraterapp.model.Session;
import it.unipi.dii.lsmd.paperraterapp.model.User;
import it.unipi.dii.lsmd.paperraterapp.persistence.MongoDBManager;
import it.unipi.dii.lsmd.paperraterapp.persistence.MongoDriver;
import it.unipi.dii.lsmd.paperraterapp.persistence.Neo4jDriverE;
import it.unipi.dii.lsmd.paperraterapp.persistence.Neo4jManagerE;
import it.unipi.dii.lsmd.paperraterapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    private MongoDBManager mongoMan;
    private Neo4jManagerE neoMan;
    @FXML private Button loginButton;
    @FXML private PasswordField passwordTf;
    @FXML private Button registerButton;
    @FXML private TextField usernameTf;

    public void initialize () {
        neoMan = new Neo4jManagerE(Neo4jDriverE.getInstance().openConnection());
        mongoMan = new MongoDBManager(MongoDriver.getInstance().openConnection());
    }

    @FXML
    void checkCredential(ActionEvent event) {
        String username = usernameTf.getText();
        String password = passwordTf.getText();

        User u = mongoMan.login(username, password);

        if (u == null) {
            usernameTf.setText("");
            passwordTf.setText("");
            System.out.println("Username or password not valid");
        }
        else {
            Session.getInstance().setUser(u);

            ProfilePageController ctrl = (ProfilePageController) Utils.changeScene(
                    "/it/unipi/dii/lsmd/paperraterapp/layout/profilepage.fxml", event);
            ctrl.setProfilePage(Session.getInstance().getUser());
        }
    }

    /**
     * If the user click the button register this function
     * will change the app stage and show the register form
     *
     * @param event
     */
    @FXML
    void loadRegisterForm(ActionEvent event) {
        Utils.changeScene("/it/unipi/dii/lsmd/paperraterapp/layout/register.fxml", event);
    }

}
