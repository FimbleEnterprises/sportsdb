package com.fimbleenterprises.sportsdb.data.api


import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SportsdbAPIServiceTest {

    private lateinit var service: SportsdbAPIService
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SportsdbAPIService::class.java)
    }

    private fun enqueueMockResponse(
      fileName:String
    ){
      val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
      val source = inputStream.source().buffer()
      val mockResponse = MockResponse()
      mockResponse.setBody(source.readString(Charsets.UTF_8))
      server.enqueue(mockResponse)

    }

    @Test
    fun getAllLeagues_sentRequest_receivedExpected(){
        runBlocking {
            // Using a response we obtained manually in a browser and saved to a file we parse it
            // in the same manner we will in production from the API using Retrofit.
            enqueueMockResponse("leaguesresponse.json")
            val responseBody = service.getAllLeagues().body()
            val request = server.takeRequest()
            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("/api/v1/json/2/all_leagues.php")
            val leagueslist = responseBody!!.leagues
            val firstleague = leagueslist[0]
            assertThat(firstleague.strLeague).isNotEmpty()
            assertThat(leagueslist.size).isGreaterThan(0)
        }
    }

    @Test
    fun getTeamsInLeague_receivedResponse_correctContent(){
        runBlocking {
            // Using a response we obtained manually in a browser and saved to a file we parse it
            // in the same manner we will in production from the API using Retrofit.
            enqueueMockResponse("teamsresponse.json")
            val responseBody = service.getAllTeams("mlb").body()
            val teamsList = responseBody!!.sportsTeams
            val firstteam = teamsList!![0]
            assertThat(firstteam.strLeague).isNotEmpty()
            assertThat(firstteam.strLeague).isEqualTo("MLB")
            assertThat(teamsList.size).isGreaterThan(0)
        }
    }

    @Test
    fun getNextFiveGames_receivedResponse_correctContent(){
        runBlocking {
            // Using a response we obtained manually in a browser and saved to a file we parse it
            // in the same manner we will in production from the API using Retrofit.
            enqueueMockResponse("nextfiveeventsresponse.json")
            val responseBody = service.getNextFiveEvents("135267").body()
            val eventsList = responseBody!!.scheduledGames
            assertThat(eventsList!![0].strLeague).isNotEmpty()
            assertThat(eventsList[0].strLeague).isEqualTo("MLB")
            assertThat(eventsList.size).isGreaterThan(0)
        }
    }

    @Test
    fun getLastFiveGames_receivedResponse_correctContent(){
        runBlocking {
            // Using a response we obtained manually in a browser and saved to a file we parse it
            // in the same manner we will in production from the API using Retrofit.
            enqueueMockResponse("lastfiveeventsresponse.json")
            val responseBody = service.getLastFiveEvents("135267").body()
            val eventslist = responseBody!!.gameResults
            assertThat(eventslist!!.size).isGreaterThan(0)
            assertThat(eventslist[0].strLeague).isNotEmpty()
            assertThat(eventslist[0].intHomeScore).isEqualTo("4")
        }
    }

    @After
    fun tearDown() {
       server.shutdown()
    }
}