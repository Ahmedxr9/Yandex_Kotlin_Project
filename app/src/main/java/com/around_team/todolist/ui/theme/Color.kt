package com.around_team.todolist.ui.theme

import androidx.compose.ui.graphics.Color

internal val baseLightPalette = JetTodoListColors(
    support = JetTodoListColors.Support(
        separator = Color(0.0F, 0.0F, 0.0F, 0.12F),
        overlay = Color(0.0F, 0.0F, 0.0F, 0.04F),
        navbarBlur = Color(0.99F, 0.99F, 0.99F, 1F),
    ),
    label = JetTodoListColors.Label(
        primary = Color(0.08F, 0.08F, 0.1F, 1.0F),
        secondary = Color(0.08F, 0.08F, 0.1F, 0.65F),
        tertiary = Color(0.08F, 0.08F, 0.1F, 0.4F),
        disable = Color(0.08F, 0.08F, 0.1F, 0.2F),
    ),
    colors = JetTodoListColors.Colors(
        red = Color(0.96F, 0.26F, 0.21F, 1.0F),
        green = Color(0.18F, 0.8F, 0.44F, 1.0F),
        blue = Color(0.0F, 0.48F, 0.98F, 1.0F),
        gray = Color(0.56F, 0.56F, 0.58F, 1.0F),
        grayLight = Color(0.88F, 0.88F, 0.9F, 1.0F),
        white = Color(1.0F, 1.0F, 1.0F, 1.0F),
    ),
    back = JetTodoListColors.Back(
        iosPrimary = Color(0.97F, 0.97F, 0.98F, 1.0F),
        primary = Color(0.98F, 0.98F, 0.99F, 1.0F),
        secondary = Color(1.0F, 1.0F, 1.0F, 1.0F),
        elevated = Color(1.0F, 1.0F, 1.0F, 1.0F),
    ),
    yandex = JetTodoListColors.Yandex(
        label = Color(1.0F, 1.0F, 1.0F, 1.0F),
        back = Color(0.0F, 0.0F, 0.0F, 1.0F)
    )
)

internal val baseDarkPalette = JetTodoListColors(
    support = JetTodoListColors.Support(
        separator = Color(1.0F, 1.0F, 1.0F, 0.15F),
        overlay = Color(0.0F, 0.0F, 0.0F, 0.4F),
        navbarBlur = Color(0.08F, 0.08F, 0.1F, 1F),
    ),
    label = JetTodoListColors.Label(
        primary = Color(1.0F, 1.0F, 1.0F, 1.0F),
        secondary = Color(1.0F, 1.0F, 1.0F, 0.7F),
        tertiary = Color(1.0F, 1.0F, 1.0F, 0.5F),
        disable = Color(1.0F, 1.0F, 1.0F, 0.25F),
    ),
    colors = JetTodoListColors.Colors(
        red = Color(1.0F, 0.3F, 0.25F, 1.0F),
        green = Color(0.2F, 0.86F, 0.35F, 1.0F),
        blue = Color(0.05F, 0.55F, 1.0F, 1.0F),
        gray = Color(0.56F, 0.56F, 0.58F, 1.0F),
        grayLight = Color(0.32F, 0.32F, 0.34F, 1.0F),
        white = Color(1.0F, 1.0F, 1.0F, 1.0F),
    ),
    back = JetTodoListColors.Back(
        iosPrimary = Color(0.0F, 0.0F, 0.0F, 1.0F),
        primary = Color(0.11F, 0.11F, 0.12F, 1.0F),
        secondary = Color(0.16F, 0.16F, 0.18F, 1.0F),
        elevated = Color(0.25F, 0.25F, 0.27F, 1.0F),
    ),
    yandex = JetTodoListColors.Yandex(
        label = Color(0.0F, 0.0F, 0.0F, 1.0F),
        back = Color(1.0F, 1.0F, 1.0F, 1.0F)
    )
)

