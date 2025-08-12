package ir.shahramkhandagi.cookguide.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import com.adivery.sdk.*

object AdManager {

    private const val TAG = "AdManager"

    private var isInitialized = false

    /**
     * مقداردهی اولیه SDK ادیوری
     * فقط یک بار در کل اپ باید صدا زده شود
     */
    fun init(application: Application, appId: String, enableLog: Boolean = false) {
        if (isInitialized) return
        Adivery.configure(application, appId)
        if (enableLog) Adivery.setLoggingEnabled(true)
        isInitialized = true
        Log.d(TAG, "Adivery SDK initialized")
    }

    /**
     * نمایش تبلیغ بنری در یک ViewGroup مشخص
     * @param context: اکتیویتی یا فرگمنت
     * @param parent: لایه‌ای که بنر داخل آن نمایش داده می‌شود
     * @param placementId: شناسه تبلیغ‌گاه بنری
     * @param listener: Listener اختیاری برای رویدادهای تبلیغ
     */
    fun showBannerAd(
        context: Context,
        parent: ViewGroup,
        placementId: String,
        listener: AdiveryAdListener? = null
    ) {
        // حذف بنر قبلی اگر وجود دارد
        for (i in 0 until parent.childCount) {
            if (parent.getChildAt(i) is AdiveryBannerAdView) {
                parent.removeViewAt(i)
                break
            }
        }

        val bannerAdView = AdiveryBannerAdView(context)
        // مقداردهی placementId و bannerSize از XML یا پیشفرض
        // چون دسترسی مستقیم به placementId در SDK نیست، باید از XML استفاده شود یا
        // اگر SDK متد loadAd(String) دارد، از آن استفاده شود
        // اینجا فرض می‌کنیم در XML مقداردهی شده یا loadAd() بدون پارامتر کافی است

        listener?.let { bannerAdView.setBannerAdListener(it) }
        parent.addView(
            bannerAdView,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        bannerAdView.loadAd()
        Log.d(TAG, "Banner ad loading started")
    }

    /**
     * آماده‌سازی و نمایش تبلیغ میان‌صفحه‌ای (Interstitial)
     */
    fun prepareInterstitialAd(context: Context, placementId: String) {
        Adivery.prepareInterstitialAd(context, placementId)
        Log.d(TAG, "Interstitial ad prepared")
    }

    fun showInterstitialAd(activity: Activity, placementId: String, onResult: ((Boolean) -> Unit)? = null) {
        if (Adivery.isLoaded(placementId)) {
            Adivery.showAd(placementId)
            onResult?.invoke(true)
            Log.d(TAG, "Interstitial ad shown")
        } else {
            Log.w(TAG, "Interstitial ad not loaded yet")
            onResult?.invoke(false)
        }
    }

    /**
     * آماده‌سازی و نمایش تبلیغ جایزه‌ای (Rewarded)
     */
    fun prepareRewardedAd(context: Context, placementId: String) {
        Adivery.prepareRewardedAd(context, placementId)
        Log.d(TAG, "Rewarded ad prepared")
    }

    fun showRewardedAd(activity: Activity, placementId: String, onResult: ((Boolean) -> Unit)? = null) {
        if (Adivery.isLoaded(placementId)) {
            Adivery.showAd(placementId)
            onResult?.invoke(true)
            Log.d(TAG, "Rewarded ad shown")
        } else {
            Log.w(TAG, "Rewarded ad not loaded yet")
            onResult?.invoke(false)
        }
    }

    /**
     * تنظیم Listener سراسری برای مدیریت رویدادهای تبلیغات
     */
    fun setGlobalListener(listener: AdiveryListener) {
        Adivery.addGlobalListener(listener)
        Log.d(TAG, "Global listener set")
    }
}
