package org.setu.model

class League (
    var name: String,
    var country: String,
    private val _clubs: MutableList<Club> = ArrayList(),
    val uid: String = java.util.UUID.randomUUID().toString()
) {



    val clubs : List<Club>
        get() = _clubs

    init{
        //Validation here
        require(name.isNotBlank()){"Name cannot be blank"}
        require(country.isNotBlank()){"Country cannot be blank"}
    }

        fun addClub(name: String, city: String, stadium: String): Club{
            val club = Club(name,city,stadium)
            _clubs.add(club)
            return club
        }
        fun removeClub(club: Club) {
            _clubs.remove(club)
        }
        fun removeClub(index: Int) {
            _clubs.removeAt(index)
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

        fun replaceClub(uid : String, club: Club): Club{
            _clubs.find { it.uid == uid }?.let {
                _clubs[_clubs.indexOf(it)] = club
            }
            return club
        }



        fun containsClub(name: String): Boolean {
            return clubs.any { it.name.equals(name, ignoreCase = true) }
        }

    override fun toString(): String {
        return "League: $name, $country"
    }
}
