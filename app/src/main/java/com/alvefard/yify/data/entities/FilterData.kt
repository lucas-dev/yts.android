package com.alvefard.yify.data.entities

class FilterData {
    var quality: String? = null
    var genre: String? = null
    var rating: String? = null
    var orderBy: String? = null

    constructor(quality: String, genre: String, rating: String, orderBy: String) {
        this.quality = quality
        this.genre = genre
        this.rating = rating
        this.orderBy = orderBy
    }

    constructor() {
        this.quality = "all"
        this.genre = "all"
        this.rating = "0"
        this.orderBy = "desc"
    }

    override fun toString(): String {
        return "FilterData{" +
                "quality='" + quality + '\''.toString() +
                ", genre='" + genre + '\''.toString() +
                ", rating='" + rating + '\''.toString() +
                ", orderBy='" + orderBy + '\''.toString() +
                '}'.toString()
    }
}