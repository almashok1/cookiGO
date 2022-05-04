package kz.adamant.preferences.domain

data class Preferences(
    val cuisines: List<Item>,
    val categories: List<Item>
) {
    data class Item(
        val id: Long,
        val name: String,
        val image: String
    )
}