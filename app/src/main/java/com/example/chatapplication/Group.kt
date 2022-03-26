package com.example.chatapplication

import java.util.*
import kotlin.collections.ArrayList

class Group {
    var id: String? = null
    var groupName: String? = null
    var createdBy: String? = null
    var createdAt: Date = Date()
//    var profiles: ArrayList<UserProfile>?=null
    constructor(){}

    constructor(groupName: String?) {

        this.groupName = groupName
    }
}