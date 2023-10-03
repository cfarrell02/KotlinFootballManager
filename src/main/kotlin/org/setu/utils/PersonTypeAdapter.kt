import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.setu.model.Person
import org.setu.model.Player
import org.setu.model.Staff
import java.io.IOException
import java.lang.reflect.Type

class PersonTypeAdapter : TypeAdapter<Person>() {

    //Class to handle the abstract class of Person
    //Written with help from https://stackoverflow.com/questions/57433875/create-an-adapterfactory-for-gson-for-dynamic-types
    //And some extra help from chatgpt :)
    //I dont take credit for this.
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Person) {
        // Implement serialization if needed

        // Create a JSON object and add the "type" field based on the actual type of 'value'
        val jsonObject = JsonObject()
        jsonObject.addProperty("type", when (value) {
            is Player -> "Player"
            is Staff -> "Staff"
            else -> throw IllegalArgumentException("Unknown Person type")
        })

        // Serialize the rest of the object
        val gson = Gson()
        val type = value.javaClass // Get the actual type of 'value'
        val element = gson.toJsonTree(value, type)
        jsonObject.add("data", element)

        gson.toJson(jsonObject, out)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Person {
        val jsonParser = JsonParser.parseReader(`in`)
        if (jsonParser.isJsonObject) {
            val jsonObject = jsonParser.asJsonObject
            val type = jsonObject.get("type").asString

            return when (type) {
                "Player" -> Gson().fromJson(jsonObject.get("data"), Player::class.java)
                "Staff" -> Gson().fromJson(jsonObject.get("data"), Staff::class.java)
                else -> throw IllegalStateException("Unknown Person type: $type")
            }
        }
        throw IllegalStateException("Unexpected JSON structure")
    }
}
