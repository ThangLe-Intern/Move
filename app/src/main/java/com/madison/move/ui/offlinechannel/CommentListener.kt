package com.madison.move.ui.offlinechannel

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText

interface CommentListener {
    fun onBackPressed()

    fun userComment(
        cancelButton: AppCompatButton,
        sendButton: AppCompatButton,
        editText: AppCompatEditText,
        listComment: MutableList<Comment>,
        user: DataModelComment
    )

    fun onWriteCommentListener(
        editText: AppCompatEditText,
        cancelButton: AppCompatButton,
        sendButton: AppCompatButton
    )

    fun onCancelUserComment(cancelButton: AppCompatButton, editText: AppCompatEditText)
    fun onSendUserComment(
        sendButton: AppCompatButton,
        listComment: MutableList<Comment>,
        editText: AppCompatEditText,
        cancelButton: AppCompatButton,
        user: DataModelComment
    )

    fun clearEdittext(editText: AppCompatEditText, cancelButton: AppCompatButton)
    fun hideKeyboard(view: View)
    fun onLoadComment(listComment: MutableList<Comment>)

}