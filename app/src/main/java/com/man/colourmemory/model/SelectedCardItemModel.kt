package com.man.colourmemory.model

public data class SelectedCardItemModel<out A, out B>(
    public val position: A,
    public val card: B
) {

}
