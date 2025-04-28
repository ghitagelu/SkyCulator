package com.example.SkyDiver.DataBaseHandler

class ListItem {


    var id: Int = 0
    var userName: String? = null
    var userWeight:Int?=null
    var userEquipment:Int?=null
    var userCanopy:Int?=null

    constructor(id: Int, userName: String, userWeight:Int, userEquipment:Int, userCanopy:Int) {
        this.id = id
        this.userName = userName
        this.userWeight = userWeight
        this.userEquipment = userEquipment
        this.userCanopy = userCanopy
    }

    constructor(userName: String, userWeight:Int, userEquipment:Int, userCanopy:Int) {
        this.userName = userName
        this.userWeight = userWeight
        this.userEquipment = userEquipment
        this.userCanopy = userCanopy
    }


}