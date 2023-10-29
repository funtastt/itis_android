package ru.kpfu.itis.android.asadullin.model

sealed class KittenModel {
    class KittenButton : KittenModel()
    class KittenCurrentDate : KittenModel()

    data class KittenData(
        val kittenId: Int,
        val kittenFactTitle: String,
        val kittenFactContent: String? = null,
        val kittenImageURL: String? = null,
        var isFavoured: Boolean = false,
    ) : KittenModel()
}