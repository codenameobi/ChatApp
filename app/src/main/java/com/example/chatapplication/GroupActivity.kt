package com.example.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GroupActivity : AppCompatActivity() {
    private lateinit var groupRecyclerView: RecyclerView
    private lateinit var groupList: ArrayList<Group>
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var mDbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        mDbRef = FirebaseDatabase.getInstance().reference
        groupList = ArrayList()
        groupAdapter = GroupAdapter(this, groupList)

        groupRecyclerView = findViewById(R.id.groupRecyclerView)
        groupRecyclerView.layoutManager = LinearLayoutManager(this)
        groupRecyclerView.adapter = groupAdapter

        mDbRef.child("Groups").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                groupList.clear()
                for(postSnapshot in snapshot.children){
                    val group = postSnapshot.getValue(Group::class.java)
                    groupList.add(group!!)
                }
                groupAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}