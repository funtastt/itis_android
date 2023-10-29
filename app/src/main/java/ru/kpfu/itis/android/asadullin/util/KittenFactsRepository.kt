package ru.kpfu.itis.android.asadullin.util

import ru.kpfu.itis.android.asadullin.model.KittenModel
import java.util.Random

object KittenFactsRepository {

    private val allKittens = mutableListOf<KittenModel>()
    private var currentKittensSublist = mutableListOf<KittenModel>()

    init {
        for (i in 1..100) {
            val kittenTitle = "Факт $i: а знали ли вы, что ..."
            val anotherKittenTitle = "Факт ${100 + i}: а знали ли вы, что ..."
            val kittenFactContent =
                "Забавные факты о кошках, номер $i: Кошки - удивительные создания. Они могут быть независимыми и ласковыми одновременно. " +
                        "Исследования показали, что кошки способствуют снижению стресса и тревожности у своих владельцев. " +
                        "Кошки также известны своей чистоплотностью и стойкостью в вымывании своих шершавых язычков."
            val anotherKittenFactContent =
                "Забавные факты о кошках, номер ${100 + i}: Кошки - удивительные создания. Они могут быть независимыми и ласковыми одновременно. " +
                        "Исследования показали, что кошки способствуют снижению стресса и тревожности у своих владельцев. " +
                        "Кошки также известны своей чистоплотностью и стойкостью в вымывании своих шершавых язычков."
            val kittenImageURL = "https://placekitten.com/300/${300 + i}"
            val anotherKittenImageURL = "https://placekitten.com/${300 + i}/300"

            val catNews = KittenModel.KittenData(
                kittenId = i,
                kittenFactTitle = kittenTitle,
                kittenFactContent = kittenFactContent,
                kittenImageURL = kittenImageURL,
                isFavoured = false
            )
            val anotherCatNews = KittenModel.KittenData(
                kittenId = i + 100,
                kittenFactTitle = anotherKittenTitle,
                kittenFactContent = anotherKittenFactContent,
                kittenImageURL = anotherKittenImageURL,
                isFavoured = false
            )

            allKittens.add(catNews)
            allKittens.add(anotherCatNews)
        }
    }

    fun addItem() {
        this.allKittens.add(
            2, KittenModel.KittenData(
                kittenId = this.allKittens.size + 1,
                kittenFactTitle = "Заседание общественной палаты",
                kittenFactContent = "Вчера прошло мальдивское заседание общественной палаты на берегу какого-то там залива",
                kittenImageURL = "https://placekitten.com/300/${this.allKittens.size + 301}",
            )
        )
    }

    fun getKittens(count: Int): MutableList<KittenModel> {
        if (currentKittensSublist.isEmpty()) {
            currentKittensSublist.add(KittenModel.KittenButton())
            val shuffled = allKittens.shuffled()
            currentKittensSublist.addAll(
                if (count <= shuffled.size) shuffled.subList(0, count)
                    .toMutableList() else shuffled.toMutableList()
            )

            addDateItems()
        }

        return currentKittensSublist
    }

    fun getAllKittens(): MutableList<KittenModel> = allKittens

    fun clearCurrentList() {
        if (currentKittensSublist.isNotEmpty()) {
            currentKittensSublist.clear()
        }
    }

    fun toggleBookmark(kittenId: Int) {
        val kitten =
            allKittens.find { (it as? KittenModel.KittenData)?.kittenId == kittenId } as KittenModel.KittenData
        kitten.isFavoured = !kitten.isFavoured
    }

    fun addRandomItems(count : Int) : MutableList<KittenModel>{
        val random = Random()
        for (i in 1..count) {
            currentKittensSublist.add(random.nextInt(currentKittensSublist.size - 1) + 1, allKittens[random.nextInt(allKittens.size)])
        }
        return currentKittensSublist
    }

    private fun addDateItems() {
        var i = 1
        while (i < currentKittensSublist.size) {
            currentKittensSublist.add(i, KittenModel.KittenCurrentDate())
            i += 9
        }
    }
}