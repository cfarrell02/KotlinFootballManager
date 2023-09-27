package org.setu.controller

import com.google.gson.Gson
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import org.setu.Club
import org.setu.League
import org.setu.Player
import tornadofx.*
import java.io.File


class MainController {
    private val leagues = ArrayList<League>()
    private var selectedLeague: League? = null
    private var selectedClub: Club? = null
    private var selectedPlayer: Player? = null

    @FXML
    lateinit var mainList: ListView<League>
    lateinit var clubList : ListView<Club>
    lateinit var positionList : ListView<String>
    lateinit var leagueName: TextField
    lateinit var leagueNation : TextField
    lateinit var mainPane: Pane
    lateinit var leaguePane: Pane
    lateinit var leagueNameLabel : Label
    lateinit var leagueNationLabel : Label
    lateinit var clubName: TextField
    lateinit var clubCity: TextField
    lateinit var clubStadium: TextField
    lateinit var clubPane: Pane
    lateinit var playerPane : Pane
    lateinit var clubNameLabel: Label
    lateinit var clubCityLabel: Label
    lateinit var clubStadiumLabel: Label
    lateinit var playerName : TextField
    lateinit var playerPosition : TextField
    lateinit var playerNumber : TextField
    lateinit var playerDOB : DatePicker
    lateinit var playerNationality : TextField
    lateinit var playerList : ListView<Player>
    lateinit var updateLeagueButton: Button
    lateinit var updateClubButton: Button
    lateinit var playerNameLabel: Label
    lateinit var playerDOBLabel: Label
    lateinit var playerNumLabel: Label
    lateinit var playerNationalityLabel: Label
    lateinit var updatePlayerButton: Button
    lateinit var positionBox : TextField
    lateinit var searchPane : Pane
    lateinit var searchBox : TextField
    lateinit var resultList : ListView<Any>


    @FXML
    fun initialize() {
        mainPane.isVisible = true
        leaguePane.isVisible = false
        clubPane.isVisible = false
        playerPane.isVisible = false
        searchPane.isVisible = false


        //Deleting from list code
        mainList.onKeyPressed = javafx.event.EventHandler { event ->
            if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                removeLeague()
            }
        }

        //Opening league code
        mainList.onMouseClicked = EventHandler { event ->
            if(mainList.selectionModel.selectedItem == null) return@EventHandler
            val league = leagues.find { l ->
                l.uid == mainList.selectionModel.selectedItem.uid }
            if(event.clickCount == 1){
                if(league != null) {
                    selectedLeague = league
                    leagueName.text = league.name
                    leagueNation.text = league.country
                    updateLeagueButton.isDisable = false
                }
            }
            else if (event.clickCount == 2) {
                if(league != null) {
                    selectedLeague = league
                    openLeague(league)
                }
            }
        }

