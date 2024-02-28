package com.DefiledOne.scripts.States;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DraynorFishingState
{
    MOVETOFISH("Move to fish"),
    FISH("Fish"),
    WAIT("Wait"),
    MOVETOFIRE("Move to fire"),
    COOK("Cook"),
    MOVETOBANK("Move to bank"),
    BANK("Bank");

    private final String gameName;
}
