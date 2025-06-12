package com.wyh.happyyousdk.utils

import android.util.Log
import com.wyh.happyyousdk.APIEncryption.BackgroundWork.LocalStorageTask
import com.wyhsdk.main.WatchYourHealth
import java.util.concurrent.TimeUnit

object APILogConstant {

    fun getLogKeys(key: String, activity: String): String {

        var tag = ""
        if (activity.equals("level", ignoreCase = true)) {
            tag = if (key.equals("Time to do Good", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_tg
            } else if (key.equals("Make a Difference", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_md
            } else if (key.equals("Reading Time", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_rt
            } else if (key.equals("Play a Sport", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_ps
            } else if (key.equals("Home Sweet Home", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_hsh
            } else if (key.equals("Charity begins at Home", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_ch
            } else if (key.equals("Go for A Stroll", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_gs
            } else if (key.equals("Take the Stairs", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_ts
            } else if (key.equals("Be Positive, Act Positive", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lbp
            } else if (key.equals("Go Natural.", ignoreCase = true) || key.equals(
                    "Go Natural",
                    ignoreCase = true
                )
            ) {
                mz_unwind_md_3dots_se_sc_l_gn
            } else if (key.equals("Rekindle an Old Friendship", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_rf
            } else if (key.equals("Be Eco-Friendly", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lbe
            } else if (key.equals("Go Green", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lgg
            } else if (key.equals("Skip White Foods", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lsf
            } else if (key.equals("Wear a Chef's hat", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lwc
            } else if (key.equals("Bond with your Friends", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lbf
            } else if (key.equals("Resist a Smoke", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_rs
            } else if (key.equals("Team Lunch", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_ltl
            } else if (key.equals(
                    "Save for a Better Tommorow",
                    ignoreCase = true
                ) || key.equals("Save for a Better Tomorrow", ignoreCase = true)
            ) {
                mz_unwind_md_3dots_se_sc_lsb
            } else if (key.equals("Surprise Your Loved Ones", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_slo
            } else if (key.equals("Greet Your Loved Ones", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lgo
            } else if (key.equals("Develop a Hobby", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_ldh
            } else if (key.equals("Me and My Diary", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lmm
            } else if (key.equals("Right Bite", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lrb
            } else if (key.equals("Try Something New", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_ltsn
            } else if (key.equals("Spread the Love", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_sl
            } else if (key.equals("Make a Healthy Choice", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lmh
            } else if (key.equals("Fruits over Juices", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lfo
            } else if (key.equals("Wear a Chef's hat", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lwh
            } else if (key.equals("Save Electricity", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lse
            } else if (key.equals("Pamper Yourself", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lpy
            } else if (key.equals("Resist Smoking (2 days)", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_rs_2d
            } else if (key.equals("Rekindle an Old Friendship", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_rf
            } else if (key.equals("Fast for 8 hours", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lfh
            } else if (key.equals("Visit an Animal Shelter", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lva
            } else if (key.equals("DIY First Aid Kit", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_ldk
            } else if (key.equals("Resist Smoking (2 days - 4 weeks)", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l
            } else if (key.equals("Helping Hands", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_hh
            } else if (key.equals("Time to Socialize", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_ts
            } else if (key.equals("Treat Yourself", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_ty
            } else if (key.equals("Gift Yourself", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lgy
            } else if (key.equals("Memory Lane", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_ml
            } else if (key.equals("Make a Promise", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lmp
            } else if (key.equals("Go Sugar free", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lgf
            } else if (key.equals("Cut the Caffeine (x2 - 1 week)", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lcc
            } else if (key.equals("Drink Warm Water", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_ldw
            } else if (key.equals("Play a Sport", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lps
            } else if (key.equals("Social Media Detox", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_sm
            } else if (key.equals("LOL!", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lol
            } else if (key.equals("Word of the Day", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_wd
            } else if (key.equals("A Better You", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_bu
            } else if (key.equals("Fam-Jam", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_l_fj
            } else if (key.equals("Clean Your Desk", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lcd
            } else if (key.equals("Manage your Waste Better", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lmb
            } else if (key.equals("A Happier You", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_lhy
            } else {
                ""
            }
        }
        else if (activity.equals("topup", ignoreCase = true)) {
            tag = if (key.equals("Are You Covered?", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_tu_ayc
            } else if (key.equals("Feed a stray", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_tu_fs
            } else if (key.equals("Home Sweet Home", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_tu_hsh
            } else if (key.equals("Develop a Hobby", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_tu_dah
            } else if (key.equals("Go for A Stroll", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_tu_gs
            } else if (key.equals("Help the Society", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_tu_hs
            } else if (key.equals("Gift Yourself", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_tu_gy
            } else if (key.equals("Team Lunch", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_tu_tl
            } else if (key.equals("Adventure Time", ignoreCase = true)) {
                mz_unwind_md_3dots_se_c_tu_at
            } else if (key.equals("Unveil the Master within", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_tu_um
            } else {
                ""
            }
        }
        else {
            tag = if (key.equals("Go Green", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_eg_gg
            } else if (key.equals("Yoga Time", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_eg_yt
            } else if (key.equals("Be Eco-Friendly", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_eg_ef
            } else if (key.equals("Twin to win", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_eg_tw
            } else if (key.equals("Unveil the master within", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_eg_u
            } else if (key.equals("Make a difference", ignoreCase = true)) {
                mz_unwind_md_3dots_se_sc_eg_md
            } else {
                ""
            }
        }

        return tag
    }

    var chat: String = "A_DB_BI_MZ_TR_TT_Chat_"
    var chat_hc_p: String = "A_DB_BI_MZ_TR_TT_Chat_HC_P"
    var chat_hc_p_cam: String = "A_DB_BI_MZ_TR_TT_Chat_HC_P_Cam"
    var chat_hc_p_file: String = "A_DB_BI_MZ_TR_TT_Chat_HC_P_File"
    var chat_hc_p_gal: String = "A_DB_BI_MZ_TR_TT_Chat_HC_P_Gal"
    var chat_hc_p_close: String = "A_DB_BI_MZ_TR_TT_Chat_HC_P_Close"
    var chat_hc_p_tm: String = "A_DB_BI_MZ_TR_TT_Chat_HC_P_TM"
    var chat_hc_cam: String = "A_DB_BI_MZ_TR_TT_Chat_HC_Cam"
    var chat_hc_audio: String = "A_DB_BI_MZ_TR_TT_Chat_HC_Audio"
    var chat_hc_a_can: String = "A_DB_BI_MZ_TR_TT_Chat_HC_A_Can"
    var chat_hc_a_send: String = "A_DB_BI_MZ_TR_TT_Chat_HC_A_Send"
    var chat_hc_image: String = "A_DB_BI_MZ_TR_TT_Chat_HC_Image"
    var chat_hc_file: String = "A_DB_BI_MZ_TR_TT_Chat_HC_File"
    var chat_hc_play: String = "A_DB_BI_MZ_TR_TT_Chat_HC_Play"
    var chat_hc_ac: String = "A_DB_BI_MZ_TR_TT_Chat_HC_AC"

    var myzone_unwind_md = "A_DB_BI_MyZone_Unwind_MD"
    var myzone_unwind_md_mj = "A_DB_BI_MyZone_Unwind_MD_MJ"
    var myzone_unwind_md_mj_sch = "A_DB_BI_MyZone_Unwind_MD_MJ_Sch"
    var myzone_unwind_md_mj_plus = "A_DB_BI_MyZone_Unwind_MD_MJ_Plus"
    var myzone_unwind_md_mj_p_md_ai = "A_DB_BI_MyZone_Unwind_MD_MJ_P_MD_AI"
    var myzone_unwind_md_mj_p_md_ai_cm = "A_DB_BI_MyZone_Unwind_MD_MJ_P_MD_AI_CM"
    var myzone_unwind_md_mj_p_md_ai_g = "A_DB_BI_MyZone_Unwind_MD_MJ_P_MD_AI_G"
    var myzone_unwind_md_mj_p_md_ai_c = "A_DB_BI_MyZone_Unwind_MD_MJ_P_MD_AI_C"
    var myzone_unwind_md_mj_p_md_save = "A_DB_BI_MyZone_Unwind_MD_MJ_P_MD_Save"
    var myzone_unwind_md_act = "A_DB_BI_MyZone_Unwind_MD_Act"
    var myzone_unwind_md_act_search = "A_DB_BI_MyZone_Unwind_MD_Act_Search"
    var myzone_unwind_md_3dots_se_ai = "A_DB_BI_MyZone_Unwind_MD_3dots_S&E_AI"
    var myzone_unwind_md_3dots_se_ai_c = "A_DB_BI_MyZone_Unwind_MD_3dots_S&E_AI_C"
    var myzone_unwind_md_3dots_se_ai_g = "A_DB_BI_MyZone_Unwind_MD_3dots_S&E_AI_G"
    var myzone_unwind_md_3dots_se_ai_cl = "A_DB_BI_MyZone_Unwind_MD_3dots_S&E_AI_CL"
    var myzone_unwind_md_3dots_se_save = "A_DB_BI_MyZone_Unwind_MD_3dots_S&E_Save"

    var myzone_unwind_md_3dots_se_sc_l = "A_DB_BI_MyZone_Unwind_MD_3dots_S&E_SC_L"
    var mz_unwind_md_3dots_se_sc_l_tg = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_TG"
    var mz_unwind_md_3dots_se_sc_l_md = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_MD"
    var mz_unwind_md_3dots_se_sc_l_rt = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_RT"
    var mz_unwind_md_3dots_se_sc_l_ps = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_PS"
    var mz_unwind_md_3dots_se_sc_l_hsh = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_HSH"
    var mz_unwind_md_3dots_se_sc_l_ch = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_CH"
    var mz_unwind_md_3dots_se_sc_l_gs = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_GS"
    var mz_unwind_md_3dots_se_sc_l_ts = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_TS"
    var mz_unwind_md_3dots_se_sc_lbp = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LBP"
    var mz_unwind_md_3dots_se_sc_l_gn = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_GN"
    var mz_unwind_md_3dots_se_sc_l_rf = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_RF"
    var mz_unwind_md_3dots_se_sc_lbe = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LBE"
    var mz_unwind_md_3dots_se_sc_lgg = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LGG"
    var mz_unwind_md_3dots_se_sc_lsf = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LSF"
    var mz_unwind_md_3dots_se_sc_lwc = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LWC"
    var mz_unwind_md_3dots_se_sc_lmd = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LMD"
    var mz_unwind_md_3dots_se_sc_lbf = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LBF"
    var mz_unwind_md_3dots_se_sc_l_rs = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_RS"
    var mz_unwind_md_3dots_se_sc_ltl = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LTL"
    var mz_unwind_md_3dots_se_sc_lsb = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LSB"
    var mz_unwind_md_3dots_se_sc_lrt = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LRT"
    var mz_unwind_md_3dots_se_sc_l_slo = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_SLO"
    var mz_unwind_md_3dots_se_sc_lgo = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LGO"
    var mz_unwind_md_3dots_se_sc_ldh = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LDH"
    var mz_unwind_md_3dots_se_sc_lmm = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LMM"
    var mz_unwind_md_3dots_se_sc_lrb = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LRB"
    var mz_unwind_md_3dots_se_sc_ltsn = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LTSN"
    var mz_unwind_md_3dots_se_sc_l_sl = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_SL"
    var mz_unwind_md_3dots_se_sc_lmh = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LMH"
    var mz_unwind_md_3dots_se_sc_lfo = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LFO"
    var mz_unwind_md_3dots_se_sc_lwh = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LWH"
    var mz_unwind_md_3dots_se_sc_lse = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LSE"
    var mz_unwind_md_3dots_se_sc_lpy = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LPY"
    var mz_unwind_md_3dots_se_sc_l_rs_2d = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_RS_2d"
    var mz_unwind_md_3dots_se_sc_lfh = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LFH"
    var mz_unwind_md_3dots_se_sc_lva = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LVA"
    var mz_unwind_md_3dots_se_sc_ldk = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LDK"
    var mz_unwind_md_3dots_se_sc_l = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L"
    var mz_unwind_md_3dots_se_sc_l_hh = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_HH"
    var mz_unwind_md_3dots_se_sc_l_ty = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_ty"
    var mz_unwind_md_3dots_se_sc_lgy = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_Lgy"
    var mz_unwind_md_3dots_se_sc_l_ml = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_ML"
    var mz_unwind_md_3dots_se_sc_lmp = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LMP"
    var mz_unwind_md_3dots_se_sc_lgf = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LGF"
    var mz_unwind_md_3dots_se_sc_lcc = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LCC"
    var mz_unwind_md_3dots_se_sc_ldw = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LDW"
    var mz_unwind_md_3dots_se_sc_lps = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LPS"
    var mz_unwind_md_3dots_se_sc_l_sm = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_SM"
    var mz_unwind_md_3dots_se_sc_lol = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LOL"
    var mz_unwind_md_3dots_se_sc_l_wd = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_WD"
    var mz_unwind_md_3dots_se_sc_l_bu = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_bU"
    var mz_unwind_md_3dots_se_sc_l_fj = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_L_FJ"
    var mz_unwind_md_3dots_se_sc_lcd = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LCD"
    var mz_unwind_md_3dots_se_sc_lmb = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LMB"
    var mz_unwind_md_3dots_se_sc_lhy = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_LHY"
    var mz_unwind_md_3dots_se_sc_tu = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_TU"
    var mz_unwind_md_3dots_se_sc_tu_ayc = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_TU_AYC"
    var mz_unwind_md_3dots_se_sc_tu_fs = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_TU_FS"
    var mz_unwind_md_3dots_se_sc_tu_hsh = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_TU_HSH"
    var mz_unwind_md_3dots_se_sc_tu_dah = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_TU_DAH"
    var mz_unwind_md_3dots_se_sc_tu_gs = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_TU_GS"
    var mz_unwind_md_3dots_se_sc_tu_hs = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_TU_HS"
    var mz_unwind_md_3dots_se_sc_tu_gy = " A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_TU_GY"
    var mz_unwind_md_3dots_se_sc_tu_tl = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_TU_TL"
    var mz_unwind_md_3dots_se_c_tu_at = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_TU_AT"
    var mz_unwind_md_3dots_se_sc_tu_um = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_TU_UM"
    var mz_unwind_md_3dots_se_sc_eg = "A_DB_BI_MZ_Unwind_MD_3dots_SE_SC_EG"
    var mz_unwind_md_3dots_se_sc_eg_gg = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_EG_GG"
    var mz_unwind_md_3dots_se_sc_eg_yt = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_EG_YT"
    var mz_unwind_md_3dots_se_sc_eg_ef = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_EG_EF"
    var mz_unwind_md_3dots_se_sc_eg_tw = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_EG_TW"
    var mz_unwind_md_3dots_se_sc_eg_u = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_EG_U"
    var mz_unwind_md_3dots_se_sc_eg_md = "A_DB_BI_MZ_Unwind_MD_3dots_S&E_SC_EG_MD"


}