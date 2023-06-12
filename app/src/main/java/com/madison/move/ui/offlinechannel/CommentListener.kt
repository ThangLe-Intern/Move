package com.madison.move.ui.offlinechannel

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.madison.move.data.model.DataUser
import com.madison.move.data.model.comment.DataComment

interface CommentListener {
    fun onBackPressed()

    fun userComment(
        cancelButton: AppCompatButton,
        sendButton: AppCompatButton,
        editText: AppCompatEditText,
    )

    fun onWriteCommentListener(
        editText: AppCompatEditText,
        cancelButton: AppCompatButton,
        sendButton: AppCompatButton
    )

    fun onCancelUserComment(cancelButton: AppCompatButton, editText: AppCompatEditText)
    fun onSendUserComment(
        sendButton: AppCompatButton,
        editText: AppCompatEditText,
        cancelButton: AppCompatButton,
        user: DataUser
    )

    fun clearEdittext(editText: AppCompatEditText, cancelButton: AppCompatButton)
    fun hideKeyboard(view: View)
    fun onLoadComment(listComment: MutableList<DataComment>)

}