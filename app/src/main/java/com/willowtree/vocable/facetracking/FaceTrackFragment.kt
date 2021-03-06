package com.willowtree.vocable.facetracking

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.ar.core.AugmentedFace
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment
import com.willowtree.vocable.utils.VocableSharedPreferences
import org.koin.android.ext.android.inject
import java.util.*


class FaceTrackFragment : ArFragment() {

    private lateinit var viewModel: FaceTrackingViewModel

    private val sharedPrefs: VocableSharedPreferences by inject()

    override fun getSessionConfiguration(session: Session): Config {
        val config = Config(session)
        config.augmentedFaceMode = Config.AugmentedFaceMode.MESH3D
        return config
    }

    override fun getSessionFeatures(): Set<Session.Feature> {
        return EnumSet.of<Session.Feature>(Session.Feature.FRONT_CAMERA)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(planeDiscoveryController) {
            hide()
            setInstructionView(null)
        }
        viewModel = ViewModelProviders.of(requireActivity()).get(FaceTrackingViewModel::class.java)
        lifecycle.addObserver(viewModel)
        subscribeToViewModel()
        attachFaceTracker()
    }

    override fun onResume() {
        super.onResume()
        enableFaceTracking(sharedPrefs.getHeadTrackingEnabled())
    }

    private fun subscribeToViewModel() {
        viewModel.adjustedVector.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.onScreenPointAvailable(arSceneView.scene.camera.worldToScreenPoint(it))
            }
        })
    }

    private fun attachFaceTracker() {
        val scene = arSceneView.scene
        scene.addOnUpdateListener {
            viewModel.onSceneUpdate(arSceneView.session?.getAllTrackables(AugmentedFace::class.java))
        }
    }

    fun enableFaceTracking(enable: Boolean) {
        if (enable) {
            arSceneView.resume()
        } else {
            arSceneView.pause()
            viewModel.onSceneUpdate(null)
        }
    }
}