package com.DefiledOne.scripts.States;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GrizzleyBearState
{
    WAIT("Wait"),
    ATTACK("Attack"),
    COMBAT("Combat");

    private final String gameName;
}
