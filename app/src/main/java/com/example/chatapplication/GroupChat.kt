package com.example.chatapplication

import java.util.*

class GroupChat {
    var senderId: String? = null
    var groupMessage: String? = null
    var date: String? = null
    var time: String? = null

    constructor(){}

    constructor(senderId: String?, groupMessage: String?, date: String?, time: String?){
        this.senderId = senderId
        this.groupMessage = groupMessage
        this.date = date
        this.time = time
    }

}