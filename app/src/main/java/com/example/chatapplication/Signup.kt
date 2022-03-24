package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

private lateinit var edtName: EditText
private lateinit var edtEmail: EditText
private lateinit var edtPassword: EditText
private lateinit var btnSignup: Button
private lateinit var mAuth: FirebaseAuth

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnSignup = findViewById(R.id.btnSignUp)
        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance()
        btnSignup.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            signup(email, password)
        }
    }

    private fun signup( email: String, password: String) {
    // signup logic
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@Signup, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@Signup, "some error occurred",Toast.LENGTH_SHORT).show()
                }
            }
    }
}