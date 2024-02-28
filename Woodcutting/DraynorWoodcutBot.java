package com.DefiledOne.scripts.Woodcutting;

import com.DefiledOne.scripts.EmbeddedUI.DraynorWoodcutController;
import com.DefiledOne.scripts.Shared.*;
import com.DefiledOne.scripts.Shared.Enums.BanksEnum;
import com.DefiledOne.scripts.Shared.Enums.TreesEnum;
import com.DefiledOne.scripts.States.WoodcuttingState;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.framework.LoopingBot;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class DraynorWoodcutBot extends LoopingBot implements EmbeddableUI
{
    private ObjectProperty<Node> botInterfaceProperty;

    public DraynorWoodcutBot()
    {
        setEmbeddableUI(this);
        BasePlayerSense.initializeKeys();
    }
    public WoodcuttingState state;

    public boolean isActive = false;
    private boolean isChopping = false;
    public TreesEnum tree = TreesEnum.TREE;

    DraynorWoodcutController controller;


    @Override
    public ObjectProperty<Node> botInterfaceProperty()
    {
        if (botInterfaceProperty == null)
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(new DraynorWoodcutController());
            try {
                Node node = loader.load(Resources.getAsStream("com/DefiledOne/scripts/EmbeddedUI/DraynorWoodcutUI.fxml"));
                botInterfaceProperty = new SimpleObjectProperty<>(node);
                controller = loader.getController();
                controller.bot = this;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return botInterfaceProperty;
    }

    @Override
    public void onStart(String... arguments)
    {
        super.onStart(arguments);
        state = WoodcuttingState.CHECKFORAXE;
    }

    @Override
    public void onLoop()
    {

        if(!isActive)
            return;

        switch(state)
        {
            case CHECKFORAXE:
                if (!PlayerShared.isPlayerValid() || !PlayerShared.hasWoodcuttingAxeEquipped())
                {
                    stop("No axe");
                    return;
                }

                state = WoodcuttingState.ISFULL;
                break;

            case ISFULL:
                if(PlayerShared.isInventoryFull())
                {
                    controller.updateStatus("Inventory full");
                    state = WoodcuttingState.MOVETOBANK;
                }

                else
                    state = WoodcuttingState.MOVETOTREE;
                break;

            case MOVETOTREE:
                if (!PlayerShared.isPlayerMoving())
                {
                    controller.updateStatus("Moving to tree");
                    TreesShared.moveToTreeAndChop(tree);
                }
                break;

            case MOVETOBANK:
                controller.updateStatus("Moving to bank");

                if (!PlayerShared.isPlayerMoving())
                    BankShared.moveToBankAndBankAllExecpt(BanksEnum.DRAYNOR, "axe", BankShared.draynorBankers);
                break;
        }
    }
}
