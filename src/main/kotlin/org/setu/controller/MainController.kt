package org.setu.controller

import com.google.gson.Gson
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.DatePicker
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import org.setu.Club
import org.setu.League
import tornadofx.*
import java.io.File
import javax.json.JsonArray


class MainController {
    private val leagues = mutableListOf<League>()
    private var selectedLeague: League? = null
    private var selectedClub: Club? = null

    @FXML
    lateinit var mainList: ListView<String>
    lateinit var clubList : ListView<String>
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
    lateinit var clubNameLabel: Label
    lateinit var clubCityLabel: Label
    lateinit var clubStadiumLabel: Label
    lateinit var playerName : TextField
    lateinit var playerPosition : TextField
    lateinit var playerNumber : TextField
    lateinit var playerDOB : DatePicker
    lateinit var playerNationality : TextField
    lateinit var playerList : ListView<String>


    @FXML
    fun initialize() {
        mainPane.isVisible = true
        leaguePane.isVisible = false
        clubPane.isVisible = false

        //Deleting from list code
        mainList.onKeyPressed = javafx.event.EventHandler { event ->
            if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                removeLeague()
            }
        }

        //Opening league code
        mainList.onMouseClicked = EventHandler { event ->
            if (event.clickCount == 2) {
                val league = leagues.find { l ->
                    l.toString().equals(mainList.selectionModel.selectedItem, ignoreCase = true) }
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
            if (event.clickCount == 2) {
                val club = selectedLeague?.searchClub(clubList.selectionModel.selectedItem)
                if(club != null) {
                    selectedClub = club
                    openClub(club)
                }
            }
        }
    }
    fun goBack(){
        //Generic go back function
        if(leaguePane.isVisible){
            leaguePane.isVisible = false
            mainPane.isVisible = true
        }
        else if(clubPane.isVisible){
            clubPane.isVisible = false
            leaguePane.isVisible = true
        }
    }

    //League Code
    fun addLeague() {
        val leagueExists = leagues.any { it.toString().equals(leagueName.text, ignoreCase = true) }
        if(leagueExists) throw Exception("League already exists")
        val newLeague = League(leagueName.text, leagueNation.text)
        leagues.add(newLeague)

        mainList.items.add(newLeague.toString())

        leagueName.text = ""
        leagueNation.text = ""
    }

    private fun removeLeague() {
        val selectedLeague = mainList.selectionModel.selectedItem
        val league = leagues.find { it.toString() == selectedLeague }
        leagues.remove(league)
        mainList.items.remove(selectedLeague)
    }

    private fun openLeague(league: League){
        println("Opening league ${league.name}")
        mainPane.isVisible = false
        clubList.items = league.listClubs().split("\n").toObservable();
        leagueNameLabel.text = league.name
        leagueNationLabel.text = league.country
        leaguePane.isVisible = true
    }

    // Club Code

    fun addClub(){
        val clubExists = selectedLeague?.containsClub(clubName.text)
        if(clubExists!!) throw Exception("Club already exists")
        selectedLeague?.addClub(clubName.text, clubCity.text, clubStadium.text)
        clubList.items.add(selectedLeague?.searchClub(clubName.text).toString())
        clubName.text = ""
        clubCity.text = ""
        clubStadium.text = ""
    }

    fun removeClub(){
        val selectedClub = clubList.selectionModel.selectedItem
        val club = selectedLeague?.searchClub(selectedClub)
        selectedLeague?.removeClub(club!!)
        clubList.items.remove(selectedClub)
    }

    fun openClub(club: Club){
        println("Opening club ${club.name}")
        leaguePane.isVisible = false
        playerList.items = club.listPlayers().split("\n").toObservable();
        clubNameLabel.text = club.name
        clubCityLabel.text = club.city
        clubStadiumLabel.text = club.stadium
        clubPane.isVisible = true
    }

    fun addPlayer(){
        val playerExists = selectedClub?.searchPlayer(playerName.text)
        if(playerExists != null) throw Exception("Player already exists")
        selectedClub?.addPlayer(playerName.text, playerDOB.value,playerPosition.text, playerNationality.text, playerNumber.text.toInt())
        playerList.items.add(selectedClub?.searchPlayer(playerName.text).toString())

    }

    fun save(){
        val file = File("leagues.json")
        //Use GSON to save the array to a file
        val jsonArray = Gson().toJson(leagues)
        file.writeText(jsonArray)
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
            mainList.items.add(league.toString())
        }
    }
}