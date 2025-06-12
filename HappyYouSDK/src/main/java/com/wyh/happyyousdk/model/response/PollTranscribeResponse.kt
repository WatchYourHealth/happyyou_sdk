package com.wyh.happyyousdk.model.response

data class PollTranscribeResponse(val msg: String, val success: Boolean, val data: PollData )

data class PollData(val status: String, val result: com.wyh.happyyousdk.model.response.Result)

data class Result(val transcripts: List<Transcript>)

data class Transcript(val text: Any?)