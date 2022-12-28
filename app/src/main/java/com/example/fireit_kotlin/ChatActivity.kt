package com.example.fireit_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView:RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var btnSend:ImageView
    private lateinit var messageAdapter: messageAdapter
    private lateinit var messageList:ArrayList<Message>
    private lateinit var mDBRef:DatabaseReference

    var reciverRoom:String?= null
    var senderRoom:String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val reciverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDBRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = reciverUid + senderUid
        reciverRoom = senderUid + reciverUid

        supportActionBar?.title=name
        chatRecyclerView = findViewById(R.id.chatRecycleView)
        messageBox = findViewById(R.id.messageBox)
        btnSend = findViewById(R.id.sendButton)
        messageList = ArrayList()
        messageAdapter = messageAdapter(this,messageList)

        chatRecyclerView.layoutManager=LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        //adding message in the chatRecycleview
        mDBRef.child("Chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        //adding message in database
        btnSend.setOnClickListener{

            val message = messageBox.text.toString()
            val messageObject = Message(message,senderUid)
            mDBRef.child("Chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDBRef.child("Chats").child(reciverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }
    }
}