package it.unipi.dii.lsmd.paperraterapp.controller;

import it.unipi.dii.lsmd.paperraterapp.model.Paper;
import it.unipi.dii.lsmd.paperraterapp.model.ReadingList;
import it.unipi.dii.lsmd.paperraterapp.model.Session;
import it.unipi.dii.lsmd.paperraterapp.persistence.*;
import it.unipi.dii.lsmd.paperraterapp.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadingListCardCtrl {
    ReadingList r;
    private MongoDBManager mongoMan;
    private Neo4jManager neoMan;

    @FXML private Label readingListTitle;
    @FXML private Text nFollower;
    @FXML private Text nPapers;
    @FXML private Text mostCommonCategory;
    @FXML private Text mostFamousPaperTitle;

    public void initialize () {
        neoMan = new Neo4jManager(Neo4jDriver.getInstance().openConnection());
        mongoMan = new MongoDBManager(MongoDriver.getInstance().openConnection());

        readingListTitle.setOnMouseClicked(mouseEvent -> clickOnReadingListTitle(mouseEvent));
    }

    public void setReadingListCard (ReadingList r, String owner) {
        this.r = r;

        // set title
        readingListTitle.setText(r.getName());

        // set num followers
        String numFollowers = Integer.toString(neoMan.getNumFollowersReadingList(r.getName(), owner));
        nFollower.setText(numFollowers);

        // set most common category
        if (!r.getPapers().isEmpty()) {
            mostCommonCategory.setText(mostCommonCategory(r.getPapers()));
            nPapers.setText(String.valueOf(r.getPapers().size()));
            //mostFamousPaperTitle.setText(getMostFamousPaperInReadingList());
        }
        else {
            mostCommonCategory.setText("N/A");
            nPapers.setText("0");
            mostFamousPaperTitle.setText(("N/A"));
        }
    }

    private String mostCommonCategory(List<Paper> papers) {
        Map<String, Integer> map = new HashMap<>();

        for (Paper p : papers) {
            Integer val = map.get(p.getCategory());
            map.put(p.getCategory(), val == null ? 1 : val + 1);
        }

        Map.Entry<String, Integer> max = null;

        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }

    private void clickOnReadingListTitle (MouseEvent mouseEvent) {
        ReadingListPageController ctrl = (ReadingListPageController) Utils.changeScene(
                "/it/unipi/dii/lsmd/paperraterapp/layout/readinglistpage.fxml", mouseEvent);
        String previousUser = Session.getInstance().getPreviousPageUser().get(
                Session.getInstance().getPreviousPageUser().size()-1).getUsername();
        ctrl.setReadingList(r, previousUser);
    }
}
