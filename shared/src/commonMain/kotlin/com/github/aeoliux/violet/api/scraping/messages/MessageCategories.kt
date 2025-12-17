package com.github.aeoliux.violet.api.scraping.messages

enum class MessageCategories(val categoryId: Int) {
    Received(5),
    Sent(6),
    Bin(7);

    companion object {
        fun fromInt(n: Int): MessageCategories {
            return entries.first { n == it.categoryId }
        }
    }
}