package com.wyh.happyyousdk.ChallangesModule.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.ChallangesModule.Activities.ChallangesActivity
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.ChallengeClick
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Activities.EnrolledChallengeActivity
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Activities.WalkhatonDetailActivity
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.model.response.challengesData
import com.wyh.happyyousdk.utils.SharedPref
import java.text.SimpleDateFormat
import java.util.*

class ChallangesListAdapter(
    val context: Context,
    val click: ChallengeClick,
    val challengesList: ArrayList<challengesData>,
    val recyclerView: RecyclerView
) : RecyclerView.Adapter<ChallangesListAdapter.ViewHolder>() {

    lateinit var res: IntArray
    var bgCount = -1


    init {
        res = intArrayOf(
            R.drawable.ic_light_pink_button_bg,
            R.drawable.ic_light_blue_button_bg,
            R.drawable.ic_light_orange_button_bg,
            R.drawable.ic_light_blue_button_bg,
            R.drawable.ic_light_orange_button_bg,
            R.drawable.ic_light_pink_button_bg,
            R.drawable.ic_light_orange_button_bg,
            R.drawable.ic_light_pink_button_bg,
            R.drawable.ic_light_blue_button_bg
        )

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parentLayout: RelativeLayout = itemView.findViewById(R.id.challange_parent_layout)
        val challengesName: TextView = itemView.findViewById(R.id.challange_name_tv)
        val progressLayout: RelativeLayout = itemView.findViewById(R.id.progress_layout)
        val joinNowLayout: LinearLayout = itemView.findViewById(R.id.join_now_layout)
        val challengeProgressBar: ProgressBar = itemView.findViewById(R.id.challenge_progress_bar);
        val challangePointsTv: TextView = itemView.findViewById(R.id.challange_points_tv)
        val challengeCompleteImg: ImageView = itemView.findViewById(R.id.challenge_complete_img)
        val challengeLogo: ImageView = itemView.findViewById(R.id.challange_image)
        val joinEnrollTv: TextView = itemView.findViewById(R.id.join_enroll_tv)
        val infoIcon: ImageView = itemView.findViewById(R.id.info_icon)
        val startedTv: TextView = itemView.findViewById(R.id.started_tv)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChallangesListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.challange_layout, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ChallangesListAdapter.ViewHolder, position: Int) {
        holder.parentLayout.setOnClickListener {
            if (challengesList[position].ChallengeType.equals("Tribe", true)) {
                if(SharedPref.getChallengeStarted()){
                    if(challengesList[position].IsUserEnrolled == "1" && challengesList[position].IsStarted == 1){
                        ChallangesActivity().getChallengeDetails(context, challengesList[position].ChallengeId, challengesList[position],recyclerView)
                    }
                }
            } else if(challengesList[position].ChallengeType.equals("Steps",true)){

                Log.d("steps", challengesList[position].IsUserEnrolled.toString())
                Log.d("steps", challengesList[position].IsStarted.toString())

                if(challengesList[position].IsUserEnrolled == "0" && (challengesList[position].IsEventEnded || challengesList[position].IsWinnerAnnounced)){
                    Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show()
                }else if(challengesList[position].IsUserEnrolled == "0" && challengesList[position].IsStarted == 0){
                    context.startActivity(Intent(context,EnrolledChallengeActivity::class.java)
                        .putExtra("startDate",challengesList[position].ChallengeStartDate)
                        .putExtra("isStarted",challengesList[position].IsStarted))
                } else if(challengesList[position].IsUserEnrolled == "1" && challengesList[position].IsStarted == 0){
                    context.startActivity(Intent(context,EnrolledChallengeActivity::class.java)
                        .putExtra("startDate",challengesList[position].ChallengeStartDate)
                        .putExtra("isStarted",challengesList[position].IsStarted))
                } else if(challengesList[position].IsUserEnrolled == "0" && challengesList[position].IsStarted == 1){
                    context.startActivity(Intent(context,EnrolledChallengeActivity::class.java)
                        .putExtra("startDate",challengesList[position].ChallengeStartDate)
                        .putExtra("isStarted",challengesList[position].IsStarted))
                } else if(challengesList[position].IsUserEnrolled == "1" && challengesList[position].IsStarted == 1){
                    val intent = Intent(context,WalkhatonDetailActivity::class.java)
                    intent.putExtra("isWinnerAnnounced", challengesList[position].IsWinnerAnnounced)
                    intent.putExtra("isEventEnded", challengesList[position].IsEventEnded)
                    context.startActivity(intent)
                }
            }
            else {
                click.onClickPerform(challengesList[position].ChallengeId, position)
            }
        }

        holder.infoIcon.visibility = View.GONE
        if (challengesList[position].ChallengePercentage.toString() == "100") {
            holder.startedTv.text = "Completed"
            holder.challengeCompleteImg.visibility = View.VISIBLE
        } else {
            holder.startedTv.text = "Started"
            holder.challengeCompleteImg.visibility = View.GONE
        }

        holder.joinNowLayout.setOnClickListener {
            if(challengesList[position].ChallengeType.equals("steps",true)){
                Log.d("steps", challengesList[position].IsUserEnrolled.toString())
                Log.d("steps", challengesList[position].IsStarted.toString())
                if(challengesList[position].IsUserEnrolled == "0" && (challengesList[position].IsEventEnded || challengesList[position].IsWinnerAnnounced)){
                    Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show()
                }else if(challengesList[position].IsUserEnrolled == "0" && challengesList[position].IsStarted == 0){
                    context.startActivity(Intent(context,EnrolledChallengeActivity::class.java)
                        .putExtra("startDate",challengesList[position].ChallengeStartDate)
                        .putExtra("isStarted",challengesList[position].IsStarted))
                } else if(challengesList[position].IsUserEnrolled == "1" && challengesList[position].IsStarted == 0){
                    context.startActivity(Intent(context,EnrolledChallengeActivity::class.java)
                        .putExtra("startDate",challengesList[position].ChallengeStartDate)
                        .putExtra("isStarted",challengesList[position].IsStarted))
                } else if(challengesList[position].IsUserEnrolled == "0" && challengesList[position].IsStarted == 1){
                    context.startActivity(Intent(context,EnrolledChallengeActivity::class.java)
                        .putExtra("startDate",challengesList[position].ChallengeStartDate)
                        .putExtra("isStarted",challengesList[position]
                            .IsStarted))

                } else if(challengesList[position].IsUserEnrolled == "1" && challengesList[position].IsStarted == 1){
                    val intent = Intent(context,WalkhatonDetailActivity::class.java)
                    intent.putExtra("isWinnerAnnounced", challengesList[position].IsWinnerAnnounced)
                    intent.putExtra("isEventEnded", challengesList[position].IsEventEnded)
                    context.startActivity(intent)
                }
            }
            else{
                click.joinNowClick(challengesList[position], challengesList[position].ChallengeId, position, challengesList.get(position).ChallengeType,
                    0, recyclerView)
            }

        }
        if (bgCount == res.size - 1) {
            bgCount = 0
        } else {
            bgCount++
        }


        if (challengesList[position].ChallengeType.equals("Tribe", true)) {
            holder.infoIcon.visibility = View.VISIBLE
            holder.challengeCompleteImg.visibility = View.GONE
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date: Date = sdf.parse(challengesList[position].ChallengeStartDate)
            if (Date().before(date)) {
                if (challengesList[position].IsUserEnrolled == "1") {
                    holder.joinEnrollTv.text = context.getString(R.string.enrolled)
                } else {
                    holder.joinEnrollTv.text = context.getString(R.string.enroll)
                }
                SharedPref.putChallengeStarted(false)
            } else {
                SharedPref.putChallengeStarted(true)
                if (challengesList[position].IsUserEnrolled == "1") {
                    holder.joinEnrollTv.text = "Join Now"
                } else {
                    holder.joinEnrollTv.text = "Join Now"
                }
            }

            if(SharedPref.getChallengeStarted()){
                if(challengesList[position].IsUserEnrolled == "1" && challengesList[position].IsStarted == 1){
                    if(challengesList[position].ChallengePercentage == 100){
                        holder.challengeCompleteImg.visibility = View.VISIBLE
                    }else{
                        holder.challengeCompleteImg.visibility = View.GONE
                    }
                    holder.progressLayout.visibility = View.VISIBLE
                    holder.joinNowLayout.visibility = View.GONE
                }else{
                    holder.progressLayout.visibility = View.GONE
                    holder.joinNowLayout.visibility = View.VISIBLE
                }
            }


        }
        else if(challengesList[position].ChallengeType.equals("Steps",true)){
            if (challengesList[position].IsUserEnrolled == "1") {
                holder.joinEnrollTv.text = context.getString(R.string.enrolled)
            } else {
                holder.joinEnrollTv.text = context.getString(R.string.enroll)
            }
        }

        else if (challengesList[position].IsUserEnrolled.equals("1")) {
            holder.progressLayout.visibility = View.VISIBLE
            holder.joinNowLayout.visibility = View.GONE

        } else {
            holder.progressLayout.visibility = View.GONE
            holder.joinNowLayout.visibility = View.VISIBLE
            holder.joinEnrollTv.text = "Join now"
        }

        holder.challengesName.setText(challengesList[position].ChallengeName)
        holder.challengeProgressBar.progress = challengesList[position].ChallengePercentage
        holder.challangePointsTv.text =
            challengesList[position].ChallengePercentage.toString() + "%"
        holder.parentLayout.background = ContextCompat.getDrawable(context, res[bgCount])
        Glide.with(context).load(challengesList[position].ChallengeLogo).into(holder.challengeLogo)


        holder.infoIcon.setOnClickListener {
            if (challengesList[position].ChallengeType.equals("Tribe", true)) {
                ChallangesActivity().getChallengeApi(context, challengesList[position].ChallengeId, true,challengesList[position].IsUserEnrolled,recyclerView
                ,challengesList[position].IsStarted)
            }
        }


    }

    override fun getItemCount(): Int {
        return challengesList.size
    }
}