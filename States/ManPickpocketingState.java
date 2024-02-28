package com.DefiledOne.scripts.States;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ManPickpocketingState
{
    MOVETOBANK("Move to Bank"),
    BANK("Bank"),
    MOVETOWOMEN("Move to Women"),
    PICKPOCKET("Pickpocket"),
    OPENBAGS("Open Bags"),
    WAIT("Wait");

    private final String gameName;
}
