package com.example.telepathy.clases

data class User(
    val name: String,
    val color: Int,
    val avatar: String? = null,
    val chatHistory: MutableList<Message> = mutableListOf()
) {
    fun addMessage(message: Message) {
        chatHistory.add(message)
    }

    fun clearChat() {
        chatHistory.clear()
    }
}
