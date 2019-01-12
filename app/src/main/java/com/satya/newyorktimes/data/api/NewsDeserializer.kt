package com.satya.newyorktimes.data.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.satya.newyorktimes.data.model.News
import java.lang.reflect.Type
import com.google.gson.reflect.TypeToken
import com.satya.newyorktimes.data.model.Multimedia
import java.util.*


class NewsDeserializer : JsonDeserializer<News> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): News {

        var desFacet = Collections.emptyList<String>()
        var orgFacet = Collections.emptyList<String>()
        var perFacet = Collections.emptyList<String>()
        var geoFacet = Collections.emptyList<String>()
        var multimedia = Collections.emptyList<Multimedia>()
        val jObj = json.asJsonObject

        var jElement = jObj?.get("des_facet")

        if (jElement?.isJsonArray == true) {
            desFacet =
                    context?.deserialize(jElement.asJsonArray, object : TypeToken<List<String>>() {

                    }.type)
        }

        jElement = jObj?.get("org_facet")

        if (jElement?.isJsonArray == true) {
            orgFacet =
                    context?.deserialize(jElement.asJsonArray, object : TypeToken<List<String>>() {

                    }.type)
        }


        jElement = jObj?.get("per_facet")

        if (jElement?.isJsonArray == true) {
            perFacet =
                    context?.deserialize(jElement.asJsonArray, object : TypeToken<List<String>>() {

                    }.type)
        }

        jElement = jObj?.get("geo_facet")

        if (jElement?.isJsonArray == true) {
            geoFacet =
                    context?.deserialize(jElement.asJsonArray, object : TypeToken<List<String>>() {

                    }.type)
        }

        jElement = jObj?.get("multimedia")

        if (jElement?.isJsonArray == true) {
            multimedia =
                    context?.deserialize(jElement.asJsonArray, object : TypeToken<List<Multimedia>>() {

                    }.type)
        }
        var news = News(
            jObj.getAsJsonPrimitive("title").asString,
            jObj.getAsJsonPrimitive("abstract").asString,
            jObj.getAsJsonPrimitive("byline").asString,
            jObj.getAsJsonPrimitive("created_date").asString,
            desFacet,
            geoFacet,
            jObj.getAsJsonPrimitive("item_type").asString,
            jObj.getAsJsonPrimitive("kicker").asString,
            jObj.getAsJsonPrimitive("material_type_facet").asString,
            multimedia,
            orgFacet,
            perFacet,
            jObj.getAsJsonPrimitive("published_date").asString,
            jObj.getAsJsonPrimitive("section").asString,
            jObj.getAsJsonPrimitive("subsection").asString,
            jObj.getAsJsonPrimitive("updated_date").asString,
            jObj.getAsJsonPrimitive("url").asString)
        return news

    }
}