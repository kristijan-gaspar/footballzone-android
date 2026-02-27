package hr.algebra.footballzone.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import hr.algebra.footballzone.R
import hr.algebra.footballzone.api.FootballZoneWorker
import hr.algebra.footballzone.api.ImageWorker
import hr.algebra.footballzone.databinding.FragmentSplashBinding
import hr.algebra.footballzone.framework.applyAnimation
import hr.algebra.footballzone.framework.callDelayed
import hr.algebra.footballzone.framework.finish
import hr.algebra.footballzone.framework.getBooleanPreference
import hr.algebra.footballzone.framework.isOnline
import hr.algebra.footballzone.framework.navigateTo
import hr.algebra.footballzone.framework.setBooleanPreference

private const val DELAY = 3000L
const val DATA_IMPORTED = "hr.algebra.footballzone.data_imported"

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSplashBinding.bind(view)

        startAnimations()
        redirect()
    }

    private fun startAnimations() {
        binding.ivLogo.applyAnimation(R.anim.blink)
    }

    private fun redirect() {
        if (getBooleanPreference(DATA_IMPORTED)) {
            callDelayed(DELAY) { navigateTo(R.id.action_splash_to_home) }
        } else {
            if (isOnline()) {
                //callDelayed(DELAY) { navigateTo(R.id.action_splash_to_home) }
                startAndObserveWork()

            } else {
                showNoInternetDialog()
            }
        }
    }

    private fun startAndObserveWork() {
        val workManager = WorkManager.getInstance(requireContext())

        val dataWork = OneTimeWorkRequest.from(FootballZoneWorker::class.java)
        val imageWork = OneTimeWorkRequest.from(ImageWorker::class.java)

        workManager
            .beginWith(dataWork)
            .then(imageWork)
            .enqueue()

        workManager.getWorkInfoByIdLiveData(imageWork.id)
            .observe(viewLifecycleOwner) { workInfo ->
                when (workInfo?.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        setBooleanPreference(DATA_IMPORTED, true)
                        navigateTo(R.id.action_splash_to_home)
                    }

                    WorkInfo.State.FAILED -> {
                        showToast(getString(R.string.data_sync_failed))
                        callDelayed(DELAY) { finish() }
                    }

                    else -> Unit
                }
            }
    }

    private fun showNoInternetDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.no_internet)
            setMessage(getString(R.string.please_check_your_internet_connection))
            setIcon(R.drawable.ic_wifi_off)
            setCancelable(false)

            setPositiveButton(getString(R.string.retry)) { _, _ ->
                redirect()
            }

            setNegativeButton(getString(R.string.exit)) { _, _ ->
                requireActivity().finish()
            }
            show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}