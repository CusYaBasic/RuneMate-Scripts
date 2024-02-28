package com.DefiledOne.scripts.States;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BarbFishAndCookState
{
    FISH("Fish"),
    COOK("Cook"),
    DROP("Drop"),
    WAIT("Wait");

    private final String gameName;
}
