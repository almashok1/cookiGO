package kz.adamant.checklist.domain

enum class MeasureUnit(
    val full: String, val mini: String
) {
    GRAM("gram", "g"),
    KILOGRAM("kilogram", "kg"),
    MILLILITER("milliliter", "ml"),
    LITER("liter", "l"),
    PIECE("piece", "pc"),
    TEASPOON("teaspoon", "tsp"),
    TABLESPOON("tablespoon", "tbsp");

    companion object {
        fun from(name: String?): MeasureUnit {
            val n = name?.lowercase()?.trim() ?: return PIECE
            values().forEach {
                if (n.contains(it.full)) {
                    return it
                }
                if (n.contains(it.mini) && n.length < 4) {
                    return it
                }
            }
            return PIECE
        }
    }
}