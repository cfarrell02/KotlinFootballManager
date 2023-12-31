package org.setu.controller

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import javafx.stage.Stage
import javafx.stage.WindowEvent
import org.setu.model.*
import org.setu.view.AlertBox
import tornadofx.*
import java.time.LocalDate
import java.util.*

class MainController: Controller() {
    private val leagueStore = LeagueStore()
    private var selectedLeague: League? = null
    private var selectedClub: Club? = null
    private var selectedPerson: Person? = null
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
    lateinit var personList : ListView<Person>
    lateinit var updateLeagueButton: Button
    lateinit var updateClubButton: Button
    lateinit var playerNameLabel: Label
    lateinit var playerDOBLabel: Label
    lateinit var playerNumLabel: Label
    lateinit var playerNationalityLabel: Label
    lateinit var updatePersonButton: Button
    lateinit var positionBox : TextField
    lateinit var searchPane : Pane
    lateinit var searchBox : TextField
    lateinit var resultList : ListView<Any>
    lateinit var staffRole : TextField
    lateinit var staffSalary : TextField
    lateinit var playerStaffToggle : ToggleButton

    val isDirty : Boolean
        get() = leagueStore.isDirty


    @FXML
    fun initialize() {
        mainPane.isVisible = true
        leaguePane.isVisible = false
        clubPane.isVisible = false
        playerPane.isVisible = false
        searchPane.isVisible = false

        playerStaffToggle.onAction = EventHandler {
            toggleStaffPlayerButton()
        }


        //Deleting from list code
        mainList.onKeyPressed = javafx.event.EventHandler { event ->
            if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                removeLeague()
            }
        }

