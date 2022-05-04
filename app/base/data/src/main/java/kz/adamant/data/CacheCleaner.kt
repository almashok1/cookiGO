package kz.adamant.data

interface CacheCleaner {
    fun clearAll()
}

class CacheCleaners(
    private val cleaners: List<CacheCleaner>
): CacheCleaner {
    override fun clearAll() {
        cleaners.forEach { it.clearAll() }
    }
}