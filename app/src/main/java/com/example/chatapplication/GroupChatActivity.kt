package com.example.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class GroupChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var groupChatAdapter: GroupChatAdapter
    private lateinit var groupChatList: ArrayList<GroupChat>
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var UsersRef: DatabaseReference
    private lateinit var GroupNameRef: DatabaseReference
    private lateinit var currentUid: String
    private lateinit var currentUserName: String
    private lateinit var currentDate: String
    private lateinit var currentTime: String

//    private String currentGroupName, currentUid, currentUserName;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)

        val groupName = intent.getStringExtra("groupName")
        supportActionBar?.title = groupName
        chatRecyclerView = findViewById(R.id.chatRecyclerview)
        sendButton = findViewById(R.id.sentButton)
        messageBox = findViewById(R.id.messageBox)
        groupChatList = ArrayList()
        groupChatAdapter = GroupChatAdapter(this, groupChatList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = groupChatAdapter

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
        UsersRef = mDbRef.child("user")
        GroupNameRef = mDbRef.child("Groups").child(groupName!!)

        GroupNameRef.child("chats").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    val message = postSnapshot.getValue(GroupChat::class.java)
                    groupChatList.add(message!!)
                }
                groupChatAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {


            }

        }
        )

        currentUid = mAuth.currentUser?.uid.toString()
        getUserInfo()

        sendButton.setOnClickListener{
            SaveGroupMessageInfoToDatabase()
            messageBox.setText("")
        }
    }

    private fun SaveGroupMessageInfoToDatabase() {
        val message = messageBox.text.toString()

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Please Write Message First..", Toast.LENGTH_LONG)
        }
        var calForDate: Calendar = Calendar.getInstance()
        var currentDateFormat = SimpleDateFormat("MMM dd, yyyy")
        currentDate = currentDateFormat.format(calForDate.time)

        var calForTime: Calendar = Calendar.getInstance()
        var currentTimeFormat = SimpleDateFormat("hh:mm a")
        currentTime = currentTimeFormat.format(calForTime.time)

        val messageObject = GroupChat(currentUid,message,currentDate,currentTime)

        GroupNameRef.child("chats").push().setValue(messageObject).addOnSuccessListener {
            Toast.makeText(this, "Group Message Sent", Toast.LENGTH_LONG)
        }
    }

    private fun getUserInfo() {
        UsersRef.child(currentUid).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    currentUserName = snapshot.child("name").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}