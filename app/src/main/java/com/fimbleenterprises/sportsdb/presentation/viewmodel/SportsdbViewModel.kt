@file:Suppress("unused")

package com.fimbleenterprises.sportsdb.presentation.viewmodel
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.fimbleenterprises.sportsdb.MyApp
import com.fimbleenterprises.sportsdb.MyBillingWrapper
import com.fimbleenterprises.sportsdb.data.model.*
import com.fimbleenterprises.sportsdb.domain.usecase.*
import com.fimbleenterprises.sportsdb.util.FimTown
import com.fimbleenterprises.sportsdb.util.Resource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class SportsdbViewModel (
    private val app: Application,
    private val getAllTeamsFromAPIUseCase: GetTeamsFromAPIUseCase,
    private val getNextFiveEventsFromAPIUseCase: GetNextFiveEventsFromAPIUseCase,
    private val insertTeamInDatabaseUseCase: InsertTeamInDatabaseUseCase,
    private val getSavedTeamsFromDatabaseUseCase: GetSavedTeamsFromDatabaseUseCase,
    private val deleteSavedTeamsUseCase: DeleteSavedTeamsUseCase,
    private val getLastFiveEventsFromAPIUseCase: GetLastFiveEventsFromAPIUseCase,
    private val searchTeamToFollowUseCase: SearchTeamToFollowUseCase,
    private val getLeaguesFromAPIUseCase: GetLeaguesFromAPIUseCase,
    private val billingWrapper: MyBillingWrapper
) : ViewModel() {

    val filteredLeagues: MutableLiveData<List<League>> = MutableLiveData()
    val allLeagues: MutableLiveData<Resource<LeagueListApiResponse>> = MutableLiveData()
    val allTeamsAPIResponse: MutableLiveData<Resource<AllTeamsAPIResponse>> = MutableLiveData()
    val followedTeams: MutableLiveData<List<SportsTeam>> = MutableLiveData()
    val searchedTeams: MutableLiveData<Resource<AllTeamsAPIResponse>> = MutableLiveData()
    val deleteCount: MutableLiveData<Int> = MutableLiveData()
    val scheduledEvents: MutableLiveData<Resource<ScheduledGamesApiResponse>> = MutableLiveData()
    val lastFiveEvents: MutableLiveData<Resource<GameResultsApiResponse>> = MutableLiveData()
    // private val mySavedTeamId: MutableLiveData<Long> = MutableLiveData()
    val showsummariesinboxscore: MutableLiveData<Boolean> = MutableLiveData()
    val userIsPro: ObservableBoolean = ObservableBoolean(false)

    /**
     * Gets all teams in a league using Retrofit.
     */
    fun getAllTeamsByLeague(league: String) = viewModelScope.launch(IO) {
        allTeamsAPIResponse.postValue(Resource.Loading())
        try{
      if(FimTown.Network.isNetworkAvailable(app)) {
          val apiResult = getAllTeamsFromAPIUseCase.execute(league)
          allTeamsAPIResponse.postValue(apiResult)
      }else{
          allTeamsAPIResponse.postValue(Resource.Error("Internet is not available"))
      }

        }catch (e:Exception){
            allTeamsAPIResponse.postValue(Resource.Error(e.message.toString()))
        }

    }

    /**
     * Gets all leagues using Retrofit.
     */
    fun getAllLeagues() = viewModelScope.launch(IO) {
        allLeagues.postValue(Resource.Loading())
        try{
            if(FimTown.Network.isNetworkAvailable(app)) {
                val apiResult = getLeaguesFromAPIUseCase.execute()
                allLeagues.postValue(apiResult)
            }else{
                allLeagues.postValue(Resource.Error("Internet is not available"))
            }

        }catch (e:Exception){
            allLeagues.postValue(Resource.Error(e.message.toString()))
        }

    }

    /**
     * This is an alternative to querying the API to effect a search.  This is a simple client-side
     * filter that performs nicely.  If for some reason the dataset were to grow exponentially then
     * we would have to implement a server-side search (like we do with sports teams).
     */
    fun filterAllLeagues(query:String) = viewModelScope.launch(IO) {
        val existingList : List<League>? = allLeagues.value?.data?.leagues
        val filteredList : ArrayList<League> = ArrayList()
        existingList?.forEach {
            if (
                (!it.strLeague.isNullOrEmpty() && it.strLeague.lowercase().contains(query.lowercase())) ||
                (!it.strLeagueAlternate.isNullOrEmpty() && it.strLeagueAlternate.lowercase().contains(query.lowercase())) ||
                (!it.strSport.isNullOrEmpty() && it.strSport.lowercase().contains(query.lowercase()))
            ) {
                filteredList.add(it)
            }
        }
        filteredLeagues.postValue(filteredList)
    }

    /**
     * Gets the selected team's last five games using Retrofit.
     */
    fun getLastFiveGames(teamid: String) = viewModelScope.launch(IO) {
        lastFiveEvents.postValue(Resource.Loading())
        try{
            if(FimTown.Network.isNetworkAvailable(app)) {
                val apiResult = getLastFiveEventsFromAPIUseCase.execute(teamid)
                lastFiveEvents.postValue(apiResult)
            } else {
                lastFiveEvents.postValue(Resource.Error("Internet is not available"))
            }

        }catch (e:Exception){
            scheduledEvents.postValue(Resource.Error(e.message.toString()))
        }

    }

    /**
     * Gets the selected team's next five games using Retrofit.
     */
    fun getNextFiveGames(teamid: String) = viewModelScope.launch(IO) {
        scheduledEvents.postValue(Resource.Loading())
        try{
            if(FimTown.Network.isNetworkAvailable(app)) {
                val apiResult = getNextFiveEventsFromAPIUseCase.execute(teamid)
                scheduledEvents.postValue(apiResult)
            } else {
                scheduledEvents.postValue(Resource.Error("Internet is not available"))
            }

        }catch (e:Exception){
            scheduledEvents.postValue(Resource.Error(e.message.toString()))
        }
    }

    /**
     * Saves the supplied team to the 'teams' table.  Will overwrite if teamids match.
     */
    fun followTeam(sportsTeam: SportsTeam) = viewModelScope.launch(Main) {
        sportsTeam.following = true
        MyApp.AppPreferences.currentLeague = sportsTeam.strLeague
        MyApp.AppPreferences.currentTeam = sportsTeam
        insertTeamInDatabaseUseCase.execute(sportsTeam)
    }

    /**
     * Retrieves all teams saved to the 'teams' table and emits it as well as assigning it to the
     * followedTeams mutable livedata.
     */
    fun getFollowedTeams() = liveData(Main) {
        getSavedTeamsFromDatabaseUseCase.execute().collect {
            followedTeams.value = it
            emit(it)
        }
    }

    /**
     * Will delete the supplied team.  You can observe the 'deletedTeamCount' mutableLiveData value
     * to confirm that the team was actually removed from the database.
     */
    fun unFollowTeam(sportsTeam: SportsTeam) {
        viewModelScope.launch {
            MyApp.AppPreferences.currentTeam = null
            deleteCount.postValue(deleteSavedTeamsUseCase.execute(sportsTeam))
        }
    }

    /**
     * Will delete the supplied team.  You can observe the 'deletedTeamCount' mutableLiveData value
     * to confirm that the team was actually removed from the database.
     */
    fun unFollowAllTeams() {
        // Unfollow all teams
        viewModelScope.launch {
            MyApp.AppPreferences.currentTeam = null
            MyApp.AppPreferences.currentLeague = null
            deleteCount.postValue(deleteSavedTeamsUseCase.execute())
        }
    }

    /**
     * Queries the API for teams with a name containing the argument.  The search functionality in
     * the API sucks but the code challenge calls for it.  MUCH better would be to retrieve all teams
     * then filter client-side as it isn't a big data ask (see how we search leagues)
     */
    fun searchTeams(query:String) = viewModelScope.launch(IO) {
        searchedTeams.postValue(Resource.Loading())
        try{
            if(FimTown.Network.isNetworkAvailable(app)) {
                val apiResult = searchTeamToFollowUseCase.execute(query)
                searchedTeams.postValue(apiResult)
            }else{
                searchedTeams.postValue(Resource.Error("Internet is not available"))
            }

        }catch (e:Exception){
            searchedTeams.postValue(Resource.Error(e.message.toString()))
        }

    }

    /**
     * Stand-in until comprehensive in-app purchasing is properly implemented
     */
    internal fun queryUserPurchasedPro(): Boolean {
        return MyApp.AppPreferences.isPro
    }

    /**
     * NOT IMPLEMENTED
     * Production would query Google's in-app purchases to see if user has purchased pro version.
     * Observe the mutable live data var: userHasPurchasedPro for the result or leverage a callback.
     */
    /*fun queryUserPurchasedPro(listener: UserIsProQueryListener)  {

        billingWrapper.queryProducts(object : MyBillingWrapper.OnQueryProductsListener {
            override fun onSuccess(products: List<SkuDetails>) {
                if (!products.isNullOrEmpty()) {
                    if (products[0].sku == PRO_SKU) {
                        userIsPro = true
                        listener.userIsPro()
                    }
                } else {
                    userIsPro = false
                    listener.userIsNotPro()
                }
            }

            override fun onFailure(error: MyBillingWrapper.Error) {
                TODO("Not yet implemented")
            }
        })
    }*/

    /**
     * NOT IMPLEMENTED
     * Production would query Google's in-app purchases to see if user has purchased pro version.
     * Observe the live data var: userHasPurchasedPro for the result.
     */
    /*internal fun queryUserPurchasedPro()  {

        Log.i(TAG, "-=SportsdbViewModel:queryUserPurchasedProForReal| Querying Google... =-")
        queryUserPurchasedPro(object : UserIsProQueryListener {
            override fun userIsPro() {
                Log.i(TAG, "-=SportsdbViewModel:userIsPro =-")
            }

            override fun userIsNotPro() {
                Log.i(TAG, "-=SportsdbViewModel:userIsNotPro =-")
            }
        })
    }*/

    /**
     * NOT IMPLEMENTED
     * Might be needed for proper in-app purchasing.
     */
    /*interface UserIsProQueryListener {
        fun userIsPro()
        fun userIsNotPro()
    }*/

    companion object {
        private const val TAG = "FIMTOWN|SportsdbViewModel"
        const val PRO_SKU = "sportsdb_pro_1"
        const val PURCHASE_DIALOG_TAG = "PURCHASE_DIALOG"
    }

    init {
        Log.i(TAG, "Initialized:SportsdbViewModel")
        // Set the initial value from prefs
        showsummariesinboxscore.postValue(MyApp.AppPreferences.showSummariesInBoxscore)

        userIsPro.set(MyApp.AppPreferences.isPro)
        // Query Google's servers to see if user has purchased our "pro" version of the app.
        // LiveData will be updated and can be queried afterwards.
        queryUserPurchasedPro()
    }

}














