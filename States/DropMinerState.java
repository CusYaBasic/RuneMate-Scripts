package com.DefiledOne.scripts.States;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DropMinerState
{
    WAIT("Wait"),
    MINE("Mine"),
    DROP("Drop");

    private final String gameName;
}
