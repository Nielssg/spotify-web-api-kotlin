/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

abstract class AbstractTest<T : GenericSpotifyApi> {
    var api: T? = null

    open fun testPrereq(): Boolean {
        val result = api != null
        if (!result) println("Prereq failed in ${this::class.simpleName}.")
        return result
    }

    suspend inline fun <reified Z : T> build(): Boolean {
        return try {
            val f = buildSpotifyApi()
            @Suppress("UNCHECKED_CAST")
            (f as? T)?.let { if (f is Z) api = it }
            api != null
        } catch (cce: Exception) {
            cce.printStackTrace()
            false
        }
    }

    fun buildSync(): Boolean {
        return try {
            @Suppress("UNCHECKED_CAST")
            (buildSpotifyApiSync() as? T)?.let { api = it }
            api != null
        } catch (cce: ClassCastException) {
            false
        }
    }
}

typealias GenericSpotifyApiTest = AbstractTest<GenericSpotifyApi>
typealias SpotifyClientApiTest = AbstractTest<SpotifyClientApi>
