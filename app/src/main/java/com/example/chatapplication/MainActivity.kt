package com.example.chatapplication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        mDbRef.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    //function to not show current login User
                    if(mAuth.currentUser?.uid !=  currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            mAuth.signOut()
            val intent = Intent (this@MainActivity, Login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        if(item.itemId == R.id.create_group){
            RequestNewGroup();
        }
        if(item.itemId == R.id.view_group){
            val intent = Intent (this@MainActivity, GroupActivity::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }

    private fun RequestNewGroup() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Enter Group Name")

        val groupNameField = EditText(this)
        groupNameField.setHint("e.g Coding Cafe")
        builder.setView(groupNameField)

        builder.setPositiveButton("Create Group"){ dialogInterface, _ ->
            val groupName = groupNameField.getText().toString()
            if(TextUtils.isEmpty(groupName)){
                Toast.makeText(this,"Please Enter Group Name",Toast.LENGTH_LONG).show()
            }
            CreateNewGroup(groupName)
        }
        builder.setNegativeButton("No"){ dialogInterface, _ ->
            dialogInterface.cancel()
        }
        builder.show()
    }

    private fun CreateNewGroup(groupName: String) {
        val groupData = Group(
            groupName = groupName
        )
        mDbRef.child("Groups").child(groupName).setValue(groupData).addOnSuccessListener {
            Toast.makeText(this,groupName + "is Created Successfully",Toast.LENGTH_LONG).show()
        }
    }
}