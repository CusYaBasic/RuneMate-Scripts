package com.DefiledOne.scripts.EmbeddedUI;

import com.DefiledOne.scripts.Shared.Enums.TreesEnum;
import com.DefiledOne.scripts.States.WoodcuttingState;
import com.DefiledOne.scripts.Woodcutting.DraynorWoodcutBot;
import com.runemate.game.api.hybrid.local.Camera;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class DraynorWoodcutController implements Initializable
{

    public DraynorWoodcutBot bot;
    long startTime;
    long endTime;

    boolean doStartOnce = false;

    @FXML
    private ComboBox tree = new ComboBox();
    @FXML
    private Button startstopbutton = new Button();
    @FXML
    private Text statusText = new Text();
    @FXML
    private Text treeText = new Text();
    @FXML
    private Text timeText = new Text();

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        setTreeComboBox();
        // Adding an ActionEvent listener
        tree.setOnAction(event -> handleTreeSelection(tree.getValue().toString()));
        TimerLoop();
    }

    public void setRuntime()
    {
        if (bot.isActive)
            endTime = System.currentTimeMillis();

        long elapsedTime = endTime - startTime;
        long seconds = elapsedTime / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timeText.setText(formattedTime);
    }

    private void handleTreeSelection(String string)
    {
        treeText.setText(string);
        bot.tree = TreesEnum.valueOf(string);
        bot.state = WoodcuttingState.MOVETOBANK;
    }

    public void TimerLoop()
    {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                setRuntime();
                System.out.println(Camera.getZoom());
            }
        }, 0, 500); // Delay of 0 milliseconds, and repeat every 500 milliseconds
    }

    private void setTreeComboBox()
    {
        // Add items to the ComboBox
        ObservableList<String> items = FXCollections.observableArrayList(
                TreesEnum.TREE.toString(),
                TreesEnum.OAK.toString(),
                TreesEnum.WILLOW.toString()
        );

        tree.setItems(items);
    }

    @FXML
    private void toggleActive()
    {
        if (tree.getSelectionModel().getSelectedItem() == null)
        {
            updateStatus("Please select a tree before starting");
            return;
        }


        if (!bot.isActive)
        {
            bot.state = WoodcuttingState.ISFULL;
            startstopbutton.setStyle("-fx-background-color: #0850c4");
            startstopbutton.setText("Pause");
            bot.isActive = true;

            if(!doStartOnce)
            {
                startTime = System.currentTimeMillis();
                doStartOnce = true;
            }
        }
        else
        {
            startstopbutton.setStyle("-fx-background-color: #08c42e");
            startstopbutton.setText("Start");
            bot.isActive = false;
        }
    }

    public void updateStatus(String status)
    {
        statusText.setText(status);
    }
}
