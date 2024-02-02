/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.runTestOnDefaultDispatcher
import com.adamratzman.spotify.utils.Market
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult
import kotlin.test.*

class ShowApiTest : AbstractTest<GenericSpotifyApi>() {
    private val market = Market.US

    @Test
    fun testGetShow(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testGetShow.name)

        assertNull(api.shows.getShow("invalid-show", market = market))
        assertEquals(
            "Freakonomics Radio",
            api.shows.getShow("spotify:show:6z4NLXyHPga1UmSJsPK7G1", market = market)?.name
        )
    }

    @Test
    fun testGetShows(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testGetShows.name)

        assertContentEquals(listOf(null, null), api.shows.getShows("hi", "dad", market = market))
        assertContentEquals(
            listOf(null, null),
            api.shows.getShows("78sdfjsdjfsjdf", "j", market = market).map { it?.id }
        )
        assertContentEquals(
            listOf("Freakonomics Radio"),
            api.shows.getShows("6z4NLXyHPga1UmSJsPK7G1", market = market).map { it?.name }
        )
    }

    @Test
    fun testGetShowEpisodes(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testGetShowEpisodes.name)

        assertFailsWith<BadRequestException> { api.shows.getShowEpisodes("hi", market = market) }
        val show = api.shows.getShow("6z4NLXyHPga1UmSJsPK7G1", market = market)!!
        assertEquals(
            show.id,
            api.shows.getShowEpisodes(show.id, market = market).first()?.toFullEpisode(market)?.show?.id
        )
    }
}
