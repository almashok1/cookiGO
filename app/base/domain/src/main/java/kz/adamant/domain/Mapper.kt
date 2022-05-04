package kz.adamant.domain

interface Mapper<IN, OUT> {
    fun map(input: IN): OUT
}