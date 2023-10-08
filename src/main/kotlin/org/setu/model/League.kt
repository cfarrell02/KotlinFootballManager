package org.setu.model

class League (
    var name: String,
    var country: String,
    private val _clubs: MutableList<Club> = mutableListOf(),
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
        fun removeClub(uid: String): Club {
            val club = _clubs.find { it.uid == uid } ?: throw IllegalArgumentException("Club with uid $uid not found")
            _clubs.remove(club)
            return club
        }
        fun listClubs(): String {
            val list = clubs.joinToString("\n") { e -> e.toString() }
            return list
        }

        fun getClub(id: String): Club? {
            return clubs.find { it.uid == id }
        }
    fun searchClub(name: String): Club? {
        return clubs.find { it.name.equals(name, ignoreCase = true) || it.toString().equals(name, ignoreCase = true) }

    }

        fun updateClub(id: String, name: String, city: String, stadium: String): Club {
            val club = clubs.find { it.uid == id }!!
            club.name = name
            club.city = city
            club.stadium = stadium
            return club
        }

        fun containsClub(name: String): Boolean {
            return clubs.any { it.name.equals(name, ignoreCase = true) }
        }

    override fun toString(): String {
        return "League: $name, $country"
    }
}
