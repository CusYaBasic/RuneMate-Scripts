package com.DefiledOne.scripts.States;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WoodcuttingState
{
    CHECKFORAXE("Check for axe"),
    ISFULL("Check if inventory full"),
    MOVETOTREE("Move To Tree"),
    CHOPTREE("Chop tree"),
    MOVETOBANK("Move To Bank"),
    BANK("Bank");

    private final String gameName;
}
