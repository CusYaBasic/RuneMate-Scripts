package com.DefiledOne.scripts.Shared.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TreesEnum
{

    TREE("Tree"),
    OAK("Oak tree"),
    WILLOW("Willow tree");

    private final String gameName;
}
