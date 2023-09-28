package org.setu.model

class League (
    var name: String,
    var country: String,
    val clubs: MutableList<Club> = ArrayList(),
    val uid: String = java.util.UUID.randomUUID().toString()
) {




    init{
        //Validation here
        require(name.isNotBlank()){"Name cannot be blank"}
        require(country.isNotBlank()){"Country cannot be blank"}
    }

        fun addClub(name: String, city: String, stadium: String) {
            val club = Club(name,city,stadium)
            clubs.add(club)
        }
        fun removeClub(club: Club) {
            clubs.remove(club)
        }
        fun removeClub(index: Int) {
            clubs.removeAt(index)
        }
        fun listClubs(): String {
            val list = clubs.joinToString("\n") { e -> e.toString() }
            return list
        }
        fun listClubsWithIndex(): String {
            val list = clubs.mapIndexed { index, club -> "${index + 1}. $club" }
            return list.joinToString("\n")
        }
        fun getClub(id: String): Club {
            return clubs.find { it.uid == id }!!
        }
        fun searchClub(name: String): Club? {
            return clubs.find { it.name.equals(name, ignoreCase = true) || it.toString().equals(name, ignoreCase = true)}
        }

        fun replaceClub(index: Int, club: Club){
            clubs[index] = club
        }



        fun containsClub(name: String): Boolean {
            return clubs.any { it.name.equals(name, ignoreCase = true) }
        }

    override fun toString(): String {
        return "$name, $country"
    }
}
