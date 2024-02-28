package com.DefiledOne.scripts.Shared.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BanksEnum
{
    LUMBRIDGE("Bank booth"),
    DRAYNOR("Bank booth");

    private final String gameName;
}