// Solarized Light color palette
// Base colors
private val solarizedBase03 = Color(0x00 / 255f, 0x2b / 255f, 0x36 / 255f) // #002b36
private val solarizedBase02 = Color(0x07 / 255f, 0x36 / 255f, 0x42 / 255f) // #073642
private val solarizedBase01 = Color(0x58 / 255f, 0x6e / 255f, 0x75 / 255f) // #586e75
private val solarizedBase00 = Color(0x65 / 255f, 0x7b / 255f, 0x83 / 255f) // #657b83
private val solarizedBase0 = Color(0x83 / 255f, 0x94 / 255f, 0x96 / 255f)  // #839496
private val solarizedBase1 = Color(0x93 / 255f, 0xa1 / 255f, 0xa1 / 255f)  // #93a1a1
private val solarizedBase2 = Color(0xee / 255f, 0xe8 / 255f, 0xd5 / 255f)  // #eee8d5
private val solarizedBase3 = Color(0xfd / 255f, 0xf6 / 255f, 0xe3 / 255f)  // #fdf6e3 - background

// Accent colors
private val solarizedYellow = Color(0xb5 / 255f, 0x89 / 255f, 0x00 / 255f) // #b58900
private val solarizedOrange = Color(0xcb / 255f, 0x4b / 255f, 0x16 / 255f) // #cb4b16
private val solarizedRed = Color(0xdc / 255f, 0x32 / 255f, 0x2f / 255f)    // #dc322f
private val solarizedMagenta = Color(0xd3 / 255f, 0x36 / 255f, 0x82 / 255f) // #d33682
private val solarizedViolet = Color(0x6c / 255f, 0x71 / 255f, 0xc4 / 255f)  // #6c71c4
private val solarizedBlue = Color(0x26 / 255f, 0x8b / 255f, 0xd2 / 255f)   // #268bd2 - primary
private val solarizedCyan = Color(0x2a / 255f, 0xa1 / 255f, 0x98 / 255f)    // #2aa198 - secondary
private val solarizedGreen = Color(0x85 / 255f, 0x99 / 255f, 0x00 / 255f)   // #859900

internal val solarizedLightPalette = JetTodoListColors(
    support = JetTodoListColors.Support(
        separator = Color(solarizedBase00.red, solarizedBase00.green, solarizedBase00.blue, 0.2F),
        overlay = Color(solarizedBase03.red, solarizedBase03.green, solarizedBase03.blue, 0.06F),
        navbarBlur = Color(solarizedBase2.red, solarizedBase2.green, solarizedBase2.blue, 1F),
    ),
    label = JetTodoListColors.Label(
        primary = solarizedBase00,      // #657b83 - readable on Base3
        secondary = solarizedBase0,      // #839496 - slightly lighter
        tertiary = solarizedBase1,       // #93a1a1 - even lighter
        disable = Color(solarizedBase1.red, solarizedBase1.green, solarizedBase1.blue, 0.5F),
    ),
    colors = JetTodoListColors.Colors(
        red = solarizedRed,              // #dc322f
        green = solarizedGreen,          // #859900
        blue = solarizedBlue,            // #268bd2 - primary
        gray = solarizedBase0,           // #839496
        grayLight = solarizedBase1,      // #93a1a1
        white = solarizedBase3,          // #fdf6e3
    ),
    back = JetTodoListColors.Back(
        iosPrimary = solarizedBase2,     // #eee8d5
        primary = solarizedBase3,        // #fdf6e3 - background
        secondary = Color(1.0F, 1.0F, 1.0F, 1.0F), // White for cards
        elevated = Color(1.0F, 1.0F, 1.0F, 1.0F),  // White for elevated surfaces
    ),
    yandex = JetTodoListColors.Yandex(
        label = Color(1.0F, 1.0F, 1.0F, 1.0F),
        back = Color(0.0F, 0.0F, 0.0F, 1.0F)
    )
)