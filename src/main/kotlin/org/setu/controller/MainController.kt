package org.setu.controller

import com.google.gson.Gson
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import javafx.stage.Screen
import org.setu.model.Club
import org.setu.model.League
import org.setu.model.Player
import org.setu.view.AlertBox
import tornadofx.*
import java.io.File
import java.time.LocalDate
import java.util.LinkedList


class MainController {
    private val leagues = ArrayList<League>()
    private var selectedLeague: League? = null
    private var selectedClub: Club? = null
    private var selectedPlayer: Player? = null
    private val screenList = LinkedList<Pane>()

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
                    openPlayer(player)
                }
            }
        }

        positionList.onKeyPressed = EventHandler { event ->
            if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                removePosition()
            }
        }

        //Search list code
        resultList.onMouseClicked = EventHandler{ event ->
            if(event.clickCount == 2){
                when(val selectedResult = resultList.selectionModel.selectedItem){
                    is League -> openLeague(selectedResult, searchPane)
                    is Club -> openClub(selectedResult, searchPane)
                    is Player -> openPlayer(selectedResult, searchPane)
                    else -> throw Exception("Unknown type")
                }
            }
        }
    }
    fun goBack(){
        val panes = listOf(mainPane, leaguePane, clubPane, playerPane, searchPane)
        panes.forEach{it.isVisible = false}
        if(screenList.isNotEmpty()){
            val pane = screenList.pop()
            pane.isVisible = true
        }
    }

    //League Code
    fun addLeague() {
        try{
        val leagueExists = leagues.any { it.toString().equals(leagueName.text, ignoreCase = true) }
        if(leagueExists) throw Exception("League already exists")
        require(leagueName.text.isNotBlank()){"Name cannot be blank"}
        require(leagueNation.text.isNotBlank()){"Nation cannot be blank"}
        val newLeague = League(leagueName.text, leagueNation.text)
        leagues.add(newLeague)

        mainList.items.add(newLeague)

        leagueName.text = ""
        leagueNation.text = ""
        }catch (e: Exception){
            AlertBox.display("Error", e.message)
        }
    }

    private fun removeLeague() {
        try{
            val selectedLeague = mainList.selectionModel.selectedItem
        if(!AlertBox.displayConfirmation("Delete League", "Are you sure you want to delete this league?", "This will delete all clubs and players in ${selectedLeague.name}"))
            return

        val league = leagues.find { it.uid == selectedLeague.uid}
        leagues.remove(league)
        mainList.items.remove(selectedLeague)
        }catch (e: Exception){
            AlertBox.display("Error", e.message)
        }
    }

    fun updateLeague(){
        try{
        val selectedLeague = mainList.selectionModel.selectedItem
        val league = leagues.find { it.uid == selectedLeague.uid }
        require(league != null){"League does not exist"}
        league.name = leagueName.text ?: league.name
        league.country = leagueNation.text ?: league.country
        mainList.items[mainList.selectionModel.selectedIndex] = league
        leagues[mainList.selectionModel.selectedIndex] = league
        }catch (e: Exception){
            AlertBox.display("Error", e.message)
        }
    }

    private fun openLeague(league: League, currentPane : Pane = mainPane){
        try{
        currentPane.isVisible = false
        screenList.push(currentPane)
        selectedLeague = league
        if(league.listClubs().isNotEmpty()) {
            clubList.items = league.clubs.toObservable()
        }
        else{
            clubList.items.clear()
        }
        leagueNameLabel.text = league.name
        leagueNationLabel.text = league.country
        leaguePane.isVisible = true
        }catch (e: Exception){
            AlertBox.display("Error", "Error opening league \n ${e.message}")
        }
    }

    // Club Code

    fun addClub(){
        try{
        val clubExists = selectedLeague?.containsClub(clubName.text)
        if(clubExists!!) throw Exception("Club already exists")
        require(clubName.text.isNotBlank()){"Name cannot be blank"}
        require(clubCity.text.isNotBlank()){"City cannot be blank"}
        require(clubStadium.text.isNotBlank()){"Stadium cannot be blank"}
        selectedLeague?.addClub(clubName.text, clubCity.text, clubStadium.text)
        clubList.items.add(selectedLeague?.searchClub(clubName.text))
        clubName.text = ""
        clubCity.text = ""
        clubStadium.text = ""
        }catch (e: Exception){
            AlertBox.display("Error", e.message)
        }
    }

    private fun removeClub(){
        try{
        val selectedClub = clubList.selectionModel.selectedItem
        if(!AlertBox.displayConfirmation("Delete Club", "Are you sure you want to delete this club?", "This will delete all players in ${selectedClub.name}"))
            return

        val club = selectedLeague?.getClub(selectedClub.uid)
        selectedLeague?.removeClub(club!!)
        clubList.items.remove(selectedClub)
        }catch (e: Exception){
            AlertBox.display("Error", e.message)
        }
    }

    fun updateClub(){
        try{
        val selectedClub = clubList.selectionModel.selectedItem
        val club = selectedLeague?.getClub(selectedClub.uid)
        require(club != null){"Club does not exist"}
        club.name = clubName.text ?: club.name
        club.city = clubCity.text ?: club.city
        club.stadium = clubStadium.text ?: club.stadium
        clubList.items[clubList.selectionModel.selectedIndex] = club
        selectedLeague?.replaceClub(clubList.selectionModel.selectedIndex, club)
        }catch (e: Exception){
            AlertBox.display("Error", e.message)
        }

    }

    private fun openClub(club: Club, currentPane : Pane = leaguePane){
        try{
        currentPane.isVisible = false
        screenList.push(currentPane)
        selectedClub = club
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
        }catch (e: Exception){
            AlertBox.display("Error", "Error opening club \n ${e.message}")
        }
    }

    fun addPlayer(){
        try{
        val playerExists = selectedClub?.searchPlayer(playerName.text)
        if(playerExists != null) throw Exception("Player already exists")
        val dob = playerDOB.value
        require(dob.isBefore(LocalDate.now())){"Date of birth must be before today"}
        require(playerName.text.isNotBlank()){"Name cannot be blank"}
        require(playerPosition.text.isNotBlank()){"Position cannot be blank"}
        require(playerNationality.text.isNotBlank()){"Nationality cannot be blank"}
        require(playerNumber.text.toInt() > 0){"Number must be greater than 0"}
        selectedClub?.addPlayer(playerName.text, dob,playerPosition.text, playerNationality.text, playerNumber.text.toInt())
        playerList.items.add(selectedClub?.searchPlayer(playerName.text))
        playerName.text = ""
        playerPosition.text = ""
        playerNationality.text = ""
        playerNumber.text = ""
        playerDOB.value = null
        }catch (e: Exception){
            AlertBox.display("Error", e.message)
        }

    }

    private fun removePlayer(){
        try{
        val selectedPlayer = playerList.selectionModel.selectedItem
        if(!AlertBox.displayConfirmation("Delete Player", "Are you sure you want to delete this player?", "This will delete ${selectedPlayer.name}"))
            return
        val player = selectedClub?.getPlayer(selectedPlayer.uid)
        selectedClub?.removePlayer(player!!)
        playerList.items.remove(selectedPlayer)
        }catch (e: Exception){
            AlertBox.display("Error", e.message)
        }
    }

    fun updatePlayer(){
        //Either replace or leave the same if empty
        try{
        val selectedPlayer = playerList.selectionModel.selectedItem
        require(selectedPlayer != null){"Player does not exist"}
        val newName = playerName.text ?: selectedPlayer.name
        val newDOB = playerDOB.value ?: selectedPlayer.dateOfBirth
        val newPosition = playerPosition.text ?: selectedPlayer.positions.get(0)
        val newNationality = playerNationality.text ?: selectedPlayer.nationality
        val newNumber = playerNumber.text ?: selectedPlayer.number.toString()
        selectedClub?.replacePlayer(playerList.selectionModel.selectedIndex, newName, newDOB, newPosition, newNationality, newNumber.toInt())
        playerList.items[playerList.selectionModel.selectedIndex] = selectedClub?.getPlayer(playerList.selectionModel.selectedItem.uid)
        }catch (e: Exception){
            AlertBox.display("Error", e.message)
        }

    }

    fun openPlayer(player: Player, currentPane : Pane = clubPane){
        try{
        currentPane.isVisible = false
        screenList.push(currentPane)
        selectedPlayer = player
        clubPane.isVisible = false
        searchPane.isVisible = false
        selectedPlayer = player
        playerNameLabel.text = player.name
        playerDOBLabel.text = player.dateOfBirth.toString()
        playerNumLabel.text = player.number.toString()
        playerNationalityLabel.text = player.nationality
        positionList.items = player.positions.toObservable()
        playerPane.isVisible = true
        }catch (e: Exception){
            AlertBox.display("Error", "Error opening player \n ${e.message}")
        }
    }

    fun save(){
        try {
        val file = File("leagues.json")
        //Use GSON to save the array to a file
        val jsonArray = Gson().toJson(leagues)
        file.writeText(jsonArray)
        }catch (e: Exception){
            AlertBox.display("Error", "Error saving file \n ${e.message}")
        }
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
        try{
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
        }catch (e: Exception){
            AlertBox.display("Error", "Error loading file \n ${e.message}")
        }
    }

    fun openSearch(){
        screenList.push(mainPane)
        searchPane.isVisible = true
        mainPane.isVisible = false
    }

    fun search(){
        val searchTerm = searchBox.text
        val searchResults = ArrayList<Any>()
        //Big bulky inefficient triple nested loop
        //Could I make this more efficient? Yes but i dont want to
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