package com.DefiledOne.scripts.States;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TroutCookingState
{
    BANK("Bank"),
    COOK("Cook"),
    WAIT("Wait");

    private final String gameName;
}
