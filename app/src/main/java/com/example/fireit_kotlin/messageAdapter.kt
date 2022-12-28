package com.example.fireit_kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class messageAdapter(val context: Context,val messageList: ArrayList<Message>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val Iteam_Send = 1
    val Iteam_Recived = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         if(viewType==1){
             val view:View = LayoutInflater.from(context).inflate(R.layout.send,parent,false)
             return SendViewHolder (view)
         }else{
             val view:View = LayoutInflater.from(context).inflate(R.layout.recived,parent,false)
             return ReviceViewHolder(view)
         }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currenctMessage = messageList[position]
        if(holder.javaClass == SendViewHolder::class.java){
            val viewHolder = holder as SendViewHolder
            holder.sendMessage.text = currenctMessage.message

        }else{
            val viewHolder = holder as ReviceViewHolder
            holder.reciveMessage.text = currenctMessage.message

        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return Iteam_Send
        }else{
            return Iteam_Recived
        }
    }

    class SendViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val sendMessage = itemView.findViewById<TextView>(R.id.txt_send_message)
    }
    class ReviceViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val reciveMessage = itemView.findViewById<TextView>(R.id.txt_recive_message)
    }
}