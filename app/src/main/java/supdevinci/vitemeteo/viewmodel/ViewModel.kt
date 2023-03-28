package supdevinci.vitemeteo.viewmodel

import supdevinci.vitemeteo.model.Position

class ViewModel {
    val text: String = "Vos coordonnées GPS sont : "
    var Position: Position? = Position(3.0f, 5.0f)

    public fun getPositionToString(): String {
        return text + Position?.longitude + " " + Position?.latitude
    }
}