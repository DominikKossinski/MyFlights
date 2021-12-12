package pl.kossa.myflights.api.simbrief.models

import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode

data class Alternate(
    val airport: Airport? = null,
    var route: String? = null,
)

class AlternateConverter : Converter<Alternate> {

    override fun read(node: InputNode?): Alternate {
        var currentChild = node?.next

        var icaoCode: String? = null
        var posLat: Float? = null
        var posLong: Float? = null
        var elevation: Float? = null
        var name: String? = null
        var route: String? = null
        while (currentChild != null) {
            when(currentChild.name) {
                "icao_code" -> icaoCode = currentChild.value
                "pos_lat" -> posLat = currentChild.value?.toFloatOrNull()
                "pos_long" -> posLong = currentChild.value?.toFloatOrNull()
                "elevation" -> elevation = currentChild.value?.toFloatOrNull()
                "name" -> name = currentChild.value
                "route" -> route = currentChild.value
            }
            currentChild = node?.next
        }
        val airport = Airport(icaoCode, posLat, posLong, elevation, name)
        return Alternate(airport, route)
    }

    override fun write(node: OutputNode?, value: Alternate?) {
        TODO("Not yet implemented")
    }

}