        //Opening league code
        mainList.onMouseClicked = EventHandler { event ->
            if(mainList.selectionModel.selectedItem == null) return@EventHandler
            val league = leagueStore.getLeague(mainList.selectionModel.selectedItem.uid)
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
        //Deleting from person list code
        personList.onKeyPressed = EventHandler { event ->
            if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                removePerson()
            }
        }
        //Opening person code
        personList.onMouseClicked = EventHandler {event ->
            if(personList.selectionModel.selectedItem == null) return@EventHandler
            val person  = selectedClub?.getPerson(personList.selectionModel.selectedItem.uid)
            if(event.clickCount == 1){
                    selectedPerson = person
                    playerName.text = person?.name
                    playerNationality.text = person?.nationality
                    playerDOB.value = person?.dateOfBirth
                    if(person is Player) {
                        playerPosition.text = person.positions[0]
                        playerNumber.text = person.number.toString()
                        toggleStaffPlayerButton("player")
                    }else if(person is Staff){
                        staffRole.text = person.role
                        staffSalary.text = person.salary.toString()
                        toggleStaffPlayerButton("staff")
                    }
                    updatePersonButton.isDisable = false
            }else if(event.clickCount == 2){
                if(person!=null){
                    openPerson(person)
                }
            }
        }
        //Deleting from position list code
        positionList.onKeyPressed = EventHandler { event ->
            if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                if(selectedPerson is Player)
                removePosition()
            }
        }

        //Search list code
        resultList.onMouseClicked = EventHandler{ event ->
            if(event.clickCount == 2){
                when(val selectedResult = resultList.selectionModel.selectedItem){
                    is League -> openLeague(selectedResult, searchPane)
                    is Club -> openClub(selectedResult, searchPane)
                    is Person -> openPerson(selectedResult, searchPane)
                    else -> throw Exception("Unknown type")
                }
            }
        }

        if( AlertBox.displayConfirmation("Load", "Load file?", "Would you content from file?",
                "Yes", "No")){
            load()
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

    //Helper button toggle method
    private fun toggleStaffPlayerButton(type : String = "none"){
        var player = playerStaffToggle.isSelected
        if(type != "none"){
            player = type.toLowerCase() == "player"
            playerStaffToggle.isSelected = player
        }
        playerStaffToggle.text = if(player) "Player" else "Staff"
        staffRole.isDisable = player
        staffSalary.isDisable = player
        playerPosition.isDisable = !player
        playerNumber.isDisable = !player
    }

    //League Code
    fun addLeague() {
        try{
        val newLeague = leagueStore.addLeague(leagueName.text, leagueNation.text)
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
        if(!AlertBox.displayConfirmation("Delete League", "Are you sure you want to delete this league?",
                "This will delete all clubs and players in ${selectedLeague.name}"))
            return
        leagueStore.removeLeague(selectedLeague.uid)
        mainList.items.remove(selectedLeague)
        }catch (e: Exception){
            AlertBox.display("Error", e.message)
        }
    }

    fun updateLeague(){
        try{
        val selectedLeague = mainList.selectionModel.selectedItem
        val newLeague = leagueStore.updateLeague(selectedLeague.uid, leagueName.text, leagueNation.text)
        mainList.items[mainList.selectionModel.selectedIndex] = newLeague
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
        val club = selectedLeague?.addClub(clubName.text, clubCity.text, clubStadium.text)
        clubList.items.add(club)
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
        if(!AlertBox.displayConfirmation("Delete Club", "Are you sure you want to delete this club?",
                "This will delete all players in ${selectedClub.name}"))
            return

        val club = selectedLeague?.getClub(selectedClub.uid)
        selectedLeague?.removeClub(club?.uid ?: throw Exception("Club does not exist"))
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
        selectedLeague?.updateClub(selectedClub.uid, club.name, club.city, club.stadium)
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
        personList.items = club.people.toObservable()
        }
        else{
            personList.items.clear()
        }
        clubNameLabel.text = club.name
        clubCityLabel.text = club.city
        clubStadiumLabel.text = club.stadium
        clubPane.isVisible = true
        }catch (e: Exception){
            AlertBox.display("Error", "Error opening club \n ${e.message}")
        }
    }

    fun addPerson(){
        try{
        val playerExists = selectedClub?.searchPlayer(playerName.text)
        if(playerExists != null) throw Exception("Player already exists")
        val dob = playerDOB.value
        require(dob.isBefore(LocalDate.now())){"Date of birth must be before today"}
        require(playerNationality.text.isNotBlank()) { "Nationality cannot be blank" }
        require(playerName.text.isNotBlank()){"Name cannot be blank"}
        if(playerStaffToggle.isSelected) {
            require(playerPosition.text.isNotBlank()){"Position cannot be blank"}
            require(playerNumber.text.toInt() > 0) { "Number must be greater than 0" }
            val player = selectedClub?.addPlayer(playerName.text, dob,playerPosition.text, playerNationality.text,
                playerNumber.text.toInt())
            personList.items.add(player)
        }else {
            require(staffRole.text.isNotBlank()) { "Role cannot be blank" }
            require(staffSalary.text.toDouble() > 0) { "Salary must be greater than 0" }
            val staff = selectedClub?.addStaff(playerName.text, dob, playerNationality.text, staffRole.text,
                staffSalary.text.toDouble())
            personList.items.add(staff)
        }
        playerName.text = ""
        playerPosition.text = ""
        playerNationality.text = ""
        playerNumber.text = ""
        playerDOB.value = null
        staffRole.text = ""
        staffSalary.text = ""
        }catch (e: Exception){
            AlertBox.display("Error", e.message)
        }

    }

    private fun removePerson(){
        try{
        val selectedPerson = personList.selectionModel.selectedItem
        if(!AlertBox.displayConfirmation("Delete Person", "Are you sure you want to delete this person?",
                "This will delete ${selectedPerson.name}"))
            return
        require(selectedPerson != null){"Person does not exist"}
        selectedClub?.removePerson(selectedPerson.uid)
        personList.items.remove(selectedPerson)
        }catch (e: Exception){
            AlertBox.display("Error", e.message)
        }
    }

    fun updatePerson(){
        //Either replace or leave the same if empty
        try{
            val selectedPerson = personList.selectionModel.selectedItem
            require(selectedPerson != null){"Player does not exist"}
            require(playerStaffToggle.isSelected == selectedPerson is Player)
            { "Cannot change from ${if(selectedPerson is Player) "player" else "staff"} to " +
                    if(selectedPerson is Player) "staff" else "player"
            }
            val newName = playerName.text ?: selectedPerson.name
            val newDOB = playerDOB.value ?: selectedPerson.dateOfBirth
            val newNationality = playerNationality.text ?: selectedPerson.nationality
            if(selectedPerson is Player) {
                val newPosition = playerPosition.text ?: selectedPerson.positions.get(0)
                val newNumber = playerNumber.text ?: selectedPerson.number.toString()
                val newPlayer = selectedClub?.updatePlayer(selectedPerson.uid, newName, newDOB, newPosition,
                    newNationality, newNumber.toInt())

                personList.items[personList.selectionModel.selectedIndex] = newPlayer
            }else if(selectedPerson is Staff){
                val newRole = staffRole.text ?: selectedPerson.role
                val newSalary = staffSalary.text ?: selectedPerson.salary.toString()
                val newStaff = selectedClub?.updateStaff(selectedPerson.uid, newName, newDOB,
                    newNationality, newRole, newSalary.toDouble())

                personList.items[personList.selectionModel.selectedIndex] = newStaff
        }
        }catch (e: Exception){
            AlertBox.display("Error", e.message)
        }
    }

    private fun openPerson(person: Person, currentPane : Pane = clubPane){
        try{
        currentPane.isVisible = false
        screenList.push(currentPane)
        selectedPerson = person
        clubPane.isVisible = false
        searchPane.isVisible = false
        playerNameLabel.text = person.name
        playerDOBLabel.text = person.dateOfBirth.toString()
        playerNationalityLabel.text = person.nationality

            if(person is Player) {
                playerNumLabel.text = person.number.toString()
                positionList.items = person.positions.toObservable()
                positionBox.isDisable = false
            }else if(person is Staff){
                positionBox.isDisable = true
                playerNumLabel.text = person.salary.toString()
                positionList.items.clear()
                positionList.items.add("Role: ${person.role}")
        }
        playerPane.isVisible = true
        }catch (e: Exception){
            AlertBox.display("Error", "Error opening player \n ${e.message}")
        }
    }


    fun addPosition(){
        val position = positionBox.text
        if(position.isNotEmpty()){
            positionList.items.add(position)
            (selectedPerson as Player).addPosition(position)
        }
    }

    private fun removePosition(){
        val position = positionList.selectionModel.selectedItem
        positionList.items.remove(position)
        (selectedPerson as Player).removePosition(position)
    }

    fun save() {
        try {
            leagueStore.save()
        } catch (e: Exception) {
            AlertBox.display("Error", "Error saving file \n ${e.message}")
        }
    }

    fun load() {
        try {
            val loadedArray = leagueStore.load()
            mainList.items.clear()
            loadedArray.forEach { league ->
                mainList.items.add(league)
            }
            clubPane.isVisible = false
            playerPane.isVisible = false
            leaguePane.isVisible = false
            mainPane.isVisible = true
        } catch (e: Exception) {
            System.err.println(e.message)
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
        resultList.items = leagueStore.search(searchTerm).toObservable()
    }


}