        //Deleting from club list code
        clubList.onKeyPressed = javafx.event.EventHandler { event ->
            if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                removeClub()
            }
        }
        //Opening club code
        clubList.onMouseClicked = EventHandler { event ->
            if(clubList.selectionModel.selectedItem == null) return@EventHandler
            val club = selectedLeague?.getClub(clubList.selectionModel.selectedItem.uid)
            if(event.clickCount == 1){
                if(club != null) {
                    selectedClub = club
                    clubName.text = club.name
                    clubCity.text = club.city
                    clubStadium.text = club.stadium
                    updateClubButton.isDisable = false
                }
            }
            else if (event.clickCount == 2) {

                if(club != null) {
                    selectedClub = club
                    openClub(club)
                }
            }
        }

        playerList.onKeyPressed = EventHandler { event ->
            if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                removePlayer()
            }
        }
        playerList.onMouseClicked = EventHandler {event ->
            if(playerList.selectionModel.selectedItem == null) return@EventHandler
            val player = selectedClub?.getPlayer(playerList.selectionModel.selectedItem.uid)
            if(event.clickCount == 1){
                    selectedPlayer = player
                    playerName.text = player?.name
                    playerPosition.text = player?.positions?.get(0)
                    playerNationality.text = player?.nationality
                    playerNumber.text = player?.number.toString()
                    playerDOB.value = player?.dateOfBirth
                    playerNumber.text = player?.number.toString()
                    updatePlayerButton.isDisable = false
            }else if(event.clickCount == 2){
                if(player!=null){
                    clubPane.isVisible = false
                    selectedPlayer = player
                    playerNameLabel.text = player.name
                    playerDOBLabel.text = player.dateOfBirth.toString()
                    playerNumLabel.text = player.number.toString()
                    playerNationalityLabel.text = player.nationality
                    positionList.items = player.positions.toObservable()
                    playerPane.isVisible = true

                }
            }
        }

        positionList.onKeyPressed = EventHandler { event ->
            if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                removePosition()
            }
        }
    }
    fun goBack(){
        //Generic go back function
        if(leaguePane.isVisible || searchPane.isVisible){
            searchPane.isVisible = false
            leaguePane.isVisible = false
            mainPane.isVisible = true
            selectedLeague = null
            updateLeagueButton.isDisable = true
        }
        else if(clubPane.isVisible){
            clubPane.isVisible = false
            leaguePane.isVisible = true
            selectedClub = null
            updateClubButton.isDisable = true
        }else if(playerPane.isVisible){
            playerPane.isVisible = false
            clubPane.isVisible = true
            selectedPlayer = null
            updatePlayerButton.isDisable = true
        }
    }

    //League Code
    fun addLeague() {
        val leagueExists = leagues.any { it.toString().equals(leagueName.text, ignoreCase = true) }
        if(leagueExists) throw Exception("League already exists")
        val newLeague = League(leagueName.text, leagueNation.text)
        leagues.add(newLeague)

        mainList.items.add(newLeague)

        leagueName.text = ""
        leagueNation.text = ""
    }

    private fun removeLeague() {
        val selectedLeague = mainList.selectionModel.selectedItem
        val league = leagues.find { it.uid == selectedLeague.uid}
        leagues.remove(league)
        mainList.items.remove(selectedLeague)
    }

    fun updateLeague(){
        val selectedLeague = mainList.selectionModel.selectedItem
        val league = leagues.find { it.uid == selectedLeague.uid }
        league?.name = leagueName.text ?: league?.name!!
        league?.country = leagueNation.text ?: league?.country!!
        mainList.items[mainList.selectionModel.selectedIndex] = league
        leagues[mainList.selectionModel.selectedIndex] = league!!
    }

    private fun openLeague(league: League){
        mainPane.isVisible = false
        if(league.listClubs().isNotEmpty()) {
            clubList.items = league.clubs.toObservable()
        }
        else{
            clubList.items.clear()
        }
        leagueNameLabel.text = league.name
        leagueNationLabel.text = league.country
        leaguePane.isVisible = true
    }

    // Club Code

    fun addClub(){
        val clubExists = selectedLeague?.containsClub(clubName.text)
        if(clubExists!!) throw Exception("Club already exists")
        selectedLeague?.addClub(clubName.text, clubCity.text, clubStadium.text)
        clubList.items.add(selectedLeague?.searchClub(clubName.text))
        clubName.text = ""
        clubCity.text = ""
        clubStadium.text = ""
    }

    fun removeClub(){
        val selectedClub = clubList.selectionModel.selectedItem
        val club = selectedLeague?.getClub(selectedClub.uid)
        selectedLeague?.removeClub(club!!)
        clubList.items.remove(selectedClub)
    }

    fun updateClub(){
        val selectedClub = clubList.selectionModel.selectedItem
        val club = selectedLeague?.getClub(selectedClub.uid)
        club?.name = clubName.text ?: club?.name!!
        club?.city = clubCity.text ?: club?.city!!
        club?.stadium = clubStadium.text ?: club?.stadium!!
        clubList.items[clubList.selectionModel.selectedIndex] = club
        selectedLeague?.replaceClub(clubList.selectionModel.selectedIndex, club!!)

    }

    private fun openClub(club: Club){
        leaguePane.isVisible = false
        if(club.listPlayers().isNotEmpty()){
        playerList.items = club.players.toObservable()
        }
        else{
            playerList.items.clear()
        }
        clubNameLabel.text = club.name
        clubCityLabel.text = club.city
        clubStadiumLabel.text = club.stadium
        clubPane.isVisible = true
    }

    fun addPlayer(){
        val playerExists = selectedClub?.searchPlayer(playerName.text)
        if(playerExists != null) throw Exception("Player already exists")
        val dob = playerDOB.value
        selectedClub?.addPlayer(playerName.text, dob,playerPosition.text, playerNationality.text, playerNumber.text.toInt())
        playerList.items.add(selectedClub?.searchPlayer(playerName.text))
        playerName.text = ""
        playerPosition.text = ""
        playerNationality.text = ""
        playerNumber.text = ""
        playerDOB.value = null

    }

    private fun removePlayer(){
        val selectedPlayer = playerList.selectionModel.selectedItem
        val player = selectedClub?.getPlayer(selectedPlayer.uid)
        selectedClub?.removePlayer(player!!)
        playerList.items.remove(selectedPlayer)
    }

    fun updatePlayer(){
        //Either replace or leave the same if empty
        val newName = playerName.text ?: selectedPlayer?.name!!
        val newDOB = playerDOB.value ?: selectedPlayer?.dateOfBirth!!
        val newPosition = playerPosition.text ?: selectedPlayer?.positions?.get(0)!!
        val newNationality = playerNationality.text ?: selectedPlayer?.nationality
        val newNumber = playerNumber.text ?: selectedPlayer?.number.toString()
        selectedClub?.replacePlayer(playerList.selectionModel.selectedIndex, newName, newDOB, newPosition, newNationality!!, newNumber.toInt())
        playerList.items[playerList.selectionModel.selectedIndex] = selectedClub?.getPlayer(playerList.selectionModel.selectedItem.uid)

    }

    fun save(){
        val file = File("leagues.json")
        //Use GSON to save the array to a file
        val jsonArray = Gson().toJson(leagues)
        file.writeText(jsonArray)
    }

    fun addPosition(){
        val position = positionBox.text
        if(position.isNotEmpty()){
            positionList.items.add(position)
            selectedPlayer?.addPosition(position)
        }
    }

    private fun removePosition(){
        val position = positionList.selectionModel.selectedItem
        positionList.items.remove(position)
        selectedPlayer?.removePosition(position)
    }

    fun load(){
        val file = File("leagues.json")
        //Use GSON to load the array from a file
        val jsonArray = file.readText()
        val leaguesArray = Gson().fromJson(jsonArray, Array<League>::class.java)
        leagues.clear()
        mainList.items.clear()
        leagues.addAll(leaguesArray)
        leagues.forEach { league ->
            mainList.items.add(league)
        }
        clubPane.isVisible = false
        playerPane.isVisible = false
        leaguePane.isVisible = false
        mainPane.isVisible = true
    }

    fun openSearch(){
        searchPane.isVisible = true
        mainPane.isVisible = false
    }

    fun search(){
        val searchTerm = searchBox.text
        val searchResults = ArrayList<Any>()
        //Big bulky inefficient triple nested loop
        //Could I make this more efficient? Yes but I'm lazy and will not be doing that lmao
        leagues.forEach { league ->
            if (league.toString().contains(searchTerm, ignoreCase = true)) {
                searchResults.add(league)
            }
            league.clubs.forEach { club ->
                if (club.toString().contains(searchTerm, ignoreCase = true)) {
                    searchResults.add(club)
                }
                club.players.forEach { player ->
                    if (player.toString().contains(searchTerm, ignoreCase = true)) {
                        searchResults.add(player)
                    }
                }
            }
        }
        resultList.items = searchResults.toObservable()
    }

}