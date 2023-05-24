package com.madison.move.data.source.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.madison.move.data.model.Video


class MoveResponse {
    @Expose
    @SerializedName("movies")
    var videos: List<Video>? = null

}