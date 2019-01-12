package com.satya.newyorktimes.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class News(

    @Expose
    @PrimaryKey()
    val title: String,
    @SerializedName("abstract")
    @Expose
    val abstrct: String?,
    @Expose
    val byline: String?,
    @Expose
    val created_date: String?,
    @Expose
    val des_facet: List<String>?,
    @Expose
    val geo_facet: List<String>?,
    @Expose
    val item_type: String?,
    @Expose
    val kicker: String?,
    @Expose
    val material_type_facet: String?,
    @Expose
    val multimedia: List<Multimedia>?,
    @Expose
    val org_facet: List<String>?,
    @Expose
    val per_facet: List<String>?,
    @Expose
    val published_date: String?,
    @Expose
    val section: String?,
    @Expose
    val subsection: String?,
    @Expose
    val updated_date: String?,
    @Expose
    val url: String?
) {

    fun getBannerImageLarge(): String {
        multimedia?.let {
            it.forEach { media ->
                if (media.format == "mediumThreeByTwo210") {
                    return media.url
                }
            }
        }
        return ""
    }

    fun getThumbImageSmall(): String {
        multimedia?.let {
            it.forEach { media ->
                if (media.format == "Standard Thumbnail") {
                    return media.url
                }
            }
        }
        return ""
    }

    override fun equals(other: Any?): Boolean {
        return other is News && this.title == other.title
                && this.section == other.section
                && this.item_type == other.item_type
                && this.byline == other.byline
                && this.published_date == other.published_date
                && this.updated_date == other.updated_date
                && this.created_date == other.created_date
                && this.url == other.url
                && this.material_type_facet == other.material_type_facet
                && this.abstrct == other.abstrct
                && this.getBannerImageLarge() == other.getBannerImageLarge()
                && this.getThumbImageSmall() == other.getThumbImageSmall()
    }
}