package com.example.littlelemon

interface Destination{
    var r:String
}

object Home: Destination{
    override var r = "Home"

}
object OnBoard: Destination{
    override var r = "OnBoard"
}
object Profile: Destination{
    override var r = "Profile"
}