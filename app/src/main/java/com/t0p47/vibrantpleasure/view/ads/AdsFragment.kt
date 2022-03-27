package com.t0p47.vibrantpleasure.view.ads

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.t0p47.vibrantpleasure.R
import com.t0p47.vibrantpleasure.databinding.AdsFragmentBinding
import com.t0p47.vibrantpleasure.extension.FragmentInjectable
import com.t0p47.vibrantpleasure.extension.ViewModelFactory
import com.t0p47.vibrantpleasure.extension.injectViewModel
import com.t0p47.vibrantpleasure.inapp_purchase.BillingManager
import com.t0p47.vibrantpleasure.utils.LOG_TAG
import javax.inject.Inject

class AdsFragment : Fragment(), FragmentInjectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var billingManager: BillingManager

    private lateinit var binding: AdsFragmentBinding
    private lateinit var viewModel: AdsViewModel

    companion object {
        fun newInstance() = AdsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.ads_fragment, container, false)
        viewModel = injectViewModel(viewModelFactory)

        Log.d(LOG_TAG, "AdsFragment: onCreateView")

        binding.btnUpdatePro.setOnClickListener{

            Log.d(LOG_TAG, "AdsFragment: onCreateView, buyProVersion")

            val sku = "update_to_professional"
            //viewModel.buyProVersion()
            billingManager.buyProVersion(requireActivity(), sku)

        }

        billingManager.skusWithSkuDetails.value?.forEach{ (key, value) ->
            Log.d(LOG_TAG,"AdsFragment: onCreateView: key: $key, value: $value")
        }

        binding.btnRateApp.setOnClickListener{
            val uri: Uri = Uri.parse("market://details?id=${requireActivity().packageName}")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=${requireActivity().packageName}")
                    )
                )
            }
        }

        binding.btnSwitchTheme.setOnClickListener {
            when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
                Configuration.UI_MODE_NIGHT_YES ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Configuration.UI_MODE_NIGHT_NO ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        binding.btnShare.setOnClickListener {
            ShareCompat.IntentBuilder.from(requireActivity())
                .setType("text/plain")
                .setChooserTitle("Share VibrantPleasure")
                .setSubject("VibrantPleasure")
                .setText("Check the VibrantPleasure app on Android: http://play.google.com/store/apps/details?id=" + requireActivity().packageName)
                .startChooser();
        }

        binding.btnEmail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("tertylian759@gmail.com"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_contact_subject))

            val packageInfo = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)

            val versionName = packageInfo.versionName

            val deviceModel = Build.MODEL

            emailIntent.putExtra(Intent.EXTRA_TEXT, "${getString(R.string.email_contact_enter_text)} \n\n\n App: ${getString(R.string.app_name)} $versionName " +
                    "\n$deviceModel, Android SDK: ${Build.VERSION.SDK_INT} \n\n\n${getString(R.string.email_contact_android_send)}")

            emailIntent.type = "message/rfc822"

            //TODO: Use R.string for translate
            startActivity(Intent.createChooser(emailIntent,"Выберите приложение для отправки Email"))
        }

        return binding.root
    }

}
