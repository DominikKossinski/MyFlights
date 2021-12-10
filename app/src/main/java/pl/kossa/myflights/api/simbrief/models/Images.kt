package pl.kossa.myflights.api.simbrief.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "images", strict = false)
data class Images(
    @field:Element(name = "directory")
    var directory: String? = null,

    @field:ElementList(name = "map", inline = true)
    var maps: List<Map>? = null
)

@Root(name = "map", strict = false)
data class Map(
    @field:Element(name = "name")
    var name: String? = null,

    @field:Element(name = "link")
    var link: String? = null
)
