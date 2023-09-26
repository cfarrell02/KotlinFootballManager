package org.setu

val leagues = mutableListOf<League>()
fun main(args: Array<String>) {
    println("Welcome to the football store!")
}

fun printMenu(){
    //League menu
    println("1. Add a new League")
    println("2. Delete a league")
    println("3. Search for a league")
    println("4. List all leagues")
    println("5. Edit a league")
    println("-1. Exit")
}


fun runMenu(){
    var option: Int
    do {
        printMenu()
        option = readln().toInt()
        when (option) {
            1 -> addLeague()
            2 -> deleteLeague()
            3 -> searchLeague()
            4 -> listLeagues()
            5 -> editLeague()
            -1 -> println("Exiting...")
            else -> println("Invalid option")
        }
    } while (option != -1)
}

fun addLeague(){
    print("Enter the name of the league: ")
    val name = readln()
    print("Enter the country of the league: ")
    val country = readln()
    val league = League(name,country)
    leagues.add(league)
}
fun deleteLeague(){
    val league = searchLeague()
    if(league != null){
        leagues.remove(league)
        println("League deleted")
    }else{
        println("League not found")
    }
}
fun searchLeague(): League{
    print("Enter name of the league: ")
    val name = readln()
    val league = leagues.find { it.name.uppercase() == name.uppercase() }
    if(league != null){
        println("League found: $league")
    }else{
        println("League not found")
    }
    return league!!
}
fun listLeagues(){
    println("List of leagues:")
    leagues.forEach { println(it) }
}
fun editLeague(){
    TODO()
}

